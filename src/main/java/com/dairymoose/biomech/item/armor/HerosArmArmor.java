package com.dairymoose.biomech.item.armor;

import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public class HerosArmArmor extends HerosArmorBase {

	public HerosArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 8;
		this.alwaysHidePlayerHat = false;
		this.hidePlayerModel = true;
		this.nearbyEnemyDamageBoost = 0.03f;
		this.explosionDamageReduction = 0.02f;
	}
	
	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_HEROS_LEFT_ARM.get();
	}

}
	