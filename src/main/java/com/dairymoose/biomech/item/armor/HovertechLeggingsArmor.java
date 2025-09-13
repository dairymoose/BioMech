package com.dairymoose.biomech.item.armor;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class HovertechLeggingsArmor extends ArmorBase {

	float floatMagnitude = 0.16f; 
	float floatBottom = 1.05f;
	float floatSpeed = 0.092f;
	float floatTop = floatBottom + 1.0f*floatMagnitude;
	
	public HovertechLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
	}
	
	@Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player ) {
            //player.getArmorSlots().forEach(wornArmor -> {
                //if (wornArmor != null && wornArmor.is(BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get())) {
                	if (entity instanceof LivingEntity living && !living.isSpectator()) {
                		BlockPos overheadBlock = entity.blockPosition().above().above();
                		if (!level.getBlockState(overheadBlock).isFaceSturdy(level, overheadBlock, Direction.DOWN)) {
                			double floatAmount = living.getY() - (entity.blockPosition().below().getY() + level.getBlockFloorHeight(entity.blockPosition().below()));
                    		double targetY = floatBottom + floatMagnitude/2.0 + Math.sin(entity.tickCount*floatSpeed)*floatMagnitude/2.0;
                    		if (floatAmount <= floatTop * 1.5) {
                    			if (!living.isCrouching() && !living.isFallFlying() && living.getDeltaMovement().y <= 0.20 && !living.isSwimming()) {
                    				if (floatAmount < floatBottom && floatAmount >= 0.0) {
                            			living.setDeltaMovement(living.getDeltaMovement().with(Axis.Y, 0.25));
                                	} else {
                                		double distToTargetY = targetY - floatAmount;

                            			living.setDeltaMovement(living.getDeltaMovement().with(Axis.Y, distToTargetY));
                            			if (entity.tickCount % 5 == 0) {
                            				int pCount = (int)(Math.random() * 4.0);
                            				for (int i=0; i<pCount; ++i) {
                            					level.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX() + (Math.random()-.5) * 0.4, player.getY() - 0.1, player.getZ() + (Math.random()-.5) * 0.4, 0.0, -0.2, 0.0);
                            				}
                            			}
                                		if (level.isClientSide) {
                            				BioMech.LOGGER.debug("floatAmount=" + floatAmount + " for targetY=" + targetY + " and tickCount=" + entity.tickCount);
                            			}
                                	}
                    				living.setOnGround(true);
                    			}
                    		} else {
                    			if (!level.isClientSide) {
                    				if (!living.isFallFlying() && living.fallDistance >= 4.0 && floatAmount >= 4.0) {
                    					BioMech.LOGGER.debug("slowfall floatAmount=" + floatAmount);
                            			living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 10, 1));
                    				}
                    			}
                    		}
                		}
                	}
                //}
            //});
        }
    }
	
}
