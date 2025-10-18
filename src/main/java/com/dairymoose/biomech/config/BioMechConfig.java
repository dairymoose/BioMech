package com.dairymoose.biomech.config;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block.BioMechStationBlock;
import com.dairymoose.biomech.block.IlluminantBlock;
import com.dairymoose.biomech.item.armor.EmergencyForcefieldUnitArmor;
import com.dairymoose.biomech.item.armor.HovertechLeggingsArmor;
import com.dairymoose.biomech.item.armor.IlluminatorArmor;
import com.dairymoose.biomech.item.armor.JetpackArmor;
import com.dairymoose.biomech.item.armor.OpticsUnitArmor;
import com.dairymoose.biomech.item.armor.RepulsorLiftArmor;
import com.dairymoose.biomech.item.armor.arm.GatlingArmArmor;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = BioMech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BioMechConfig {

	public static final ForgeConfigSpec clientSpec;
	public static final BioMechClientConfig CLIENT;
	
	public static final ForgeConfigSpec commonSpec;
	public static final BioMechCommonConfig COMMON;
	
	public static final ForgeConfigSpec serverSpec;
	public static final BioMechServerConfig SERVER;

	static {
	    Pair<BioMechClientConfig, ForgeConfigSpec> pair2client = (new ForgeConfigSpec.Builder()).configure(BioMechClientConfig::new);
	    clientSpec = (ForgeConfigSpec)pair2client.getRight();
	    CLIENT = (BioMechClientConfig)pair2client.getLeft();
	    
	    Pair<BioMechCommonConfig, ForgeConfigSpec> pair2common = (new ForgeConfigSpec.Builder()).configure(BioMechCommonConfig::new);
	    commonSpec = (ForgeConfigSpec)pair2common.getRight();
	    COMMON = (BioMechCommonConfig)pair2common.getLeft();
	    
	    Pair<BioMechServerConfig, ForgeConfigSpec> pair2server = (new ForgeConfigSpec.Builder()).configure(BioMechServerConfig::new);
	    serverSpec = (ForgeConfigSpec)pair2server.getRight();
	    SERVER = (BioMechServerConfig)pair2server.getLeft();
	}
	
	public BioMechConfig() {

	}
	
	private static void addConfigElement(BooleanValue configElement) {
		List<String> path;
		
		path = configElement.getPath();
		String key = path.get(path.size() - 1);
		BioMechCraftingFlags.putFlag(key, configElement.get());
	}
	
	@SuppressWarnings("deprecation")
	public static void reinit() {
		BioMech.LOGGER.debug("[BioMech]: Update config");
		
		try {
			addConfigElement(BioMechConfig.COMMON.enableBioMechStation);
			addConfigElement(BioMechConfig.COMMON.enableBioMechActivator);
			addConfigElement(BioMechConfig.COMMON.enableBioMechDeactivator);
			
			Field[] fields = BioMechCommonConfig.class.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				if (fieldName!= null && fieldName.startsWith("enable") && fieldName.endsWith("Crafting")) {
					addConfigElement((BooleanValue) field.get(BioMechConfig.COMMON));
				}
			}
			
			BioMechStationBlock.configWalkToBioMechStation = BioMechConfig.COMMON.walkToBioMechStation.get().booleanValue();
			BioMech.alwaysAllowMechArmUsage = !BioMechConfig.CLIENT.requireEmptyHandsToActivateBioMechHands.get().booleanValue();
			GatlingArmArmor.gatlingDamage = BioMechConfig.SERVER.gatlingDamage.get().floatValue();
			GatlingArmArmor.gatlingEnergyPerSec = BioMechConfig.SERVER.gatlingEnergyPerSec.get().floatValue();
			GatlingArmArmor.gatlingMinFalloff = BioMechConfig.SERVER.gatlingMinFalloffFactor.get().floatValue();
			
			if (BioMechRegistry.ITEM_EMERGENCY_FORCEFIELD_UNIT.get() instanceof EmergencyForcefieldUnitArmor armor) {
				armor.setForcefieldCooldown(BioMechConfig.COMMON.emergencyForcefieldUnitCooldown.get().floatValue());
			}
			
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					BioMech.requireModifierKeyForArmUsage = BioMechConfig.CLIENT.requireModifierKeyToActivateHands.get().booleanValue();
					BioMech.hideMainHandWhileInactive = BioMechConfig.CLIENT.hideMainHandWhileNotInUse.get().booleanValue();
					BioMech.hideOffHandWhileInactive = BioMechConfig.CLIENT.hideOffHandWhileNotInUse.get().booleanValue();
					
					OpticsUnitArmor.unopenedChestsOnly = BioMechConfig.CLIENT.opticsUnitHighlightsUnopenedChestsOnly.get().booleanValue();
					BioMech.ClientModEvents.opticsUnitZoomEnabled = BioMechConfig.CLIENT.opticsUnitAllowZoomIn.get().booleanValue();
					OpticsUnitArmor.canEverHighlightSpawners = BioMechConfig.CLIENT.opticsUnitCanEverHighlightSpawners.get().booleanValue();
					OpticsUnitArmor.canEverHighlightChests = BioMechConfig.CLIENT.opticsUnitCanEverHighlightChests.get().booleanValue();
					
					BioMech.ClientModEvents.inventoryButtonVisible = BioMechConfig.CLIENT.showBioMechInventoryButton.get().booleanValue();
					BioMech.ClientModEvents.inventoryButtonScreenX = BioMechConfig.CLIENT.bioMechInventoryButtonX.get();
					BioMech.ClientModEvents.inventoryButtonScreenY = BioMechConfig.CLIENT.bioMechInventoryButtonY.get();
					
					RepulsorLiftArmor.particleEnabled = BioMechConfig.CLIENT.enableRepulsorLiftParticle.get().booleanValue();
					
					JetpackArmor.flameParticleEnabled = BioMechConfig.CLIENT.enableJetpackFlameParticle.get().booleanValue();
					JetpackArmor.smokeParticleEnabled = BioMechConfig.CLIENT.enableJetpackSmokeParticle.get().booleanValue();
					JetpackArmor.soundEnabled = BioMechConfig.CLIENT.enableJetpackSound.get().booleanValue();
					HovertechLeggingsArmor.particleEnabled = BioMechConfig.CLIENT.enableHovertechParticle.get().booleanValue();
					
					IlluminatorArmor.updateTickPeriod = BioMechConfig.CLIENT.illuminatorUpdateLightingTickPeriod.get().intValue();
					IlluminantBlock.lightLevel  = BioMechConfig.CLIENT.illuminatorLightValue.get().intValue();
				}
				};});

		} catch (Exception ex) {
			BioMech.LOGGER.error("Error initializing config", ex);
		}
	}
	
//	@SubscribeEvent
//	public static void onConfigReloaded(ModConfigEvent.Reloading event) {
//		if (commonSpec.isLoaded()) {
//			BioMechConfig.reinit();
//		}
//	}
	
	@SubscribeEvent
    public static void onConfigLoadedEvent(ModConfigEvent event) {
		if (commonSpec.isLoaded()) {
			BioMechConfig.reinit();
		}
    }
	
}
