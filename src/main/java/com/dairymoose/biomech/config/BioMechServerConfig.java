package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechServerConfig {
	
	public static final double defaultChestLootChance = 0.0833; //1 in 12
	public static final double defaultMineshaftLootChance = 0.40;
	public static final double defaultDungeonLootChance = 0.60;
	public static final double defaultAncientCityLootChance = 0.80;
	public static final double defaultShipwreckLootChance = 0.33;
	public static final double defaultNetherFortressLootChance = 0.60;
	public final ConfigValue<Double> lootBioMechInChest;
	public final ConfigValue<Double> lootBioMechInMineshaft;
	public final ConfigValue<Double> lootBioMechInDungeon;
	public final ConfigValue<Double> lootBioMechInAncientCity;
	public final ConfigValue<Double> lootBioMechInShipwreck;
	public final ConfigValue<Double> lootBioMechInNetherFortress;
	public final ForgeConfigSpec.BooleanValue elytraMechChestplateCanBeLooted;
	
	public final ConfigValue<Double> gatlingMinFalloffFactor;
	public final ConfigValue<Double> gatlingDamage;
	public final ConfigValue<Double> gatlingEnergyPerSec;

	public BioMechServerConfig(ForgeConfigSpec.Builder builder) {
		builder.push("loot");
		this.lootBioMechInChest = builder.comment("lootBioMechInChest").translation("config.biomech.lootBioMechInChest").define("lootBioMechInChest", defaultChestLootChance);
		this.lootBioMechInMineshaft = builder.comment("lootBioMechInMineshaft").translation("config.biomech.lootBioMechInMineshaft").define("lootBioMechInMineshaft", defaultMineshaftLootChance);
		this.lootBioMechInDungeon = builder.comment("lootBioMechInDungeon").translation("config.biomech.lootBioMechInDungeon").define("lootBioMechInDungeon", defaultDungeonLootChance);
		this.lootBioMechInAncientCity = builder.comment("lootBioMechInAncientCity").translation("config.biomech.lootBioMechInAncientCity").define("lootBioMechInAncientCity", defaultAncientCityLootChance);
		this.lootBioMechInShipwreck = builder.comment("lootBioMechInShipwreck").translation("config.biomech.lootBioMechInShipwreck").define("lootBioMechInShipwreck", defaultShipwreckLootChance);
		this.lootBioMechInNetherFortress = builder.comment("lootBioMechInNetherFortress").translation("config.biomech.lootBioMechInNetherFortress").define("lootBioMechInNetherFortress", defaultNetherFortressLootChance);
		this.elytraMechChestplateCanBeLooted = builder.comment("elytraMechChestplateCanBeLooted").translation("config.biomech.elytraMechChestplateCanBeLooted").define("elytraMechChestplateCanBeLooted", true);
		builder.pop();
		
		builder.push("hand_items");
		this.gatlingMinFalloffFactor = builder.comment("gatlingMinFalloffFactor").translation("config.biomech.gatlingMinFalloffFactor").defineInRange("gatlingMinFalloffFactor", 0.5, 0.0, 1.0);
		this.gatlingDamage = builder.comment("gatlingDamage").translation("config.biomech.gatlingDamage").define("gatlingDamage", 30.0);
		this.gatlingEnergyPerSec = builder.comment("gatlingEnergyPerSec").translation("config.biomech.gatlingEnergyPerSec").define("gatlingEnergyPerSec", 10.0);
		builder.pop();
	}
	
}
