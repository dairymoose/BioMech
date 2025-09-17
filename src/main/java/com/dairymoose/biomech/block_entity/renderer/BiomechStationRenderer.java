package com.dairymoose.biomech.block_entity.renderer;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.block_entity.BiomechStationBlockEntity;
import com.dairymoose.biomech.block_entity.anim.BiomechStationAnimator;

import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class BiomechStationRenderer extends AzBlockEntityRenderer<BiomechStationBlockEntity> {
	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/block/biomech_station.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/block/biomech_station.png"
    );

    public BiomechStationRenderer() {
		super(
            AzBlockEntityRendererConfig.<BiomechStationBlockEntity>builder(GEO, TEX)
                .setAnimatorProvider(BiomechStationAnimator::new).build()
        );
    }

}