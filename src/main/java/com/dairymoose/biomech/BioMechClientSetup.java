package com.dairymoose.biomech;

import com.dairymoose.biomech.armor.renderer.BackJetpackRenderer;
import com.dairymoose.biomech.armor.renderer.BackScubaTankRenderer;
import com.dairymoose.biomech.armor.renderer.BatteryPackRenderer;
import com.dairymoose.biomech.armor.renderer.ColossusChestplateRenderer;
import com.dairymoose.biomech.armor.renderer.CpuRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechArmorRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechHeadRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechLegsRenderer;
import com.dairymoose.biomech.armor.renderer.ElytraMechChestplateRenderer;
import com.dairymoose.biomech.armor.renderer.EmergencyForcefieldUnitRenderer;
import com.dairymoose.biomech.armor.renderer.GasMaskRenderer;
import com.dairymoose.biomech.armor.renderer.HerosChestplateRenderer;
import com.dairymoose.biomech.armor.renderer.HerosHeadpieceRenderer;
import com.dairymoose.biomech.armor.renderer.HerosLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.HovertechLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.IlluminatorRenderer;
import com.dairymoose.biomech.armor.renderer.InterceptorArmsRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechChestplateRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechHeadRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechLegsRenderer;
import com.dairymoose.biomech.armor.renderer.LavastrideLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.LoadLifterChassisRenderer;
import com.dairymoose.biomech.armor.renderer.MobilityTreadsRenderer;
import com.dairymoose.biomech.armor.renderer.NightVisionVisorRenderer;
import com.dairymoose.biomech.armor.renderer.OpticsUnitRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechBodyRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechHeadRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechLegsRenderer;
import com.dairymoose.biomech.armor.renderer.PortableStorageUnitRenderer;
import com.dairymoose.biomech.armor.renderer.PowerChestRenderer;
import com.dairymoose.biomech.armor.renderer.PowerHelmetRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.RepulsorLiftRenderer;
import com.dairymoose.biomech.armor.renderer.SpiderWalkersRenderer;
import com.dairymoose.biomech.armor.renderer.SpringLoadedLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.TeleportationCrystalRenderer;
import com.dairymoose.biomech.armor.renderer.arm.BuzzsawLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.BuzzsawRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.DiamondMechLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.DiamondMechRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.DiggerLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.DiggerRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.DrillLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.DrillRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.ExtendoLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.ExtendoRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.GatlingLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.GatlingRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.HarvesterLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.HarvesterRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.HerosLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.HerosRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.IronMechLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.IronMechRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.LoadLifterLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.LoadLifterRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.MiningLaserLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.MiningLaserRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.PipeMechLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.PipeMechRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.PowerLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.arm.PowerRightArmRenderer;
import com.dairymoose.biomech.client.screen.BioMechStationScreen;
import com.dairymoose.biomech.client.screen.PortableStorageUnitScreen;
import com.dairymoose.biomech.item.renderer.BioMechStationItemRenderer;
import com.dairymoose.biomech.item.renderer.BuzzsawItemRenderer;
import com.dairymoose.biomech.item.renderer.DiamondMechArmItemRenderer;
import com.dairymoose.biomech.item.renderer.DiggerArmItemRenderer;
import com.dairymoose.biomech.item.renderer.DrillItemRenderer;
import com.dairymoose.biomech.item.renderer.ExtendoArmItemRenderer;
import com.dairymoose.biomech.item.renderer.GatlingItemRenderer;
import com.dairymoose.biomech.item.renderer.HarvesterArmItemRenderer;
import com.dairymoose.biomech.item.renderer.HerosArmItemRenderer;
import com.dairymoose.biomech.item.renderer.IronMechArmItemRenderer;
import com.dairymoose.biomech.item.renderer.LoadLifterArmItemRenderer;
import com.dairymoose.biomech.item.renderer.MiningLaserItemRenderer;
import com.dairymoose.biomech.item.renderer.PipeMechArmItemRenderer;
import com.dairymoose.biomech.item.renderer.PowerArmItemRenderer;

import mod.azure.azurelib.animation.cache.AzIdentityRegistry;
import mod.azure.azurelib.render.armor.AzArmorRendererRegistry;
import mod.azure.azurelib.render.item.AzItemRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class BioMechClientSetup {

	@SuppressWarnings("unchecked")
	public static void doClientSetup(FMLClientSetupEvent event) {
		//"bone" is null error?  The .geo file is missing from geo.item package
    	AzArmorRendererRegistry.register(HovertechLeggingsRenderer::new, BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get());
    	AzArmorRendererRegistry.register(PowerLeggingsRenderer::new, BioMechRegistry.ITEM_POWER_LEGGINGS.get());
    	AzArmorRendererRegistry.register(LavastrideLeggingsRenderer::new, BioMechRegistry.ITEM_LAVASTRIDE_LEGGINGS.get());
    	AzArmorRendererRegistry.register(PowerChestRenderer::new, BioMechRegistry.ITEM_POWER_CHEST.get());
    	AzArmorRendererRegistry.register(PowerRightArmRenderer::new, BioMechRegistry.ITEM_POWER_ARM.get());
    	AzArmorRendererRegistry.register(PowerLeftArmRenderer::new, BioMechRegistry.ITEM_POWER_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(PowerHelmetRenderer::new, BioMechRegistry.ITEM_POWER_HELMET.get());
    	AzArmorRendererRegistry.register(MiningLaserRightArmRenderer::new, BioMechRegistry.ITEM_MINING_LASER_ARM.get());
    	AzArmorRendererRegistry.register(MiningLaserLeftArmRenderer::new, BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(BackScubaTankRenderer::new, BioMechRegistry.ITEM_SCUBA_TANK.get());
    	AzArmorRendererRegistry.register(BackJetpackRenderer::new, BioMechRegistry.ITEM_JETPACK.get());
    	AzArmorRendererRegistry.register(SpiderWalkersRenderer::new, BioMechRegistry.ITEM_SPIDER_WALKERS.get());
    	AzArmorRendererRegistry.register(NightVisionVisorRenderer::new, BioMechRegistry.ITEM_NIGHT_VISION_VISOR.get());
    	AzArmorRendererRegistry.register(SpringLoadedLeggingsRenderer::new, BioMechRegistry.ITEM_SPRING_LOADED_LEGGINGS.get());
    	AzArmorRendererRegistry.register(ElytraMechChestplateRenderer::new, BioMechRegistry.ITEM_ELYTRA_MECH_CHESTPLATE.get());
    	AzArmorRendererRegistry.register(DrillRightArmRenderer::new, BioMechRegistry.ITEM_DRILL_ARM.get());
    	AzArmorRendererRegistry.register(DrillLeftArmRenderer::new, BioMechRegistry.ITEM_DRILL_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(BuzzsawRightArmRenderer::new, BioMechRegistry.ITEM_BUZZSAW_ARM.get());
    	AzArmorRendererRegistry.register(BuzzsawLeftArmRenderer::new, BioMechRegistry.ITEM_BUZZSAW_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(GatlingRightArmRenderer::new, BioMechRegistry.ITEM_GATLING_ARM.get());
    	AzArmorRendererRegistry.register(GatlingLeftArmRenderer::new, BioMechRegistry.ITEM_GATLING_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(InterceptorArmsRenderer::new, BioMechRegistry.ITEM_INTERCEPTOR_ARMS.get());
    	AzArmorRendererRegistry.register(BatteryPackRenderer::new, BioMechRegistry.ITEM_BATTERY_PACK.get());
    	AzArmorRendererRegistry.register(DiggerRightArmRenderer::new, BioMechRegistry.ITEM_DIGGER_ARM.get());
    	AzArmorRendererRegistry.register(DiggerLeftArmRenderer::new, BioMechRegistry.ITEM_DIGGER_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(PortableStorageUnitRenderer::new, BioMechRegistry.ITEM_PORTABLE_STORAGE_UNIT.get());
    	AzArmorRendererRegistry.register(TeleportationCrystalRenderer::new, BioMechRegistry.ITEM_TELEPORTATION_CRYSTAL.get());
    	AzArmorRendererRegistry.register(CpuRenderer::new, BioMechRegistry.ITEM_CPU.get());
    	AzArmorRendererRegistry.register(GasMaskRenderer::new, BioMechRegistry.ITEM_GAS_MASK.get());
    	AzArmorRendererRegistry.register(RepulsorLiftRenderer::new, BioMechRegistry.ITEM_REPULSOR_LIFT.get());
    	AzArmorRendererRegistry.register(ColossusChestplateRenderer::new, BioMechRegistry.ITEM_COLOSSUS_CHESTPLATE.get());
    	AzArmorRendererRegistry.register(ExtendoRightArmRenderer::new, BioMechRegistry.ITEM_EXTENDO_ARM.get());
    	AzArmorRendererRegistry.register(ExtendoLeftArmRenderer::new, BioMechRegistry.ITEM_EXTENDO_LEFT_ARM.get());
    	AzArmorRendererRegistry.register(IlluminatorRenderer::new, BioMechRegistry.ITEM_ILLUMINATOR.get());
    	AzArmorRendererRegistry.register(EmergencyForcefieldUnitRenderer::new, BioMechRegistry.ITEM_EMERGENCY_FORCEFIELD_UNIT.get());
    	AzArmorRendererRegistry.register(HarvesterRightArmRenderer::new, BioMechRegistry.ITEM_HARVESTER_ARM.get());
    	AzArmorRendererRegistry.register(HarvesterLeftArmRenderer::new, BioMechRegistry.ITEM_HARVESTER_LEFT_ARM.get());
    	
    	//LOAD LIFTER
    	AzArmorRendererRegistry.register(OpticsUnitRenderer::new, BioMechRegistry.ITEM_OPTICS_UNIT.get());
    	AzArmorRendererRegistry.register(LoadLifterChassisRenderer::new, BioMechRegistry.ITEM_LOAD_LIFTER_CHASSIS.get());
    	AzArmorRendererRegistry.register(MobilityTreadsRenderer::new, BioMechRegistry.ITEM_MOBILITY_TREADS.get());
    	AzArmorRendererRegistry.register(LoadLifterRightArmRenderer::new, BioMechRegistry.ITEM_LOAD_LIFTER_ARM.get());
    	AzArmorRendererRegistry.register(LoadLifterLeftArmRenderer::new, BioMechRegistry.ITEM_LOAD_LIFTER_LEFT_ARM.get());
    	//LOAD LIFTER
    	
    	//HERO
    	AzArmorRendererRegistry.register(HerosHeadpieceRenderer::new, BioMechRegistry.ITEM_HEROS_HEADPIECE.get());
    	AzArmorRendererRegistry.register(HerosChestplateRenderer::new, BioMechRegistry.ITEM_HEROS_CHESTPLATE.get());
    	AzArmorRendererRegistry.register(HerosLeggingsRenderer::new, BioMechRegistry.ITEM_HEROS_LEGGINGS.get());
    	AzArmorRendererRegistry.register(HerosRightArmRenderer::new, BioMechRegistry.ITEM_HEROS_ARM.get());
    	AzArmorRendererRegistry.register(HerosLeftArmRenderer::new, BioMechRegistry.ITEM_HEROS_LEFT_ARM.get());
    	//HERO
    	
    	//IRON MECH
    	AzArmorRendererRegistry.register(IronMechHeadRenderer::new, BioMechRegistry.ITEM_IRON_MECH_HEAD.get());
    	AzArmorRendererRegistry.register(IronMechChestplateRenderer::new, BioMechRegistry.ITEM_IRON_MECH_CHESTPLATE.get());
    	AzArmorRendererRegistry.register(IronMechLegsRenderer::new, BioMechRegistry.ITEM_IRON_MECH_LEGS.get());
    	AzArmorRendererRegistry.register(IronMechRightArmRenderer::new, BioMechRegistry.ITEM_IRON_MECH_ARM.get());
    	AzArmorRendererRegistry.register(IronMechLeftArmRenderer::new, BioMechRegistry.ITEM_IRON_MECH_LEFT_ARM.get());
    	//IRON MECH
    	
    	//DIAMOND MECH
    	AzArmorRendererRegistry.register(DiamondMechHeadRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_HEAD.get());
    	AzArmorRendererRegistry.register(DiamondMechArmorRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_CHESTPLATE.get());
    	AzArmorRendererRegistry.register(DiamondMechLegsRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_LEGS.get());
    	AzArmorRendererRegistry.register(DiamondMechRightArmRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_ARM.get());
    	AzArmorRendererRegistry.register(DiamondMechLeftArmRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_LEFT_ARM.get());
    	//DIAMOND MECH
    	
    	//PIPE MECH
    	AzArmorRendererRegistry.register(PipeMechHeadRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_HEAD.get());
    	AzArmorRendererRegistry.register(PipeMechBodyRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_BODY.get());
    	AzArmorRendererRegistry.register(PipeMechLegsRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_LEGS.get());
    	AzArmorRendererRegistry.register(PipeMechRightArmRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_ARM.get());
    	AzArmorRendererRegistry.register(PipeMechLeftArmRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_LEFT_ARM.get());
    	//PIPE MECH
    	
    	//------ Arm items - render item display / right arm only ------
    	AzItemRendererRegistry.register(GatlingItemRenderer::new, BioMechRegistry.ITEM_GATLING_ARM.get());
    	AzItemRendererRegistry.register(BuzzsawItemRenderer::new, BioMechRegistry.ITEM_BUZZSAW_ARM.get());
    	AzItemRendererRegistry.register(DrillItemRenderer::new, BioMechRegistry.ITEM_DRILL_ARM.get());
    	AzItemRendererRegistry.register(MiningLaserItemRenderer::new, BioMechRegistry.ITEM_MINING_LASER_ARM.get());
    	AzItemRendererRegistry.register(PowerArmItemRenderer::new, BioMechRegistry.ITEM_POWER_ARM.get());
    	AzItemRendererRegistry.register(PipeMechArmItemRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_ARM.get());
    	AzItemRendererRegistry.register(IronMechArmItemRenderer::new, BioMechRegistry.ITEM_IRON_MECH_ARM.get());
    	AzItemRendererRegistry.register(DiamondMechArmItemRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_ARM.get());
    	AzItemRendererRegistry.register(DiggerArmItemRenderer::new, BioMechRegistry.ITEM_DIGGER_ARM.get());
    	AzItemRendererRegistry.register(HerosArmItemRenderer::new, BioMechRegistry.ITEM_HEROS_ARM.get());
    	AzItemRendererRegistry.register(LoadLifterArmItemRenderer::new, BioMechRegistry.ITEM_LOAD_LIFTER_ARM.get());
    	AzItemRendererRegistry.register(ExtendoArmItemRenderer::new, BioMechRegistry.ITEM_EXTENDO_ARM.get());
    	AzItemRendererRegistry.register(HarvesterArmItemRenderer::new, BioMechRegistry.ITEM_HARVESTER_ARM.get());
    	//------ Arm items - render item display / right arm only ------
    	
    	//------ All Animated ------
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_MINING_LASER_ARM.get(), BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_DRILL_ARM.get(), BioMechRegistry.ITEM_DRILL_LEFT_ARM.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_BUZZSAW_ARM.get(), BioMechRegistry.ITEM_BUZZSAW_LEFT_ARM.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_GATLING_ARM.get(), BioMechRegistry.ITEM_GATLING_LEFT_ARM.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_INTERCEPTOR_ARMS.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_SPRING_LOADED_LEGGINGS.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_DIGGER_ARM.get(), BioMechRegistry.ITEM_DIGGER_LEFT_ARM.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_TELEPORTATION_CRYSTAL.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_REPULSOR_LIFT.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_EXTENDO_ARM.get(), BioMechRegistry.ITEM_EXTENDO_LEFT_ARM.get());
    	AzIdentityRegistry.register(BioMechRegistry.ITEM_HARVESTER_ARM.get(), BioMechRegistry.ITEM_HARVESTER_LEFT_ARM.get());
    	//------ All Animated ------
    	
    	
    	
    	
    	//------
    	
    	MenuScreens.register(BioMechRegistry.MENU_TYPE_BIOMECH_STATION.get(), BioMechStationScreen::new);
    	MenuScreens.register(BioMechRegistry.MENU_TYPE_PORTABLE_STORAGE_UNIT.get(), PortableStorageUnitScreen::new);
    	
    	//BioMech Station only
    	AzItemRendererRegistry.register(BioMechStationItemRenderer::new, BioMechRegistry.ITEM_BIOMECH_STATION.get());
    	//BioMech Station only
	}
	
}
