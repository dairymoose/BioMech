package com.dairymoose.biomech.menu;

import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BioMechArmorMenu extends AbstractContainerMenu {

	public static final int CONTAINER_ID = 0;
	   public static final int RESULT_SLOT = 0;
	   public static final int CRAFT_SLOT_START = 1;
	   public static final int CRAFT_SLOT_END = 5;
	   public static final int ARMOR_SLOT_START = 5;
	   public static final int ARMOR_SLOT_END = 9;
	   public static final int INV_SLOT_START = 9;
	   public static final int INV_SLOT_END = 36;
	   public static final int USE_ROW_SLOT_START = 36;
	   public static final int USE_ROW_SLOT_END = 45;
	   public static final int SHIELD_SLOT = 45;
	   public static final ResourceLocation BLOCK_ATLAS = new ResourceLocation("textures/atlas/blocks.png");
	   public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
	   public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
	   public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
	   public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
	   public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");
	   static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
	   private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	   public boolean active;
	   private Player owner;
	   private Container container = null;

		public BioMechArmorMenu(int p_39640_, Inventory p_39641_) {
			this(p_39640_, p_39641_, new SimpleContainer(6));
		}
	   
	   public BioMechArmorMenu(int p_39640_, Inventory p_39641_, final Container p_39708_) {
	      super(BioMechRegistry.MENU_TYPE_BIOMECH_STATION.get(), p_39640_);
	      //this.active = p_39641_;
	      //this.owner = p_39708_;
	      //this.addSlot(new ResultSlot(p_39640_.player, this.craftSlots, this.resultSlots, 0, 154, 28));
	      p_39708_.startOpen(p_39641_.player);
	      this.container = p_39708_;
	      
	      this.addSlot(new Slot(p_39708_, 0, 31, 13));
	      this.addSlot(new Slot(p_39708_, 1, 68, 16));
	      this.addSlot(new Slot(p_39708_, 2, 50, 29));
	      this.addSlot(new Slot(p_39708_, 3, 68, 34));
	      this.addSlot(new Slot(p_39708_, 4, 86, 29));
	      this.addSlot(new Slot(p_39708_, 5, 68, 52));

	      for(int l = 0; l < 3; ++l) {
	         for(int j1 = 0; j1 < 9; ++j1) {
	            this.addSlot(new Slot(p_39641_, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
	         }
	      }

	      for(int i1 = 0; i1 < 9; ++i1) {
	         this.addSlot(new Slot(p_39641_, i1, 8 + i1 * 18, 142));
	      }
	   }

	   public void removed(Player p_39721_) {
	      super.removed(p_39721_);
	      if (this.container != null) {
	    	  this.container.stopOpen(p_39721_);
	      }
	   }

	   public boolean stillValid(Player p_39712_) {
	      return true;
	   }

	   public ItemStack quickMoveStack(Player p_39723_, int p_39724_) {
	      ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.slots.get(p_39724_);
	      if (slot.hasItem()) {
	         ItemStack itemstack1 = slot.getItem();
	         itemstack = itemstack1.copy();
	         EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
	         if (p_39724_ == 0) {
	            if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
	               return ItemStack.EMPTY;
	            }

	            slot.onQuickCraft(itemstack1, itemstack);
	         } else if (p_39724_ >= 1 && p_39724_ < 5) {
	            if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (p_39724_ >= 5 && p_39724_ < 9) {
	            if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - equipmentslot.getIndex()).hasItem()) {
	            int i = 8 - equipmentslot.getIndex();
	            if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (equipmentslot == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
	            if (!this.moveItemStackTo(itemstack1, 45, 46, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (p_39724_ >= 9 && p_39724_ < 36) {
	            if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (p_39724_ >= 36 && p_39724_ < 45) {
	            if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.setByPlayer(ItemStack.EMPTY);
	         } else {
	            slot.setChanged();
	         }

	         if (itemstack1.getCount() == itemstack.getCount()) {
	            return ItemStack.EMPTY;
	         }

	         slot.onTake(p_39723_, itemstack1);
	         if (p_39724_ == 0) {
	            p_39723_.drop(itemstack1, false);
	         }
	      }

	      return itemstack;
	   }


}