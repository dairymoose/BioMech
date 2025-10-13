package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class HerosArmorBase extends ArmorBase {

	public HerosArmorBase(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
	}

	
	public static float nearbyEnemiesDiameter = 6.0f;
	public static String TAG_DAMAGE_BOOSTING = "DamageBoosting";
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_HEROS_HEADPIECE.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (!player.level().isClientSide) {
						List<Monster> monsters = level.getEntitiesOfClass(Monster.class, AABB.ofSize(player.position(), nearbyEnemiesDiameter, nearbyEnemiesDiameter, nearbyEnemiesDiameter), (e) -> !e.isDeadOrDying() && !e.isSpectator());	
						if (!monsters.isEmpty()) {
							stack.getOrCreateTag().putBoolean(HerosArmorBase.TAG_DAMAGE_BOOSTING, true);
						} else {
							stack.getOrCreateTag().putBoolean(HerosArmorBase.TAG_DAMAGE_BOOSTING, false);
						}
					}
				}
			}
		}
	}
	
}
	