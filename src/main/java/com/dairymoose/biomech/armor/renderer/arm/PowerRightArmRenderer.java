package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.render.armor.AzArmorRenderer;
import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

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
    
    public PowerRightArmRenderer(AzArmorRendererConfig config) {
        super(config);
    }
}