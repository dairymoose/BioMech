package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.armor.renderer.MiningLaserRightArmRenderer;
import com.dairymoose.biomech.item.anim.MiningLaserAnimator;

import mod.azure.azurelib.rewrite.render.item.AzItemRenderer;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class MiningLaserItemRenderer extends AzItemRenderer {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        MiningLaserRightArmRenderer.GEO.getPath().replace(".geo", "_item.geo") //item file is identical - but if we use the same file it uses just 1 model - and causes the GUI item to move in 3d
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
    	MiningLaserRightArmRenderer.TEX.getPath()
    );

    public MiningLaserItemRenderer() {
		super(
			AzItemRendererConfig.builder(GEO, TEX)
			.setAnimatorProvider(MiningLaserAnimator::new)
			.useNewOffset(true).build()
        );
    }

}