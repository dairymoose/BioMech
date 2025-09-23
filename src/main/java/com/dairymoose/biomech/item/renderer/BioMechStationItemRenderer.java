package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.rewrite.render.item.AzItemRenderer;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class BioMechStationItemRenderer extends AzItemRenderer {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/block/biomech_station.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/block/biomech_station.png"
    );

    public BioMechStationItemRenderer() {
		super(
			AzItemRendererConfig.builder(GEO, TEX).useNewOffset(true).build()
        );
    }

}