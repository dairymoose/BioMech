package com.dairymoose.biomech.item.armor.arm;

import java.util.UUID;

import com.dairymoose.biomech.PermanentModifiers;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;

public class ArmUtil {
	
	enum BoostInstance {
		INST_1,
		INST_2
	}
	
	public static void attributeBoostPerArm(BoostInstance inst, Player player, MechPart handPart, Attribute attribute, float amount, Operation op) {
		UUID booster = null;
		String boostText = null;
		
		if (handPart == MechPart.RightArm) {
			if (inst == BoostInstance.INST_1)
				booster = PermanentModifiers.rightArmBoost;
			else
				booster = PermanentModifiers.rightArmBoost2;
			boostText = "boost_right_arm"; 
		} else {
			if (inst == BoostInstance.INST_1)
				booster = PermanentModifiers.leftArmBoost;
			else
				booster = PermanentModifiers.leftArmBoost2;
			boostText = "boost_left_arm";
		}
		
		if (booster != null && boostText != null) {
			AttributeInstance attribInst = player.getAttribute(attribute);
			AttributeModifier thisBoost = attribInst.getModifier(booster);
			if (thisBoost == null)
				attribInst.addPermanentModifier(new AttributeModifier(booster, boostText, amount, op));
		}
	}
}
