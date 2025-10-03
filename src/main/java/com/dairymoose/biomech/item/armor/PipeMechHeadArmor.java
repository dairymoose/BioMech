package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PipeMechHeadArmor extends ArmorBase {

	public PipeMechHeadArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 5;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Head;
		this.damageAvoidPct = 0.03f;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_PIPE_MECH_HEAD.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}
	
}
