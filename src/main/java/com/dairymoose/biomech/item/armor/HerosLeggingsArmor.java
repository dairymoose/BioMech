package com.dairymoose.biomech.item.armor;

import net.minecraft.world.item.ArmorMaterial;

public class HerosLeggingsArmor extends HerosArmorBase {

	public HerosLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 8;
		this.alwaysHidePlayerHat = false;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
		this.nearbyEnemyDamageBoost = 0.04f;
		this.explosionDamageReduction = 0.08f;
	}

}
	