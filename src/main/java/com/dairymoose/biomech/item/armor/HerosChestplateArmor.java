package com.dairymoose.biomech.item.armor;

import net.minecraft.world.item.ArmorMaterial;

public class HerosChestplateArmor extends HerosArmorBase {

	public HerosChestplateArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 90;
		this.suitEnergyPerSec = 1.0f;
		this.alwaysHidePlayerHat = false;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Chest;
		this.nearbyEnemyDamageBoost = 0.05f;
		this.explosionDamageReduction = 0.08f;
	}

}
	