package com.dairymoose.biomech;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

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

			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					;
				}
				};});

			
			
		} catch (Exception ex) {
			BioMech.LOGGER.error("Error initializing config", ex);
		}
	}
	
	@SubscribeEvent
	public static void onConfigReloaded(ModConfigEvent.Reloading event) {
		if (commonSpec.isLoaded()) {
			BioMechConfig.reinit();
		}
	}
	
	@SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    	reinit();
    }
	
}
