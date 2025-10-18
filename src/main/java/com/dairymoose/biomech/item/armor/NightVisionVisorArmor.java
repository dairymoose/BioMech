package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NightVisionVisorArmor extends ArmorBase {
	
	public NightVisionVisorArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 5;
		this.hidePlayerModel = false;
		this.alwaysHidePlayerHat = false;
		this.mechPart = MechPart.Head;
	}

	private boolean toggledOn = true;
	@Override
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown, int bonusData, boolean serverOriginator) {
		if (keyIsDown) {
			toggledOn = !toggledOn;
		}
		super.onHotkeyPressed(player, playerData, keyIsDown, bonusData, serverOriginator);
	}
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_SPIDER_WALKERS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					MobEffectInstance nightVision = player.getEffect(MobEffects.NIGHT_VISION);
					if (toggledOn) {
						if (nightVision == null || nightVision.endsWithin(210)) {
							player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 250, 1, false, false, false));
						}
					} else {
						if (nightVision != null)
							player.removeEffect(MobEffects.NIGHT_VISION);
					}
				}
			}
		}
	}
	
}
	