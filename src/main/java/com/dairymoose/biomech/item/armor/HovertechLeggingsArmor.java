package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.menu.BioMechStationMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HovertechLeggingsArmor extends ArmorBase {

	float floatMagnitude = 0.15f; 
	float floatBottom = 1.10f;
	float floatSpeed = 0.092f;
	float maxFloatDeltaAdjustment = 0.08f;
	float floatTop = floatBottom + 1.0f*floatMagnitude;
	
	public HovertechLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 4;
		this.suitEnergyPerSec = 0.5f;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (player.containerMenu instanceof BioMechStationMenu) {
						return;
					}
					BlockPos overheadBlock = entity.blockPosition().above().above();
					if (!level.getBlockState(overheadBlock).isFaceSturdy(level, overheadBlock, Direction.DOWN)) {
						double floatAmount = living.getY() - (entity.blockPosition().below().getY()
								+ level.getBlockFloorHeight(entity.blockPosition().below()));
						double targetY = floatBottom + floatMagnitude / 2.0
								+ Math.sin(entity.tickCount * floatSpeed) * floatMagnitude / 2.0;
						if (floatAmount <= floatTop * 1.5) {
							living.fallDistance = 0;
							if (!living.isCrouching() && !living.isFallFlying() && living.getDeltaMovement().y <= 0.20
									&& !living.isSwimming() && !living.isInWaterOrBubble()) {
								if (floatAmount < floatBottom && floatAmount >= 0.0) {
									living.setDeltaMovement(living.getDeltaMovement().with(Axis.Y, 0.26));
								} else {
									double distToTargetY = targetY - floatAmount;

									if (distToTargetY > 0.0) {
										living.setDeltaMovement(living.getDeltaMovement().with(Axis.Y,
												Math.min(maxFloatDeltaAdjustment, distToTargetY)));
									} else {
										living.setDeltaMovement(living.getDeltaMovement().with(Axis.Y,
												Math.max(-maxFloatDeltaAdjustment, distToTargetY)));
									}
									if (entity.tickCount % 4 == 0) {
										int pCount = (int) (Math.random() * 5.0);
										for (int i = 0; i < pCount; ++i) {
											level.addParticle(ParticleTypes.ASH,
													player.getX() + (Math.random() - .5) * 0.6, player.getY() - 0.1,
													player.getZ() + (Math.random() - .5) * 0.6, 0.0, -1.2, 0.0);
										}
									}
									if (level.isClientSide) {
										//BioMech.LOGGER.debug("floatAmount=" + floatAmount + " for targetY=" + targetY
										//		+ " and tickCount=" + entity.tickCount);
									}
								}
								living.setOnGround(true);
							}
						} else {
							if (!level.isClientSide) {
								if (!living.isFallFlying() && living.fallDistance >= 3.0) {
									//BioMech.LOGGER.debug("slowfall floatAmount=" + floatAmount);
									living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 1));
								}
							}
						}
					}
				}
			}
		}
	}
	
}
