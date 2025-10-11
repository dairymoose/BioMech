package com.dairymoose.biomech.item.armor.arm;

import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.world.item.ArmorMaterial;

public class DiggerLeftArmArmor extends DiggerArmArmor {

	public DiggerLeftArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.mechPart = MechPart.LeftArm;
		this.addToLootTable = false;
	}
	
}
	