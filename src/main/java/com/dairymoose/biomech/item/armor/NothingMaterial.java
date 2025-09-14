package com.dairymoose.biomech.item.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

public class NothingMaterial implements ArmorMaterial {

	@Override
	public int getDurabilityForType(Type type) {
		return ArmorMaterials.IRON.getDefenseForType(type);
	}

	@Override
	public int getDefenseForType(Type type) {
		return 0;
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public SoundEvent getEquipSound() {
		return ArmorMaterials.IRON.getEquipSound();
	}

	@Override
	public Ingredient getRepairIngredient() {
		return ArmorMaterials.IRON.getRepairIngredient();
	}

	@Override
	public String getName() {
		return "nothing";
	}

	@Override
	public float getToughness() {
		return 0;
	}

	@Override
	public float getKnockbackResistance() {
		return 0;
	}

}
