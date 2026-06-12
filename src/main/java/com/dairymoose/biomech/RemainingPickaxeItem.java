package com.dairymoose.biomech;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class RemainingPickaxeItem extends PickaxeItem {

	public ItemStack remaining = ItemStack.EMPTY;
	
	public RemainingPickaxeItem(Tier p_42961_, Properties p_42964_) {
		super(p_42961_, p_42964_);
	}

	public RemainingPickaxeItem(ItemStack remaining) {
		this(Tiers.IRON, new Properties());
		
		this.remaining = remaining;
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}
	
	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return true;
	}
	
	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
		return this.remaining;
	}
	
}
