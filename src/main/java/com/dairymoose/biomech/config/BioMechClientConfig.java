package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechClientConfig {

	public final ForgeConfigSpec.BooleanValue requireModifierKeyToActivateHands;
	public final ForgeConfigSpec.BooleanValue requireEmptyHandsToActivateBioMechHands;
	public final ForgeConfigSpec.BooleanValue hideMainHandWhileNotInUse;
	public final ForgeConfigSpec.BooleanValue hideOffHandWhileNotInUse;
	
	public final ForgeConfigSpec.BooleanValue opticsUnitHighlightsUnopenedChestsOnly;
	public final ForgeConfigSpec.BooleanValue opticsUnitCanEverHighlightSpawners;
	public final ForgeConfigSpec.BooleanValue opticsUnitCanEverHighlightChests;
	public final ForgeConfigSpec.BooleanValue opticsUnitAllowZoomIn;
	
	public final ConfigValue<Double> showEnergySuitGuiThreshold;
	public final ConfigValue<Double> energySuitGuiXScale;
	public final ConfigValue<Double> energySuitGuiYScale;
	public final ConfigValue<Double> energySuitGuiXPos;
	public final ConfigValue<Double> energySuitGuiYPos;
	
	public final ForgeConfigSpec.BooleanValue showSuitEnergyText;
	public final ConfigValue<Double> suitEnergyTextXScale;
	public final ConfigValue<Double> suitEnergyTextYScale;
	
	public final ForgeConfigSpec.BooleanValue showEnergyDrainRate;
	
	public final ForgeConfigSpec.BooleanValue showBioMechInventoryButton;
	public final ForgeConfigSpec.IntValue bioMechInventoryButtonX;
	public final ForgeConfigSpec.IntValue bioMechInventoryButtonY;
	
	public final ForgeConfigSpec.BooleanValue enableRepulsorLiftParticle;
	
	public BioMechClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("first_person");
		this.hideMainHandWhileNotInUse = builder.comment("hideMainHandWhileNotInUse").translation("config.biomech.hideMainHandWhileNotInUse").define("hideMainHandWhileNotInUse", false);
		this.hideOffHandWhileNotInUse = builder.comment("hideOffHandWhileNotInUse").translation("config.biomech.hideOffHandWhileNotInUse").define("hideOffHandWhileNotInUse", false);
		builder.pop();
		
		builder.push("input");
		this.requireModifierKeyToActivateHands = builder.comment("requireModifierKeyToActivateHands").translation("config.biomech.requireModifierKeyToActivateHands").define("requireModifierKeyToActivateHands", true);
		this.requireEmptyHandsToActivateBioMechHands = builder.comment("requireEmptyHandsToActivateBioMechHands").translation("config.biomech.requireEmptyHandsToActivateBioMechHands").define("requireEmptyHandsToActivateBioMechHands", false);
		builder.pop();
		
		builder.push("items");
		this.opticsUnitHighlightsUnopenedChestsOnly = builder.comment("opticsUnitHighlightsUnopenedChestsOnly").translation("config.biomech.opticsUnitHighlightsUnopenedChestsOnly").define("opticsUnitHighlightsUnopenedChestsOnly", true);
		this.opticsUnitCanEverHighlightSpawners = builder.comment("opticsUnitCanEverHighlightSpawners").translation("config.biomech.opticsUnitCanEverHighlightSpawners").define("opticsUnitCanEverHighlightSpawners", true);
		this.opticsUnitCanEverHighlightChests = builder.comment("opticsUnitCanEverHighlightChests").translation("config.biomech.opticsUnitCanEverHighlightChests").define("opticsUnitCanEverHighlightChests", true);
		this.opticsUnitAllowZoomIn = builder.comment("opticsUnitAllowZoomIn").translation("config.biomech.opticsUnitAllowZoomIn").define("opticsUnitAllowZoomIn", true);
		this.enableRepulsorLiftParticle = builder.comment("enableRepulsorLiftParticle").translation("config.biomech.enableRepulsorLiftParticle").define("enableRepulsorLiftParticle", true);
		builder.pop();
		
		builder.push("gui");
		this.showBioMechInventoryButton = builder.comment("showBioMechInventoryButton").translation("config.biomech.showBioMechInventoryButton").define("showBioMechInventoryButton", true);
		this.bioMechInventoryButtonX = builder.comment("bioMechInventoryButtonX").translation("config.biomech.bioMechInventoryButtonX").defineInRange("bioMechInventoryButtonX", 77, 0, 255);
		this.bioMechInventoryButtonY = builder.comment("bioMechInventoryButtonY").translation("config.biomech.bioMechInventoryButtonY").defineInRange("bioMechInventoryButtonY", 7, 0, 255);
		builder.pop();
		
		builder.push("gui");
		this.showEnergySuitGuiThreshold = builder.comment("showEnergySuitGuiThreshold").translation("config.biomech.showEnergySuitGuiThreshold").defineInRange("showEnergySuitGuiThreshold", 0.9999, 0.0, 1.0);
		this.energySuitGuiXScale = builder.comment("energySuitGuiXScale").translation("config.biomech.energySuitGuiXScale").defineInRange("energySuitGuiXScale", 1.25, 0.0, 5.0);
		this.energySuitGuiYScale = builder.comment("energySuitGuiYScale").translation("config.biomech.energySuitGuiYScale").defineInRange("energySuitGuiYScale", 1.25, 0.0, 5.0);
		this.energySuitGuiXPos = builder.comment("energySuitGuiXPos").translation("config.biomech.energySuitGuiXPos").defineInRange("energySuitGuiXPos", 0.29, 0.0, 1.0);
		this.energySuitGuiYPos = builder.comment("energySuitGuiYPos").translation("config.biomech.energySuitGuiYPos").defineInRange("energySuitGuiYPos", 0.97, 0.0, 1.0);
		this.showSuitEnergyText = builder.comment("showSuitEnergyText").translation("config.biomech.showSuitEnergyText").define("showSuitEnergyText", true);
		this.suitEnergyTextXScale = builder.comment("suitEnergyTextXScale").translation("config.biomech.suitEnergyTextXScale").defineInRange("suitEnergyTextXScale", 0.5, 0.0, 5.0);
		this.suitEnergyTextYScale = builder.comment("suitEnergyTextYScale").translation("config.biomech.suitEnergyTextYScale").defineInRange("suitEnergyTextYScale", 0.5, 0.0, 5.0);
		this.showEnergyDrainRate = builder.comment("showEnergyDrainRate").translation("config.biomech.showEnergyDrainRate").define("showEnergyDrainRate", false);
		builder.pop();
	}
	
}
