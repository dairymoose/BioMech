package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class IronMechArmArmor extends ArmorBase {

	public IronMechArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.damageAbsorbPct = 0.02f;
	}

	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick,
			boolean bothHandsInactive, boolean bothHandsActive) {

	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_IRON_MECH_LEFT_ARM.get();
	}

	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isLeftArm) {
		if (entity instanceof Player player) {
			HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());

			if (has != null && (!has.leftHandActive && isLeftArm || !has.rightHandActive && !isLeftArm)) {
				// this.dispatcher.mining(player, stack);
				List<Item> armorItems = new ArrayList<Item>();
				player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
				if (armorItems.contains(BioMechRegistry.ITEM_IRON_MECH_ARM.get())
						|| armorItems.contains(BioMechRegistry.ITEM_IRON_MECH_LEFT_ARM.get()) || slotId == -1) {
					if (entity instanceof LivingEntity living && !living.isSpectator()) {

					}
				}
			}
		}
	}

}
