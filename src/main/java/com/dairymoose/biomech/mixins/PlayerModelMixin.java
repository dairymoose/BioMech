package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.SpiderWalkersArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin extends HumanoidModel<LivingEntity> {
	
	public PlayerModelMixin(ModelPart p_170677_) {
		super(p_170677_);
	}

	private void copyToSleeves() {
		PlayerModel model = (PlayerModel)(Object)this;
		model.leftPants.copyFrom(this.leftLeg);
		model.rightPants.copyFrom(this.rightLeg);
		model.leftSleeve.copyFrom(this.leftArm);
		model.rightSleeve.copyFrom(this.rightArm);
		model.jacket.copyFrom(this.body);
	}
	
//	@Redirect(method="setupAnim", at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/client/model/HumanoidModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
//            ordinal = -1
//            ))
	@Inject(method = "setupAnim", at = @At(value = "TAIL"), cancellable = false)
	//public void handleSetupAnim(HumanoidModel humanoidModel, LivingEntity living, float a, float b, float c, float d, float e) {
	public void handleSetupAnim(LivingEntity living, float a, float b, float c, float d, float e, CallbackInfo info) {
		//super.setupAnim(living, a, b, c, d, e);
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(living.getUUID());
		if (playerData != null) {
			SlottedItem chestSlot = playerData.getForSlot(MechPart.Chest);
			if (chestSlot.itemStack.getItem() instanceof ArmorBase base) {
				if (chestSlot.visible) {
					//defaults:
//					HumanoidModel.setupAnim():
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

					if (base.getHeadY() != 0.0f) {
						this.head.y = base.getHeadY();
						if (this.crouching) {
							this.head.y += 4.2f;
						}
						
						this.hat.y = this.head.y;
					}
					this.rightArm.x = -base.getArmDistance();
					this.leftArm.x = base.getArmDistance();
					
					this.rightArm.y = base.getArmY();
					this.leftArm.y = base.getArmY();
					if (this.crouching) {
						this.rightArm.y += 3.2f;
						this.leftArm.y += 3.2f;
					}
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
			} else if (legSlot.itemStack.getItem() instanceof ArmorBase base) {
				if (base.isViewBobDisabled()) {
					if (legSlot.visible) {
						if (living.isControlledByLocalInstance()) {
							Minecraft.getInstance().player.oBob = 0.0f;
	            			Minecraft.getInstance().player.bob = 0.0f;
						}
						
						//greatly reduce arm bob
						if (this.attackTime == 0.0f && this.swimAmount == 0.0f) {
							this.rightArm.xRot = this.rightArm.xRot * base.getViewBobArmSwayModifier();
							this.leftArm.xRot = this.leftArm.xRot * base.getViewBobArmSwayModifier();
						}
						
						//normal leg walking forward-and-back
						this.rightLeg.xRot = 0.0f;
						this.leftLeg.xRot = 0.0f;
					}
				}
			}
			
			ItemStack priorMh = BioMech.ClientModEvents.priorItems.get(EquipmentSlot.MAINHAND);
			ItemStack priorOh = BioMech.ClientModEvents.priorItems.get(EquipmentSlot.OFFHAND);
			//undo the 'item hold' arm position from biomech activator
			if (priorMh != null && priorMh.is(BioMechRegistry.ITEM_BIOMECH_ACTIVATOR.get())) {
				this.rightArm.xRot += 0.33f;
			}
			if (priorOh != null && priorOh.is(BioMechRegistry.ITEM_BIOMECH_ACTIVATOR.get())) {
				this.leftArm.xRot += 0.33f;
			}
			
			
			//prevent laser arms from pivoting up and down while walking
			HandActiveStatus has = BioMech.handActiveMap.get(living.getUUID());
			if (has != null && has.leftHandActive) {
				this.leftArm.xRot = 0.0f;
			}
			if (has != null && has.rightHandActive) {
				this.rightArm.xRot = 0.0f;
			}
		}
		
		this.copyToSleeves();
	}
	
}
