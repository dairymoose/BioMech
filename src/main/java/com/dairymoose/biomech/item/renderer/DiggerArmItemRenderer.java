package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.armor.renderer.DiggerRightArmRenderer;
import com.dairymoose.biomech.item.anim.DiggerAnimator;
import com.dairymoose.biomech.item.anim.DrillAnimator;

import mod.azure.azurelib.rewrite.render.item.AzItemRenderer;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class DiggerArmItemRenderer extends AzItemRenderer {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        DiggerRightArmRenderer.GEO.getPath().replace(".geo", "_item.geo") //item file is identical - but if we use the same file it uses just 1 model - and causes the GUI item to move in 3d
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
    	DiggerRightArmRenderer.TEX.getPath()
    );

    public DiggerArmItemRenderer() {
		super(
			AzItemRendererConfig.builder(GEO, TEX)
			.setAnimatorProvider(DiggerAnimator::new)
			.useNewOffset(true).build()
        );
    }

}