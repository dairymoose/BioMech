package com.dairymoose.biomech;

import net.minecraftforge.common.ForgeConfigSpec;

public class BioMechCommonConfig {

	public final ForgeConfigSpec.BooleanValue enableBioMechStation;
	
	public BioMechCommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("common");
		
		builder.push("crafting");
		this.enableBioMechStation = builder.comment("enableBioMechStation").translation("config.xenotech.enableBioMechStation").define("enableBioMechStation", true);
		builder.pop();

		builder.pop();
	}
	
}
