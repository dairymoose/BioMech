package com.dairymoose.biomech.item.anim;

import org.jetbrains.annotations.NotNull;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.animation.controller.AzAnimationController;
import mod.azure.azurelib.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.animation.impl.AzItemAnimator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RepulsorLiftAnimator extends AzItemAnimator {
	public static ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(BioMech.MODID,
			"animations/item/repulsor_lift.animation.json");

	@Override
	public void registerControllers(AzAnimationControllerContainer<ItemStack> animationControllerContainer) {
		animationControllerContainer.add(AzAnimationController.builder(this, "base_controller").build());
	}

	@Override
	public @NotNull ResourceLocation getAnimationLocation(ItemStack animatable) {
		return ANIMATIONS;
	}
}
