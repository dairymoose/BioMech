package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.ElytraMechChestplateAnimator;

import mod.azure.azurelib.render.armor.AzArmorRenderer;
import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class ElytraMechChestplateRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/elytra_mech_chestplate.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/elytra_mech_chestplate.png"
    );

    public ElytraMechChestplateRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.setAnimatorProvider(ElytraMechChestplateAnimator::new)
        		.build());
    }
}