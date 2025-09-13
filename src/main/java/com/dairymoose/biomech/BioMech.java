package com.dairymoose.biomech;

import java.lang.reflect.Field;

import org.slf4j.Logger;

import com.dairymoose.biomech.item.armor.HovertechLeggingsArmor;
import com.dairymoose.biomech.renderer.HovertechArmorRenderer;
import com.mojang.logging.LogUtils;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.rewrite.render.armor.AzArmorModel;
import mod.azure.azurelib.rewrite.render.armor.AzArmorModelRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererRegistry;
import mod.azure.azurelib.rewrite.render.layer.AzArmorLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BioMech.MODID)
public class BioMech
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "biomech";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public BioMech(FMLJavaModLoadingContext context)
    {
    	AzureLib.initialize();

		LOGGER.debug(BioMechRegistry.TAB_BIOMECH_CREATIVE.toString());
		
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        modEventBus.addListener(this::addItemsToCreativeTab);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    
    private void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == BioMechRegistry.TAB_BIOMECH_CREATIVE.get()) {
			Field[] allFields = BioMechRegistry.class.getDeclaredFields();
			for (Field f : allFields) {
				if (f.getType() == RegistryObject.class) {
					try {
						RegistryObject value = (RegistryObject) f.get(null);
						if (value != null) {
							if (value.get() instanceof Item) {
								LOGGER.debug("Value is: " + value.get());
								event.accept(value);
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						LOGGER.error("Error registering items with creative tab", e);
					}
				}
			}
		}
	}

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    
    ItemStack priorItem = null;
    ItemStack itemToRender;
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
    	if (itemToRender == null) {
    		itemToRender = new ItemStack(BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get());
    	}
    	Player renderEntity = event.getEntity();
    	AzArmorLayer<Player> layer = new AzArmorLayer();
    	AzArmorRenderer armorRenderer = AzArmorRendererRegistry.getOrNull(itemToRender.getItem());
    	priorItem = null;
    	if (armorRenderer != null && renderEntity != null) {
    		PlayerModel playerModel = event.getRenderer().getModel();
    		armorRenderer.prepForRender(renderEntity, itemToRender, itemToRender.getEquipmentSlot(), playerModel);
        	//AzArmorModelRenderer renderer = new AzArmorModelRenderer(armorRenderer.rendererPipeline());
    		RenderType renderType = armorRenderer.rendererPipeline().config().getRenderType(itemToRender);
    		AzArmorModel armorModel = armorRenderer.rendererPipeline().armorModel();
    		playerModel.copyPropertiesTo(armorModel);
    		try {
    			//layer.render(armorRenderer.rendererPipeline().context());
    			//armorModel.renderToBuffer(event.getPoseStack(), null, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    			priorItem = event.getEntity().getItemBySlot(EquipmentSlot.LEGS);
    			event.getEntity().setItemSlot(EquipmentSlot.LEGS, itemToRender);
    		} catch (Exception e) {
    			LOGGER.error("render error", e);
    		}
//        	armorRenderer.rendererPipeline().armorModel().renderToBuffer(event.getPoseStack(), 
//        			event.getMultiBufferSource().getBuffer(armorRenderer.rendererPipeline().config().getRenderType(itemToRender)), 
//        			event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    	}
    }
    
    @SubscribeEvent
    public void onPostRenderPlayer(RenderPlayerEvent.Post event) {
    	if (itemToRender == null) {
    		itemToRender = new ItemStack(BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get());
    	}
    	Player renderEntity = event.getEntity();
    	AzArmorLayer<Player> layer = new AzArmorLayer();
    	AzArmorRenderer armorRenderer = AzArmorRendererRegistry.getOrNull(itemToRender.getItem());
    	if (armorRenderer != null && renderEntity != null) {
    		PlayerModel playerModel = event.getRenderer().getModel();
    		armorRenderer.prepForRender(renderEntity, itemToRender, itemToRender.getEquipmentSlot(), playerModel);
        	//AzArmorModelRenderer renderer = new AzArmorModelRenderer(armorRenderer.rendererPipeline());
    		RenderType renderType = armorRenderer.rendererPipeline().config().getRenderType(itemToRender);
    		AzArmorModel armorModel = armorRenderer.rendererPipeline().armorModel();
    		playerModel.copyPropertiesTo(armorModel);
    		try {
    			if (priorItem != null) {
    				event.getEntity().setItemSlot(EquipmentSlot.LEGS, priorItem);
    			}
    			//layer.render(armorRenderer.rendererPipeline().context());
    			//armorModel.renderToBuffer(event.getPoseStack(), null, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    			
    		} catch (Exception e) {
    			LOGGER.error("render error", e);
    		}
//        	armorRenderer.rendererPipeline().armorModel().renderToBuffer(event.getPoseStack(), 
//        			event.getMultiBufferSource().getBuffer(armorRenderer.rendererPipeline().config().getRenderType(itemToRender)), 
//        			event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    	}
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        	AzArmorRendererRegistry.register(HovertechArmorRenderer::new, BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get());
        }
    }
}
