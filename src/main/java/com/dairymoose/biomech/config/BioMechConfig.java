package com.dairymoose.biomech.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.block.BioMechStationBlock;
import com.dairymoose.biomech.item.armor.GatlingArmArmor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
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
			
			addConfigElement(BioMechConfig.COMMON.enableBiomechScrapCrafting);
			
			addConfigElement(BioMechConfig.COMMON.enableIronMechArmorCrafting);
			addConfigElement(BioMechConfig.COMMON.enableDiamondMechArmorCrafting);
			
			addConfigElement(BioMechConfig.COMMON.enableSpiderWalkersCrafting);
			addConfigElement(BioMechConfig.COMMON.enableScubaTankCrafting);
			addConfigElement(BioMechConfig.COMMON.enableJetpackCrafting);
			addConfigElement(BioMechConfig.COMMON.enableElytraMechChestplateCrafting);
			
			BioMechStationBlock.configWalkToBioMechStation = BioMechConfig.COMMON.walkToBioMechStation.get().booleanValue();
			BioMech.alwaysAllowMechArmUsage = !BioMechConfig.CLIENT.requireEmptyHandsToActivateBioMechHands.get().booleanValue();
			GatlingArmArmor.gatlingDamage = BioMechConfig.SERVER.gatlingDamage.get().floatValue();
			GatlingArmArmor.gatlingEnergyPerSec = BioMechConfig.SERVER.gatlingEnergyPerSec.get().floatValue();
			GatlingArmArmor.gatlingMinFalloff = BioMechConfig.SERVER.gatlingMinFalloffFactor.get().floatValue();
			
			DistExecutor.runWhenOn(Dist.CLIENT, () -> {return new Runnable() {
				@Override
				public void run() {
					BioMech.requireModifierKeyForArmUsage = BioMechConfig.CLIENT.requireModifierKeyToActivateHands.get().booleanValue();
					BioMech.hideMainHandWhileInactive = BioMechConfig.CLIENT.hideMainHandWhileNotInUse.get().booleanValue();
					BioMech.hideOffHandWhileInactive = BioMechConfig.CLIENT.hideOffHandWhileNotInUse.get().booleanValue();
				}
				};});

			File f = getBiomechEarlyConfigFile();
			CompoundTag tag = new CompoundTag();
			tag.putDouble("lootBioMechInChest", BioMechConfig.SERVER.lootBioMechInChest.get());
			tag.putDouble("lootBioMechInMineshaft", BioMechConfig.SERVER.lootBioMechInMineshaft.get());
			tag.putDouble("lootBioMechInDungeon", BioMechConfig.SERVER.lootBioMechInDungeon.get());
			tag.putDouble("lootBioMechInAncientCity", BioMechConfig.SERVER.lootBioMechInAncientCity.get());
			tag.putDouble("lootBioMechInShipwreck", BioMechConfig.SERVER.lootBioMechInShipwreck.get());
			tag.putDouble("lootBioMechInNetherFortress", BioMechConfig.SERVER.lootBioMechInNetherFortress.get());
			tag.putBoolean("ElytraMechChestplateCanBeLooted", BioMechConfig.SERVER.elytraMechChestplateCanBeLooted.get().booleanValue());
			BioMech.LOGGER.debug("[BioMech]: Save global loot chance: " + BioMechConfig.SERVER.lootBioMechInChest.get());
			BioMech.LOGGER.debug("[BioMech]: Save mineshaft loot chance: " + BioMechConfig.SERVER.lootBioMechInMineshaft.get());
			BioMech.LOGGER.debug("[BioMech]: Save dungeon loot chance: " + BioMechConfig.SERVER.lootBioMechInDungeon.get());
			BioMech.LOGGER.debug("[BioMech]: Save ancient_city loot chance: " + BioMechConfig.SERVER.lootBioMechInAncientCity.get());
			BioMech.LOGGER.debug("[BioMech]: Save shipwreck loot chance: " + BioMechConfig.SERVER.lootBioMechInShipwreck.get());
			BioMech.LOGGER.debug("[BioMech]: Save nether fortress loot chance: " + BioMechConfig.SERVER.lootBioMechInNetherFortress.get());
			BioMech.LOGGER.debug("[BioMech]: Save elytra chestplate loot flag: " + BioMechConfig.SERVER.elytraMechChestplateCanBeLooted.get());
			NbtIo.write(tag, f);
			
		} catch (Exception ex) {
			BioMech.LOGGER.error("Error initializing config", ex);
		}
	}
	
	public static File getBiomechEarlyConfigFile() {
		File f = new File("biomech.cfg");
		if (!f.exists()) {
			try {
				if (f.createNewFile()) {
					return f;
				}
			} catch (IOException e) {
				BioMech.LOGGER.error("Error creating biomech.cfg", e);
			}
		} else {
			return f;
		}
		
		return null;
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
