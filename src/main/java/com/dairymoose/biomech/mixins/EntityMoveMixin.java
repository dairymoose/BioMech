package com.dairymoose.biomech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.LavastrideLeggingsArmor;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.item.armor.RepulsorLiftArmor;
import com.dairymoose.biomech.item.armor.SpiderWalkersArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Entity.class)
public abstract class EntityMoveMixin {
	
	public EntityMoveMixin() {
		
	}

	@Redirect(method="move", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;onGround()Z",
            ordinal = 1
            ))
	public boolean handleOnGround(Entity entity) {
		if (entity instanceof Player player) {
			BioMechPlayerData playerData = BioMech.globalPlayerData.get(entity.getUUID());
			if (playerData != null) {
				BlockPos blockpos = entity.getOnPosLegacy();
		        BlockState blockstate = entity.level().getBlockState(blockpos);
		        Block block = blockstate.getBlock();
				
				boolean hasRepulsor = playerData.getForSlot(MechPart.Leggings).itemStack.getItem() instanceof RepulsorLiftArmor;
				boolean isLavastrideVsMagma = playerData.getForSlot(MechPart.Leggings).itemStack.getItem() instanceof LavastrideLeggingsArmor && block instanceof MagmaBlock;
				if (hasRepulsor || isLavastrideVsMagma) {
					return false;
				}
			}
		}
		
		return entity.onGround();
		//block.stepOn(level, pos, blockState, entity);
	}
	
}