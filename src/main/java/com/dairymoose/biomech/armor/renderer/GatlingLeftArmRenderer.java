package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.GatlingAnimator;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class GatlingLeftArmRenderer extends GatlingRightArmRenderer {
    protected static final ResourceLocation GEO_LEFT = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        GEO.getPath().replace("right", "left")
    );

    public GatlingLeftArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO_LEFT, TEX)
        		.setAnimatorProvider(GatlingAnimator::new)
        		.build());
    }
}