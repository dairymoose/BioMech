package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechServerConfig {
	
	public final ConfigValue<Double> gatlingMinFalloffFactor;
	public final ConfigValue<Double> gatlingDamage;
	public final ConfigValue<Double> gatlingEnergyPerSec;

	public BioMechServerConfig(ForgeConfigSpec.Builder builder) {		
		builder.push("hand_items");
		this.gatlingMinFalloffFactor = builder.comment("gatlingMinFalloffFactor").translation("config.biomech.gatlingMinFalloffFactor").defineInRange("gatlingMinFalloffFactor", 0.5, 0.0, 1.0);
		this.gatlingDamage = builder.comment("gatlingDamage").translation("config.biomech.gatlingDamage").define("gatlingDamage", 26.0);
		this.gatlingEnergyPerSec = builder.comment("gatlingEnergyPerSec").translation("config.biomech.gatlingEnergyPerSec").define("gatlingEnergyPerSec", 15.0);
		builder.pop();
	}
	
}
