package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechServerConfig {
	
	public final ConfigValue<Double> lootBioMechInChest;
	public final ConfigValue<Double> lootBioMechInMineshaft;
	public final ConfigValue<Double> lootBioMechInDungeon;

	public BioMechServerConfig(ForgeConfigSpec.Builder builder) {
		builder.push("loot");
		this.lootBioMechInChest = builder.comment("lootBioMechInChest").translation("config.biomech.lootBioMechInChest").define("lootBioMechInChest", 0.0625);
		this.lootBioMechInMineshaft = builder.comment("lootBioMechInMineshaft").translation("config.biomech.lootBioMechInMineshaft").define("lootBioMechInMineshaft", 0.33);
		this.lootBioMechInDungeon = builder.comment("lootBioMechInDungeon").translation("config.biomech.lootBioMechInDungeon").define("lootBioMechInDungeon", 0.33);
		builder.pop();
	}
	
}
