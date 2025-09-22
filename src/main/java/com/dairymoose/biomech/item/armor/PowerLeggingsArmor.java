package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PowerLeggingsArmor extends ArmorBase {

	int crouchingTicks = 0;
	int maxJumpBoost = 7;
	int minTimeToGainJumpBoost = 10;
	int jumpBoostAccumulationAfterFirst = 10;
	int lastJumpBoostLevel = 0;
	boolean inAir = false;
	
	public PowerLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}

	public int jumpBoost() {
		if (crouchingTicks < minTimeToGainJumpBoost) {
			return 0;
		}
		if (crouchingTicks <= minTimeToGainJumpBoost) {
			return 1;
		}
		return Math.min(1 + (int)((crouchingTicks-minTimeToGainJumpBoost)/jumpBoostAccumulationAfterFirst), maxJumpBoost);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_POWER_LEGGINGS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (!level.isClientSide) {
						boolean b = (!living.isFallFlying() && !living.isSwimming() && living.isCrouching()
								&& living.onGround() && !inAir);
						boolean b2 = ((crouchingTicks + 1) >= minTimeToGainJumpBoost
								&& (crouchingTicks + 1) % jumpBoostAccumulationAfterFirst == 0);
						//BioMech.LOGGER.info("crouchingTicks=" + crouchingTicks + " and inAir=" + inAir
						//		+ " with jumpPower=" + this.jumpBoost() + " and crouching=" + living.isCrouching()
						//		+ ", b=" + b + ", b2=" + b2);
					}
					if (!living.isFallFlying() && !living.isSwimming() && living.isCrouching() && living.onGround()
							&& !inAir) {
						if (FMLEnvironment.dist == Dist.CLIENT) {
							if (level.isClientSide)
								++crouchingTicks;
						} else {
							++crouchingTicks;
						}

						int newJumpBoost = this.jumpBoost();

						if (level.isClientSide) {
							if (newJumpBoost > 0) {
								// if (entity.tickCount % 8 == 0) {
								if (newJumpBoost > lastJumpBoostLevel
										|| (newJumpBoost == maxJumpBoost && entity.tickCount % 20 == 0)) {
									int pCount = lastJumpBoostLevel;
									for (int i = 0; i < pCount; ++i) {
										level.addParticle(ParticleTypes.CRIT, player.getX(), player.getY() + 0.3,
												player.getZ(), 0.5 + Math.random(), 0.0, 0.5 + Math.random());
									}
								}
							}
						}

						if (crouchingTicks >= minTimeToGainJumpBoost
								&& crouchingTicks % jumpBoostAccumulationAfterFirst == 0) {
							lastJumpBoostLevel = newJumpBoost;
							if (!level.isClientSide)
								living.addEffect(new MobEffectInstance(MobEffects.JUMP, 30, newJumpBoost));
						}
					} else {
						if (living.hasEffect(MobEffects.JUMP) && crouchingTicks >= minTimeToGainJumpBoost
								&& !living.onGround()) {
							inAir = true;
							if (!level.isClientSide)
								living.addEffect(new MobEffectInstance(MobEffects.JUMP, 30, this.jumpBoost()));
						} else {
							living.removeEffect(MobEffects.JUMP);
							inAir = false;
							crouchingTicks = 0;
							lastJumpBoostLevel = 0;
						}
					}
				}
			}
		}
	}
	
}
	