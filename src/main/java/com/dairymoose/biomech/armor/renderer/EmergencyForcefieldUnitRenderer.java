package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class EmergencyForcefieldUnitRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/emergency_forcefield_unit.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/emergency_forcefield_unit.png"
    );

    public EmergencyForcefieldUnitRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX).build());
    }
}