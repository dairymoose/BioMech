package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.DrillAnimator;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LoadLifterRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/load_lifter_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/load_lifter_arm.png"
    );

    public LoadLifterRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		
        		.build());
    }
    
    public LoadLifterRightArmRenderer(AzRendererConfig <ItemStack> config) {
        super(config);
    }
}