package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.SpiderWalkersArmor;

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
			SlottedItem chestSlot = playerData.getForSlot(MechPart.Chest);
			if (chestSlot.itemStack.getItem() instanceof ArmorBase base) {
				if (chestSlot.visible) {
					//defaults:
//					this.rightArm.z = 0.0F;
//				    this.rightArm.x = -5.0F;
//				    this.leftArm.z = 0.0F;
//				    this.leftArm.x = 5.0F;
					//...
//					if (this.crouching) {
//						this.body.xRot = 0.5F;
//						this.rightArm.xRot += 0.4F;
//						this.leftArm.xRot += 0.4F;
//						this.rightLeg.z = 4.0F;
//						this.leftLeg.z = 4.0F;
//						this.rightLeg.y = 12.2F;
//						this.leftLeg.y = 12.2F;
//						this.head.y = 4.2F;
//						this.body.y = 3.2F;
//						this.leftArm.y = 5.2F;
//						this.rightArm.y = 5.2F;
//					} else {
//						this.body.xRot = 0.0F;
//						this.rightLeg.z = 0.0F;
//						this.leftLeg.z = 0.0F;
//						this.rightLeg.y = 12.0F;
//						this.leftLeg.y = 12.0F;
//						this.head.y = 0.0F;
//						this.body.y = 0.0F;
//						this.leftArm.y = 2.0F;
//						this.rightArm.y = 2.0F;
//					}

					this.rightArm.x = -base.getArmDistance();
					this.leftArm.x = base.getArmDistance();
				}
			}
			SlottedItem legSlot = playerData.getForSlot(MechPart.Leggings);
			if (legSlot.itemStack.getItem() instanceof SpiderWalkersArmor armor) {
				if (legSlot.visible) {
					//left-right-shuffle
					this.rightLeg.yRot = this.rightLeg.xRot/5.0f;
					this.leftLeg.yRot = -this.leftLeg.xRot/5.0f;
					
					//normal leg walking forward-and-back
					this.rightLeg.xRot = this.rightLeg.xRot/4.0f;
					this.leftLeg.xRot = this.leftLeg.xRot/4.0f;
				}
			}
		}
	}
	
}
