package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin extends HumanoidModel<LivingEntity> {
	
	public PlayerModelMixin(ModelPart p_170677_) {
		super(p_170677_);
	}

	@Redirect(method="setupAnim", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/HumanoidModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
            ordinal = -1
            ))
	public void handleSetupAnim(HumanoidModel humanoidModel, LivingEntity living, float a, float b, float c, float d, float e) {
		super.setupAnim(living, a, b, c, d, e);
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(living.getUUID());
		if (playerData != null) {
			SlottedItem slotted = playerData.getForSlot(MechPart.Chest);
			if (slotted.itemStack.getItem() instanceof ArmorBase base) {
				if (slotted.visible) {
					//defaults:
					//this.rightArm.x = -5.0F;
				    //this.leftArm.x = 5.0F;
					this.rightArm.x = -base.getArmDistance();
					this.leftArm.x = base.getArmDistance();
				}
			}
		}
	}
	
}
