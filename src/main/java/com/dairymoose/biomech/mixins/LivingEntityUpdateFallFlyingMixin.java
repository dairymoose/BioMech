package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.armor.renderer.MobilityTreadsRenderer;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.ElytraMechChestplateArmor;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.item.armor.SpiderWalkersArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;

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
			if (chestSlot.itemStack.getItem() instanceof ElytraMechChestplateArmor base) {
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
