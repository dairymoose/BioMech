package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.BuzzsawAnimator;
import com.dairymoose.biomech.item.anim.GrappleAnimator;

import mod.azure.azurelib.render.armor.AzArmorRenderer;
import mod.azure.azurelib.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class GrappleRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/grapple_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/grapple_arm.png"
    );

    public GrappleRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.setAnimatorProvider(GrappleAnimator::new)
        		.build());
    }
    
    public GrappleRightArmRenderer(AzArmorRendererConfig config) {
        super(config);
    }
}