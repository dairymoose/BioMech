package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IronMechChestArmor extends ArmorBase {

	public IronMechChestArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 40;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Chest;
		this.armDistance = 8.0f;
		this.addToLootTable = false;
		this.damageAbsorbPct = 0.05f;
	}

	//public static DamageSource bioMechAbsorb = new DamageSource(Holder.direct(BioMechRegistry.DAMAGE_TYPE_BIOMECH_ABSORB.get()));
	//public static DamageSource bioMechAbsorb = null;
	//public static DamageSource bioMechAbsorb = new DamageSource(Holder.direct(BioMechRegistry.DAMAGE_TYPE_BIOMECH_ABSORB.get()));
	public static DamageType bioMechAbsorbDamageType = new DamageType("biomech:biomech_absorb", 0.0f);
	public static float energyDamageMultiplier = 15.0f;
	
	public static float getEnergyDamageForAttack(float damageMitigated) {
		float energyDamage = damageMitigated * energyDamageMultiplier;
		return energyDamage;
	}
	
	public static float getDamageMitigated(float absorbPct, float amount) {
		float damageMitigated = absorbPct * amount;
		return damageMitigated;
	}
	
	public static float getDamageAfterMitigation(float amount, float damageMitigated) {
		float damageAfterMitigation = amount - damageMitigated;
		return damageAfterMitigation;
	}
	
	public static boolean absorbDirectAttack(float absorbPct, DamageSource damageSource, float amount, Player player) {
		if (damageSource.type() != bioMechAbsorbDamageType && PipeMechBodyArmor.damageSourceIsDirect(damageSource, player)) {
			float damageMitigated = IronMechChestArmor.getDamageMitigated(absorbPct, amount);
			float damageAfterMitigation = IronMechChestArmor.getDamageAfterMitigation(amount, damageMitigated);
			player.hurt(new DamageSource(Holder.direct(bioMechAbsorbDamageType)), damageAfterMitigation);
			
			return true;
		}
		
		return false;
	}
	
	public static float getTotalDamageAbsorbPct(Player player) {
		float absorbPct = 0.0f;
		
		BioMechPlayerData playerData = null;
    	playerData = BioMech.globalPlayerData.get(player.getUUID());
    	if (playerData != null) {
    		List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						absorbPct += base.getDamageAbsorbPercent();
					}
				}
			}
    	}
    	
    	return absorbPct;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_IRON_MECH_CHESTPLATE.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}
	
}
