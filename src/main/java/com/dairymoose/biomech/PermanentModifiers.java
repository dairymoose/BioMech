package com.dairymoose.biomech;

import net.minecraft.resources.ResourceLocation;

public class PermanentModifiers {

	// 1.21 AttributeModifiers are keyed by ResourceLocation instead of UUID.
	public static ResourceLocation chestBoost = ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "chest_boost");
	public static ResourceLocation rightArmBoost = ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "right_arm_boost");
	public static ResourceLocation leftArmBoost = ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "left_arm_boost");
	public static ResourceLocation rightArmBoost2 = ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "right_arm_boost2");
	public static ResourceLocation leftArmBoost2 = ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "left_arm_boost2");

}
