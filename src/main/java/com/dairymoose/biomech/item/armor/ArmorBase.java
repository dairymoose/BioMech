package com.dairymoose.biomech.item.armor;

import java.util.List;

import com.dairymoose.biomech.client.screen.BioMechStationScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class ArmorBase extends ArmorItem {

	protected MechPart mechPart = null;
	protected boolean hidePlayerModel = false;
	protected int suitEnergy = 0;
	private static ArmorMaterial NOTHING_MATERIAL = new NothingMaterial();
	
	public ArmorBase(ArmorMaterial material, Type type, Properties props) {
		super(NOTHING_MATERIAL, type, props);
	}
	
	public Item getLeftArmItem() {
		return null;
	}

	public boolean shouldHidePlayerModel() {
		return this.hidePlayerModel;
	}
	
	public MechPart getMechPart() {
		return this.mechPart;
	}
	
	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return false;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> comp, TooltipFlag flags) {
		super.appendHoverText(stack, level, comp, flags);
		if (level.isClientSide && Minecraft.getInstance().screen instanceof BioMechStationScreen) {
			comp.add(Component.translatable("item.biomech.generic.tooltip2"));
		}
		else {
			comp.add(Component.translatable("item.biomech.generic.tooltip"));
		}
		if (suitEnergy > 0) {
			MutableComponent suitEnergyTt = Component.translatable("tooltip.biomech.suitenergy");
			comp.add(Component.literal("§2+" + suitEnergy + " " + suitEnergyTt.getString() + "§0"));
		}
		comp.add(Component.empty());
		MutableComponent t1 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip1", "");
		MutableComponent t2 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip2", "");
		MutableComponent t3 = Component.translatableWithFallback("item.biomech." + ForgeRegistries.ITEMS.getKey(this).getPath() + ".tooltip3", "");
		if (!"".equals(t1.getString())) {
			comp.add(t1);
		}
		if (!"".equals(t2.getString())) {
			comp.add(t2);
		}
		if (!"".equals(t3.getString())) {
			comp.add(t3);
		}
	}
	
}
