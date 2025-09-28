package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechServerConfig {
	
	public final ConfigValue<Double> lootBioMechInChest;
	public final ConfigValue<Double> lootBioMechInMineshaft;

	public BioMechServerConfig(ForgeConfigSpec.Builder builder) {
		builder.push("loot");
		this.lootBioMechInChest = builder.comment("lootBioMechInChest").translation("config.biomech.lootBioMechInChest").define("lootBioMechInChest", 0.05);
		this.lootBioMechInMineshaft = builder.comment("lootBioMechInMineshaft").translation("config.biomech.lootBioMechInMineshaft").define("lootBioMechInMineshaft", 0.33);
		builder.pop();
	}
	
}
