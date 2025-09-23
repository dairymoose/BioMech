package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PowerRightArmRenderer extends AzArmorRenderer {
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/right_power_arm.geo.json"
    );

    public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/right_power_arm.png"
    );

    public PowerRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX).build());
    }
    
    public PowerRightArmRenderer(AzRendererConfig <ItemStack> config) {
        super(config);
    }
}