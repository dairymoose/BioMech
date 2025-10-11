package com.dairymoose.biomech.armor.renderer.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.anim.MiningLaserAnimator;

import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MiningLaserRightArmRenderer extends AzArmorRenderer {
	public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/mining_laser_right_arm.geo.json"
    );

	public static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/mining_laser.png"
    );

    public MiningLaserRightArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.setAnimatorProvider(MiningLaserAnimator::new)
        		.build());
    }
    
    public MiningLaserRightArmRenderer(AzRendererConfig <ItemStack> config) {
        super(config);
    }
}