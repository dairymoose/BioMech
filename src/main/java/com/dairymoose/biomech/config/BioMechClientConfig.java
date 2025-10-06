package com.dairymoose.biomech.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class BioMechClientConfig {

	public final ForgeConfigSpec.BooleanValue requireEmptyHandsToActivateBioMechHands;
	public final ForgeConfigSpec.BooleanValue hideMainHandWhileNotInUse;
	public final ForgeConfigSpec.BooleanValue hideOffHandWhileNotInUse;
	public final ConfigValue<Double> showEnergySuitGuiThreshold;
	//public final ConfigValue<Double> energySuitGuiOpacity;
	public final ConfigValue<Double> energySuitGuiXScale;
	public final ConfigValue<Double> energySuitGuiYScale;
	public final ConfigValue<Double> energySuitGuiXPos;
	public final ConfigValue<Double> energySuitGuiYPos;
	
	public final ForgeConfigSpec.BooleanValue showSuitEnergyText;
	public final ConfigValue<Double> suitEnergyTextXScale;
	public final ConfigValue<Double> suitEnergyTextYScale;
	
	public final ForgeConfigSpec.BooleanValue showEnergyDrainRate;
	
	public BioMechClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("first_person");
		this.hideMainHandWhileNotInUse = builder.comment("hideMainHandWhileNotInUse").translation("config.biomech.hideMainHandWhileNotInUse").define("hideMainHandWhileNotInUse", false);
		this.hideOffHandWhileNotInUse = builder.comment("hideOffHandWhileNotInUse").translation("config.biomech.hideOffHandWhileNotInUse").define("hideOffHandWhileNotInUse", false);
		this.requireEmptyHandsToActivateBioMechHands = builder.comment("requireEmptyHandsToActivateBioMechHands").translation("config.biomech.requireEmptyHandsToActivateBioMechHands").define("requireEmptyHandsToActivateBioMechHands", false);
		builder.pop();
		
		builder.push("gui");
		this.showEnergySuitGuiThreshold = builder.comment("showEnergySuitGuiThreshold").translation("config.biomech.showEnergySuitGuiThreshold").defineInRange("showEnergySuitGuiThreshold", 0.9999, 0.0, 1.0);
		//this.energySuitGuiOpacity = builder.comment("energySuitGuiOpacity").translation("config.biomech.energySuitGuiOpacity").defineInRange("energySuitGuiOpacity", 0.75, 0.0, 1.0);
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
