package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class PipeMechHeadRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/pipe_mech_head.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/pipe_mech_head.png"
    );

    public PipeMechHeadRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX).build());
    }
}