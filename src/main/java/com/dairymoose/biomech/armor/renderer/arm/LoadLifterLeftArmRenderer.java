package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class LoadLifterLeftArmRenderer extends LoadLifterRightArmRenderer {
    protected static final ResourceLocation GEO_LEFT = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        GEO.getPath().replace("right", "left")
    );

    public LoadLifterLeftArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO_LEFT, TEX)
        		
        		.build());
    }
}