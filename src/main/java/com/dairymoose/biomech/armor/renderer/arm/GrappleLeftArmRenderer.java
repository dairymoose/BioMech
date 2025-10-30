package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.BuzzsawAnimator;
import com.dairymoose.biomech.item.anim.GrappleAnimator;

import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class GrappleLeftArmRenderer extends GrappleRightArmRenderer {
    protected static final ResourceLocation GEO_LEFT = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        GEO.getPath().replace("right", "left")
    );

    public GrappleLeftArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO_LEFT, TEX)
        		.setAnimatorProvider(GrappleAnimator::new)
        		.build());
    }
}