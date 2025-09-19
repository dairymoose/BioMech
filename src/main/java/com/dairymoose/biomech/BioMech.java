package com.dairymoose.biomech;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;

import com.dairymoose.biomech.block_entity.renderer.BioMechStationRenderer;
import com.dairymoose.biomech.item.anim.BioMechStationItemRenderer;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MechPartUtil;
import com.dairymoose.biomech.item.renderer.HovertechLeggingsRenderer;
import com.dairymoose.biomech.item.renderer.LavastrideLeggingsRenderer;
import com.dairymoose.biomech.item.renderer.PowerChestRenderer;
import com.dairymoose.biomech.item.renderer.PowerHelmetRenderer;
import com.dairymoose.biomech.item.renderer.PowerLeftArmRenderer;
import com.dairymoose.biomech.item.renderer.PowerLeggingsRenderer;
import com.dairymoose.biomech.item.renderer.PowerRightArmRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.rewrite.render.armor.AzArmorModel;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererRegistry;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererRegistry;
import mod.azure.azurelib.rewrite.render.layer.AzArmorLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
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
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

//    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
//    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
//            .withTabsBefore(CreativeModeTabs.COMBAT)
//            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
//            .displayItems((parameters, output) -> {
//                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
//            }).build());

    private static BioMechCraftingFlags craftingFlags;
    
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
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);

        craftingFlags = new BioMechCraftingFlags();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BioMechConfig.commonSpec);
	    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BioMechConfig.clientSpec);
	    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BioMechConfig.serverSpec);
    }
    
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> inputType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> tickerInterface) {
		return inputType == expectedType ? (BlockEntityTicker) tickerInterface : null;
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
        BioMechConfig.reinit();
    }

//    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event)
//    {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
//            event.accept(EXAMPLE_BLOCK_ITEM);
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    
    private boolean doArmorOverride = false;
    ItemStack priorItem = null;
    ItemStack itemToRender;
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
    	if (itemToRender == null) {
    		itemToRender = new ItemStack(BioMechRegistry.ITEM_POWER_RIGHT_ARM.get());
    	}
    	Player renderEntity = event.getEntity();
    	if (renderEntity.isSpectator())
    		return;
    	AzArmorLayer<Player> layer = new AzArmorLayer();
    	AzArmorRenderer armorRenderer = AzArmorRendererRegistry.getOrNull(itemToRender.getItem());
    	priorItem = null;
    	if (armorRenderer != null && renderEntity != null) {
    		PlayerModel playerModel = event.getRenderer().getModel();
    		armorRenderer.prepForRender(renderEntity, itemToRender, itemToRender.getEquipmentSlot(), playerModel);
        	//AzArmorModelRenderer renderer = new AzArmorModelRenderer(armorRenderer.rendererPipeline());
    		RenderType renderType = armorRenderer.rendererPipeline().config().getRenderType(itemToRender);
    		AzArmorModel armorModel = armorRenderer.rendererPipeline().armorModel();
    		//playerModel.copyPropertiesTo(armorModel);
    		
    		try {
    			//layer.render(armorRenderer.rendererPipeline().context());
    			//armorModel.renderToBuffer(event.getPoseStack(), null, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    			if (doArmorOverride) {
    				priorItem = event.getEntity().getItemBySlot(EquipmentSlot.LEGS);
        			event.getEntity().setItemSlot(EquipmentSlot.LEGS, itemToRender);
    			}
    			priorItem = event.getEntity().getItemBySlot(EquipmentSlot.CHEST);
    			event.getEntity().setItemSlot(EquipmentSlot.CHEST, itemToRender);
    			HumanoidArmorLayer hal = new HumanoidArmorLayer(event.getRenderer(), playerModel, armorModel, Minecraft.getInstance().getModelManager());
    			PoseStack poseStack = event.getPoseStack();
    			poseStack.pushPose();
    			//reproduce rotations and scale from LivingEntityRenderer
    			boolean shouldSit = renderEntity.isPassenger() && (renderEntity.getVehicle() != null && renderEntity.getVehicle().shouldRiderSit());
				float f = Mth.rotLerp(event.getPartialTick(), renderEntity.yBodyRotO, renderEntity.yBodyRot);
				float f1 = Mth.rotLerp(event.getPartialTick(), renderEntity.yHeadRotO, renderEntity.yHeadRot);
				float f2 = f1 - f;
				if (shouldSit && renderEntity.getVehicle() instanceof LivingEntity) {
					LivingEntity livingentity = (LivingEntity) renderEntity.getVehicle();
					f = Mth.rotLerp(event.getPartialTick(), livingentity.yBodyRotO, livingentity.yBodyRot);
					f2 = f1 - f;
					float f3 = Mth.wrapDegrees(f2);
					if (f3 < -85.0F) {
						f3 = -85.0F;
					}

					if (f3 >= 85.0F) {
						f3 = 85.0F;
					}

					f = f1 - f3;
					if (f3 * f3 > 2500.0F) {
						f += f3 * 0.2F;
					}

					f2 = f1 - f;
				}
    			
    			this.setupRotations((AbstractClientPlayer)renderEntity, poseStack, 0.0f, f, event.getPartialTick());
    			poseStack.scale(-1.0F, -1.0F, 1.0F);
    		    this.scale((AbstractClientPlayer)renderEntity, poseStack, event.getPartialTick());
    		    poseStack.translate(0.0F, -1.501F, 0.0F);
    		    
    			hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, EquipmentSlot.CHEST, event.getPackedLight(), armorModel);
    			event.getEntity().setItemSlot(EquipmentSlot.CHEST, new ItemStack(BioMechRegistry.ITEM_POWER_LEFT_ARM.get()));
    			hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, EquipmentSlot.CHEST, event.getPackedLight(), armorModel);
    			event.getEntity().setItemSlot(EquipmentSlot.CHEST, priorItem);
    			poseStack.popPose();
    		} catch (Exception e) {
    			LOGGER.error("render error", e);
    		}
    		
    		Iterable<ItemStack> armorSlots = event.getEntity().getArmorSlots();
    		for (ItemStack itemStack : armorSlots) {
    			if (itemStack.getItem() instanceof ArmorBase armorBase) {
    				MechPart mechPart = armorBase.getMechPart();
    				if (mechPart != null) {
    					List<ModelPart> parts = MechPartUtil.getCorrespondingModelParts(playerModel, mechPart);
    					if (armorBase.shouldHidePlayerModel()) {
    						for (ModelPart part : parts) {
    							part.visible = false;
    						}
    					} else {
//    						for (ModelPart part : parts) {
//    							part.visible = true;
//    						}
    					}
    				}
    			}
    		}
//    		playerModel.leftLeg.visible = false;
//    		playerModel.leftPants.visible = false;
//    		
//    		playerModel.rightLeg.visible = false;
//    		playerModel.rightPants.visible = false;
//    		
//    		playerModel.leftArm.visible = false;
//    		playerModel.leftSleeve.visible = false;
//    		
//    		playerModel.rightArm.visible = false;
//    		playerModel.rightSleeve.visible = false;
//    		
//    		playerModel.body.visible = false;
    		
    		//playerModel.leftSleeve.visible = false;
//        	armorRenderer.rendererPipeline().armorModel().renderToBuffer(event.getPoseStack(), 
//        			event.getMultiBufferSource().getBuffer(armorRenderer.rendererPipeline().config().getRenderType(itemToRender)), 
//        			event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    	}
    }
    
    protected void setupRotations(AbstractClientPlayer player, PoseStack poseStack, float bob, float yaw, float partialTick) {
    	if (!player.hasPose(Pose.SLEEPING)) {
    		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
         }
     }
    
    protected void scale(AbstractClientPlayer player, PoseStack poseStack, float partialTick) {
        float f = 0.9375F;
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
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
    			if (doArmorOverride) {
    				if (priorItem != null) {
        				event.getEntity().setItemSlot(EquipmentSlot.LEGS, priorItem);
        			}
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
        	AzArmorRendererRegistry.register(HovertechLeggingsRenderer::new, BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get());
        	AzArmorRendererRegistry.register(PowerLeggingsRenderer::new, BioMechRegistry.ITEM_POWER_LEGGINGS.get());
        	AzArmorRendererRegistry.register(LavastrideLeggingsRenderer::new, BioMechRegistry.ITEM_LAVASTRIDE_LEGGINGS.get());
        	AzArmorRendererRegistry.register(PowerChestRenderer::new, BioMechRegistry.ITEM_POWER_CHEST.get());
        	AzArmorRendererRegistry.register(PowerRightArmRenderer::new, BioMechRegistry.ITEM_POWER_RIGHT_ARM.get());
        	AzArmorRendererRegistry.register(PowerLeftArmRenderer::new, BioMechRegistry.ITEM_POWER_LEFT_ARM.get());
        	AzArmorRendererRegistry.register(PowerHelmetRenderer::new, BioMechRegistry.ITEM_POWER_HELMET.get());
        	
        	AzItemRendererRegistry.register(BioMechStationItemRenderer::new, BioMechRegistry.ITEM_BIOMECH_STATION.get());
        }
        
        @SubscribeEvent
		public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
			event.registerBlockEntityRenderer(BioMechRegistry.BLOCK_ENTITY_BIOMECH_STATION.get(), context -> new BioMechStationRenderer());
		}
    }
}
