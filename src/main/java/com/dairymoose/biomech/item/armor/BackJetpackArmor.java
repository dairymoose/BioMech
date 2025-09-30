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
	
	public static float yPerTickStage2 = 0.115f;
	public static float yPerTickStage3 = 0.087f;
	public static float yPerTickFalling = 0.12f;
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
								
								float deltaY = yPerTickFalling;
								if (player.getDeltaMovement().y <= 0.0f) {
									deltaY = yPerTickStage2;
									BioMech.LOGGER.info("yPerTickFalling, " + player.getDeltaMovement().y);
								} else if (player.getDeltaMovement().y <= 0.20f) {
									deltaY = yPerTickStage2;
									BioMech.LOGGER.info("yPerTickStage2, " + player.getDeltaMovement().y);
								} else {
									deltaY = yPerTickStage3;
									BioMech.LOGGER.info("yPerTickStage3, " + player.getDeltaMovement().y);
								}
								
								player.addDeltaMovement(new Vec3(0.0f, deltaY, 0.0f));
								if (player.getDeltaMovement().y >= 0.0) {
									BioMech.LOGGER.info("reset fallDistance");
									player.resetFallDistance();
								} else if (player.getDeltaMovement().y >= -0.3) {
									BioMech.LOGGER.info("set fallDistance=0.0");
									player.fallDistance = 0.0f;
								} else if (player.getDeltaMovement().y >= -0.4) {
									BioMech.LOGGER.info("set fallDistance=1.0");
									player.fallDistance = 1.0f;
								} else if (player.getDeltaMovement().y >= -0.5) {
									BioMech.LOGGER.info("set fallDistance=2.0");
									player.fallDistance = 2.0f;
								} else if (player.getDeltaMovement().y >= -0.6) {
									BioMech.LOGGER.info("set fallDistance=3.0");
									player.fallDistance = 3.0f;
								} else if (player.getDeltaMovement().y >= -0.7) {
									BioMech.LOGGER.info("set fallDistance=4.0");
									player.fallDistance = 4.0f;
								} else if (player.getDeltaMovement().y >= -0.9) {
									BioMech.LOGGER.info("set fallDistance=4.0");
									player.fallDistance = 5.0f;
								} else if (player.getDeltaMovement().y >= -1.3) {
									BioMech.LOGGER.info("set fallDistance=4.0");
									player.fallDistance = 6.0f;
								} else {
									player.fallDistance -= 0.01f;
								}
							} else {
								//playerData.spendSuitEnergy(player, 0.0f);
							}
						}
					}
				}
			}
		}
	}
	
}
