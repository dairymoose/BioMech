package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.client.screen.PortableStorageUnitScreen;
import com.dairymoose.biomech.packet.serverbound.ServerboundOpenPortableStorageUnitPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PortableStorageUnitArmor extends ArmorBase {

	public PortableStorageUnitArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.suitEnergyPerSec = 0.5f;
		this.alwaysHidePlayerHat = false;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Back;
	}

	public static String ITEM_LIST = "ItemList";
	
	@Override
	public void onHotkeyHeld(Player player, BioMechPlayerData playerData) {
		int tickDiff = Minecraft.getInstance().player.tickCount - PortableStorageUnitScreen.exitTick;
		if (tickDiff < 0 || tickDiff >= 3) {
			BioMechNetwork.INSTANCE.sendToServer(new ServerboundOpenPortableStorageUnitPacket());
		}
	}
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_PORTABLE_STORAGE_UNIT.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}

	public static CompoundTag serialize(BioMechPlayerData playerData) {
		CompoundTag tag = new CompoundTag();
		ListTag itemList = new ListTag();
		for (int i=0; i<playerData.portableStorageUnitItems.size(); ++i) {
			CompoundTag item = new CompoundTag();
			playerData.portableStorageUnitItems.get(i).save(item);
			itemList.add(item);
		}
		tag.put(ITEM_LIST, itemList);
		return tag;
	}
	
	public static void deserialize(BioMechPlayerData playerData, CompoundTag tag) {
		if (tag.contains(ITEM_LIST)) {
			ListTag listTag = tag.getList(ITEM_LIST, CompoundTag.TAG_COMPOUND);
			if (listTag != null) {
				for (int i=0; i<listTag.size(); ++i) {
					CompoundTag item = listTag.getCompound(i);
					playerData.portableStorageUnitItems.set(i, ItemStack.of(item));
				}
			}
		}
	}
	
}
	