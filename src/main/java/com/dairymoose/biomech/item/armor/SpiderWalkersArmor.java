package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SpiderWalkersArmor extends ArmorBase {
	
	public SpiderWalkersArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}

	public static float energyPerSec = 1.0f;
	public static float energyPerTick = energyPerSec/20.0f;
	
	public static float energyPerSecHover = 1.0f;
	public static float energyPerTickHover = energyPerSecHover/20.0f;
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_SPIDER_WALKERS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());
					if (has != null) {
						if (has.modifierKeyActive) {
							BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
							if (playerData != null) {
								if (player.horizontalCollision) {
									//climb while colliding
									if (playerData.getSuitEnergy() >= energyPerTick) {
										playerData.spendSuitEnergy(player, energyPerTick);
										
										Vec3 delta = player.getDeltaMovement();
										player.setDeltaMovement(delta.x, 0.35, delta.z);
										player.resetFallDistance();
									}
								} else {
									boolean hasAnyMatch = false;
									
									float gripRadius = 1.40f;
									for (float x=-gripRadius; x<=gripRadius; ++x) {
										for (float y=0.0f; y<=0.0f; ++y) {
											for (float z=-gripRadius; z<=gripRadius; ++z) {
												BlockPos pos = BlockPos.containing(player.position().add(x, y, z));
												BlockState state = level.getBlockState(pos);
												if (!state.isAir()) {
													hasAnyMatch = true;
													break;
												}
											}
										}
									}
									
									if (hasAnyMatch) {
										playerData.spendSuitEnergy(player, energyPerTickHover);
										
										Vec3 delta = player.getDeltaMovement();
										player.setDeltaMovement(delta.x, 0.0, delta.z);
										player.resetFallDistance();
										player.setOnGround(true);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
}
	