package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzAutoGlowingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class OpticsUnitRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/optics_unit.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/optics_unit.png"
    );

    public OpticsUnitRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX).addRenderLayer(new AzAutoGlowingLayer<ItemStack>()).build());
    }
}