package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.ExtendoArmDispatcher;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.arm.ArmUtil.BoostInstance;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public abstract class ExtendoArmArmor extends ArmorBase {

	public final ExtendoArmDispatcher dispatcher;
	
	public ExtendoArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 15;
		this.hidePlayerModel = true;
		this.dispatcher = new ExtendoArmDispatcher();
		this.bonusBlockReach = 2.0f;
		this.bonusEntityReach = 0.5f;
		this.hasAttributeModifier = true;
	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_EXTENDO_LEFT_ARM.get();
	}
	
	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		super.onHandTick(active, itemStack, player, handPart, partialTick, bothHandsInactive, bothHandsActive);
		
		Level level = player.level();
		if (!level.isClientSide) {
			ArmUtil.attributeBoostPerArm(BoostInstance.INST_1, player, handPart, ForgeMod.BLOCK_REACH.get(), this.bonusBlockReach, Operation.ADDITION);
			ArmUtil.attributeBoostPerArm(BoostInstance.INST_2, player, handPart, ForgeMod.ENTITY_REACH.get(), this.bonusEntityReach, Operation.ADDITION);
		}
	}
	
	class XzInfo {
		double x;
		double z;
		
		XzInfo(double x, double z) {
			this.x = x;
			this.z = z;
		}
	}
	
	private Map<UUID, XzInfo> lastXz = new HashMap<>();
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_EXTENDO_ARM.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (player.level().isClientSide) {
						BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

						if (playerData != null) {
							XzInfo info = lastXz.get(player.getUUID());
							if (info != null && (player.getX() != info.x || player.getZ() != info.z)) {
								info.x = player.getX();
								info.z = player.getZ();
								BioMech.clientSideItemAnimation(itemStack, this.dispatcher.WALKING_COMMAND.cmd);
							} else {
								if (info == null) {
									lastXz.put(player.getUUID(), new XzInfo(player.getX(), player.getZ()));
								}
								BioMech.clientSideItemAnimation(itemStack, this.dispatcher.PASSIVE_COMMAND.cmd);
							}
						}
					}
				}
			}
		}
	}

}
	