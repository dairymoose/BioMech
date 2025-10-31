package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.entity.GrapplingHook;
import com.dairymoose.biomech.item.anim.GrappleArmDispatcher;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.loading.FMLEnvironment;

public abstract class GrappleArmArmor extends ArmorBase {

	public final GrappleArmDispatcher dispatcher;
	
	protected static final String USE_TICKS = "useTicks";
	
	public GrappleArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new GrappleArmDispatcher();
	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_GRAPPLE_LEFT_ARM.get();
	}
	
	public static float energyToLaunch = 15.0f;
	protected int startUsingTickCount = 3;
	
	public static float defaultMaximumTetherDistance = 30.0f;
	
	public static Map<UUID, GrappleInfo> grappleInfoMap = new HashMap<>();
	
	public static class GrappleInfo {
		public Vec3 hookPos;
		public GrapplingHook grappleEntity;
		public float grappleTetherDistance;
		public float grappleMaximumTetherDistance;
		public boolean wantToBreakTetherOnNextTick = false;
		public String hookDimension;
		
		@Override
		public String toString() {
			return "[GrappleInfo hookPos=" + hookPos + ", grappleTetherDistance=" + grappleTetherDistance + "]";
		}
	}
	
	private double getDistanceToHook(Player player, Vec3 hookPos) {
		return hookPos.distanceTo(player.position());
	}
	
	private double getStraightLineDistanceToHook(Player player, Vec3 hookPos) {
		double diffY = hookPos.y - player.position().y;
		double diffX = hookPos.x - player.position().x;
		double diffZ = hookPos.z - player.position().z;

		double diffXSqr = diffX * diffX;
		double diffZSqr = diffZ * diffZ;

		double straightLineDistance = Math.sqrt(diffXSqr + diffZSqr);
		return straightLineDistance;
	}
	
	public static MechPart clientLastUsedArm = MechPart.RightArm;
	private float localPlayerLastFallDist = -1.0f;
	private boolean justDidRecalculate = false;
	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

		if (playerData != null) {
			ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);

			if (playerData.getSuitEnergy() < energyToLaunch) {
				active = false;
			}

			if (active) {
				int useTicks = thirdPersonItemStack.getTag().getInt(USE_TICKS);
				if (FMLEnvironment.dist == Dist.CLIENT) {
					if (player.level().isClientSide)
						++useTicks;
				} else {
					++useTicks;
				}
				
				thirdPersonItemStack.getTag().putInt(USE_TICKS, useTicks);

				GrappleInfo grappleInfo = GrappleArmArmor.grappleInfoMap.get(player.getUUID());
				boolean didHit = false;
				if (player.level().isClientSide) {
					if (useTicks <= startUsingTickCount) {
						if (useTicks == 1) {
							//this.startUsingSound(player);
						}
						// BioMech.LOGGER.info("useTicks=" + useTicks);
						//this.startUsingAnimation(itemStack);
						BioMech.clientSideItemAnimation(itemStack, this.dispatcher.LAUNCH_COMMAND.cmd);
						// send packet to server asking for start_using anim

						//this.thirdPersonStartUsingAnimation(thirdPersonItemStack);
						//player.setYBodyRot(player.getYHeadRot());
						player.setDiscardFriction(false);
					} else {
						//this.miningAnimation(itemStack);
						
						// send packet to server asking for mining anim
						//this.thirdPersonMiningAnimation(thirdPersonItemStack);
						//player.setYBodyRot(player.getYHeadRot());
						
						clientLastUsedArm = handPart;
						if (player.isLocalPlayer()) {
							if (active && useTicks > startUsingTickCount && grappleInfo != null && grappleInfo.hookPos != null) {
								if (!player.onGround() && player.fallDistance >= 0.0f) {
									if (localPlayerLastFallDist == 0.0f && player.fallDistance > 0.0f) {
										//we just started falling, recalculate tether length now
										float oldTetherDist = shortenTetherToCurrent(player, grappleInfo);
										Vec3 deltaMov = player.getDeltaMovement();
										player.setDeltaMovement(new Vec3(deltaMov.x, 0.0, deltaMov.z));
										BioMech.LOGGER.debug("recalculate tether distance from " + oldTetherDist + " to " + grappleInfo.grappleTetherDistance);
										justDidRecalculate = true;
									}
									localPlayerLastFallDist = player.fallDistance;
								}
								if (!player.onGround() && (justDidRecalculate || player.fallDistance >= 0.2f)) {
									if (!player.shouldDiscardFriction()) {
										float oldTetherDist = shortenTetherToCurrent(player, grappleInfo);
										player.setDiscardFriction(true);
									}
									double m = 1.0;
									double g = -player.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
									
									double diffX = grappleInfo.hookPos.x - player.position().x;
									double diffY = grappleInfo.hookPos.y - player.position().y;
									double diffZ = grappleInfo.hookPos.z - player.position().z;

									//https://www.acs.psu.edu/drussell/Demos/Pendulum/Pendulum.html
									double distToHook = this.getDistanceToHook(player, grappleInfo.hookPos);
									double straightLineDistance = this.getStraightLineDistanceToHook(player, grappleInfo.hookPos);
									double angleToHook = 90.0 - Math.toDegrees(Math.atan2(diffY, straightLineDistance));
									
									Vec3 deltaMov = player.getDeltaMovement();
									
									double inlineForce = m * g * Math.sin(Math.toRadians(angleToHook));
									double straightLineForce = Math.cos(Math.toRadians(angleToHook)) * inlineForce;
									double gravityForce = Math.sin(Math.toRadians(angleToHook)) * inlineForce + deltaMov.y;
									
									double yawTo = Math.toDegrees(Math.atan2(diffZ, diffX));
									
									if (angleToHook > 90.0) {
										inlineForce = 0.0;
										gravityForce = deltaMov.y;
									}
									
									//https://en.wikipedia.org/wiki/Centripetal_force
									//A(c) = v^2 / r
									//vector directed towards center of curvature
									double centripetalAcceleration = inlineForce*inlineForce / distToHook;
									double centripetalY = centripetalAcceleration * Math.cos(Math.toRadians(angleToHook));
									double centripetalXZ = centripetalAcceleration * Math.sin(Math.toRadians(angleToHook));
									double centripetalX = (float)(centripetalXZ * -Math.sin(Math.toRadians(yawTo)));
									double centripetalZ = (float)(centripetalXZ * Math.cos(Math.toRadians(yawTo)));
									//double yComponent = gravityForce + deltaMov.y;
									double yComponent = gravityForce;
									
									double movementFriction = 0.9965;
									//double movementFriction = 1.0;
									double swingX = Math.cos(Math.toRadians(yawTo + 180.0));
									double swingZ = Math.sin(Math.toRadians(yawTo + 180.0));
									double deltaX = swingX*straightLineForce + (float)deltaMov.x*movementFriction;
									double deltaZ = swingZ*straightLineForce + (float)deltaMov.z*movementFriction;
									
									deltaX += centripetalX;
									yComponent += centripetalY;
									deltaZ += centripetalZ;
									
									Vec3 projectedLocation = player.position().add(deltaX, yComponent, deltaZ);
									double projectedDistToHook = projectedLocation.distanceTo(grappleInfo.hookPos);
									double beyondTetherLengthAdjustment = projectedDistToHook > grappleInfo.grappleTetherDistance ? (projectedDistToHook - grappleInfo.grappleTetherDistance)*Math.cos(Math.toRadians(angleToHook)) : 0.0;
									if (beyondTetherLengthAdjustment > 0.0) {
										BioMech.LOGGER.debug("beyond tether length adjustment: " + beyondTetherLengthAdjustment);
									}
									double deltaY = yComponent + beyondTetherLengthAdjustment;
									
									player.setDeltaMovement(deltaX, deltaY, deltaZ);
									//BioMech.LOGGER.debug("inline force=" + inlineForce + " with deltaY=" + deltaY + " with yComponent=" + yComponent + " and cetripetalY=" + centripetalY + " for " + angleToHook);
								} else {
									player.setDiscardFriction(false);
									justDidRecalculate = false;
								}
							}
						}
					}
				}

				if (!player.level().isClientSide) {
					Level level = player.level();
					if (active && useTicks == (startUsingTickCount + 1)) {
						if (grappleInfo == null || (grappleInfo != null && grappleInfo.hookPos == null)) {
							level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_CHAIN, SoundSource.PLAYERS, 1.0f, 1.2f);
							if (grappleInfo == null) {
								grappleInfo = new GrappleInfo();
								GrappleArmArmor.grappleInfoMap.put(player.getUUID(), grappleInfo);
							}
							grappleInfo.grappleTetherDistance = 0.0f;
							if (!level.isClientSide && grappleInfo.grappleEntity == null) {
								BioMech.LOGGER.debug("launch grapple for player: " + player);
								itemStack.getOrCreateTag().putBoolean("Launched", true);
								GrapplingHook launched = new GrapplingHook(level, player);
								launched.setItem(new ItemStack(Items.ARROW));
								launched.entityOwner = player;
								launched.mechPart = handPart;
								grappleInfo.grappleEntity = launched;
								launched.setYRot(player.getYRot());
								launched.setXRot(player.getXRot());
								launched.setOldPosAndRot();
								//last 3 args: extra pitch, launch speed mult, random factor
								launched.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0f, 0.0F);
								level.addFreshEntity(launched);
								itemStack.getOrCreateTag().putInt("LaunchedEntityId", launched.getId());
								itemStack.getOrCreateTag().putLong("LaunchedTimestamp", System.currentTimeMillis());
							}
						}
					} else if (active && useTicks > startUsingTickCount && grappleInfo != null && grappleInfo.hookPos != null) {
						BioMech.allowFlyingForPlayer(player);
						player.resetFallDistance();
					}
				}
				
			} else {
				if (bothHandsInactive) {
					justDidRecalculate = false;
					player.setDiscardFriction(false);
					GrappleInfo grappleInfo = GrappleArmArmor.grappleInfoMap.get(player.getUUID());
					if (grappleInfo != null) {
						if (player.level().isClientSide) {
							if (grappleInfo.hookPos != null) {
								CompoundTag tag = thirdPersonItemStack.getTag();
								if (tag != null) {
									int useTicks = tag.getInt(USE_TICKS);
									
									if (useTicks > startUsingTickCount) {
										tag.putInt(USE_TICKS, 0);
										//launch player towards hook if they are looking at it
										Vec3 vecToHook = grappleInfo.hookPos.subtract(player.position());
										Vec3 vecToHookNormalized = vecToHook.normalize();
										Vec3 viewVec = player.getViewVector(1.0f);
										float launchLookAtThreshold = 0.8f;
										double dotProduct = viewVec.dot(vecToHookNormalized);
										BioMech.LOGGER.debug("dotProduct=" + dotProduct + " and vecToHook len=" + vecToHook.length());
										if (dotProduct >= launchLookAtThreshold || vecToHook.length() <= 3.0) {
											Vec3 averageVec = vecToHookNormalized.add(viewVec).normalize();
											
											Vec3 targetLocationVec = averageVec.scale(vecToHook.length());
											double xzOvershoot = 1.0;
											double yOvershoot = 3.5;
											if (targetLocationVec.y < 0.0) {
												xzOvershoot = 5.0;
												yOvershoot = 5.0;
												BioMech.LOGGER.debug("Use xzOvershoot of " + xzOvershoot + " for downward target");
											}
											Vec3 overshootVec = targetLocationVec.with(Axis.Y, 0.0).normalize().scale(xzOvershoot).with(Axis.Y, yOvershoot);
											targetLocationVec = targetLocationVec.add(overshootVec);
											double launchDistance = targetLocationVec.length();
											double desiredY = targetLocationVec.y;
											
											float airFriction = 0.91f;
											//geometric series
											//v + v*0.91 + v*0.91^2 + v*0.91^3 + v*0.91^4 + v*0.91^5 = dist
											//Formula: \(S_{n}=\frac{a(1-r^{n})}{1-r}\)
											//\(S_{n}\) is the sum of the first \(n\) terms
											//\(a\) is the first term
											//\(r\) is the common ratio (the value you multiply by to get the next term) 
											//\(n\) is the number of terms
											//a = sum*(1-r)/(1-r^n)
											//v = dist*(1-airFriction)
											int n = 15;
											//double launchVelocity = launchDistance/((1-Math.pow(airFriction, n+1))/(1-airFriction));
											double launchVelocity = launchDistance/((1-Math.pow(airFriction, n+1))/(1-airFriction));
											double predictedDistanceIn5Sec = launchVelocity * ((1-Math.pow(airFriction, n+1))/(1-airFriction));
											double calcDist5Sec = 0.0;
											
											double launchVelocityWithFriction = launchVelocity;
											for (int i=0; i<n; ++i) {
												calcDist5Sec += launchVelocityWithFriction;
												launchVelocityWithFriction *= airFriction;
											}
											
											//double finalLaunchScale = Math.pow(launchVelocity, 1.4);
											double finalLaunchScale = launchVelocity;
											Vec3 launchVec = averageVec.scale(finalLaunchScale);
											
											double g = player.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
											double ySimulation = desiredY * 0.05;
											double ySimIncrement = Math.abs(desiredY * 0.01);
											
											int attempts = 50;
											int ticksToTry = 50;
											double yDistTravelled = 0.0;
											double ySimulationInitial = ySimulation;
											
											//check against yDistTravelled = 0 so that negative desiredY values don't immediately skip the loop
											while (attempts > 0 && (yDistTravelled == 0.0 || yDistTravelled < desiredY)) {
												--attempts;
												
												yDistTravelled = 0.0;
												ySimulation = ySimulationInitial + ySimIncrement;
												ySimulationInitial = ySimulation;
												
												BioMech.LOGGER.debug("new attempt with launch velocity = " + ySimulationInitial + " for goal=" + desiredY + " with overshootVec=" + overshootVec);
												for (int i=0; i<ticksToTry; ++i) {
													yDistTravelled += ySimulation;
													ySimulation -= g;
													ySimulation *= 0.98;
													BioMech.LOGGER.debug("travelled dist = " + yDistTravelled);
													if (yDistTravelled >= desiredY) {
														BioMech.LOGGER.debug("breaking early due to meeting goal = " + desiredY);
														break;
													}
												}
											}
											if (yDistTravelled >= desiredY) {
												BioMech.LOGGER.debug("met y goal with yDistTravelled=" + yDistTravelled + " vs desiredY=" + desiredY + " giving launchY=" + ySimulationInitial);
											}
											launchVec = launchVec.with(Axis.Y, ySimulationInitial);
											
											BioMech.LOGGER.debug("dist = " + launchDistance + " and launchVec=" + launchVec + " having finalLaunchScale=" + finalLaunchScale + " and launchVelocity=" + launchVelocity + " with predictedDist=" + predictedDistanceIn5Sec + " and calcDist5=" + calcDist5Sec);
											BioMech.LOGGER.debug("launchVec speed=" + launchVec.length());
											Vec3 deltaMov = player.getDeltaMovement();
											player.setOnGround(false);
											if (desiredY <= 0.0f) {
												Vec3 pos = player.position();
												player.setPos(pos.x, pos.y + 0.33, pos.z);
												BioMech.LOGGER.debug("Launch with ground boost");
											}
											player.setDeltaMovement(new Vec3(deltaMov.x + launchVec.x, deltaMov.y + launchVec.y, deltaMov.z + launchVec.z));
											BioMech.LOGGER.debug("player starting loc = " + player.position());
											BioMech.LOGGER.debug("player starting deltaMov = " + player.getDeltaMovement());
										}
									}
								}
							}
						} else {
							BioMech.allowFlyingForPlayer(player);
							player.resetFallDistance();
						}
						//BioMech.LOGGER.info("grappleEntity=" + grappleInfo.grappleEntity);
						if (grappleInfo.grappleEntity != null) {
							if (!player.level().isClientSide) {
								grappleInfo.grappleEntity.discard();
								grappleInfo.grappleEntity = null;
							}
						}
						
						if (grappleInfo.grappleEntity == null || grappleInfo.grappleEntity.isRemoved()) {
							GrappleArmArmor.grappleInfoMap.remove(player.getUUID());
						}
					}
					if (thirdPersonItemStack.getTag() != null && thirdPersonItemStack.getTag().contains(USE_TICKS)) {
						thirdPersonItemStack.getTag().putInt(USE_TICKS, 0);
					}
					if (player.level().isClientSide) {
						BioMech.clientSideItemAnimation(itemStack, this.dispatcher.INERT_COMMAND.cmd);
					}
				}
			}
		}
	}

	private float shortenTetherToCurrent(Player player, GrappleInfo grappleInfo) {
		float oldTetherDist = grappleInfo.grappleTetherDistance;
		grappleInfo.grappleTetherDistance = (float) this.getDistanceToHook(player, grappleInfo.hookPos);
		return oldTetherDist;
	}
	
	@Override
	public void postHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
		if (playerData != null) {
			ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);

			if (playerData.getSuitEnergy() < energyToLaunch) {
				active = false;
			}
			
			CompoundTag tag = thirdPersonItemStack.getTag();
			if (tag != null) {
				int useTicks = tag.getInt(USE_TICKS);
				//+2 is required as this doesn't execute in the expected order in single-player
				if (active && useTicks == (startUsingTickCount + 2)) {
					playerData.spendSuitEnergy(player, energyToLaunch);
				}
			}
		}
	}

	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_GRAPPLE_ARM.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					
				}
			}
		}
	}

}
	