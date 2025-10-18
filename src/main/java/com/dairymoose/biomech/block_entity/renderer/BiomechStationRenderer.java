package com.dairymoose.biomech.block_entity.renderer;

import org.jetbrains.annotations.NotNull;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block.BioMechStationBlock;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;
import com.dairymoose.biomech.block_entity.anim.BioMechStationAnimator;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.azure.azurelib.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.render.block.AzBlockEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
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
    
    @Override
    public void render(@NotNull BioMechStationBlockEntity entity, float partialTick, @NotNull PoseStack poseStack,
    		@NotNull MultiBufferSource source, int packedLight, int packedOverlay) {
    	if (entity.getBlockState().is(BioMechRegistry.BLOCK_BIOMECH_STATION.get())) {
    		if (entity.getBlockState().getValue(BioMechStationBlock.MULTIBLOCK).booleanValue()) {
    			return;
    		}
    	}
    	super.render(entity, partialTick, poseStack, source, packedLight, packedOverlay);
    }

}