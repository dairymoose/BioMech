package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.armor.renderer.arm.HarvesterRightArmRenderer;
import com.dairymoose.biomech.item.anim.HarvesterAnimator;

import mod.azure.azurelib.render.item.AzItemRenderer;
import mod.azure.azurelib.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class HarvesterArmItemRenderer extends AzItemRenderer {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        HarvesterRightArmRenderer.GEO.getPath().replace(".geo", "_item.geo") //item file is identical - but if we use the same file it uses just 1 model - and causes the GUI item to move in 3d
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
    	HarvesterRightArmRenderer.TEX.getPath()
    );

    public HarvesterArmItemRenderer() {
		super(
			AzItemRendererConfig.builder(GEO, TEX).useNewOffset(true)
			.setAnimatorProvider(HarvesterAnimator::new)
			.build()
        );
    }

}