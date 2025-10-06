package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.DrillAnimator;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DrillRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/drill_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/drill_arm.png"
    );

    public DrillRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.setAnimatorProvider(DrillAnimator::new)
        		.build());
    }
    
    public DrillRightArmRenderer(AzRendererConfig <ItemStack> config) {
        super(config);
    }
}