package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class BackJetpackArmor extends ArmorBase {

	public BackJetpackArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.hidePlayerModel = false;
		this.mechPart = MechPart.Back;
	}

	//public static float energyPerSecMaxDrain = -0.5f;
	public static boolean jetpackPreviouslyActive = false;
	public static boolean jetpackInputActive = false;
	public static float energyPerSec = 7.0f;
	public static float energyPerTick = energyPerSec / 20.0f;
	
	public static float yPerTickStage1 = 0.13f;
	public static float yPerTickStage2 = 0.092f;
	public static float fallFlyingBoost = 0.095f;
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_BACK_JETPACK.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					boolean modifierActive = BioMech.localPlayerHoldingAlt || BioMech.primedForMidairJump;
					
					BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
					if (!living.isSpectator() && modifierActive && BioMech.localPlayerJumping) {
						jetpackInputActive = true;
					} else if (!living.isSpectator() && jetpackPreviouslyActive && BioMech.localPlayerJumping) {
						jetpackInputActive = true;
					}
					
					if (jetpackInputActive && !BioMech.localPlayerJumping) {
						jetpackInputActive = false;
						jetpackPreviouslyActive = true;
					}
					
					if (player.onGround()) {
						jetpackPreviouslyActive = false;
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
								
								if (!player.isFallFlying()) {
									float deltaY = 0.0f;
									if (player.getDeltaMovement().y <= 0.20f) {
										deltaY = yPerTickStage1;
										BioMech.LOGGER.info("yPerTickStage1, " + player.getDeltaMovement().y);
									} else if (player.getDeltaMovement().y < 0.30) {
										deltaY = yPerTickStage2;
										BioMech.LOGGER.info("yPerTickStage2, " + player.getDeltaMovement().y);
									} else {
										double movementSpeedSqr = player.getDeltaMovement().y*player.getDeltaMovement().y;
										deltaY = yPerTickStage2;
										if (movementSpeedSqr > 0.0) {
											//acceleration is slowed down as we speed up to simulate wind resistance
											//starts at 11.22*0.09=1.01
											deltaY /= 11.22f*movementSpeedSqr;
											BioMech.LOGGER.info("deltaY=" + deltaY + " vs yPerTickStage2=" + yPerTickStage2);
										}
										
										BioMech.LOGGER.info("yPerTickStageFinal, " + player.getDeltaMovement().y);
									}
									
									player.addDeltaMovement(new Vec3(0.0f, deltaY, 0.0f));
									if (player.getDeltaMovement().y >= -0.3) {
										player.resetFallDistance();
									} else if (player.getDeltaMovement().y >= -0.4) {
										player.fallDistance = 1.0f;
									} else if (player.getDeltaMovement().y >= -0.5) {
										player.fallDistance = 1.5f;
									} else if (player.getDeltaMovement().y >= -0.6) {
										player.fallDistance = 2.0f;
									} else if (player.getDeltaMovement().y >= -0.7) {
										player.fallDistance = 2.5f;
									} else if (player.getDeltaMovement().y >= -0.9) {
										player.fallDistance = 3.0f;
									} else if (player.getDeltaMovement().y >= -1.3) {
										player.fallDistance = 3.5f;
									} else {
										player.fallDistance -= 0.01f;
									}
								} else {
									float pitch = player.getXRot();
									float yaw = player.getYRot();
									
									double movementSpeedSqr = player.getDeltaMovement().lengthSqr();
									float calcFallFlyingBoost = fallFlyingBoost;
									//22 bps / 20 = 1.1 bpt = 1.21 movementSpeedSqr
									if (movementSpeedSqr >= 1.21) {
										//acceleration is slowed down as we speed up to simulate wind resistance
										//starts at 0.84*1.21=1.02
										calcFallFlyingBoost /= 0.84f*movementSpeedSqr;
										BioMech.LOGGER.info("calcFallFlyingBoost=" + calcFallFlyingBoost + " vs fallFlyingBoost=" + fallFlyingBoost);
									}
									
									float yComponent = (float)(calcFallFlyingBoost * Math.sin(Math.toRadians(-pitch)));
									float horizontalComponent = (float)(calcFallFlyingBoost * Math.cos(Math.toRadians(-pitch)));
									float xComponent = (float)(horizontalComponent * -Math.sin(Math.toRadians(yaw)));
									float zComponent = (float)(horizontalComponent * Math.cos(Math.toRadians(yaw)));
									
									player.addDeltaMovement(new Vec3(xComponent, yComponent, zComponent));
								}
							}
						}
					}
				}
			}
		}
	}
	
}
