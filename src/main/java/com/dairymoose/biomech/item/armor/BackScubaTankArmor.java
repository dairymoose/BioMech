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

public class BackScubaTankArmor extends ArmorBase {

	public BackScubaTankArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = false;
		this.mechPart = MechPart.Back;
	}

	public static float energyPerSec = 0.25f;
	public static float energyPerTick = energyPerSec / 20.0f;
	public static float MIN_ENERGY_TO_USE = 0.70f;
	public static int AIR_INCREMENT = 3;
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_BACK_SCUBA_TANK.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (living.isUnderWater()) {
						BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
						
						boolean active = false;
						if (playerData != null) {
							if (playerData.getSuitEnergy() < energyPerTick || living.hasEffect(MobEffects.WATER_BREATHING) || living.hasEffect(MobEffects.CONDUIT_POWER)) {
								active = false;
							} else {
								if (playerData.canRegenEnergy(player) && playerData.getSuitEnergyPercent() < MIN_ENERGY_TO_USE) {
									active = false;
								} else
									active = true;
							}
							
							if (active) {
								playerData.spendSuitEnergy(player, energyPerTick);
								int air = player.getAirSupply();
								int maxAir = player.getMaxAirSupply();
								air += AIR_INCREMENT;
								if (air > maxAir) {
									air = maxAir;
								}
								
								player.setAirSupply(air);
							}
						}
					}
				}
			}
		}
	}
	
}
