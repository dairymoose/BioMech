package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.DiggerAnimator;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class DiggerLeftArmRenderer extends DiggerRightArmRenderer {
    protected static final ResourceLocation GEO_LEFT = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        GEO.getPath().replace("right", "left")
    );

    public DiggerLeftArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO_LEFT, TEX)
        		.setAnimatorProvider(DiggerAnimator::new)
        		.build());
    }
}