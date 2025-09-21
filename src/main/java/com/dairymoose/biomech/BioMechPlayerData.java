package com.dairymoose.biomech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BioMechPlayerData {
	public static final int SLOT_COUNT = 6;
	
	public Player player = null;
	public ItemStack head = ItemStack.EMPTY;
	public ItemStack chest = ItemStack.EMPTY;
	public ItemStack leggings = ItemStack.EMPTY;
	public ItemStack leftArm = ItemStack.EMPTY;
	public ItemStack rightArm = ItemStack.EMPTY;
	public ItemStack back = ItemStack.EMPTY;
	
	Object lock = new Object();
	private List<ItemStack> allSlots = null;
	public List<ItemStack> getAllSlots() {
		if (allSlots == null) {
			synchronized (lock) {
				if (allSlots == null) {
					allSlots = new ArrayList<ItemStack>();
					allSlots.add(head);
					allSlots.add(chest);
					allSlots.add(leggings);
					allSlots.add(leftArm);
					allSlots.add(rightArm);
					allSlots.add(back);
				}
			}
		}
		return allSlots;
	}
	
	private static class IntHolder {
		public int value;
	}
	public static CompoundTag serialize(BioMechPlayerData data) {
		CompoundTag result = new CompoundTag();
		
		CompoundTag items = new CompoundTag();
		IntHolder tagCounter = new IntHolder();
		tagCounter.value = 0;
		data.getAllSlots().forEach((item) ->
			{
				if (item != null) {
					CompoundTag itemTag = item.getOrCreateTag();
					items.put("Slot" + tagCounter.value, itemTag);
				}
				
				++tagCounter.value;
			}
		);
		
		result.put("Items", items);
		return result;
	}
	
	public static BioMechPlayerData deserialize(CompoundTag tag) {
		BioMechPlayerData data = null;
		if (tag != null) {
			CompoundTag items = tag.getCompound("Items");
			if (items != null) {
				data = new BioMechPlayerData();
				for (int i=0; i<SLOT_COUNT; ++i) {
					CompoundTag slottedItem = items.getCompound("Slot" + i);
					if (slottedItem != null) {
						ItemStack itemStack = ItemStack.of(slottedItem);
						data.getAllSlots().set(i, itemStack);
					}
				}
			}
		}
		
		return data;
	}
}
