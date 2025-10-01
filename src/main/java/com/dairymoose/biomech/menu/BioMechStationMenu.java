package com.dairymoose.biomech.menu;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class BioMechStationMenu extends AbstractContainerMenu {

	   public boolean active;
	   private Container container = null;

		public BioMechStationMenu(int p_39640_, Inventory p_39641_) {
			this(p_39640_, p_39641_, new SimpleContainer(6));
		}
	   
		public static MechPart[] mechPartsBySlot = {MechPart.Back, MechPart.Head, MechPart.RightArm, MechPart.Chest, MechPart.LeftArm, MechPart.Leggings};
		public static int[] xCoordinatesBySlot = {31, 68, 50, 68, 86, 68};
		public static int[] yCoordinatesBySlot = {13, 16, 29, 34, 29, 52};
		public static int[] mechPartsByOrdinal = {1, 3, 5, 4, 2, 0};
		public BioMechStationMenu(int p_39640_, Inventory inventory, final Container stationContainer) {
	      super(BioMechRegistry.MENU_TYPE_BIOMECH_STATION.get(), p_39640_);
	      //this.active = p_39641_;
	      //this.owner = p_39708_;
	      //this.addSlot(new ResultSlot(p_39640_.player, this.craftSlots, this.resultSlots, 0, 154, 28));
	      stationContainer.startOpen(inventory.player);
	      this.container = stationContainer;
	      
	      BioMechPlayerData playerData = BioMech.globalPlayerData.computeIfAbsent(inventory.player.getUUID(), (uuid) -> new BioMechPlayerData());
	      
	      this.container.clearContent();
	      
	      //first 6 slots: [0 - 5]
	      int slotIdCounter = 0;
	      for (MechPart mechPart : mechPartsBySlot) {
	    	  this.container.setItem(slotIdCounter, playerData.getForSlot(mechPart).itemStack);
	    	  
	    	  this.addSlot(new Slot(stationContainer, slotIdCounter, xCoordinatesBySlot[slotIdCounter], yCoordinatesBySlot[slotIdCounter]) {
		    	  public void setByPlayer(ItemStack p_270969_) {
		               BioMechStationMenu.onEquipItem(inventory.player, mechPart, p_270969_, this.getItem());
		               super.setByPlayer(p_270969_);
		            }

		            public int getMaxStackSize() {
		               return 1;
		            }

		            public boolean mayPlace(ItemStack p_39746_) {
		               return (p_39746_.getItem() instanceof ArmorBase && ((ArmorBase) p_39746_.getItem()).getMechPart() == mechPart) ||
		            		   (mechPart == MechPart.LeftArm && (p_39746_.getItem() instanceof ArmorBase && ((ArmorBase) p_39746_.getItem()).getMechPart() == MechPart.RightArm));
		            }

		            public boolean mayPickup(Player p_39744_) {
		               ItemStack itemstack = this.getItem();
		               return !itemstack.isEmpty() && !p_39744_.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(p_39744_);
		            }
		            
		            @Override
		            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		            	return super.getNoItemIcon();
		            }
		      });
	    	  
	    	  ++slotIdCounter;
	      }

	      //next 27 slots: [6 - 32]
	      for(int l = 0; l < 3; ++l) {
	         for(int j1 = 0; j1 < 9; ++j1) {
	            this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
	         }
	      }

	      //next 9 slots: [33 - 41]
	      for(int i1 = 0; i1 < 9; ++i1) {
	         this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
	      }
	   }

		public Slot getSlotForMechPart(MechPart part) {
			if (part != null) {
				return this.slots.get(mechPartsByOrdinal[part.ordinal()]);
			}

			return null;
		}

		static void onEquipItem(Player player, MechPart mechPart, ItemStack newItem, ItemStack oldItem) {
			BioMechPlayerData playerData = BioMech.globalPlayerData.computeIfAbsent(player.getUUID(), (uuid) -> new BioMechPlayerData());
			if (playerData != null) {
				BioMech.LOGGER.debug("onEquipItem: " + mechPart + " with " + newItem);
				playerData.setForSlot(mechPart, newItem);
			}
			if (!player.level().isClientSide)
				BioMech.sendItemSlotUpdateForPlayer(player);
		}

		public void removed(Player player) {
			super.removed(player);
			if (this.container != null) {
				this.container.clearContent();
				this.container.stopOpen(player);

				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					playerData.recalculateSuitEnergyMax();
				}
			}
		}

		public boolean stillValid(Player player) {
			return true;
		}

		public ItemStack quickMoveStack(Player p_39723_, int slotIndex) {
			ItemStack itemstack = ItemStack.EMPTY;
			Slot slot = this.slots.get(slotIndex);
			if (slot.hasItem()) {
				ItemStack itemstack1 = slot.getItem();
				itemstack = itemstack1.copy();
				if (itemstack.getItem() instanceof ArmorBase base) {
					if (slotIndex < mechPartsBySlot.length) {
						//inventory slots are all beyond 5
						if (!this.moveItemStackTo(itemstack1, 6, 42, false)) {
				            return ItemStack.EMPTY;
				         }
					} else {
						MechPart part = base.getMechPart();
						Slot mechSlot = this.getSlotForMechPart(part);
						if (mechSlot != null) {
							if (!mechSlot.hasItem()) {
								if (!this.moveItemStackTo(itemstack1, mechSlot.index, mechSlot.index + 1, false)) {
									return ItemStack.EMPTY;
								}
							} else {
								if (part == MechPart.RightArm) {
									Slot leftArmSlot = this.getSlotForMechPart(MechPart.LeftArm);
									if (leftArmSlot != null && !leftArmSlot.hasItem()) {
										if (!this.moveItemStackTo(itemstack1, leftArmSlot.index, leftArmSlot.index + 1, false)) {
											return ItemStack.EMPTY;
										}
									}
								}
							}
						}
					}
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
				if (slotIndex == 0) {
					p_39723_.drop(itemstack1, false);
				}
			}

			return itemstack;
		}


}