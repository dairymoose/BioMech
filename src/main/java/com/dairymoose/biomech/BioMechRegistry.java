package com.dairymoose.biomech;

import com.dairymoose.biomech.block.BioMechStationBlock;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;
import com.dairymoose.biomech.item.armor.HovertechLeggingsArmor;
import com.dairymoose.biomech.item.armor.LavastrideLeggingsArmor;
import com.dairymoose.biomech.item.armor.PowerChestArmor;
import com.dairymoose.biomech.item.armor.PowerHelmetArmor;
import com.dairymoose.biomech.item.armor.PowerLeftArmArmor;
import com.dairymoose.biomech.item.armor.PowerLeggingsArmor;
import com.dairymoose.biomech.item.armor.PowerRightArmArmor;
import com.dairymoose.biomech.menu.BioMechArmorMenu;

import net.minecraft.network.chat.Component;
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
	
	public static RegistryObject<BlockEntityType<BioMechStationBlockEntity>> BLOCK_ENTITY_BIOMECH_STATION = BioMech.BLOCK_ENTITY_TYPES.register("biomech_station", () -> BioMechStationBlockEntity.BIOMECH_STATION_BLOCK_ENTITY);
	
	public static RegistryObject<MenuType> MENU_TYPE_BIOMECH_STATION = BioMech.MENUS.register("biomech_station_menu", () -> new MenuType(BioMechArmorMenu::new, FeatureFlags.DEFAULT_FLAGS));
	
	public static RegistryObject<Item> ITEM_BIOMECH_STATION = BioMech.ITEMS.register("biomech_station", () -> new BlockItem(BioMechRegistry.BLOCK_BIOMECH_STATION.get(), new Item.Properties()));
	
	public static RegistryObject<Item> ITEM_HOVERTECH_LEGGINGS = BioMech.ITEMS.register("hovertech_leggings", () -> new HovertechLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_POWER_CHEST = BioMech.ITEMS.register("power_chest", () -> new PowerChestArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_LEGGINGS = BioMech.ITEMS.register("power_leggings", () -> new PowerLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_RIGHT_ARM = BioMech.ITEMS.register("right_power_arm", () -> new PowerRightArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_LEFT_ARM = BioMech.ITEMS.register("left_power_arm", () -> new PowerLeftArmArmor(ArmorMaterials.IRON, Type.CHESTPLATE, (new Item.Properties())));
	public static RegistryObject<Item> ITEM_POWER_HELMET = BioMech.ITEMS.register("power_helmet", () -> new PowerHelmetArmor(ArmorMaterials.IRON, Type.HELMET, (new Item.Properties())));
	
	public static RegistryObject<Item> ITEM_LAVASTRIDE_LEGGINGS = BioMech.ITEMS.register("lavastride_leggings", () -> new LavastrideLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));
	

	//public static RegistryObject<SoundEvent> SOUND_STALKER_AMBIENT = AwakenedEvil.SOUNDS.register("stalker_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AwakenedEvil.MODID, "stalker_ambient")));
	
}
