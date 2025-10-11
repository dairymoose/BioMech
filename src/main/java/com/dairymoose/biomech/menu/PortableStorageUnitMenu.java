package com.dairymoose.biomech.menu;

import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PortableStorageUnitMenu extends AbstractContainerMenu {

	public boolean active;
	private Container container = null;
	private final int containerRows = BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY/9;

	public PortableStorageUnitMenu(int p_39640_, Inventory p_39641_) {
		this(p_39640_, p_39641_, new SimpleContainer(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY));
	}

	public PortableStorageUnitMenu(int p_39640_, Inventory inventory, final Container storageUnitContainer) {
		super(BioMechRegistry.MENU_TYPE_PORTABLE_STORAGE_UNIT.get(), p_39640_);
		storageUnitContainer.startOpen(inventory.player);
		this.container = storageUnitContainer;

		// first 63 slots: [0 - 62]
		for (int l = 0; l < 7; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(storageUnitContainer, j1 + l*9, 8 + j1 * 18, 8 + l * 18));
			}
		}

		// next 27 slots: [63 - 89]
		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 138 + l * 18));
			}
		}

		// next 9 slots: [90 - 98]
		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 196));
		}
	}

	public void removed(Player player) {
		super.removed(player);
		if (this.container != null) {
			this.container.stopOpen(player);
		}
	}

	public boolean stillValid(Player player) {
		return true;
	}

	public ItemStack quickMoveStack(Player p_39253_, int p_39254_) {
	      ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.slots.get(p_39254_);
	      if (slot != null && slot.hasItem()) {
	         ItemStack itemstack1 = slot.getItem();
	         itemstack = itemstack1.copy();
	         if (p_39254_ < this.containerRows * 9) {
	            if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 9, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.setByPlayer(ItemStack.EMPTY);
	         } else {
	            slot.setChanged();
	         }
	      }

	      return itemstack;
	   }

}