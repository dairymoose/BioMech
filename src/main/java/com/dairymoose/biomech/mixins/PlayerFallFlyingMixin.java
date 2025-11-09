package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.armor.ElytraEnabledArmor;
import com.dairymoose.biomech.item.armor.ElytraMechChestplateArmor;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerFallFlyingMixin extends LivingEntity {
	
	public PlayerFallFlyingMixin() {
		super(null, null);
	}

	@Inject(method = "Lnet/minecraft/world/entity/player/Player;tryToStartFallFlying()Z", at = @At(value = "HEAD"), cancellable = true)
	public void handleTryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(this.getUUID());
		if (playerData != null) {
			SlottedItem chestSlot = playerData.getForSlot(MechPart.Chest);
			SlottedItem backSlot = playerData.getForSlot(MechPart.Back);
			if (chestSlot.itemStack.getItem() instanceof ElytraEnabledArmor base ||
					backSlot.itemStack.getItem() instanceof ElytraEnabledArmor base2) {
				((Player)(LivingEntity)this).startFallFlying();
				cir.setReturnValue(true);
				cir.cancel();
			}
		}
	}
	
}
