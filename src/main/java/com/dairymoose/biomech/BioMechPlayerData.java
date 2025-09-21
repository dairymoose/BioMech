package com.dairymoose.biomech;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BioMechPlayerData {
	public static final int SLOT_COUNT = 6;
	
	public Player player = null;
	public SlottedItem head = new SlottedItem(MechPart.Head);
	public SlottedItem chest = new SlottedItem(MechPart.Chest);
	public SlottedItem leggings = new SlottedItem(MechPart.Leggings);
	public SlottedItem leftArm = new SlottedItem(MechPart.LeftArm);
	public SlottedItem rightArm = new SlottedItem(MechPart.RightArm);
	public SlottedItem back = new SlottedItem(MechPart.Back);
	
	public static class SlottedItem {
		public SlottedItem(MechPart mechPart) {
			this.itemStack = ItemStack.EMPTY;
			this.mechPart = mechPart;
		}
		
		public boolean visible = true;
		public ItemStack itemStack;
		public MechPart mechPart;
	}
	
	public void setForSlot(MechPart mechPart, ItemStack itemStack) {
		if (mechPart != null) {
			SlottedItem slottedItem = this.getAllSlots().get(mechPart.ordinal());
			if (slottedItem != null) {
				slottedItem.itemStack = itemStack;
			}
			else {
				BioMech.LOGGER.error("Failed to set item for slot: " + mechPart + " with item=" + itemStack);
			}
		}
	}
	
	public SlottedItem getForSlot(MechPart mechPart) {
		if (mechPart != null) {
			SlottedItem slottedItem = this.getAllSlots().get(mechPart.ordinal());
			return slottedItem;
		}
		
		return null;
	}
	
	Object lock = new Object();
	private List<SlottedItem> allSlots = null;
	public List<SlottedItem> getAllSlots() {
		if (allSlots == null) {
			synchronized (lock) {
				if (allSlots == null) {
					allSlots = new ArrayList<SlottedItem>();
					Field[] fields = BioMechPlayerData.class.getDeclaredFields();
					
					//ensure allSlots list has the correct size
					for (Field field : fields) {
						if (field.getType() == SlottedItem.class) {
							allSlots.add(null);
						}
					}
					
					//populate with data
					for (Field field : fields) {
						if (field.getType() == SlottedItem.class) {
							try {
								SlottedItem slottedItem = (SlottedItem) field.get(this);
								allSlots.set(slottedItem.mechPart.ordinal(), slottedItem);
							} catch (Exception e) {
								BioMech.LOGGER.error("Error during getAllSlots", e);
							}
						}
					}
				}
			}
		}
		return allSlots;
	}
	
	public static CompoundTag serialize(BioMechPlayerData data) {
		CompoundTag result = new CompoundTag();
		
		CompoundTag items = new CompoundTag();
		data.getAllSlots().forEach((slotted) ->
			{
				if (slotted != null) {
					CompoundTag slotTag = new CompoundTag();
					CompoundTag itemTag = new CompoundTag(); 
					slotted.itemStack.save(itemTag);
					slotTag.put("Item", itemTag);
					slotTag.putBoolean("Visible", slotted.visible);
					items.put(slotted.mechPart.name() + "Slot", slotTag);
				}
			}
		);
		
		result.put("Items", items);
		return result;
	}
	
	public static void setFieldByMechPart(BioMechPlayerData playerData, MechPart part, SlottedItem newData) {
		Field[] fields = BioMechPlayerData.class.getDeclaredFields();
		
		//ensure allSlots list has the correct size
		for (Field field : fields) {
			if (field.getType() == SlottedItem.class) {
				try {
					SlottedItem slottedItem = (SlottedItem) field.get(playerData);
					if (slottedItem.mechPart == part) {
						field.set(playerData, newData);
					}
				} catch (Exception e) {
					BioMech.LOGGER.error("Error assigning value to slotted item", e);
				}
			}
		}
	}
	
	public static BioMechPlayerData deserialize(CompoundTag tag) {
		BioMechPlayerData data = null;
		if (tag != null) {
			CompoundTag items = tag.getCompound("Items");
			if (items != null) {
				data = new BioMechPlayerData();
				MechPart[] parts = MechPart.values();
				for (MechPart part : parts) {
					CompoundTag slottedItemTag = items.getCompound(part.name() + "Slot");
					if (slottedItemTag != null) {
						SlottedItem slottedItem = new SlottedItem(part);
						
						CompoundTag itemStackTag = slottedItemTag.getCompound("Item");
						slottedItem.itemStack = ItemStack.of(itemStackTag);
						
						boolean visible = slottedItemTag.getBoolean("Visible");
						slottedItem.visible = visible;
						
						data.getAllSlots().set(part.ordinal(), slottedItem);
						setFieldByMechPart(data, part, slottedItem);
					}
				}
			}
		}
		
		return data;
	}
	
	@Override
	public String toString() {
		CompoundTag compound = BioMechPlayerData.serialize(this);
		return NbtUtils.prettyPrint(compound);
	}
}
