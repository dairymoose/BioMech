package com.dairymoose.biomech.item.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class MiningLaserLeftArmRenderer extends MiningLaserRightArmRenderer {
    protected static final ResourceLocation GEO_LEFT = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        GEO.getPath().replace("right", "left")
    );

    public MiningLaserLeftArmRenderer() {
        super(AzArmorRendererConfig.builder(GEO_LEFT, TEX).build());
    }
}