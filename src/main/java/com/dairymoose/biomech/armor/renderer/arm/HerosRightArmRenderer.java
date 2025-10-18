package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.render.armor.AzArmorRenderer;
import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class HerosRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/heros_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/heros_arm.png"
    );

    public HerosRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		
        		.build());
    }
    
    public HerosRightArmRenderer(AzArmorRendererConfig config) {
        super(config);
    }
}