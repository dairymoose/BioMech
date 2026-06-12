package com.dairymoose.biomech.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

public class BioMechServerConfig {
	
	public final ConfigValue<Double> gatlingMinFalloffFactor;
	public final ConfigValue<Double> gatlingDamage;
	public final ConfigValue<Double> gatlingEnergyPerSec;

	public BioMechServerConfig(ModConfigSpec.Builder builder) {		
		builder.push("hand_items");
		this.gatlingMinFalloffFactor = builder.comment("gatlingMinFalloffFactor").translation("config.biomech.gatlingMinFalloffFactor").defineInRange("gatlingMinFalloffFactor", 0.5, 0.0, 1.0);
		this.gatlingDamage = builder.comment("gatlingDamage").translation("config.biomech.gatlingDamage").define("gatlingDamage", 30.0);
		this.gatlingEnergyPerSec = builder.comment("gatlingEnergyPerSec").translation("config.biomech.gatlingEnergyPerSec").define("gatlingEnergyPerSec", 15.0);
		builder.pop();
	}
	
}
