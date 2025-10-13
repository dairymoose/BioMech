package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.packet.serverbound.ServerboundMiningArmBlockTargetPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundMiningArmEntityTargetPacket;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractMiningArmArmor extends ArmorBase {

	public AbstractMiningArmArmor(ArmorMaterial material, Type type, Properties props) {
		super(material, type, props);
	}
	
	class DestroyBlockProgressList {
		DestroyBlockProgressList() {
			list = new ArrayList<>();
		}
		
		List<DestroyBlockProgress> list;
	}
	
	class DestroyBlockProgress {
		BlockPos pos;
		float progress = 0.0f;
		int destroyBlockProgressId = 0;
		public static float progressMax = 100.0f;
	}

	protected float minSpeedMult = 2.5f;
	protected float maxSpeedMult = minSpeedMult * 6.0f;
	protected double blockReachMult = 1.6;
	protected ItemStack miningTool = new ItemStack(Items.IRON_PICKAXE);
	public static int START_USING_TICK_COUNT = 5;
	
	protected float energyPerSec = 4.0f;
	protected float energyPerTick;
	
	protected float energyPerSecMiss = 1.0f;
	protected float energyPerTickMiss;
	
	protected double particleDistanceFromPlayerFirstPerson = 0.2;
	protected double particlePerpendicularDistanceFirstPerson = 0.40;
	protected double particleYFirstPerson = 1.3;
	
	protected double particleDistanceFromPlayerThirdPerson = particleDistanceFromPlayerFirstPerson;
	protected double particlePerpendicularDistanceThirdPerson = particlePerpendicularDistanceFirstPerson;
	protected double particleYThirdPerson = particleYFirstPerson;
	
	protected float wrongToolPenalty = 1.0f;
	protected boolean instantDestroyLeaves = false;
	protected boolean onlyMinesMatchingBlocks = false;
	protected boolean originMustMatchToolToMineArea = true;
	
	private static final boolean serverEverChecksBlockHits = false;
	private static final boolean serverEverChecksEntityHits = false;
	
	protected int soundTickPeriod = 3;
	Map<Player, DestroyBlockProgressList> dbpMap = new HashMap<>();
	Map<Player, DestroyBlockProgressList> dbpMapClient = new HashMap<>();
	protected int xSize = 1;
	protected int ySize = 1;
	protected int zSize = 1;
	
	protected float minMiningProgress = 0.0f;

	protected abstract float getMiningPower(int useTicks);
	
	protected void startUsingSound(Player player) {
		
	}
	
	protected void beginHandTick(Player player) {
		
	}
	
	protected abstract void passiveAnimation(ItemStack itemStack);
	protected abstract void inertAnimation(ItemStack itemStack);
	protected abstract void startUsingAnimation(ItemStack itemStack);
	protected abstract void miningAnimation(ItemStack itemStack);
	protected abstract void thirdPersonStartUsingAnimation(ItemStack itemStack);
	protected abstract void thirdPersonMiningAnimation(ItemStack itemStack);

	private Object previousClientTarget = null;
	
	public static Map<Player, BlockPos> blockTargetMap = new ConcurrentHashMap<>();
	public static Map<Player, Entity> entityTargetMap = new ConcurrentHashMap<>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick,
			boolean bothHandsInactive, boolean bothHandsActive) {

		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

		if (playerData != null) {
			this.beginHandTick(player);
			this.energyPerTick = energyPerSec / 20.0f;
			this.energyPerTickMiss = energyPerSecMiss / 20.0f;
			
			ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);
			
			if (playerData.getSuitEnergy() < energyPerTick) {
				active = false;
			}
			
			if (active) {
				int useTicks = thirdPersonItemStack.getTag().getInt("useTicks");
				if (FMLEnvironment.dist == Dist.CLIENT) {
					if (player.level().isClientSide)
						++useTicks;
				} else {
					++useTicks;
				}
				
				thirdPersonItemStack.getTag().putInt("useTicks", useTicks);

				boolean didHit = false;
				if (player.level().isClientSide) {
					if (useTicks <= START_USING_TICK_COUNT) {
						if (useTicks == 1) {
							this.startUsingSound(player);
						}
						// BioMech.LOGGER.info("useTicks=" + useTicks);
						this.startUsingAnimation(itemStack);
						// send packet to server asking for start_using anim

						this.thirdPersonStartUsingAnimation(thirdPersonItemStack);
						player.setYBodyRot(player.getYHeadRot());
					} else {
						this.miningAnimation(itemStack);
						
						// send packet to server asking for mining anim
						this.thirdPersonMiningAnimation(thirdPersonItemStack);
						player.setYBodyRot(player.getYHeadRot());

						HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
								(e) -> (e instanceof LivingEntity && !((LivingEntity)e).isDeadOrDying()) && !e.isRemoved() && !e.isSpectator(),
								player.getBlockReach() * blockReachMult);

						double handMult = 1.0;
						if (handPart == MechPart.RightArm)
							handMult = -1.0;
						
						Vec3 originalPlayerViewVec = player.getViewVector(partialTick);
						Vec3 viewVec = originalPlayerViewVec;

						double yaw = player.getYRot();
						
						double yawOffset = 90.0;
						if (handPart == MechPart.RightArm)
							yaw += yawOffset;
						else
							yaw -= yawOffset;
						
						double particleStartY = particleYThirdPerson;
						double particleDistance = particleDistanceFromPlayerThirdPerson;
						double perpendicularDist = particlePerpendicularDistanceThirdPerson;
						
						if (playerData.getForSlot(MechPart.Chest).itemStack.getItem() instanceof ArmorBase ab) {
							if (ab.getArmDistance() > 5.0f && playerData.getForSlot(MechPart.Chest).visible) {
								//wide chest armors displace the arms
								perpendicularDist *= 0.93f * ab.getArmDistance() / 5.0f;
							}
						}
						
						class FirstPersonCameraChecker {
							boolean isFirstPerson = false;
						}
						FirstPersonCameraChecker cc = new FirstPersonCameraChecker();
						DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {
							@Override
							public void run() {
								cc.isFirstPerson = Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
							}});
						
						if (player.isLocalPlayer() && cc.isFirstPerson) {
							particleDistance = particleDistanceFromPlayerFirstPerson;
							particleStartY = particleYFirstPerson;
							perpendicularDist = particlePerpendicularDistanceFirstPerson;
						}
						double xComp = perpendicularDist * -Math.sin(Math.toRadians(yaw));
						double zComp = perpendicularDist * Math.cos(Math.toRadians(yaw));
						
						Vec3 viewVecOffset = player.getViewVector(1.0f).scale(particleDistance);
						Vec3 startLoc = player.position().add(viewVecOffset).add(
								new Vec3(xComp, particleStartY, zComp));
						//our rendering is off by 1 tick, add delta movement to account for this
						startLoc.add(player.getDeltaMovement());
						
						if (player.isLocalPlayer() && cc.isFirstPerson) {
							//the particles line up in 3rd person but look very wrong in first person
							//do an entirely new calculation in first person
							double lowerPitchBy = 25.0;
							
							double xRot = player.getXRot();
							xRot += lowerPitchBy;
							double yRot = player.getYRot();

							double look3dDist = 0.5;
							//3d projection with player view vector - with distance 'look3dDist'
							float yComp3 = (float)(look3dDist * Math.sin(Math.toRadians(-xRot)));
							float horizontalComponent = (float)(look3dDist * Math.cos(Math.toRadians(-xRot)));
							float xComp3 = (float)(horizontalComponent * -Math.sin(Math.toRadians(yRot)));
							float zComp3 = (float)(horizontalComponent * Math.cos(Math.toRadians(yRot)));

							double firstPersonStandNextToDist = 0.25;
							xComp = firstPersonStandNextToDist * -Math.sin(Math.toRadians(yaw));
							zComp = firstPersonStandNextToDist * Math.cos(Math.toRadians(yaw));
							
							//positioned next to the player, we'll look in the exact same direction as the player, but slightly lower
							startLoc = player.getEyePosition().add(xComp, 0.0, zComp).add(new Vec3(xComp3, yComp3, zComp3));
							//our rendering is off by 1 tick, add delta movement to account for this
							startLoc.add(player.getDeltaMovement());
							
							viewVec = new Vec3(0.0, 0.0, 0.0);
						}
						
						Vec3 endLoc = hitResult.getLocation();
						if (hitResult instanceof EntityHitResult ehr) {
							Vec3 vecToEntity = ehr.getLocation().subtract(player.position());
							viewVec = originalPlayerViewVec;
							endLoc = player.getEyePosition(partialTick).add(viewVec.scale(vecToEntity.length()));
						}
						this.onSpawnParticles(player, startLoc, endLoc, useTicks, viewVec);

						BlockPos newBlockTarget = null;
						Entity newEntityTarget = null;
						
						float miningPower = this.getMiningPower(useTicks);
						if (hitResult instanceof BlockHitResult bhr) {
							BlockPos pos = bhr.getBlockPos();
							BlockState blockState = player.level().getBlockState(pos);
							if (!blockState.isAir() && !blockState.getFluidState().isSource()) {
								didHit = true;
								float blockDestroySpeed = blockState.getDestroySpeed(player.level(), pos);

								//mineAllBlocks(dbpMapClient, player, miningPower, bhr);
								
								ParticleType particles = ForgeRegistries.PARTICLE_TYPES
										.getValue(ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.BLOCK));
								if (particles != null && blockDestroySpeed > 0.0f) {
									BlockParticleOption blockParticle = new BlockParticleOption(ParticleTypes.BLOCK,
											blockState);
									for (int i = 0; i < 3; ++i) {
										player.level().addParticle(blockParticle,
												endLoc.x + (Math.random() * 1.0 - 0.5),
												endLoc.y + (Math.random() * 1.0 - 0.5),
												endLoc.z + (Math.random() * 1.0 - 0.5), 0.0D, 0.0D, 0.0D);
									}
								}
							}
							
							newBlockTarget = pos;
						} else if (hitResult instanceof EntityHitResult ehr) {
							didHit = true;
							
							newEntityTarget = ehr.getEntity();
						}
						
						if (newBlockTarget != null) {
							//our hitscan yielded a block
							
							if (previousClientTarget != newBlockTarget) {
								BioMechNetwork.INSTANCE.sendToServer(new ServerboundMiningArmBlockTargetPacket(newBlockTarget));
								previousClientTarget = newBlockTarget;
							}
						} else if (newEntityTarget != null) {
							//our hitscan yielded an entity
							
							if (previousClientTarget != newEntityTarget) {
								BioMechNetwork.INSTANCE.sendToServer(new ServerboundMiningArmEntityTargetPacket(newEntityTarget));
								previousClientTarget = newEntityTarget;
							}
						}
						
						
						if (player.tickCount % soundTickPeriod == 0) {
							playSound(player, useTicks, didHit);
						}
					}
				}

				if (!player.level().isClientSide) {
					if (active && useTicks >= START_USING_TICK_COUNT) {
						
						float miningPower = this.getMiningPower(useTicks);
						
						BlockPos blockTarget = blockTargetMap.get(player);
						Entity entityTarget = entityTargetMap.get(player);
						if (entityTarget == null) {
							HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
									(e) -> (e instanceof LivingEntity && !((LivingEntity)e).isDeadOrDying()) && !e.isRemoved() && !e.isSpectator(),
									player.getBlockReach() * blockReachMult);
							
							if (hitResult instanceof BlockHitResult bhr) {
								if (serverEverChecksBlockHits) {
									blockTarget = bhr.getBlockPos();
								}
							} else if (hitResult instanceof EntityHitResult ehr) {
								if (serverEverChecksEntityHits) {
									entityTarget = ehr.getEntity();
								}
							}
						}
						
						if (entityTarget != null) {
							didHit = true;
							Entity e = entityTarget;
							if (e instanceof LivingEntity living) {
								if (!living.isInvulnerable() && !living.isDeadOrDying()) {
									dealEntityDamage(player, bothHandsActive, miningPower, living);
								}
								
								if (living.isDeadOrDying() || living.isRemoved()) {
									entityTargetMap.remove(player);
								}
							}
						} else if (blockTarget != null) {
							BlockState blockState = player.level().getBlockState(blockTarget);
							
							if (!blockState.isAir() && !blockState.getFluidState().isSource()) {
								didHit = true;
								mineAllBlocks(dbpMap, player, miningPower, blockTarget);
							}
						}
						
						
					} else {
						if (bothHandsInactive) {
							DestroyBlockProgressList dbpList = dbpMap.get(player);
							for (DestroyBlockProgress dbp : dbpList.list) {
								if (dbp != null && dbp.pos != null) {
									if (dbp.destroyBlockProgressId != 0) {
										player.level().destroyBlockProgress(dbp.destroyBlockProgressId, dbp.pos, 10);
									}
									dbp.pos = null;
									dbp.progress = 0;
									dbp.destroyBlockProgressId = 0;
								}
							}
						}
					}
				}
				
				if (didHit) {
					if (energyPerTick > 0.0f)
						playerData.spendSuitEnergy(player, energyPerTick);
				} else {
					if (energyPerTickMiss > 0.0f)
						playerData.spendSuitEnergy(player, energyPerTickMiss);
				}
				
				
			} else {
				if (thirdPersonItemStack.getTag() != null && thirdPersonItemStack.getTag().contains("useTicks")) {
					thirdPersonItemStack.getTag().putInt("useTicks", 0);
				}
				if (player.level().isClientSide) {
					this.passiveAnimation(itemStack);
				}
			}
		}
	}

	private void mineAllBlocks(Map<Player, DestroyBlockProgressList> destroyBlockProgressMap, Player player, float miningPower, BlockPos blockTarget) {
		DestroyBlockProgressList dbpList = destroyBlockProgressMap.computeIfAbsent(player,
				(p) -> new DestroyBlockProgressList());
		BlockPos origin = blockTarget;
		int xDiff = xSize/2;
		int yDiff = ySize/2;
		int zDiff = zSize/2;
		int expectedSize = xSize*ySize*zSize;
		if (originMustMatchToolToMineArea) {
			BlockState originState = player.level().getBlockState(origin);
			if (!this.miningTool.isCorrectToolForDrops(originState)) {
				xDiff = 0;
				yDiff = 0;
				zDiff = 0;
				expectedSize = 1;
			}
		}
		BlockPos minPos = origin.relative(Axis.X, -xDiff).relative(Axis.Y, -yDiff).relative(Axis.Z, -zDiff);
		BlockPos maxPos = origin.relative(Axis.X, xDiff).relative(Axis.Y, yDiff).relative(Axis.Z, zDiff);
		Iterable<BlockPos> blocks = BlockPos.betweenClosed(minPos, maxPos);
		int dbpIndex = 0;
		
		for (BlockPos pos : blocks) {
			if (dbpList.list.size() < expectedSize)
				dbpList.list.add(new DestroyBlockProgress());
			DestroyBlockProgress dpb = dbpList.list.get(dbpIndex);
			//new BlockPos is required because 'pos' is a MutableBlockPos
			populateDestroyBlockProgress(player, miningPower, new BlockPos(pos), dpb, pos.equals(origin));
			
			++dbpIndex;
		}
	}

	private int mineBlocksDestructionId = -1;
	private void populateDestroyBlockProgress(Player player, float miningPower, BlockPos pos, DestroyBlockProgress dbp, boolean isOrigin) {
		if (dbp.pos == null || !dbp.pos.equals(pos)) {
			if (dbp.destroyBlockProgressId != 0)
				player.level().destroyBlockProgress(dbp.destroyBlockProgressId, dbp.pos, 10);
			dbp.pos = pos;
			dbp.progress = 0;
			dbp.destroyBlockProgressId = mineBlocksDestructionId--; 
		}
		
		BlockState blockState = player.level().getBlockState(pos);
		float blockDestroySpeed = blockState.getDestroySpeed(player.level(), dbp.pos);
		float toolSpeed = miningTool.getDestroySpeed(blockState);
		boolean isCorrect = miningTool.isCorrectToolForDrops(blockState);
		float penaltyMod = isCorrect ? 1.0f : wrongToolPenalty;
		float miningSpeed = penaltyMod * (toolSpeed / blockDestroySpeed);
		float speedMult = Mth.lerp(miningPower, minSpeedMult, maxSpeedMult);
		
		if (miningSpeed > 0.0f) {
			if (!onlyMinesMatchingBlocks || (isOrigin || isCorrect))
				dbp.progress += Math.max(minMiningProgress, miningSpeed * speedMult);
			
			if (blockState.is(BlockTags.LEAVES)) {
				if (instantDestroyLeaves) {
					dbp.progress = dbp.progressMax;
				}
			}
		}
		
		//if (player.level().isClientSide) {
			player.level().destroyBlockProgress(dbp.destroyBlockProgressId, dbp.pos,
					(int) (10 * (float) dbp.progress / DestroyBlockProgress.progressMax));
		//}
		if (dbp.progress >= DestroyBlockProgress.progressMax) {
			//if (player.level().isClientSide) {
			if (dbp.destroyBlockProgressId != 0) {
				player.level().destroyBlockProgress(dbp.destroyBlockProgressId, dbp.pos, 10);
			}
			//}
			if (!player.level().isClientSide) {
				player.level().destroyBlock(dbp.pos, true);
			}
			
			dbp.pos = null;
			dbp.progress = 0;
			dbp.destroyBlockProgressId = 0;
		}
	}

	protected abstract void playSound(Player player, int useTicks, boolean didHit);
	
	protected abstract void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living);

	protected abstract void onSpawnParticles(Player player, Vec3 startLoc, Vec3 endLoc, int useTicks, Vec3 viewVec);

	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isLeftArm) {
		if (entity instanceof Player player) {
			HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());

			if (has != null && (!has.leftHandActive && isLeftArm || !has.rightHandActive && !isLeftArm)) {
				// this.dispatcher.mining(player, stack);
				List<Item> armorItems = new ArrayList<Item>();
				player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(armorItemStack.getItem()));
				if (armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_ARM.get())
						|| armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get()) || slotId == -1) {
					if (entity instanceof LivingEntity living && !living.isSpectator()) {
						if (level.isClientSide) {
							if (itemStack.getItem() instanceof ArmorBase base) {
								if (player.getMainHandItem().isEmpty()
										&& base.getMechPart() == MechPart.RightArm) {
									this.passiveAnimation(itemStack);
								} else if (player.getOffhandItem().isEmpty()
										&& base.getMechPart() == MechPart.LeftArm) {
									this.passiveAnimation(itemStack);
								} else {
									this.inertAnimation(itemStack);
								}
							}
						}
					}
				}
			}
		}
	}

}
