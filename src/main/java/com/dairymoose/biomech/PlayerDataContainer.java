package com.dairymoose.biomech;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerDataContainer implements Container {
	private BioMechPlayerData playerData;
	
	private NonNullList<ItemStack> items() {
		return this.playerData.portableStorageUnitItems;
	}
	
	public PlayerDataContainer(BioMechPlayerData playerData) {
		this.playerData = playerData;
	}

	@Override
	public void clearContent() {
		this.items().clear();
	}

	@Override
	public int getContainerSize() {
		return this.items().size();
	}

	@Override
	public boolean isEmpty() {
		return this.items().isEmpty();
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		return this.items().get(p_18941_);
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		ItemStack itemstack = ContainerHelper.removeItem(this.items(), p_18942_, p_18943_);
		if (!itemstack.isEmpty()) {
			this.setChanged();
		}

		return itemstack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		return ContainerHelper.takeItem(this.items(), p_18951_);
	}

	@Override
	public void setItem(int p_18944_, ItemStack p_18945_) {
		this.items().set(p_18944_, p_18945_);
	      if (p_18945_.getCount() > this.getMaxStackSize()) {
	    	  p_18945_.setCount(this.getMaxStackSize());
	      }

	      this.setChanged();
	}

	@Override
	public void setChanged() {
		;
	}

	@Override
	public boolean stillValid(Player p_18946_) {
		return true;
	}
	
}
