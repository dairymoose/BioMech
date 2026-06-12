package com.dairymoose.biomech.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

public class BioMechCommonConfig {

	public static final double defaultChestLootChance = 0.0833; //1 in 12
	public static final double defaultMineshaftLootChance = 0.40;
	public static final double defaultDungeonLootChance = 0.60;
	public static final double defaultAncientCityLootChance = 0.80;
	public static final double defaultShipwreckLootChance = 0.33;
	public static final double defaultNetherFortressLootChance = 0.60;
	
	public final ModConfigSpec.BooleanValue enableBioMechStation;
	public final ModConfigSpec.BooleanValue enableBioMechActivator;
	public final ModConfigSpec.BooleanValue enableBioMechDeactivator;
	
	public final ModConfigSpec.BooleanValue enableBiomechScrapCrafting;
	
	public final ModConfigSpec.BooleanValue enableIronMechArmorCrafting;
	public final ModConfigSpec.BooleanValue enableDiamondMechArmorCrafting;
	
	public final ModConfigSpec.BooleanValue enableBatteryPackCrafting;
	public final ModConfigSpec.BooleanValue enableBuzzsawArmCrafting;
	public final ModConfigSpec.BooleanValue enableColossusChestplateCrafting;
	public final ModConfigSpec.BooleanValue enableCpuCrafting;
	public final ModConfigSpec.BooleanValue enableDiggerArmCrafting;
	public final ModConfigSpec.BooleanValue enableDrillArmCrafting;
	public final ModConfigSpec.BooleanValue enableElytraMechChestplateCrafting;
	public final ModConfigSpec.BooleanValue enableEmergencyForcefieldUnitCrafting;
	public final ModConfigSpec.BooleanValue enableExtendoArmCrafting;
	public final ModConfigSpec.BooleanValue enableGasMaskCrafting;
	public final ModConfigSpec.BooleanValue enableGatlingArmCrafting;
	public final ModConfigSpec.BooleanValue enableGrappleArmCrafting;
	public final ModConfigSpec.BooleanValue enableHarvesterArmCrafting;
	public final ModConfigSpec.BooleanValue enableHerosArmCrafting;
	public final ModConfigSpec.BooleanValue enableHerosChestplateCrafting;
	public final ModConfigSpec.BooleanValue enableHerosHeadpieceCrafting;
	public final ModConfigSpec.BooleanValue enableHerosLeggingsCrafting;
	public final ModConfigSpec.BooleanValue enableHovertechLeggingsCrafting;
	public final ModConfigSpec.BooleanValue enableIlluminatorCrafting;
	public final ModConfigSpec.BooleanValue enableInterceptorArmsCrafting;
	public final ModConfigSpec.BooleanValue enableJetpackCrafting;
	public final ModConfigSpec.BooleanValue enableLavastrideLeggingsCrafting;
	public final ModConfigSpec.BooleanValue enableLoadLifterArmCrafting;
	public final ModConfigSpec.BooleanValue enableLoadLifterChassisCrafting;
	public final ModConfigSpec.BooleanValue enableMiningLaserArmCrafting;
	public final ModConfigSpec.BooleanValue enableMobilityTreadsCrafting;
	public final ModConfigSpec.BooleanValue enableNightVisionVisorCrafting;
	public final ModConfigSpec.BooleanValue enableOpticsUnitCrafting;
	public final ModConfigSpec.BooleanValue enablePipeMechArmCrafting;
	public final ModConfigSpec.BooleanValue enablePipeMechBodyCrafting;
	public final ModConfigSpec.BooleanValue enablePipeMechHeadCrafting;
	public final ModConfigSpec.BooleanValue enablePipeMechLegsCrafting;
	public final ModConfigSpec.BooleanValue enablePortableStorageUnitCrafting;
	public final ModConfigSpec.BooleanValue enablePowerArmCrafting;
	public final ModConfigSpec.BooleanValue enablePowerChestCrafting;
	public final ModConfigSpec.BooleanValue enablePowerHelmetCrafting;
	public final ModConfigSpec.BooleanValue enablePowerLeggingsCrafting;
	public final ModConfigSpec.BooleanValue enableRepulsorLiftCrafting;
	public final ModConfigSpec.BooleanValue enableScubaTankCrafting;
	public final ModConfigSpec.BooleanValue enableSpiderWalkersCrafting;
	public final ModConfigSpec.BooleanValue enableSpringLoadedLeggingsCrafting;
	public final ModConfigSpec.BooleanValue enableTeleportationCrystalCrafting;
	public final ModConfigSpec.BooleanValue enableTransformerModuleHelicopterCrafting;

	public final ModConfigSpec.BooleanValue enableBatteryPackLooting;
	public final ModConfigSpec.BooleanValue enableBuzzsawArmLooting;
	public final ModConfigSpec.BooleanValue enableColossusChestplateLooting;
	public final ModConfigSpec.BooleanValue enableCpuLooting;
	public final ModConfigSpec.BooleanValue enableDiggerArmLooting;
	public final ModConfigSpec.BooleanValue enableDrillArmLooting;
	public final ModConfigSpec.BooleanValue enableElytraMechChestplateLooting;
	public final ModConfigSpec.BooleanValue enableEmergencyForcefieldUnitLooting;
	public final ModConfigSpec.BooleanValue enableExtendoArmLooting;
	public final ModConfigSpec.BooleanValue enableGasMaskLooting;
	public final ModConfigSpec.BooleanValue enableGatlingArmLooting;
	public final ModConfigSpec.BooleanValue enableGrappleArmLooting;
	public final ModConfigSpec.BooleanValue enableHarvesterArmLooting;
	public final ModConfigSpec.BooleanValue enableHerosArmLooting;
	public final ModConfigSpec.BooleanValue enableHerosChestplateLooting;
	public final ModConfigSpec.BooleanValue enableHerosHeadpieceLooting;
	public final ModConfigSpec.BooleanValue enableHerosLeggingsLooting;
	public final ModConfigSpec.BooleanValue enableHovertechLeggingsLooting;
	public final ModConfigSpec.BooleanValue enableIlluminatorLooting;
	public final ModConfigSpec.BooleanValue enableInterceptorArmsLooting;
	public final ModConfigSpec.BooleanValue enableJetpackLooting;
	public final ModConfigSpec.BooleanValue enableLavastrideLeggingsLooting;
	public final ModConfigSpec.BooleanValue enableLoadLifterArmLooting;
	public final ModConfigSpec.BooleanValue enableLoadLifterChassisLooting;
	public final ModConfigSpec.BooleanValue enableMiningLaserArmLooting;
	public final ModConfigSpec.BooleanValue enableMobilityTreadsLooting;
	public final ModConfigSpec.BooleanValue enableNightVisionVisorLooting;
	public final ModConfigSpec.BooleanValue enableOpticsUnitLooting;
	public final ModConfigSpec.BooleanValue enablePipeMechArmLooting;
	public final ModConfigSpec.BooleanValue enablePipeMechBodyLooting;
	public final ModConfigSpec.BooleanValue enablePipeMechHeadLooting;
	public final ModConfigSpec.BooleanValue enablePipeMechLegsLooting;
	public final ModConfigSpec.BooleanValue enablePortableStorageUnitLooting;
	public final ModConfigSpec.BooleanValue enablePowerArmLooting;
	public final ModConfigSpec.BooleanValue enablePowerChestLooting;
	public final ModConfigSpec.BooleanValue enablePowerHelmetLooting;
	public final ModConfigSpec.BooleanValue enablePowerLeggingsLooting;
	public final ModConfigSpec.BooleanValue enableRepulsorLiftLooting;
	public final ModConfigSpec.BooleanValue enableScubaTankLooting;
	public final ModConfigSpec.BooleanValue enableSpiderWalkersLooting;
	public final ModConfigSpec.BooleanValue enableSpringLoadedLeggingsLooting;
	public final ModConfigSpec.BooleanValue enableTeleportationCrystalLooting;
	public final ModConfigSpec.BooleanValue enableTransformerModuleHelicopterLooting;
	
	public final ConfigValue<Double> lootBioMechInChest;
	public final ConfigValue<Double> lootBioMechInMineshaft;
	public final ConfigValue<Double> lootBioMechInDungeon;
	public final ConfigValue<Double> lootBioMechInAncientCity;
	public final ConfigValue<Double> lootBioMechInShipwreck;
	public final ConfigValue<Double> lootBioMechInNetherFortress;
	
	public final ModConfigSpec.BooleanValue walkToBioMechStation;
	
	public final ConfigValue<Double> emergencyForcefieldUnitCooldown;
	
	public BioMechCommonConfig(ModConfigSpec.Builder builder) {
		builder.push("crafting");
		this.enableBioMechStation = builder.comment("enableBioMechStation").translation("config.biomech.enableBioMechStation").define("enableBioMechStation", true);
		this.enableBioMechActivator = builder.comment("enableBioMechActivator").translation("config.biomech.enableBioMechActivator").define("enableBioMechActivator", true);
		this.enableBioMechDeactivator = builder.comment("enableBioMechDeactivator").translation("config.biomech.enableBioMechDeactivator").define("enableBioMechDeactivator", true);
		
		this.enableBiomechScrapCrafting = builder.comment("enableBiomechScrapCrafting").translation("config.biomech.enableBiomechScrapCrafting").define("enableBiomechScrapCrafting", true);
		
		this.enableIronMechArmorCrafting = builder.comment("enableIronMechArmorCrafting").translation("config.biomech.enableIronMechArmorCrafting").define("enableIronMechArmorCrafting", true);
		this.enableDiamondMechArmorCrafting = builder.comment("enableDiamondMechArmorCrafting").translation("config.biomech.enableDiamondMechArmorCrafting").define("enableDiamondMechArmorCrafting", true);
		
		//looted items crafting
		this.enableBatteryPackCrafting = builder.comment("enableBatteryPackCrafting").translation("config.biomech.enableBatteryPackCrafting").define("enableBatteryPackCrafting", true);
		this.enableBuzzsawArmCrafting = builder.comment("enableBuzzsawArmCrafting").translation("config.biomech.enableBuzzsawArmCrafting").define("enableBuzzsawArmCrafting", true);
		this.enableColossusChestplateCrafting = builder.comment("enableColossusChestplateCrafting").translation("config.biomech.enableColossusChestplateCrafting").define("enableColossusChestplateCrafting", true);
		this.enableCpuCrafting = builder.comment("enableCpuCrafting").translation("config.biomech.enableCpuCrafting").define("enableCpuCrafting", true);
		this.enableDiggerArmCrafting = builder.comment("enableDiggerArmCrafting").translation("config.biomech.enableDiggerArmCrafting").define("enableDiggerArmCrafting", true);
		this.enableDrillArmCrafting = builder.comment("enableDrillArmCrafting").translation("config.biomech.enableDrillArmCrafting").define("enableDrillArmCrafting", true);
		this.enableElytraMechChestplateCrafting = builder.comment("enableElytraMechChestplateCrafting").translation("config.biomech.enableElytraMechChestplateCrafting").define("enableElytraMechChestplateCrafting", true);
		this.enableEmergencyForcefieldUnitCrafting = builder.comment("enableEmergencyForcefieldUnitCrafting").translation("config.biomech.enableEmergencyForcefieldUnitCrafting").define("enableEmergencyForcefieldUnitCrafting", true);
		this.enableExtendoArmCrafting = builder.comment("enableExtendoArmCrafting").translation("config.biomech.enableExtendoArmCrafting").define("enableExtendoArmCrafting", true);
		this.enableGasMaskCrafting = builder.comment("enableGasMaskCrafting").translation("config.biomech.enableGasMaskCrafting").define("enableGasMaskCrafting", true);
		this.enableGatlingArmCrafting = builder.comment("enableGatlingArmCrafting").translation("config.biomech.enableGatlingArmCrafting").define("enableGatlingArmCrafting", true);
		this.enableGrappleArmCrafting = builder.comment("enableGrappleArmCrafting").translation("config.biomech.enableGrappleArmCrafting").define("enableGrappleArmCrafting", true);
		this.enableHarvesterArmCrafting = builder.comment("enableHarvesterArmCrafting").translation("config.biomech.enableHarvesterArmCrafting").define("enableHarvesterArmCrafting", true);
		this.enableHerosArmCrafting = builder.comment("enableHerosArmCrafting").translation("config.biomech.enableHerosArmCrafting").define("enableHerosArmCrafting", true);
		this.enableHerosChestplateCrafting = builder.comment("enableHerosChestplateCrafting").translation("config.biomech.enableHerosChestplateCrafting").define("enableHerosChestplateCrafting", true);
		this.enableHerosHeadpieceCrafting = builder.comment("enableHerosHeadpieceCrafting").translation("config.biomech.enableHerosHeadpieceCrafting").define("enableHerosHeadpieceCrafting", true);
		this.enableHerosLeggingsCrafting = builder.comment("enableHerosLeggingsCrafting").translation("config.biomech.enableHerosLeggingsCrafting").define("enableHerosLeggingsCrafting", true);
		this.enableHovertechLeggingsCrafting = builder.comment("enableHovertechLeggingsCrafting").translation("config.biomech.enableHovertechLeggingsCrafting").define("enableHovertechLeggingsCrafting", true);
		this.enableIlluminatorCrafting = builder.comment("enableIlluminatorCrafting").translation("config.biomech.enableIlluminatorCrafting").define("enableIlluminatorCrafting", true);
		this.enableInterceptorArmsCrafting = builder.comment("enableInterceptorArmsCrafting").translation("config.biomech.enableInterceptorArmsCrafting").define("enableInterceptorArmsCrafting", true);
		this.enableJetpackCrafting = builder.comment("enableJetpackCrafting").translation("config.biomech.enableJetpackCrafting").define("enableJetpackCrafting", true);
		this.enableLavastrideLeggingsCrafting = builder.comment("enableLavastrideLeggingsCrafting").translation("config.biomech.enableLavastrideLeggingsCrafting").define("enableLavastrideLeggingsCrafting", true);
		this.enableLoadLifterArmCrafting = builder.comment("enableLoadLifterArmCrafting").translation("config.biomech.enableLoadLifterArmCrafting").define("enableLoadLifterArmCrafting", true);
		this.enableLoadLifterChassisCrafting = builder.comment("enableLoadLifterChassisCrafting").translation("config.biomech.enableLoadLifterChassisCrafting").define("enableLoadLifterChassisCrafting", true);
		this.enableMiningLaserArmCrafting = builder.comment("enableMiningLaserArmCrafting").translation("config.biomech.enableMiningLaserArmCrafting").define("enableMiningLaserArmCrafting", true);
		this.enableMobilityTreadsCrafting = builder.comment("enableMobilityTreadsCrafting").translation("config.biomech.enableMobilityTreadsCrafting").define("enableMobilityTreadsCrafting", true);
		this.enableNightVisionVisorCrafting = builder.comment("enableNightVisionVisorCrafting").translation("config.biomech.enableNightVisionVisorCrafting").define("enableNightVisionVisorCrafting", true);
		this.enableOpticsUnitCrafting = builder.comment("enableOpticsUnitCrafting").translation("config.biomech.enableOpticsUnitCrafting").define("enableOpticsUnitCrafting", true);
		this.enablePipeMechArmCrafting = builder.comment("enablePipeMechArmCrafting").translation("config.biomech.enablePipeMechArmCrafting").define("enablePipeMechArmCrafting", true);
		this.enablePipeMechBodyCrafting = builder.comment("enablePipeMechBodyCrafting").translation("config.biomech.enablePipeMechBodyCrafting").define("enablePipeMechBodyCrafting", true);
		this.enablePipeMechHeadCrafting = builder.comment("enablePipeMechHeadCrafting").translation("config.biomech.enablePipeMechHeadCrafting").define("enablePipeMechHeadCrafting", true);
		this.enablePipeMechLegsCrafting = builder.comment("enablePipeMechLegsCrafting").translation("config.biomech.enablePipeMechLegsCrafting").define("enablePipeMechLegsCrafting", true);
		this.enablePortableStorageUnitCrafting = builder.comment("enablePortableStorageUnitCrafting").translation("config.biomech.enablePortableStorageUnitCrafting").define("enablePortableStorageUnitCrafting", true);
		this.enablePowerArmCrafting = builder.comment("enablePowerArmCrafting").translation("config.biomech.enablePowerArmCrafting").define("enablePowerArmCrafting", true);
		this.enablePowerChestCrafting = builder.comment("enablePowerChestCrafting").translation("config.biomech.enablePowerChestCrafting").define("enablePowerChestCrafting", true);
		this.enablePowerHelmetCrafting = builder.comment("enablePowerHelmetCrafting").translation("config.biomech.enablePowerHelmetCrafting").define("enablePowerHelmetCrafting", true);
		this.enablePowerLeggingsCrafting = builder.comment("enablePowerLeggingsCrafting").translation("config.biomech.enablePowerLeggingsCrafting").define("enablePowerLeggingsCrafting", true);
		this.enableRepulsorLiftCrafting = builder.comment("enableRepulsorLiftCrafting").translation("config.biomech.enableRepulsorLiftCrafting").define("enableRepulsorLiftCrafting", true);
		this.enableScubaTankCrafting = builder.comment("enableScubaTankCrafting").translation("config.biomech.enableScubaTankCrafting").define("enableScubaTankCrafting", true);
		this.enableSpiderWalkersCrafting = builder.comment("enableSpiderWalkersCrafting").translation("config.biomech.enableSpiderWalkersCrafting").define("enableSpiderWalkersCrafting", true);
		this.enableSpringLoadedLeggingsCrafting = builder.comment("enableSpringLoadedLeggingsCrafting").translation("config.biomech.enableSpringLoadedLeggingsCrafting").define("enableSpringLoadedLeggingsCrafting", true);
		this.enableTeleportationCrystalCrafting = builder.comment("enableTeleportationCrystalCrafting").translation("config.biomech.enableTeleportationCrystalCrafting").define("enableTeleportationCrystalCrafting", true);
		this.enableTransformerModuleHelicopterCrafting = builder.comment("enableTransformerModuleHelicopterCrafting").translation("config.biomech.enableTransformerModuleHelicopterCrafting").define("enableTransformerModuleHelicopterCrafting", true);
		builder.pop();
		
		builder.push("looting");
		this.enableBatteryPackLooting = builder.comment("enableBatteryPackLooting").translation("config.biomech.enableBatteryPackLooting").define("enableBatteryPackLooting", true);
		this.enableBuzzsawArmLooting = builder.comment("enableBuzzsawArmLooting").translation("config.biomech.enableBuzzsawArmLooting").define("enableBuzzsawArmLooting", true);
		this.enableColossusChestplateLooting = builder.comment("enableColossusChestplateLooting").translation("config.biomech.enableColossusChestplateLooting").define("enableColossusChestplateLooting", true);
		this.enableCpuLooting = builder.comment("enableCpuLooting").translation("config.biomech.enableCpuLooting").define("enableCpuLooting", true);
		this.enableDiggerArmLooting = builder.comment("enableDiggerArmLooting").translation("config.biomech.enableDiggerArmLooting").define("enableDiggerArmLooting", true);
		this.enableDrillArmLooting = builder.comment("enableDrillArmLooting").translation("config.biomech.enableDrillArmLooting").define("enableDrillArmLooting", true);
		this.enableElytraMechChestplateLooting = builder.comment("enableElytraMechChestplateLooting").translation("config.biomech.enableElytraMechChestplateLooting").define("enableElytraMechChestplateLooting", true);
		this.enableEmergencyForcefieldUnitLooting = builder.comment("enableEmergencyForcefieldUnitLooting").translation("config.biomech.enableEmergencyForcefieldUnitLooting").define("enableEmergencyForcefieldUnitLooting", true);
		this.enableExtendoArmLooting = builder.comment("enableExtendoArmLooting").translation("config.biomech.enableExtendoArmLooting").define("enableExtendoArmLooting", true);
		this.enableGasMaskLooting = builder.comment("enableGasMaskLooting").translation("config.biomech.enableGasMaskLooting").define("enableGasMaskLooting", true);
		this.enableGatlingArmLooting = builder.comment("enableGatlingArmLooting").translation("config.biomech.enableGatlingArmLooting").define("enableGatlingArmLooting", true);
		this.enableGrappleArmLooting = builder.comment("enableGrappleArmLooting").translation("config.biomech.enableGrappleArmLooting").define("enableGrappleArmLooting", true);
		this.enableHarvesterArmLooting = builder.comment("enableHarvesterArmLooting").translation("config.biomech.enableHarvesterArmLooting").define("enableHarvesterArmLooting", true);
		this.enableHerosArmLooting = builder.comment("enableHerosArmLooting").translation("config.biomech.enableHerosArmLooting").define("enableHerosArmLooting", true);
		this.enableHerosChestplateLooting = builder.comment("enableHerosChestplateLooting").translation("config.biomech.enableHerosChestplateLooting").define("enableHerosChestplateLooting", true);
		this.enableHerosHeadpieceLooting = builder.comment("enableHerosHeadpieceLooting").translation("config.biomech.enableHerosHeadpieceLooting").define("enableHerosHeadpieceLooting", true);
		this.enableHerosLeggingsLooting = builder.comment("enableHerosLeggingsLooting").translation("config.biomech.enableHerosLeggingsLooting").define("enableHerosLeggingsLooting", true);
		this.enableHovertechLeggingsLooting = builder.comment("enableHovertechLeggingsLooting").translation("config.biomech.enableHovertechLeggingsLooting").define("enableHovertechLeggingsLooting", true);
		this.enableIlluminatorLooting = builder.comment("enableIlluminatorLooting").translation("config.biomech.enableIlluminatorLooting").define("enableIlluminatorLooting", true);
		this.enableInterceptorArmsLooting = builder.comment("enableInterceptorArmsLooting").translation("config.biomech.enableInterceptorArmsLooting").define("enableInterceptorArmsLooting", true);
		this.enableJetpackLooting = builder.comment("enableJetpackLooting").translation("config.biomech.enableJetpackLooting").define("enableJetpackLooting", true);
		this.enableLavastrideLeggingsLooting = builder.comment("enableLavastrideLeggingsLooting").translation("config.biomech.enableLavastrideLeggingsLooting").define("enableLavastrideLeggingsLooting", true);
		this.enableLoadLifterArmLooting = builder.comment("enableLoadLifterArmLooting").translation("config.biomech.enableLoadLifterArmLooting").define("enableLoadLifterArmLooting", true);
		this.enableLoadLifterChassisLooting = builder.comment("enableLoadLifterChassisLooting").translation("config.biomech.enableLoadLifterChassisLooting").define("enableLoadLifterChassisLooting", true);
		this.enableMiningLaserArmLooting = builder.comment("enableMiningLaserArmLooting").translation("config.biomech.enableMiningLaserArmLooting").define("enableMiningLaserArmLooting", true);
		this.enableMobilityTreadsLooting = builder.comment("enableMobilityTreadsLooting").translation("config.biomech.enableMobilityTreadsLooting").define("enableMobilityTreadsLooting", true);
		this.enableNightVisionVisorLooting = builder.comment("enableNightVisionVisorLooting").translation("config.biomech.enableNightVisionVisorLooting").define("enableNightVisionVisorLooting", true);
		this.enableOpticsUnitLooting = builder.comment("enableOpticsUnitLooting").translation("config.biomech.enableOpticsUnitLooting").define("enableOpticsUnitLooting", true);
		this.enablePipeMechArmLooting = builder.comment("enablePipeMechArmLooting").translation("config.biomech.enablePipeMechArmLooting").define("enablePipeMechArmLooting", true);
		this.enablePipeMechBodyLooting = builder.comment("enablePipeMechBodyLooting").translation("config.biomech.enablePipeMechBodyLooting").define("enablePipeMechBodyLooting", true);
		this.enablePipeMechHeadLooting = builder.comment("enablePipeMechHeadLooting").translation("config.biomech.enablePipeMechHeadLooting").define("enablePipeMechHeadLooting", true);
		this.enablePipeMechLegsLooting = builder.comment("enablePipeMechLegsLooting").translation("config.biomech.enablePipeMechLegsLooting").define("enablePipeMechLegsLooting", true);
		this.enablePortableStorageUnitLooting = builder.comment("enablePortableStorageUnitLooting").translation("config.biomech.enablePortableStorageUnitLooting").define("enablePortableStorageUnitLooting", true);
		this.enablePowerArmLooting = builder.comment("enablePowerArmLooting").translation("config.biomech.enablePowerArmLooting").define("enablePowerArmLooting", true);
		this.enablePowerChestLooting = builder.comment("enablePowerChestLooting").translation("config.biomech.enablePowerChestLooting").define("enablePowerChestLooting", true);
		this.enablePowerHelmetLooting = builder.comment("enablePowerHelmetLooting").translation("config.biomech.enablePowerHelmetLooting").define("enablePowerHelmetLooting", true);
		this.enablePowerLeggingsLooting = builder.comment("enablePowerLeggingsLooting").translation("config.biomech.enablePowerLeggingsLooting").define("enablePowerLeggingsLooting", true);
		this.enableRepulsorLiftLooting = builder.comment("enableRepulsorLiftLooting").translation("config.biomech.enableRepulsorLiftLooting").define("enableRepulsorLiftLooting", true);
		this.enableScubaTankLooting = builder.comment("enableScubaTankLooting").translation("config.biomech.enableScubaTankLooting").define("enableScubaTankLooting", true);
		this.enableSpiderWalkersLooting = builder.comment("enableSpiderWalkersLooting").translation("config.biomech.enableSpiderWalkersLooting").define("enableSpiderWalkersLooting", true);
		this.enableSpringLoadedLeggingsLooting = builder.comment("enableSpringLoadedLeggingsLooting").translation("config.biomech.enableSpringLoadedLeggingsLooting").define("enableSpringLoadedLeggingsLooting", true);
		this.enableTeleportationCrystalLooting = builder.comment("enableTeleportationCrystalLooting").translation("config.biomech.enableTeleportationCrystalLooting").define("enableTeleportationCrystalLooting", true);
		this.enableTransformerModuleHelicopterLooting = builder.comment("enableTransformerModuleHelicopterLooting").translation("config.biomech.enableTransformerModuleHelicopterLooting").define("enableTransformerModuleHelicopterLooting", true);
		builder.pop();
		
		builder.push("loot_chances");
		this.lootBioMechInChest = builder.comment("lootBioMechInChest").translation("config.biomech.lootBioMechInChest").define("lootBioMechInChest", defaultChestLootChance);
		this.lootBioMechInMineshaft = builder.comment("lootBioMechInMineshaft").translation("config.biomech.lootBioMechInMineshaft").define("lootBioMechInMineshaft", defaultMineshaftLootChance);
		this.lootBioMechInDungeon = builder.comment("lootBioMechInDungeon").translation("config.biomech.lootBioMechInDungeon").define("lootBioMechInDungeon", defaultDungeonLootChance);
		this.lootBioMechInAncientCity = builder.comment("lootBioMechInAncientCity").translation("config.biomech.lootBioMechInAncientCity").define("lootBioMechInAncientCity", defaultAncientCityLootChance);
		this.lootBioMechInShipwreck = builder.comment("lootBioMechInShipwreck").translation("config.biomech.lootBioMechInShipwreck").define("lootBioMechInShipwreck", defaultShipwreckLootChance);
		this.lootBioMechInNetherFortress = builder.comment("lootBioMechInNetherFortress").translation("config.biomech.lootBioMechInNetherFortress").define("lootBioMechInNetherFortress", defaultNetherFortressLootChance);
		builder.pop();
		
		builder.push("items");
		this.emergencyForcefieldUnitCooldown = builder.comment("emergencyForcefieldUnitCooldown").translation("config.biomech.emergencyForcefieldUnitCooldown").define("emergencyForcefieldUnitCooldown", 90.0);
		builder.pop();
		
		builder.push("biomech_station");
		this.walkToBioMechStation = builder.comment("walkToBioMechStation").translation("config.biomech.walkToBioMechStation").define("walkToBioMechStation", true);
		builder.pop();
	}
	
}
