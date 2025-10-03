package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.armor.renderer.PipeMechRightArmRenderer;

import mod.azure.azurelib.rewrite.render.item.AzItemRenderer;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class PipeMechArmItemRenderer extends AzItemRenderer {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        PipeMechRightArmRenderer.GEO.getPath().replace(".geo", "_item.geo") //item file is identical - but if we use the same file it uses just 1 model - and causes the GUI item to move in 3d
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
    	PipeMechRightArmRenderer.TEX.getPath()
    );

    public PipeMechArmItemRenderer() {
		super(
			AzItemRendererConfig.builder(GEO, TEX).useNewOffset(true).build()
        );
    }

}