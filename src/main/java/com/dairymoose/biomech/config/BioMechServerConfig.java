package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechServerConfig {
	
	public static final double defaultChestLootChance = 0.067;
	public static final double defaultMineshaftLootChance = 0.40;
	public static final double defaultDungeonLootChance = 0.40;
	public final ConfigValue<Double> lootBioMechInChest;
	public final ConfigValue<Double> lootBioMechInMineshaft;
	public final ConfigValue<Double> lootBioMechInDungeon;

	public BioMechServerConfig(ForgeConfigSpec.Builder builder) {
		builder.push("loot");
		this.lootBioMechInChest = builder.comment("lootBioMechInChest").translation("config.biomech.lootBioMechInChest").define("lootBioMechInChest", defaultChestLootChance);
		this.lootBioMechInMineshaft = builder.comment("lootBioMechInMineshaft").translation("config.biomech.lootBioMechInMineshaft").define("lootBioMechInMineshaft", defaultMineshaftLootChance);
		this.lootBioMechInDungeon = builder.comment("lootBioMechInDungeon").translation("config.biomech.lootBioMechInDungeon").define("lootBioMechInDungeon", defaultDungeonLootChance);
		builder.pop();
	}
	
}
