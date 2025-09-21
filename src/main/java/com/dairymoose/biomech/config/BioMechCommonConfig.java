package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BioMechCommonConfig {

	public final ForgeConfigSpec.BooleanValue enableBioMechStation;
	
	public final ForgeConfigSpec.BooleanValue walkToBioMechStation;
	
	public BioMechCommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("crafting");
		this.enableBioMechStation = builder.comment("enableBioMechStation").translation("config.xenotech.enableBioMechStation").define("enableBioMechStation", true);
		builder.pop();
		
		builder.push("biomech_station");
		this.walkToBioMechStation = builder.comment("walkToBioMechStation").translation("config.xenotech.walkToBioMechStation").define("walkToBioMechStation", true);
		builder.pop();
	}
	
}
