package com.dairymoose.biomech;

import com.dairymoose.biomech.block.BioMechStationBlock;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;
import com.dairymoose.biomech.item.BioMechActivator;
import com.dairymoose.biomech.item.BioMechDeactivator;
import com.dairymoose.biomech.item.armor.BackJetpackArmor;
import com.dairymoose.biomech.item.armor.BackScubaTankArmor;
import com.dairymoose.biomech.item.armor.BatteryPackArmor;
import com.dairymoose.biomech.item.armor.BuzzsawLeftArmArmor;
import com.dairymoose.biomech.item.armor.BuzzsawRightArmArmor;
import com.dairymoose.biomech.item.armor.DiamondMechChestArmor;
import com.dairymoose.biomech.item.armor.DiamondMechHeadArmor;
import com.dairymoose.biomech.item.armor.DiamondMechLeftArmArmor;
import com.dairymoose.biomech.item.armor.DiamondMechLegsArmor;
import com.dairymoose.biomech.item.armor.DiamondMechRightArmArmor;
import com.dairymoose.biomech.item.armor.DrillLeftArmArmor;
import com.dairymoose.biomech.item.armor.DrillRightArmArmor;
import com.dairymoose.biomech.item.armor.ElytraMechChestplateArmor;
import com.dairymoose.biomech.item.armor.GatlingArmArmor;
import com.dairymoose.biomech.item.armor.GatlingLeftArmArmor;
import com.dairymoose.biomech.item.armor.GatlingRightArmArmor;
import com.dairymoose.biomech.item.armor.HovertechLeggingsArmor;
import com.dairymoose.biomech.item.armor.InterceptorArmsArmor;
import com.dairymoose.biomech.item.armor.IronMechChestArmor;
import com.dairymoose.biomech.item.armor.IronMechHeadArmor;
import com.dairymoose.biomech.item.armor.IronMechLeftArmArmor;
import com.dairymoose.biomech.item.armor.IronMechLegsArmor;
import com.dairymoose.biomech.item.armor.IronMechRightArmArmor;
import com.dairymoose.biomech.item.armor.LavastrideLeggingsArmor;
import com.dairymoose.biomech.item.armor.MiningLaserLeftArmArmor;
import com.dairymoose.biomech.item.armor.MiningLaserRightArmArmor;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.item.armor.NightVisionVisorArmor;
import com.dairymoose.biomech.item.armor.PipeMechBodyArmor;
import com.dairymoose.biomech.item.armor.PipeMechHeadArmor;
import com.dairymoose.biomech.item.armor.PipeMechLeftArmArmor;
import com.dairymoose.biomech.item.armor.PipeMechLegsArmor;
import com.dairymoose.biomech.item.armor.PipeMechRightArmArmor;
import com.dairymoose.biomech.item.armor.PowerChestArmor;
import com.dairymoose.biomech.item.armor.PowerHelmetArmor;
import com.dairymoose.biomech.item.armor.PowerLeftArmArmor;
import com.dairymoose.biomech.item.armor.PowerLeggingsArmor;
import com.dairymoose.biomech.item.armor.PowerRightArmArmor;
import com.dairymoose.biomech.item.armor.SpiderWalkersArmor;
import com.dairymoose.biomech.item.armor.SpringLoadedLeggingsArmor;
import com.dairymoose.biomech.menu.BioMechStationMenu;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public class BioMechRegistry {

	public static RegistryObject<CreativeModeTab> TAB_BIOMECH_CREATIVE = BioMech.CREATIVE_MODE_TABS.register("biomech_creative", () -> CreativeModeTab.builder().icon(() -> (new ItemStack(BioMechRegistry.ITEM_BIOMECH_STATION.get()))).title(Component.literal("BioMech")).build());
	
	public static RegistryObject<Block> BLOCK_BIOMECH_STATION = BioMech.BLOCKS.register("biomech_station", () -> new BioMechStationBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f, 5.0f)));
	
	public static RegistryObject<Block> BLOCK_BIOMECH_SCRAP = BioMech.BLOCKS.register("biomech_scrap_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f, 5.0f)));
	
	public static RegistryObject<BlockEntityType<BioMechStationBlockEntity>> BLOCK_ENTITY_BIOMECH_STATION = BioMech.BLOCK_ENTITY_TYPES.register("biomech_station", () -> BioMechStationBlockEntity.BIOMECH_STATION_BLOCK_ENTITY);
	
	public static RegistryObject<MenuType> MENU_TYPE_BIOMECH_STATION = BioMech.MENUS.register("biomech_station_menu", () -> new MenuType(BioMechStationMenu::new, FeatureFlags.DEFAULT_FLAGS));
	
	public static RegistryObject<ParticleType> PARTICLE_TYPE_LASER = BioMech.PARTICLES.register("laser", () -> new SimpleParticleType(false));
	public static RegistryObject<ParticleType> PARTICLE_TYPE_THICKER_LASER = BioMech.PARTICLES.register("thicker_laser", () -> new SimpleParticleType(false));
	public static RegistryObject<ParticleType> PARTICLE_TYPE_THICKEST_LASER = BioMech.PARTICLES.register("thickest_laser", () -> new SimpleParticleType(false));
	public static RegistryObject<ParticleType> PARTICLE_TYPE_MAX_LASER = BioMech.PARTICLES.register("max_laser", () -> new SimpleParticleType(false));
	
	public static RegistryObject<ParticleType> PARTICLE_TYPE_INSTANT_SMOKE = BioMech.PARTICLES.register("instant_smoke", () -> new SimpleParticleType(false));
	
	public static RegistryObject<ParticleType> PARTICLE_TYPE_MUZZLE_FLASH = BioMech.PARTICLES.register("muzzle_flash", () -> new SimpleParticleType(false));
	
	public static RegistryObject<SoundEvent> SOUND_EVENT_LASER_LOOP = BioMech.SOUNDS.register("laser_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BioMech.MODID, "laser_loop")));
	public static RegistryObject<SoundEvent> SOUND_EVENT_JETPACK_LOOP = BioMech.SOUNDS.register("jetpack_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BioMech.MODID, "jetpack_loop")));
	public static RegistryObject<SoundEvent> SOUND_EVENT_MINING_DRILL = BioMech.SOUNDS.register("mining_drill", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BioMech.MODID, "mining_drill")));
	public static RegistryObject<SoundEvent> SOUND_EVENT_BUZZSAW_LOOP = BioMech.SOUNDS.register("buzzsaw_loop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BioMech.MODID, "buzzsaw_loop")));
	public static RegistryObject<SoundEvent> SOUND_EVENT_GATLING_SPIN_UP = BioMech.SOUNDS.register("gatling_spin_up", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BioMech.MODID, "gatling_spin_up")));
	public static RegistryObject<SoundEvent> SOUND_EVENT_GATLING_FIRING = BioMech.SOUNDS.register("gatling_firing", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BioMech.MODID, "gatling_firing")));
	
	public static final ResourceKey<DamageType> BIOMECH_ABSORB = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "biomech_absorb"));
	
	//same attributes as absorb, but does NOT ignore armor
	public static final ResourceKey<DamageType> BIOMECH_BONUS_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(BioMech.MODID, "biomech_bonus_damage"));
	
	public static RegistryObject<Item> ITEM_BIOMECH_STATION = BioMech.ITEMS.register("biomech_station", () -> new BlockItem(BioMechRegistry.BLOCK_BIOMECH_STATION.get(), new Item.Properties()));
	
	public static RegistryObject<Item> ITEM_BIOMECH_SCRAP = BioMech.ITEMS.register("biomech_scrap", () -> new Item(new Item.Properties().stacksTo(255)));
	public static RegistryObject<Item> ITEM_BIOMECH_SCRAP_BLOCK = BioMech.ITEMS.register("biomech_scrap_block", () -> new BlockItem(BioMechRegistry.BLOCK_BIOMECH_SCRAP.get(), new Item.Properties()));
	
	public static RegistryObject<Item> ITEM_BIOMECH_ACTIVATOR = BioMech.ITEMS.register("biomech_activator", () -> new BioMechActivator(new Item.Properties()));
	public static RegistryObject<Item> ITEM_BIOMECH_DEACTIVATOR = BioMech.ITEMS.register("biomech_deactivator", () -> new BioMechDeactivator(new Item.Properties()));
	
	public static RegistryObject<Item> ITEM_SPIDER_WALKERS = BioMech.ITEMS.register("spider_walkers", () -> new SpiderWalkersArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_MOBILITY_TREADS = BioMech.ITEMS.register("mobility_treads", () -> new MobilityTreadsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_ELYTRA_MECH_CHESTPLATE = BioMech.ITEMS.register("elytra_mech_chestplate", () -> new ElytraMechChestplateArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_HOVERTECH_LEGGINGS = BioMech.ITEMS.register("hovertech_leggings", () -> new HovertechLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_NIGHT_VISION_VISOR = BioMech.ITEMS.register("night_vision_visor", () -> new NightVisionVisorArmor(ArmorMaterials.IRON, Type.HELMET, (new Item.Properties())));
	
	//IRON MECH
	public static RegistryObject<Item> ITEM_IRON_MECH_HEAD = BioMech.ITEMS.register("iron_mech_head", () -> new IronMechHeadArmor(ArmorMaterials.IRON, Type.HELMET, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_IRON_MECH_CHESTPLATE = BioMech.ITEMS.register("iron_mech_chestplate", () -> new IronMechChestArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_IRON_MECH_LEGS = BioMech.ITEMS.register("iron_mech_legs", () -> new IronMechLegsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_IRON_MECH_ARM = BioMech.ITEMS.register("iron_mech_arm", () -> new IronMechRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	//IRON MECH
	
	//DIAMOND MECH
	public static RegistryObject<Item> ITEM_DIAMOND_MECH_HEAD = BioMech.ITEMS.register("diamond_mech_head", () -> new DiamondMechHeadArmor(ArmorMaterials.IRON, Type.HELMET, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_DIAMOND_MECH_CHESTPLATE = BioMech.ITEMS.register("diamond_mech_chestplate", () -> new DiamondMechChestArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_DIAMOND_MECH_LEGS = BioMech.ITEMS.register("diamond_mech_legs", () -> new DiamondMechLegsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_DIAMOND_MECH_ARM = BioMech.ITEMS.register("diamond_mech_arm", () -> new DiamondMechRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	//DIAMOND MECH
	
	public static RegistryObject<Item> ITEM_SPRING_LOADED_LEGGINGS = BioMech.ITEMS.register("spring_loaded_leggings", () -> new SpringLoadedLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	
	//PIPE MECH
	public static RegistryObject<Item> ITEM_PIPE_MECH_HEAD = BioMech.ITEMS.register("pipe_mech_head", () -> new PipeMechHeadArmor(ArmorMaterials.IRON, Type.HELMET, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_PIPE_MECH_BODY = BioMech.ITEMS.register("pipe_mech_body", () -> new PipeMechBodyArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_PIPE_MECH_LEGS = BioMech.ITEMS.register("pipe_mech_legs", () -> new PipeMechLegsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_PIPE_MECH_ARM = BioMech.ITEMS.register("pipe_mech_arm", () -> new PipeMechRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	//PIPE MECH
	
	//POWER MECH
	public static RegistryObject<Item> ITEM_POWER_HELMET = BioMech.ITEMS.register("power_helmet", () -> new PowerHelmetArmor(ArmorMaterials.IRON, Type.HELMET, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_CHEST = BioMech.ITEMS.register("power_chest", () -> new PowerChestArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_LEGGINGS = BioMech.ITEMS.register("power_leggings", () -> new PowerLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_ARM = BioMech.ITEMS.register("power_arm", () -> new PowerRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	//POWER MECH
	
	public static RegistryObject<Item> ITEM_INTERCEPTOR_ARMS = BioMech.ITEMS.register("interceptor_arms", () -> new InterceptorArmsArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_BATTERY_PACK = BioMech.ITEMS.register("battery_pack", () -> new BatteryPackArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_BACK_SCUBA_TANK = BioMech.ITEMS.register("back_scuba_tank", () -> new BackScubaTankArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_BACK_JETPACK = BioMech.ITEMS.register("back_jetpack", () -> new BackJetpackArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_DRILL_ARM = BioMech.ITEMS.register("drill_arm", () -> new DrillRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_BUZZSAW_ARM = BioMech.ITEMS.register("buzzsaw_arm", () -> new BuzzsawRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_GATLING_ARM = BioMech.ITEMS.register("gatling_arm", () -> new GatlingRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_MINING_LASER_ARM = BioMech.ITEMS.register("mining_laser_arm", () -> new MiningLaserRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_LAVASTRIDE_LEGGINGS = BioMech.ITEMS.register("lavastride_leggings", () -> new LavastrideLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_GATLING_LEFT_ARM = BioMech.ITEMS.register("left_gatling_arm", () -> new GatlingLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_BUZZSAW_LEFT_ARM = BioMech.ITEMS.register("left_buzzsaw_arm", () -> new BuzzsawLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_DRILL_LEFT_ARM = BioMech.ITEMS.register("left_drill_arm", () -> new DrillLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_DIAMOND_MECH_LEFT_ARM = BioMech.ITEMS.register("left_diamond_mech_arm", () -> new DiamondMechLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_IRON_MECH_LEFT_ARM = BioMech.ITEMS.register("left_iron_mech_arm", () -> new IronMechLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_MINING_LASER_LEFT_ARM = BioMech.ITEMS.register("mining_laser_left_arm", () -> new MiningLaserLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_LEFT_ARM = BioMech.ITEMS.register("left_power_arm", () -> new PowerLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_PIPE_MECH_LEFT_ARM = BioMech.ITEMS.register("left_pipe_mech_arm", () -> new PipeMechLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_REMAINING_PICKAXE = BioMech.ITEMS.register("remaining_pickaxe", () -> new RemainingPickaxeItem(null));
	
	//public static RegistryObject<SoundEvent> SOUND_STALKER_AMBIENT = AwakenedEvil.SOUNDS.register("stalker_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AwakenedEvil.MODID, "stalker_ambient")));
	
}
