package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.armor.renderer.arm.ExtendoRightArmRenderer;
import com.dairymoose.biomech.item.anim.ExtendoArmAnimator;

import mod.azure.azurelib.render.item.AzItemRenderer;
import mod.azure.azurelib.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class ExtendoArmItemRenderer extends AzItemRenderer {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        ExtendoRightArmRenderer.GEO.getPath().replace(".geo", "_item.geo") //item file is identical - but if we use the same file it uses just 1 model - and causes the GUI item to move in 3d
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
    	ExtendoRightArmRenderer.TEX.getPath()
    );

    public ExtendoArmItemRenderer() {
		super(
			AzItemRendererConfig.builder(GEO, TEX).useNewOffset(true)
			.setAnimatorProvider(ExtendoArmAnimator::new)
			.build()
        );
    }

}