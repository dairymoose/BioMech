package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BioMechCommonConfig {

	public final ForgeConfigSpec.BooleanValue enableBioMechStation;
	public final ForgeConfigSpec.BooleanValue enableBioMechActivator;
	public final ForgeConfigSpec.BooleanValue enableBioMechDeactivator;
	
	public final ForgeConfigSpec.BooleanValue walkToBioMechStation;
	
	public BioMechCommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("crafting");
		this.enableBioMechStation = builder.comment("enableBioMechStation").translation("config.biomech.enableBioMechStation").define("enableBioMechStation", true);
		this.enableBioMechActivator = builder.comment("enableBioMechActivator").translation("config.biomech.enableBioMechActivator").define("enableBioMechActivator", true);
		this.enableBioMechDeactivator = builder.comment("enableBioMechDeactivator").translation("config.biomech.enableBioMechDeactivator").define("enableBioMechDeactivator", true);
		builder.pop();
		
		builder.push("biomech_station");
		this.walkToBioMechStation = builder.comment("walkToBioMechStation").translation("config.biomech.walkToBioMechStation").define("walkToBioMechStation", true);
		builder.pop();
	}
	
}
