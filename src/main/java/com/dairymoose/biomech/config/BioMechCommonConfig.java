package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BioMechCommonConfig {

	public final ForgeConfigSpec.BooleanValue enableBioMechStation;
	public final ForgeConfigSpec.BooleanValue enableBioMechActivator;
	public final ForgeConfigSpec.BooleanValue enableBioMechDeactivator;
	
	public final ForgeConfigSpec.BooleanValue enableBiomechScrapCrafting;
	
	public final ForgeConfigSpec.BooleanValue enableIronMechChestplateCrafting;
	public final ForgeConfigSpec.BooleanValue enableDiamondMechChestplateCrafting;
	
	public final ForgeConfigSpec.BooleanValue enableSpiderWalkersCrafting;
	public final ForgeConfigSpec.BooleanValue enableScubaTankCrafting;
	public final ForgeConfigSpec.BooleanValue enableJetpackCrafting;
	
	public final ForgeConfigSpec.BooleanValue walkToBioMechStation;
	
	public BioMechCommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("crafting");
		this.enableBioMechStation = builder.comment("enableBioMechStation").translation("config.biomech.enableBioMechStation").define("enableBioMechStation", true);
		this.enableBioMechActivator = builder.comment("enableBioMechActivator").translation("config.biomech.enableBioMechActivator").define("enableBioMechActivator", true);
		this.enableBioMechDeactivator = builder.comment("enableBioMechDeactivator").translation("config.biomech.enableBioMechDeactivator").define("enableBioMechDeactivator", true);
		
		this.enableBiomechScrapCrafting = builder.comment("enableBiomechScrapCrafting").translation("config.biomech.enableBiomechScrapCrafting").define("enableBiomechScrapCrafting", true);
		
		this.enableIronMechChestplateCrafting = builder.comment("enableIronMechChestplateCrafting").translation("config.biomech.enableIronMechChestplateCrafting").define("enableIronMechChestplateCrafting", true);
		this.enableDiamondMechChestplateCrafting = builder.comment("enableDiamondMechChestplateCrafting").translation("config.biomech.enableDiamondMechChestplateCrafting").define("enableDiamondMechChestplateCrafting", true);
		
		this.enableSpiderWalkersCrafting = builder.comment("enableSpiderWalkersCrafting").translation("config.biomech.enableSpiderWalkersCrafting").define("enableSpiderWalkersCrafting", true);
		this.enableScubaTankCrafting = builder.comment("enableScubaTankCrafting").translation("config.biomech.enableScubaTankCrafting").define("enableScubaTankCrafting", true);
		this.enableJetpackCrafting = builder.comment("enableJetpackCrafting").translation("config.biomech.enableJetpackCrafting").define("enableJetpackCrafting", true);
		builder.pop();
		
		builder.push("biomech_station");
		this.walkToBioMechStation = builder.comment("walkToBioMechStation").translation("config.biomech.walkToBioMechStation").define("walkToBioMechStation", true);
		builder.pop();
	}
	
}
