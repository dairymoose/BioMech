package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.DrillAnimator;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class HerosLeftArmRenderer extends HerosRightArmRenderer {
    protected static final ResourceLocation GEO_LEFT = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        GEO.getPath().replace("right", "left")
    );

    public HerosLeftArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO_LEFT, TEX)
        		
        		.build());
    }
}