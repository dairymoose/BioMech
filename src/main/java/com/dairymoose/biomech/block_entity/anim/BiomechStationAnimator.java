package com.dairymoose.biomech.block_entity.anim;

import org.jetbrains.annotations.NotNull;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;

import mod.azure.azurelib.rewrite.animation.AzAnimatorConfig;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzBlockAnimator;
import net.minecraft.resources.ResourceLocation;

public class BioMechStationAnimator extends AzBlockAnimator<BioMechStationBlockEntity> {
    public BioMechStationAnimator() {
		super(AzAnimatorConfig.defaultConfig());
	}

	private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "animations/block/biomech_station.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<BioMechStationBlockEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, "base_controller")
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(BioMechStationBlockEntity animatable) {
        return ANIMATIONS;
    }
}