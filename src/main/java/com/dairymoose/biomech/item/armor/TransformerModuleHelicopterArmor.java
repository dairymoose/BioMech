package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.anim.ElytraMechChestplateDispatcher;
import com.dairymoose.biomech.item.anim.TransformerModuleHelicopterDispatcher;
import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.BroadcastType;
import com.dairymoose.biomech.ToggledStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class TransformerModuleHelicopterArmor extends ArmorBase {

	final TransformerModuleHelicopterDispatcher dispatcher;
	
	public TransformerModuleHelicopterArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.suitEnergyPerSec = 0.5f;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Back;
		
		this.dispatcher = new TransformerModuleHelicopterDispatcher();
	}
	
	@Override
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown, int bonusData, boolean serverOriginator) {
		if (keyIsDown) {
			ToggledStatus status = playerData.helicopterModeEnabled;
			if (status != null) {
				if (bonusData == -1) {
					if (FMLEnvironment.dist == Dist.CLIENT) {
						if (player.level().isClientSide) {
							status.toggledOn = !status.toggledOn;
						}
					} else {
						status.toggledOn = !status.toggledOn;
					}
				} else {
					status.toggledOn = (bonusData == 1 ? true : false);
				}
				if (!status.toggledOn) {
					if (player.getForcedPose() == Pose.FALL_FLYING) {
						player.setForcedPose(null);
					}
				}
				
				this.sendHotkeyToServer(player, keyIsDown, status.toggledOn ? 1 : 0, BroadcastType.SEND_TO_ALL_CLIENTS, serverOriginator);
			}
		}
		super.onHotkeyPressed(player, playerData, keyIsDown, bonusData, serverOriginator);
	}

	@Override
	public void onUnequip(BioMechPlayerData playerData, Player player, ItemStack itemStack, MechPart mechPart) {
		super.onUnequip(playerData, player, itemStack, mechPart);
		
		this.onHotkeyPressed(player, playerData, true, 0, false);
	}
	
	double savedGravity = 0.0;
	public float fwdSpeed = 0.0f;
	public float lateralSpeed = 0.0f;
	public float ySpeed = 0.0f;
	
	public float maxFwdSpeed = 0.12f;
	public float maxLateralSpeed = 0.06f;
	public float maxYSpeed = 0.11f;
	
	public static float addedFwdSpeedPerTick = 0.0045f;
	public static float addedLateralSpeedPerTick = 0.0045f;
	public static float addedYPerTick = 0.0012f;
	public static float ROT_PER_TICK = 120.0f;

	public float rightArmRot = 0.0f;
	public float leftArmRot = 0.0f;
	
	protected float energyPerSec = 3.0f;
	protected float energyPerTick = energyPerSec/20.0f;
	
	@SuppressWarnings("deprecation")
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_TRANSFORMER_MODULE_HELICOPTER.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
					if (playerData != null) {
						boolean active = playerData.helicopterModeEnabled.toggledOn;
						
						if (playerData.getSuitEnergy() < energyPerTick) {
							active = false;
							this.onUnequip(playerData, player, itemStack, mechPart);
						}
						
						if (active) {
							playerData.spendSuitEnergy(player, energyPerTick);
							
							if (level.isClientSide) {
								if (player.getForcedPose() == Pose.FALL_FLYING && !player.isFallFlying()) {

									player.setOnGround(false);

									float g = (float) player.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
									Vec3 delta = player.getDeltaMovement();
									
									rightArmRot -= ROT_PER_TICK;
									leftArmRot += ROT_PER_TICK;
									
									class ImpulseChecker {
										boolean hasImpulse = false;
										Vec2 movementVec = null;
									}
									ImpulseChecker ic = new ImpulseChecker();
									DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {

										@Override
										public void run() {
											ic.hasImpulse = Minecraft.getInstance().player.input.forwardImpulse > 1.0E-5F ||
											Minecraft.getInstance().player.input.forwardImpulse < -1.0E-5F ||
											Minecraft.getInstance().player.input.right || Minecraft.getInstance().player.input.left;
											
											ic.movementVec = Minecraft.getInstance().player.input.getMoveVector();
										}});
									double xComp = 0.0;
									double zComp = 0.0;
									if (player.isLocalPlayer()) { // && ic.hasImpulse && ic.movementVec != null) {
										if (ic.hasImpulse) {
											fwdSpeed += ic.movementVec.y * addedFwdSpeedPerTick;
											lateralSpeed += ic.movementVec.x * addedLateralSpeedPerTick;
										}
										
										if (!ic.hasImpulse || Math.abs(ic.movementVec.y) <= 1E-6)
											fwdSpeed *= 0.988;
										if (!ic.hasImpulse || Math.abs(ic.movementVec.x) <= 1E-6)
											lateralSpeed *= 0.85;
										if (!BioMech.localPlayerJumping && !player.isCrouching()) {
											ySpeed *= 0.95f;
										}
										if (Math.abs(fwdSpeed) <= 1E-6) {
											fwdSpeed = 0.0f;
										}
										if (Math.abs(lateralSpeed) <= 1E-6) {
											lateralSpeed = 0.0f;
										}
										if (Math.abs(ySpeed) <= 1E-6) {
											ySpeed = 0.0f;
										}
										
										fwdSpeed = Math.min(maxFwdSpeed, fwdSpeed);
										fwdSpeed = Math.max(-maxFwdSpeed, fwdSpeed);
										
										lateralSpeed = Math.min(maxLateralSpeed, lateralSpeed);
										lateralSpeed = Math.max(-maxLateralSpeed, lateralSpeed);
										
										xComp = fwdSpeed * -Math.sin(Math.toRadians(player.getYRot())) + lateralSpeed * -Math.sin(Math.toRadians(player.getYRot()-90.0));
										zComp = fwdSpeed * Math.cos(Math.toRadians(player.getYRot())) + lateralSpeed * Math.cos(Math.toRadians(player.getYRot()-90.0));
									}
									
									if (player.horizontalCollision) {
										xComp = 0.0;
										zComp = 0.0;
										fwdSpeed = 0.0f;
										lateralSpeed = 0.0f;
									}
									if (player.verticalCollision) {
										ySpeed = 0.0f;
									}
									
									double dx = delta.x + xComp;
									double dz = delta.z + zComp;
									
									if (BioMech.localPlayerJumping || player.isCrouching()) {
										float toAdd = addedYPerTick;
										if (player.isCrouching()) {
											toAdd = -toAdd;
										}

										boolean reversing = false;
										if (toAdd < 0.0f && ySpeed > 0.0f && delta.y > 0.0f) {
											reversing = true;
										} else if (toAdd > 0.0f && ySpeed < 0.0f && delta.y < 0.0f) {
											reversing = true;
										}
										if (reversing) {
											toAdd *= 12.0f;
										}
										
										ySpeed += toAdd;
										
										ySpeed = Math.min(maxYSpeed, ySpeed);
										ySpeed = Math.max(-maxYSpeed, ySpeed);
										
										double newY = delta.y;
										newY += g;
										
										double yFinal = ySpeed*Math.pow(1.0 - Math.min(1.0f, Math.abs(newY/(maxYSpeed*4.5f))), 0.9);
										newY += yFinal;
										BioMech.LOGGER.trace("delta.y=" + delta.y + ", ySpeed=" + ySpeed +", limiter=" + Math.pow(1.0 - Math.min(1.0f, Math.abs(delta.y/(maxYSpeed*5))), 0.9) + ", total = " + yFinal + ", newY=" + newY);

										player.setDeltaMovement(dx, newY, dz);
									} else {
										
										double dy = delta.y;
										dy += g;
										BlockPos belowPos = player.blockPosition().below();
										BlockState belowState = level.getBlockState(belowPos);
										if (belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
											dy = 0.06;
										} else {
											if (Math.abs(dy) <= 0.02) {
												dy = 0.0;
											} else {
												dy *= 0.982;
											}
										}
										BioMech.LOGGER.info("set dy to: " + dy + " from delta.y=" + delta.y);
										
										player.setDeltaMovement(dx, dy, dz);
									}
									player.resetFallDistance();
								}
							} else {
								BioMech.allowFlyingForPlayer(player);
								player.resetFallDistance();
							}
							
							BioMech.clientSideItemAnimation(itemStack, this.dispatcher.ACTIVE_COMMAND.cmd);
						} else {
							fwdSpeed = 0.0f;
							lateralSpeed = 0.0f;
							ySpeed = 0.0f;
							
							BioMech.clientSideItemAnimation(itemStack, this.dispatcher.INACTIVE_COMMAND.cmd);
						}
					}
				}
			}
		}
	}
	
}
