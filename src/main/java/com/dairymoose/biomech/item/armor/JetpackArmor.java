package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JetpackArmor extends ArmorBase {

	public JetpackArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Back;
	}

	//public static float energyPerSecMaxDrain = -0.5f;
	public static boolean jetpackPreviouslyActive = false;
	public static boolean jetpackInputActive = false;
	public static float energyPerSec = 7.0f;
	public static float energyPerTick = energyPerSec / 20.0f;
	
	public static float yPerTickStage1 = 0.117f;
	public static float yPerTickStage2 = 0.092f;
	public static float fallFlyingBoost = 0.095f;
	
	public static Map<UUID, Double> lastY = new HashMap<>();
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_JETPACK.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());
					if (has != null) {
						BioMech.MidAirJumpStatus maj = BioMech.primedForMidAirJumpMap.get(entity.getUUID());
						boolean modifierActive = has.modifierKeyActive || (maj != null && maj.primedForMidAirJump);
						
						BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
						if (!living.isSpectator() && modifierActive && has.jumpActive) {
							jetpackInputActive = true;
						} else if (!living.isSpectator() && jetpackPreviouslyActive && has.jumpActive) {
							jetpackInputActive = true;
						}
						
						if (jetpackInputActive && !has.jumpActive) {
							jetpackInputActive = false;
							jetpackPreviouslyActive = true;
						}
						
						if (player.onGround()) {
							jetpackPreviouslyActive = false;
						} else {
							BioMech.allowFlyingForPlayer(player);
						}
						
						if (jetpackInputActive) {
							
							boolean active = false;
							if (playerData != null) {
								if (playerData.getSuitEnergy() < energyPerTick) {
									active = false;
								} else {
									active = true;
								}
								
								if (active) {
									playerData.spendSuitEnergy(player, energyPerTick);
									
									boolean isFlyingOrSwimming = player.isFallFlying() || player.isSwimming();
									if (!isFlyingOrSwimming) {
										if (player.isLocalPlayer() || !level.isClientSide) {
											float deltaY = 0.0f;
											if (player.getDeltaMovement().y <= 0.20f) {
												deltaY = yPerTickStage1;
												//BioMech.LOGGER.info("yPerTickStage1, " + player.getDeltaMovement().y);
											} else if (player.getDeltaMovement().y < 0.30) {
												deltaY = yPerTickStage2;
												//BioMech.LOGGER.info("yPerTickStage2, " + player.getDeltaMovement().y);
											} else {
												double movementSpeedSqr = player.getDeltaMovement().y*player.getDeltaMovement().y;
												deltaY = yPerTickStage2;
												if (movementSpeedSqr > 0.0) {
													//acceleration is slowed down as we speed up to simulate wind resistance
													//starts at 11.22*0.09=1.01
													deltaY /= 11.22f*movementSpeedSqr;
													//BioMech.LOGGER.info("deltaY=" + deltaY + " vs yPerTickStage2=" + yPerTickStage2);
												}
												
												//BioMech.LOGGER.info("yPerTickStageFinal, " + player.getDeltaMovement().y);
											}
											if (player.isUnderWater()) {
												deltaY *= 0.15f;
											}
											
											player.addDeltaMovement(new Vec3(0.0f, deltaY, 0.0f));
											if (!level.isClientSide) {
												double yLastTick = lastY.computeIfAbsent(player.getUUID(), (uuid) -> player.getY());
												lastY.put(player.getUUID(), player.getY());
												double apparentYSpeed = player.getY() - yLastTick;
												//BioMech.LOGGER.info("apparentYSpeed=" + apparentYSpeed);
												if (apparentYSpeed >= -0.3) {
													player.resetFallDistance();
												} else if (apparentYSpeed >= -0.4) {
													player.fallDistance = 1.0f;
												} else if (apparentYSpeed >= -0.5) {
													player.fallDistance = 1.5f;
												} else if (apparentYSpeed >= -0.6) {
													player.fallDistance = 2.0f;
												} else if (apparentYSpeed >= -0.7) {
													player.fallDistance = 2.5f;
												} else if (apparentYSpeed >= -0.9) {
													player.fallDistance = 3.0f;
												} else if (apparentYSpeed >= -1.3) {
													player.fallDistance = 3.5f;
												} else {
													player.fallDistance -= 0.01f;
												}
											}
										}
									} else {
										if (player.isLocalPlayer() || !level.isClientSide) {
											float pitch = player.getXRot();
											float yaw = player.getYRot();
											
											double movementSpeedSqr = player.getDeltaMovement().lengthSqr();
											float calcFallFlyingBoost = fallFlyingBoost;
											//22 bps / 20 = 1.1 bpt = 1.21 movementSpeedSqr
											if (movementSpeedSqr >= 1.21) {
												//acceleration is slowed down as we speed up to simulate wind resistance
												//starts at 0.84*1.21=1.02
												calcFallFlyingBoost /= 0.84f*movementSpeedSqr;
												//BioMech.LOGGER.info("calcFallFlyingBoost=" + calcFallFlyingBoost + " vs fallFlyingBoost=" + fallFlyingBoost);
											}
											if (player.isSwimming()) {
												calcFallFlyingBoost *= 0.33f;
											}
											
											float yComponent = (float)(calcFallFlyingBoost * Math.sin(Math.toRadians(-pitch)));
											float horizontalComponent = (float)(calcFallFlyingBoost * Math.cos(Math.toRadians(-pitch)));
											float xComponent = (float)(horizontalComponent * -Math.sin(Math.toRadians(yaw)));
											float zComponent = (float)(horizontalComponent * Math.cos(Math.toRadians(yaw)));
											
											player.addDeltaMovement(new Vec3(xComponent, yComponent, zComponent));
										}
									}
									
									float particleY = 0.6f;
									float particleDepth = -0.27f;
									
									float smokeYOffset = 0.12f;
									float smokeDepthOffset = 0.0f;
									float[] angleAdjust = { -22.0f, 22.0f};
									float angleJitter = 5.0f;
									if (isFlyingOrSwimming) {
										particleY = 0.25f;
										particleDepth = -0.05f;
										
										smokeYOffset = 0.0f;
										smokeDepthOffset = -0.03f;
										
										angleAdjust[0] = -80.0f;
										angleAdjust[1] = 80.0f;
										angleJitter = 10.0f;
									}
									for (int a=0; a<angleAdjust.length; ++a) {
										float angle = player.yBodyRot + angleAdjust[a];
										angle += (Math.random() - 0.5f) * 2.0f*angleJitter;
										
										double xComp = -Math.sin(Math.toRadians(angle));
										double zComp = Math.cos(Math.toRadians(angle));
										Vec3 loc = player.position().add(
												new Vec3(particleDepth * xComp, particleY, particleDepth * zComp));
										Vec3 smokeLoc = loc.add(new Vec3(smokeDepthOffset * xComp, smokeYOffset, smokeDepthOffset * zComp));
										
										if (!isFlyingOrSwimming || player.tickCount % 2 == 0) {
											player.level().addParticle((ParticleOptions) BioMechRegistry.PARTICLE_TYPE_INSTANT_SMOKE.get(), smokeLoc.x, smokeLoc.y, smokeLoc.z,
													0.0f, 0.0f, 0.0f);
										}
										
										if (player.tickCount % 2 == 0) {
											player.level().addParticle(ParticleTypes.SMALL_FLAME, loc.x, loc.y, loc.z,
													0.0f, -0.4f, 0.0f);
										}
										if (player.tickCount % 5 == 0) {
											float volume = 0.6f;
											float pitch = 1.0f; 
											player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_JETPACK_LOOP.get(), SoundSource.PLAYERS, volume, pitch, false);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
}
