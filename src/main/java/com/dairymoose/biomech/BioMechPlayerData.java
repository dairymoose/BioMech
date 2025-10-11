package com.dairymoose.biomech;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class BioMechPlayerData {
	public static final int SLOT_COUNT = 6;
	
	public SlottedItem head = new SlottedItem(MechPart.Head);
	public SlottedItem chest = new SlottedItem(MechPart.Chest);
	public SlottedItem leggings = new SlottedItem(MechPart.Leggings);
	public SlottedItem leftArm = new SlottedItem(MechPart.LeftArm);
	public SlottedItem rightArm = new SlottedItem(MechPart.RightArm);
	public SlottedItem back = new SlottedItem(MechPart.Back);
	
	public static boolean storageUnitHasCraftingTable = true;
	//public static final int PORTABLE_STORAGE_UNIT_CAPACITY = 63;
	public static final int PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER = 45;
	public static final int PORTABLE_STORAGE_UNIT_CAPACITY = PORTABLE_STORAGE_UNIT_CAPACITY_NO_CRAFTER + (storageUnitHasCraftingTable ? 10 : 0);
	public NonNullList<ItemStack> portableStorageUnitItems = NonNullList.withSize(BioMechPlayerData.PORTABLE_STORAGE_UNIT_CAPACITY, ItemStack.EMPTY);
	
	public float suitEnergyPerSecBaseline = 3.0f;
	
	private float suitEnergy = 0.0f;
	public float suitEnergyMax = 0.0f;
	public float suitEnergyPerSec = suitEnergyPerSecBaseline;
	public float suitEnergyPerSecTemporaryModifier = 0.0f;
	
	public int lastUsedEnergyTick = -1000;
	public static final int ticksRequiredToRegenEnergy = 40;
	
	public static String SUIT_ENERGY = "SuitEnergy";
	public static String SUIT_ENERGY_MAX = "SuitEnergyMax";
	public static String SUIT_ENERGY_PER_SEC = "SuitEnergyPerSec";
	public static String ITEM = "Item";
	public static String ITEMS = "Items";
	public static String SLOT = "Slot";
	public static String VISIBLE = "Visible";
	
	public static class SlottedItem {
		public SlottedItem(MechPart mechPart) {
			this.itemStack = ItemStack.EMPTY;
			this.leftArmItemStack = ItemStack.EMPTY;
			this.mechPart = mechPart;
		}
		
		public boolean visible = true;
		public ItemStack itemStack;
		public ItemStack leftArmItemStack;
		public ItemStack clientTempHandStack = ItemStack.EMPTY;
		public MechPart mechPart;
	}
	
	public float getSuitEnergy() {
		return this.suitEnergy;
	}
	
	public float getSuitEnergyPercent() {
		return this.suitEnergy/this.suitEnergyMax;
	}
	
	public void setSuitEnergy(float amount) {
		this.suitEnergy = amount;
		if (this.suitEnergy > this.suitEnergyMax) {
			this.suitEnergy = this.suitEnergyMax;
		}
	}
	
	public int getTicksSinceLastEnergyUsage(Player player) {
		return player.tickCount - lastUsedEnergyTick;
	}
	
	public int remainingTicksForEnergyRegen(Player player) {
		return Math.max(0, ticksRequiredToRegenEnergy - this.getTicksSinceLastEnergyUsage(player));
	}
	
	public boolean canRegenEnergy(Player player) {
		int remainingTicks = this.remainingTicksForEnergyRegen(player);
		if (remainingTicks == 0) {
			return true;
		}
		
		return false;
	}
	
	public void internalSpendSuitEnergy(Player player, float amount) {
		this.lastUsedEnergyTick = player.tickCount;
		this.suitEnergy -= amount;
		if (this.suitEnergy < 0.0f)
			this.suitEnergy = 0.0f;
	}
	
	public void spendSuitEnergy(Player player, float amount) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			if (player.level().isClientSide)
				this.internalSpendSuitEnergy(player, amount);
		} else {
			this.internalSpendSuitEnergy(player, amount);
		}
		
	}
	
	private void internalTickEnergy(Player player) {
		if (this.canRegenEnergy(player)) {
			this.suitEnergy += suitEnergyPerSec/20.0f + suitEnergyPerSecTemporaryModifier/20.0f;
			if (this.suitEnergy > suitEnergyMax) {
				this.suitEnergy = suitEnergyMax;
			}
			if (this.suitEnergy < 0.0f) {
				this.suitEnergy = 0.0f;
			}
		}
	}
	
	public void tickEnergy(Player player) {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			if (player.level().isClientSide)
				this.internalTickEnergy(player);
		} else {
			this.internalTickEnergy(player);
		}
	}
	
	public void restoreAllSuitEnergy() {
		this.suitEnergy = suitEnergyMax;
	}
	
	public void recalculateSuitEnergyMax() {
		DoubleSummaryStatistics dssEnergyPerSec = this.getAllSlots().stream().filter((slotted) -> slotted.itemStack.getItem() instanceof ArmorBase).
				map((slotted) -> ((ArmorBase)slotted.itemStack.getItem()).getSuitEnergyPerSec()).collect(Collectors.summarizingDouble((d) -> d));
		DoubleSummaryStatistics dssEnergy = this.getAllSlots().stream().filter((slotted) -> slotted.itemStack.getItem() instanceof ArmorBase).
				map((slotted) -> ((ArmorBase)slotted.itemStack.getItem()).getSuitEnergy()).collect(Collectors.summarizingDouble((d) -> d));
		this.suitEnergyMax = (float) dssEnergy.getSum();
		this.suitEnergyPerSec = suitEnergyPerSecBaseline + (float) dssEnergyPerSec.getSum();
		BioMech.LOGGER.debug("suitEnergyMax=" + this.suitEnergyMax + ", suitEnergyPerSec=" + this.suitEnergyPerSec);
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
					slotTag.put(ITEM, itemTag);
					slotTag.putBoolean(VISIBLE, slotted.visible);
					items.put(slotted.mechPart.name() + SLOT, slotTag);
				}
			}
		);
		
		result.put(ITEMS, items);
		result.putFloat(SUIT_ENERGY, data.suitEnergy);
		result.putFloat(SUIT_ENERGY_MAX, data.suitEnergyMax);
		result.putFloat(SUIT_ENERGY_PER_SEC, data.suitEnergyPerSec);
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
			CompoundTag items = tag.getCompound(ITEMS);
			if (items != null) {
				data = new BioMechPlayerData();
				MechPart[] parts = MechPart.values();
				for (MechPart part : parts) {
					CompoundTag slottedItemTag = items.getCompound(part.name() + SLOT);
					if (slottedItemTag != null) {
						SlottedItem slottedItem = new SlottedItem(part);
						
						CompoundTag itemStackTag = slottedItemTag.getCompound(ITEM);
						slottedItem.itemStack = ItemStack.of(itemStackTag);
						
						boolean visible = slottedItemTag.getBoolean(VISIBLE);
						slottedItem.visible = visible;
						
						data.getAllSlots().set(part.ordinal(), slottedItem);
						setFieldByMechPart(data, part, slottedItem);
					}
				}
				
				data.suitEnergy = tag.getFloat(SUIT_ENERGY);
				data.suitEnergyMax = tag.getFloat(SUIT_ENERGY_MAX);
				data.suitEnergyPerSec = tag.getFloat(SUIT_ENERGY_PER_SEC);
			}
		}
		
		return data;
	}
	
//	@Override
//	public String toString() {
//		CompoundTag compound = BioMechPlayerData.serialize(this);
//		return NbtUtils.prettyPrint(compound);
//	}
}
