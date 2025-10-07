package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;

import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PipeMechBodyArmor extends ArmorBase {

	public PipeMechBodyArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 70;
		this.suitEnergyPerSec = 1.0f;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Chest;
		this.damageAvoidPct = 0.05f;
	}

	public static float energyLostFromAvoidAttack = 5.0f;
	
	public static boolean damageSourceIsDirect(DamageSource damageSource, Player player) {
		return damageSource.type().effects() == DamageEffects.HURT && damageSource.type() != player.damageSources().magic().type() && damageSource.type() != player.damageSources().fall().type();
	}
	
	public static boolean avoidDirectAttack(float avoidPct, DamageSource damageSource, float amount, Player player) {
		//avoid dodging burn ticks + poison ticks
		if (PipeMechBodyArmor.damageSourceIsDirect(damageSource, player)) {
			double rnd = Math.random();
			if (rnd < avoidPct) {
				return true;
			}
		} else {
			//BioMech.LOGGER.info("ignoring damage of type: " + damageSource);
		}
		
		return false;
	}
	
	public static float getTotalDamageAvoidPct(Player player) {
		float avoidPct = 0.0f;
		
		BioMechPlayerData playerData = null;
    	playerData = BioMech.globalPlayerData.get(player.getUUID());
    	if (playerData != null) {
    		List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						avoidPct += base.getDamageAvoidPercent();
					}
				}
			}
    	}
    	
    	return avoidPct;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_PIPE_MECH_BODY.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}
	
}
