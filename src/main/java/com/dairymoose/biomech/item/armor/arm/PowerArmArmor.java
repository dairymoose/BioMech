package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.armor.ArmorBase;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class PowerArmArmor extends ArmorBase {

	public PowerArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 0;
		this.hidePlayerModel = true;
	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_POWER_LEFT_ARM.get();
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_POWER_ARM.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}

}
	