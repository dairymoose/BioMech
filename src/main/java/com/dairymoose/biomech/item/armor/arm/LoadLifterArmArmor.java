package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.TransientModifiers;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class LoadLifterArmArmor extends ArmorBase {
	
	public LoadLifterArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.hpBoostAmount = 1.0f;
	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_LOAD_LIFTER_LEFT_ARM.get();
	}
	
	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		super.onHandTick(active, itemStack, player, handPart, partialTick, bothHandsInactive, bothHandsActive);
		
		Level level = player.level();
		if (!level.isClientSide) {
			UUID booster = null;
			String boostText = null;
			
			if (handPart == MechPart.RightArm) {
				booster = TransientModifiers.rightArmHpBoost;
				boostText = "hp_boost_right_arm"; 
			} else {
				booster = TransientModifiers.leftArmHpBoost;
				boostText = "hp_boost_left_arm";
			}
			
			if (booster != null && boostText != null) {
				AttributeInstance inst = player.getAttribute(Attributes.MAX_HEALTH);
				AttributeModifier thisBoost = inst.getModifier(booster);
				if (thisBoost == null)
					inst.addTransientModifier(new AttributeModifier(booster, boostText, this.hpBoostAmount, Operation.ADDITION));
			}
		}
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_LOAD_LIFTER_ARM.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					
				}
			}
		}
	}

}
	