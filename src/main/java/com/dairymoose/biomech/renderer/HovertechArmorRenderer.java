package com.dairymoose.biomech.renderer;

import com.dairymoose.biomech.BioMechMod;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class HovertechArmorRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMechMod.MODID,
        "geo/item/armor_hovertech_leggings.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMechMod.MODID,
        "textures/item/hovertech_leggings.png"
    );

    public HovertechArmorRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX).build());
    }
}