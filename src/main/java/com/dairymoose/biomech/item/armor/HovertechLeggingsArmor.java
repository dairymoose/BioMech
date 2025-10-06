package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.menu.BioMechStationMenu;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class HovertechLeggingsArmor extends ArmorBase {

	float floatMagnitude = 0.15f; 
	float floatBottom = 1.10f;
	float floatSpeed = 0.092f;
	float maxFloatDeltaAdjustment = 0.08f;
	float floatTop = floatBottom + 1.0f*floatMagnitude;
	
	public HovertechLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 4;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}

	boolean lastAltState = false;
	boolean toggledOn = true;
	double pickupRadiusScale = 2.0f;
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

					BlockPos sturdyBelowPos = entity.blockPosition();
					for (int i=0; i<3; ++i) {
						BlockState sturdyBelowState = level.getBlockState(sturdyBelowPos);
						if (sturdyBelowState.isFaceSturdy(level, sturdyBelowPos, Direction.UP)) {
							break;
						} else {
							sturdyBelowPos = sturdyBelowPos.below();
						}
					}
					sturdyBelowPos = sturdyBelowPos.above();
					
					BlockPos overheadBlock = sturdyBelowPos.above();
					BlockPos overheadBlock2 = sturdyBelowPos.above().above();
					BlockPos overheadBlock3 = sturdyBelowPos.above().above().above();
					
					boolean overheadNotSturdy = !level.getBlockState(overheadBlock).isFaceSturdy(level, overheadBlock, Direction.DOWN);
					boolean overhead2NotSturdy = !level.getBlockState(overheadBlock2).isFaceSturdy(level, overheadBlock2, Direction.DOWN);
					boolean overhead3NotSturdy = !level.getBlockState(overheadBlock3).isFaceSturdy(level, overheadBlock3, Direction.DOWN);
					
					if (level.isClientSide) {
						if (BioMech.localPlayerHoldingAlt && !lastAltState) {
							toggledOn = !toggledOn;
							BioMech.LOGGER.debug("Hovertech: swap toggledOn to " + toggledOn);
						}
						lastAltState = BioMech.localPlayerHoldingAlt;
					}
					
					if (toggledOn && overheadNotSturdy && overhead2NotSturdy && overhead3NotSturdy) {
						BioMech.allowFlyingForPlayer(player);
						
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
									if (!level.isClientSide) {
										
										//expand pickup range
										if (player.getHealth() > 0.0F && !player.isSpectator()) {
									         AABB aabb;
									         if (player.isPassenger() && !player.getVehicle().isRemoved()) {
									            aabb = player.getBoundingBox().minmax(player.getVehicle().getBoundingBox()).inflate(pickupRadiusScale*1.0D, pickupRadiusScale*0.0D, pickupRadiusScale*1.0D);
									         } else {
									            aabb = player.getBoundingBox().inflate(pickupRadiusScale*1.0D, pickupRadiusScale*0.5D, pickupRadiusScale*1.0D);
									         }

									         List<Entity> list = player.level().getEntities(player, aabb);
									         List<Entity> list1 = Lists.newArrayList();

									         for(int i = 0; i < list.size(); ++i) {
									            Entity e = list.get(i);
									            if (e.getType() == EntityType.EXPERIENCE_ORB) {
									               list1.add(e);
									            } else if (!e.isRemoved()) {
									            	e.playerTouch(player);
									            }
									         }

									         if (!list1.isEmpty()) {
									        	 list1.get((int)(Math.random() * list1.size())).playerTouch(player);
									         }
									      }
									}
								}
								BioMech.LOGGER.info("set on ground");
								living.setOnGround(true);
							} else if (living.isCrouching()) {
								if (living.getDeltaMovement().y >= -0.08f && living.getDeltaMovement().y <= -0.01) {
									//fall 5x faster than normal
									living.setDeltaMovement(living.getDeltaMovement().x, -0.40f, living.getDeltaMovement().z);
								}
							}
						} else {
							if (!level.isClientSide) {
								if (!living.isFallFlying() && living.fallDistance >= 3.0) {
									//BioMech.LOGGER.debug("slowfall floatAmount=" + floatAmount);
									living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 1, false, false, false));
								}
							}
						}
					}
				}
			}
		}
	}
	
}
