package com.dairymoose.biomech.armor.renderer;

import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.TeleportationCrystalAnimator;

import mod.azure.azurelib.render.armor.AzArmorRenderer;
import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TeleportationCrystalRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/teleportation_crystal.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/teleportation_crystal.png"
    );

    public TeleportationCrystalRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.addRenderLayer(new AzAutoGlowingLayer<UUID, ItemStack>())
        		.setAnimatorProvider(TeleportationCrystalAnimator::new)
        		.build());
    }
}