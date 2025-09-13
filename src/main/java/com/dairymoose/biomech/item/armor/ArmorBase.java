package com.dairymoose.biomech.item.armor;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class ArmorBase extends ArmorItem {

	public ArmorBase(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return true;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> comp, TooltipFlag flags) {
		super.appendHoverText(stack, level, comp, flags);
		comp.add(Component.translatable("item.biomech.generic.tooltip"));
		comp.add(Component.translatable("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip"));
	}
	
}
