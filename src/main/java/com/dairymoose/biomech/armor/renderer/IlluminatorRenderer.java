package com.dairymoose.biomech.armor.renderer;

import java.util.UUID;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.render.armor.AzArmorRenderer;
import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class IlluminatorRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/illuminator.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/illuminator.png"
    );

    public IlluminatorRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.addRenderLayer(new AzAutoGlowingLayer<UUID, ItemStack>())
        		.build());
    }
}