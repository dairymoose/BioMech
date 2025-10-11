package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.MiningLaserAnimator;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PipeMechRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/pipe_mech_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/pipe_mech_right_arm.png"
    );

    public PipeMechRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.setAnimatorProvider(MiningLaserAnimator::new)
        		.build());
    }
    
    public PipeMechRightArmRenderer(AzRendererConfig <ItemStack> config) {
        super(config);
    }
}