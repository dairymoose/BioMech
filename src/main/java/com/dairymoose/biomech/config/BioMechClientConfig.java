package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BioMechClientConfig {

	public final ForgeConfigSpec.BooleanValue hideOffHandWhileNotInUse;
	
	public BioMechClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("first_person");
		this.hideOffHandWhileNotInUse = builder.comment("hideOffHandWhileNotInUse").translation("config.biomech.hideOffHandWhileNotInUse").define("hideOffHandWhileNotInUse", false);
		builder.pop();
	}
	
}
