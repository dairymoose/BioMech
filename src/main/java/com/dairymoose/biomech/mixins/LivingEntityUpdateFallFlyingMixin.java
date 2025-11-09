package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.armor.ElytraEnabledArmor;
import com.dairymoose.biomech.item.armor.ElytraMechChestplateArmor;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityUpdateFallFlyingMixin extends Entity {
	
	public LivingEntityUpdateFallFlyingMixin() {
		super(null, null);
	}

	@Inject(method = "Lnet/minecraft/world/entity/LivingEntity;updateFallFlying()V", at = @At(value = "HEAD"), cancellable = true)
	public void handleUpdateFallFlying(CallbackInfo cir) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(this.getUUID());
		if (playerData != null) {
			SlottedItem chestSlot = playerData.getForSlot(MechPart.Chest);
			SlottedItem backSlot = playerData.getForSlot(MechPart.Back);
			if (chestSlot.itemStack.getItem() instanceof ElytraEnabledArmor base ||
					backSlot.itemStack.getItem() instanceof ElytraEnabledArmor base2) {
				boolean flag = this.getSharedFlag(7);
				if (flag && !this.onGround() && !this.isPassenger() && !((LivingEntity)(Entity)this).hasEffect(MobEffects.LEVITATION)) {
					;
				} else {
					flag = false;
				}

				if (!this.level().isClientSide) {
					this.setSharedFlag(7, flag);
				}
				cir.cancel();
			}
		}
	}
	
}
