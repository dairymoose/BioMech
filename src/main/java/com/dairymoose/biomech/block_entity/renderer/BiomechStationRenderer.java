package com.dairymoose.biomech.block_entity.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;
import com.dairymoose.biomech.block_entity.anim.BioMechStationAnimator;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class BioMechStationRenderer extends AzBlockEntityRenderer<BioMechStationBlockEntity> {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/block/biomech_station.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/block/biomech_station.png"
    );

    public BioMechStationRenderer() {
		super(
            AzBlockEntityRendererConfig.<BioMechStationBlockEntity>builder(GEO, TEX)
                .setAnimatorProvider(BioMechStationAnimator::new).build()
        );
    }

}