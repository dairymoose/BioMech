package com.dairymoose.biomech;

import com.dairymoose.biomech.item.armor.HovertechLeggingsArmor;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class BioMechRegistry {

	public static RegistryObject<CreativeModeTab> TAB_BIOMECH_CREATIVE = BioMech.CREATIVE_MODE_TABS.register("biomech_creative", () -> CreativeModeTab.builder().icon(() -> (new ItemStack(BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get()))).title(Component.literal("BioMech")).build());
	
	//public static RegistryObject<Block> BLOCK_CORRUPTED_IRON_ORE = AwakenedEvil.BLOCKS.register("corrupted_iron_ore", () -> new CorruptedOreBlock(Blocks.IRON_ORE, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
	
	//public static RegistryObject<BlockEntityType<CorruptedPistonBlockEntity>> BLOCK_ENTITY_CORRUPTED_PISTON = AwakenedEvil.BLOCK_ENTITY_TYPES.register("corrupted_piston", () -> CorruptedPistonBlockEntity.CORRUPTED_PISTON_BLOCK_ENTITY);
	
	//public static RegistryObject<Item> ITEM_BUBBLE = AwakenedEvil.ITEMS.register("bubble", () -> new BubbleItem(new Item.Properties()));
	public static RegistryObject<Item> ITEM_HOVERTECH_LEGGINGS = BioMech.ITEMS.register("hovertech_leggings", () -> new HovertechLeggingsArmor(ArmorMaterials.IRON, Type.LEGGINGS, (new Item.Properties())));

	//public static RegistryObject<SoundEvent> SOUND_STALKER_AMBIENT = AwakenedEvil.SOUNDS.register("stalker_ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AwakenedEvil.MODID, "stalker_ambient")));
	
}
