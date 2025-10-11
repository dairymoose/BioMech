package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.DiggerAnimator;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DiggerRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/digger_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/digger_arm.png"
    );

    public DiggerRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.setAnimatorProvider(DiggerAnimator::new)
        		.build());
    }
    
    public DiggerRightArmRenderer(AzRendererConfig <ItemStack> config) {
        super(config);
    }
}