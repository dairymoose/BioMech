package com.dairymoose.biomech.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class PortableStorageUnitMenu extends AbstractContainerMenu {

	public boolean active;
	private Container container = null;
	private final int containerRows = BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER/9;
	private Player player;
	private ContainerLevelAccess access;
	
	private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
	private final ResultContainer resultSlots = new ResultContainer();
	
	private List<Slot> inventorySlots = new ArrayList<>();
	private List<Slot> craftingSlots = new ArrayList<>();

	public PortableStorageUnitMenu(int p_39640_, Inventory p_39641_) {
		this(p_39640_, p_39641_, new SimpleContainer(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY));
	}

	public List<Slot> getInventorySlots() {
		return this.inventorySlots;
	}
	
	public List<Slot> getCraftingSlots() {
		return this.craftingSlots;
	}
	
	public PortableStorageUnitMenu(int p_39640_, Inventory inventory, final Container storageUnitContainer) {
		super(BioMechRegistry.MENU_TYPE_PORTABLE_STORAGE_UNIT.get(), p_39640_);
		storageUnitContainer.startOpen(inventory.player);
		this.container = storageUnitContainer;
		this.player = inventory.player;
		this.access = ContainerLevelAccess.create(inventory.player.level(), inventory.player.blockPosition());

		int rows = BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER/9;
		
		// first 63 slots: [0 - 62] - no crafter
		// first 45 slots: [0 - 44] - with crafter
		int storageLeft = 8;
		int storageTop = 8;
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			storageTop += 59;
		}
		for (int l = 0; l < rows; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				Slot s = new Slot(storageUnitContainer, j1 + l*9, storageLeft + j1 * 18, storageTop + l * 18);
				this.inventorySlots.add(s);
				this.addSlot(s);
			}
		}
		
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			int craftingTableLeft = 34;
			int craftingTableTop = 8;
			for (int l = 0; l < 3; ++l) {
				for (int j1 = 0; j1 < 3; ++j1) {
					Slot s = new Slot(craftSlots, j1 + l*3, craftingTableLeft + j1 * 18, craftingTableTop + l * 18);
					this.craftingSlots.add(s);
					this.addSlot(s);
				}
			}
			
			int resultSlotLeft = 128;
			int resultSlotTop = 26;
			this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 3*3, resultSlotLeft, resultSlotTop));
		}
		
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			if (!player.level().isClientSide) {
				//populate crafter and result slots from playerData
				BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
				if (playerData != null) {
					//Populate the temporary containers for crafting/result slots
					int k=0;
					for (int i=BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER; i<(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER + 9); ++i) {
						ItemStack itemStack = playerData.portableStorageUnitItems.get(i);
						BioMech.LOGGER.debug("add to crafting slots: " + itemStack);
						this.craftSlots.setItem(k, itemStack);
						
						++k;
					}
					
					this.resultSlots.setItem(0, playerData.portableStorageUnitItems.get(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER + 9));
				}
			}
		}

		int inventoryLeft = 8;
		int inventoryTop = 138;
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			inventoryTop += 23;
		}
		// next 27 slots: [63 - 89]
		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				Slot s = new Slot(inventory, j1 + (l + 1) * 9, inventoryLeft + j1 * 18, inventoryTop + l * 18);
				this.inventorySlots.add(s);
				this.addSlot(s);
			}
		}

		int hotbarLeft = 8;
		int hotbarTop = 196;
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			hotbarTop += 23;
		}
		// next 9 slots: [90 - 98]
		for (int i1 = 0; i1 < 9; ++i1) {
			Slot s = new Slot(inventory, i1, hotbarLeft + i1 * 18, hotbarTop);
			this.inventorySlots.add(s);
			this.addSlot(s);
		}
	}

	public void removed(Player player) {
		super.removed(player);
		if (this.container != null) {
			this.container.stopOpen(player);
		}
		
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
			if (playerData != null) {
				//This container is a temporary container
				//It will go away when the inventory is removed
				//Instead, we will copy the items to our PortableStorageUnit inventory
				int k=0;
				for (ItemStack itemStack : this.craftSlots.getItems()) {
					BioMech.LOGGER.debug("save from crafting slots into PSU storage: " + itemStack);
					playerData.portableStorageUnitItems.set(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER + k, itemStack);
					
					++k;
				}
				playerData.portableStorageUnitItems.set(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER + 9, this.resultSlots.getItem(0));
			}
		}
		
//		this.access.execute((p_39371_, p_39372_) -> {
//			this.clearContainer(player, this.craftSlots);
//		});
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
	
	//Crafting Table
	
	protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player craftingPlayer, CraftingContainer craftingContainer, ResultContainer resultContainer) {
		if (!level.isClientSide) {
			ServerPlayer serverplayer = (ServerPlayer) craftingPlayer;
			ItemStack itemstack = ItemStack.EMPTY;
			Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
			if (optional.isPresent()) {
				CraftingRecipe craftingrecipe = optional.get();
				if (resultContainer.setRecipeUsed(level, serverplayer, craftingrecipe)) {
					ItemStack itemstack1 = craftingrecipe.assemble(craftingContainer, level.registryAccess());
					if (itemstack1.isItemEnabled(level.enabledFeatures())) {
						itemstack = itemstack1;
					}
				}
			}

			resultContainer.setItem(0, itemstack);
			menu.setRemoteSlot(0, itemstack);
			serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
		}
	}

	@Override
	public void slotsChanged(Container p_39366_) {
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			this.access.execute((p_39386_, p_39387_) -> {
				slotChangedCraftingGrid(this, p_39386_, this.player, this.craftSlots, this.resultSlots);
			});
		}
	}

	public void fillCraftSlotsStackedContents(StackedContents p_39374_) {
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			this.craftSlots.fillStackedContents(p_39374_);
		}
	}

	public void clearCraftingContent() {
		//this.craftSlots.clearContent();
		//this.resultSlots.clearContent();
	}

	public boolean recipeMatches(Recipe<? super CraftingContainer> p_39384_) {
		if (BioMechPlayerData.storageUnitHasCraftingTable) {
			return p_39384_.matches(this.craftSlots, this.player.level());
		}
		return false;
	}

}