package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.TeleportationCrystalDispatcher;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeleportationCrystalArmor extends ArmorBase {

	public final TeleportationCrystalDispatcher dispatcher;
	public static int TELEPORT_HOLD_TIME_TICKS = 4*20;
	
	public TeleportationCrystalArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Back;
		this.dispatcher = new TeleportationCrystalDispatcher();
	}

	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_TELEPORTATION_CRYSTAL.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					BioMech.clientSideItemAnimation(itemStack, this.dispatcher.PASSIVE_COMMAND.cmd);
				}
			}
		}
	}
	
}
