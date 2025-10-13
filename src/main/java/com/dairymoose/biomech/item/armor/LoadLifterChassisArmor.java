package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.PermanentModifiers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LoadLifterChassisArmor extends ArmorBase {

	public LoadLifterChassisArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 80;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Chest;
		this.armDistance = 7.0f;
		this.hpBoostAmount = 3.0f;
		this.backArmorTranslation = 0.25;
	}
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_LOAD_LIFTER_CHASSIS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					if (!level.isClientSide) {
						AttributeInstance inst = living.getAttribute(Attributes.MAX_HEALTH);
						AttributeModifier thisBoost = inst.getModifier(PermanentModifiers.chestBoost);
						if (thisBoost == null)
							inst.addPermanentModifier(new AttributeModifier(PermanentModifiers.chestBoost, "boost_chest", this.hpBoostAmount, Operation.ADDITION));
					}
				}
			}
		}
	}
	
}
