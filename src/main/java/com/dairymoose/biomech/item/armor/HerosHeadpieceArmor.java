package com.dairymoose.biomech.item.armor;

import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;

public class HerosHeadpieceArmor extends HerosArmorBase {

	public HerosHeadpieceArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.alwaysHidePlayerHat = false;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Head;
		this.nearbyEnemyDamageBoost = 0.06f;
		this.explosionDamageReduction = 0.30f;
	}
	
	public static float getTotalNearbyDamageBoostPct(Player player) {
		float avoidPct = 0.0f;
		
		BioMechPlayerData playerData = null;
    	playerData = BioMech.globalPlayerData.get(player.getUUID());
    	if (playerData != null) {
    		List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						avoidPct += base.getNearbyEnemyDamageBoost();
					}
				}
			}
    	}
    	
    	return avoidPct;
	}
	
	public static float getTotalExplosionDamageReduction(Player player) {
		float avoidPct = 0.0f;
		
		BioMechPlayerData playerData = null;
    	playerData = BioMech.globalPlayerData.get(player.getUUID());
    	if (playerData != null) {
    		List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						avoidPct += base.explosionDamageReduction;
					}
				}
			}
    	}
    	
    	return avoidPct;
	}

}
	