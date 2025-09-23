package com.dairymoose.biomech;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.armor.renderer.HovertechLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.LavastrideLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.MiningLaserLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.MiningLaserRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.PowerChestRenderer;
import com.dairymoose.biomech.armor.renderer.PowerHelmetRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.PowerRightArmRenderer;
import com.dairymoose.biomech.block_entity.renderer.BioMechStationRenderer;
import com.dairymoose.biomech.client.screen.BioMechStationScreen;
import com.dairymoose.biomech.config.BioMechConfig;
import com.dairymoose.biomech.config.BioMechCraftingFlags;
import com.dairymoose.biomech.item.anim.MiningLaserDispatcher;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MechPartUtil;
import com.dairymoose.biomech.item.armor.MiningLaserArmArmor;
import com.dairymoose.biomech.item.renderer.BioMechStationItemRenderer;
import com.dairymoose.biomech.item.renderer.MiningLaserItemRenderer;
import com.dairymoose.biomech.item.renderer.PowerArmItemRenderer;
import com.dairymoose.biomech.packet.clientbound.ClientboundUpdateSlottedItemPacket;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.rewrite.animation.AzAnimator;
import mod.azure.azurelib.rewrite.animation.AzAnimatorAccessor;
import mod.azure.azurelib.rewrite.animation.cache.AzIdentityRegistry;
import mod.azure.azurelib.rewrite.animation.dispatch.AzDispatchSide;
import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.primitive.AzBakedAnimation;
import mod.azure.azurelib.rewrite.render.armor.AzArmorModel;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererRegistry;
import mod.azure.azurelib.rewrite.render.item.AzItemRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
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
	    
	    int msgId = 0;
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundUpdateSlottedItemPacket.class, ClientboundUpdateSlottedItemPacket::write, ClientboundUpdateSlottedItemPacket::new, ClientboundUpdateSlottedItemPacket::handle);
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
    
    public static Map<UUID, BioMechPlayerData> globalPlayerData = new HashMap<>();
    public static Set<UUID> pendingCleanupPlayers = new HashSet<>();
   
    public static void sendItemSlotUpdateForPlayer(Player player) {
    	BioMechPlayerData playerData = globalPlayerData.computeIfAbsent(player.getUUID(), (uuid) -> new BioMechPlayerData());
		CompoundTag playerDataTag = BioMechPlayerData.serialize(playerData);
		ClientboundUpdateSlottedItemPacket slottedItemPacket = new ClientboundUpdateSlottedItemPacket(player.getUUID(), playerDataTag);
		BioMechNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), slottedItemPacket);
    }
    
    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent event) {
    	if (event.phase == TickEvent.Phase.START) {
    		BioMechPlayerData playerData = globalPlayerData.get(event.player.getUUID());
    		if (playerData != null) {
    			List<SlottedItem> slottedItems = playerData.getAllSlots();
    			for (SlottedItem slotted : slottedItems) {
    				if (!slotted.itemStack.isEmpty()) {
    					
    					ItemStack tickingItem = slotted.itemStack;
    					if (slotted.mechPart == MechPart.LeftArm && !slotted.leftArmItemStack.isEmpty()) {
    						tickingItem = slotted.leftArmItemStack;
    					}
    					tickingItem.inventoryTick(event.player.level(), event.player, -1, slotted.mechPart == MechPart.LeftArm);
    				}
    			}
    		}
    	}
    }
    
    @SubscribeEvent
    public void onPlayerJoinServerEvent(final PlayerEvent.PlayerLoggedInEvent event) {
    	LOGGER.debug("Player " + event.getEntity().getDisplayName().getString() + " joined the server!");
    	if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer sp) {
    		BioMech.sendItemSlotUpdateForPlayer(sp);
    	}
    }
    
    @SubscribeEvent
    public void onPlayerLeaveServerEvent(final PlayerEvent.PlayerLoggedOutEvent event) {
    	LOGGER.debug("Player " + event.getEntity().getDisplayName().getString() + " left the server!");
    	pendingCleanupPlayers.add(event.getEntity().getUUID());
    }
    
    @SubscribeEvent
    public void onLoadPlayerEvent(final PlayerEvent.LoadFromFile event)
    {
        File modDataFile = event.getPlayerFile("biomech");

        if (modDataFile.exists()) {
        	try {
                CompoundTag compound = NbtIo.read(modDataFile);
                globalPlayerData.put(event.getEntity().getUUID(), BioMechPlayerData.deserialize(compound));
                LOGGER.debug("Load custom player data for " + event.getEntity().getDisplayName().getString() + " to " + modDataFile.getAbsolutePath() + ": " + compound.getAsString());
            } catch (Exception e) {
                LOGGER.error("Error loading player data for " + event.getEntity().getDisplayName().getString() + ": " + e.getMessage(), e);
            }
        } else {
        	LOGGER.info("BioMech file does not exist for player " + event.getEntity().getDisplayName().getString());
        }
    }
    
    @SubscribeEvent
    public void onSavePlayerEvent(final PlayerEvent.SaveToFile event)
    {
        File modDataFile = event.getPlayerFile("biomech");

        try {
        	BioMechPlayerData playerData = null;
        	playerData = globalPlayerData.get(event.getEntity().getUUID());
        	if (playerData != null) {
        		CompoundTag root = BioMechPlayerData.serialize(playerData);
        	    NbtIo.write(root, modDataFile);
        		LOGGER.debug("Saved custom player data for " + event.getEntity().getDisplayName().getString() + " to " + modDataFile.getAbsolutePath());
        	} else {
        		LOGGER.info("Player data was null while saving for " + event.getEntity().getDisplayName().getString() + " to " + modDataFile.getAbsolutePath());
        	}
        } catch (Exception e) {
            LOGGER.error("Error saving player data for " + event.getEntity().getDisplayName().getString() + ": " + e.getMessage(), e);
        }
        
        if (pendingCleanupPlayers.contains(event.getEntity().getUUID())) {
        	globalPlayerData.remove(event.getEntity().getUUID());
        	pendingCleanupPlayers.remove(event.getEntity().getUUID());
        }
    }

    public static boolean hideOffHandWhileInactive = false;
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
    	static ClientModEvents inst = new ClientModEvents();

    	public ClientModEvents() {
    		MinecraftForge.EVENT_BUS.register(this);
    		
    		
		}

    	public static final KeyMapping HOTKEY_ENABLE_ARM_FUNCTION = new KeyMapping("key.hold_to_enable", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.categories.biomech");
    	public static final KeyMapping HOTKEY_RIGHT_ARM = new KeyMapping("key.right_arm", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, "key.categories.biomech");
    	public static final KeyMapping HOTKEY_LEFT_ARM = new KeyMapping("key.left_arm", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.biomech");
    	
        public static final List<KeyMapping> allKeyMappings = List.of(HOTKEY_RIGHT_ARM, HOTKEY_LEFT_ARM);

    	@SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
    		for (KeyMapping mapping : allKeyMappings) {
				event.register(mapping);
			}
        }
    	
    	@SubscribeEvent
    	public void onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
    		if (rightArmActive && event.isAttack() && Minecraft.getInstance().options.keyAttack.getKey().equals(HOTKEY_RIGHT_ARM.getKey())) {
    			event.setSwingHand(false);
    			event.setCanceled(true);
    		}
    		
    		if (leftArmActive && event.isUseItem() && Minecraft.getInstance().options.keyUse.getKey().equals(HOTKEY_LEFT_ARM.getKey())) {
    			event.setSwingHand(false);
    			event.setCanceled(true);
    		}
    	}

    	public boolean isRightMechArmActive(BioMechPlayerData playerData) {
    		return !playerData.getForSlot(MechPart.RightArm).itemStack.isEmpty() && Minecraft.getInstance().player.getMainHandItem().isEmpty();
    	}
    	
    	public boolean isLeftMechArmActive(BioMechPlayerData playerData) {
    		return !playerData.getForSlot(MechPart.LeftArm).itemStack.isEmpty() && Minecraft.getInstance().player.getOffhandItem().isEmpty();
    	}
    	
    	public boolean isMechArmActive(BioMechPlayerData playerData, MechPart part) {
    		if (part == MechPart.RightArm) {
    			return this.isRightMechArmActive(playerData);
    		}
    		else if (part == MechPart.LeftArm) {
    			return this.isLeftMechArmActive(playerData);
    		}
    		return false;
    	}
    	
    	public static boolean requireModifierKeyForArmUsage = true;
    	boolean rightArmActive = false;
    	boolean leftArmActive = false;
    	@SubscribeEvent
        public void onClientTick(final ClientTickEvent event) {
        	if (event.phase == TickEvent.Phase.START) {
        		BioMechPlayerData playerData = this.getDataForLocalPlayer();
        		if (playerData != null) {
        			if (requireModifierKeyForArmUsage) {
            			if (HOTKEY_ENABLE_ARM_FUNCTION.isDown()) {
                			if (isRightMechArmActive(playerData) && HOTKEY_RIGHT_ARM.isDown()) {
                				while (HOTKEY_RIGHT_ARM.consumeClick());
                    			rightArmActive = true;
                    		}
                			
                			if (isLeftMechArmActive(playerData) && HOTKEY_LEFT_ARM.isDown()) {
                				while (HOTKEY_LEFT_ARM.consumeClick());
                    			leftArmActive = true;
                    		}
                		}
                		if (!HOTKEY_RIGHT_ARM.isDown()) {
                			rightArmActive = false;
                		}
                		
                		if (!HOTKEY_LEFT_ARM.isDown()) {
                			leftArmActive = false;
                		}
            		} else {
            			if (isRightMechArmActive(playerData) && HOTKEY_RIGHT_ARM.isDown()) {
                			rightArmActive = true;
                		} else {
                			rightArmActive = false;
                		}
                		
                		if (isLeftMechArmActive(playerData) && HOTKEY_LEFT_ARM.isDown()) {
                			leftArmActive = true;
                		} else {
                			leftArmActive = false;
                		}
            		}
        		}
        	}
        }
    	
        @SubscribeEvent
		public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
			event.registerBlockEntityRenderer(BioMechRegistry.BLOCK_ENTITY_BIOMECH_STATION.get(), context -> new BioMechStationRenderer());
		}
        
        public BioMechPlayerData getDataForLocalPlayer() {
        	if (Minecraft.getInstance().player != null) {
        		BioMechPlayerData playerData = null;
            	playerData = BioMech.globalPlayerData.get(Minecraft.getInstance().player.getUUID());
            	return playerData;
        	}
        	
        	return null;
        }
        
        //AzureLib bug: dispatcher doesn't work client side because it uses AzAnimatorAccessor instead of AzIdentifiableItemStackAnimatorCache
        public void clientSideItemAnimation(ItemStack itemStack, AzCommand command) {
        	AzAnimator<ItemStack> anim = AzAnimatorAccessor.getOrNull(itemStack);
        	if (anim != null) {
        		command.actions().forEach(action -> action.handle(AzDispatchSide.CLIENT, anim));
        	}
        	//AzItemStackDispatchCommandPacket packet = new AzItemStackDispatchCommandPacket(itemStack.getTag().getUUID("az_id"), command);
			//packet.handle();
        }
        
        public static boolean disableAllRenderingLogic = false;
        
        ItemStack mainHandRenderStack = ItemStack.EMPTY;
        ItemStack offHandRenderStack = ItemStack.EMPTY;
        @SubscribeEvent
        public void onRenderFirstPersonHand(RenderHandEvent event) {
        	if (disableAllRenderingLogic)
        		return;
        	
        	try {
        		BioMechPlayerData playerData = this.getDataForLocalPlayer();
            	
            	EquipmentSlot equipSlot = null;
            	MechPart handPart = null;
            	//if (event.getArm() == HumanoidArm.RIGHT) {
            	if (event.getHand() == InteractionHand.MAIN_HAND) {
            		handPart = MechPart.RightArm;
            		equipSlot = EquipmentSlot.MAINHAND;
            	//} else if (event.getArm() == HumanoidArm.LEFT) {
            	} else if (event.getHand() == InteractionHand.OFF_HAND) {
            		handPart = MechPart.LeftArm;
            		equipSlot = EquipmentSlot.OFFHAND;
            	}
            	
            	boolean currentArmActive = false;
            	if (handPart == MechPart.RightArm && rightArmActive || handPart == MechPart.LeftArm && leftArmActive) {
            		currentArmActive = true;
            	}
            	if (playerData != null && handPart != null && equipSlot != null) {
            		if (isMechArmActive(playerData, handPart) && playerData.getForSlot(handPart).visible) {
            			if (handPart == MechPart.RightArm && !ItemStack.isSameItem(mainHandRenderStack, playerData.getForSlot(handPart).itemStack)) {
            				mainHandRenderStack = new ItemStack(playerData.getForSlot(handPart).itemStack.getItem());
            			} else if (handPart == MechPart.LeftArm && !ItemStack.isSameItem(offHandRenderStack, playerData.getForSlot(handPart).itemStack)) {
            				offHandRenderStack = new ItemStack(playerData.getForSlot(handPart).itemStack.getItem());
            			}
            			ItemStack newRenderItem = mainHandRenderStack;
            			if (handPart == MechPart.LeftArm) {
            				newRenderItem = offHandRenderStack;
            			}
            			event.setCanceled(true);
            			
            			if (playerData.getForSlot(handPart).itemStack.getItem() instanceof ArmorBase base) {
            				if (base instanceof MiningLaserArmArmor laser) {
            					//laser.dispatcher.mining(Minecraft.getInstance().player, playerData.getForSlot(handPart).itemStack);
            				}
            			}
            			
            			if (currentArmActive) {
            				if (newRenderItem.getItem() instanceof ArmorBase base) {
                				if (base instanceof MiningLaserArmArmor laser) {
                					//laser.dispatcher.mining(Minecraft.getInstance().player, newRenderItem);
                					
                					AzAnimator<ItemStack> anim = AzAnimatorAccessor.getOrNull(newRenderItem);
                					if (anim != null) {
                						AzBakedAnimation startUsing = anim.getAnimation(newRenderItem, MiningLaserDispatcher.START_USING_COMMAND.animationName);
                    					int useTicks = newRenderItem.getTag().getInt("useTicks");
                    					++useTicks;
                    					newRenderItem.getTag().putInt("useTicks", useTicks);
                    					if (useTicks <= (int)startUsing.length()) {
                    						this.clientSideItemAnimation(newRenderItem, MiningLaserDispatcher.START_USING_COMMAND.command);
                    						//send packet to server asking for start_using anim
                    					}
                    					else {
                    						this.clientSideItemAnimation(newRenderItem, MiningLaserDispatcher.MINING_COMMAND.command);
                    						//send packet to server asking for mining anim
                    					}
                					} else {
                						BioMech.LOGGER.error("Could not get animator for item: " + newRenderItem);
                					}
                				}
                			}
            			} else {
            				newRenderItem.getTag().putInt("useTicks", 0);
            				this.clientSideItemAnimation(newRenderItem, MiningLaserDispatcher.PASSIVE_COMMAND.command);
            				//send packet to server asking for passive anim
            			}
            			
            			if (handPart == MechPart.LeftArm && hideOffHandWhileInactive && !currentArmActive) {
            				return;
            			}
            			ItemInHandRenderer iihr = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
            			iihr.renderArmWithItem(Minecraft.getInstance().player, event.getPartialTick(), event.getInterpolatedPitch(), event.getHand(), 
            					event.getSwingProgress(), newRenderItem, event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
            		}
            	}
        	}
        	catch (Exception e) {
        		BioMech.LOGGER.error("Error while rendering hands", e);
        	}
        }
        
        Map<EquipmentSlot, ItemStack> priorItems = new HashMap<>();
        //ItemStack priorItem = null;
        ItemStack itemToRender;
        @SuppressWarnings("unchecked")
		@SubscribeEvent
        public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        	if (disableAllRenderingLogic)
        		return;
        	
        	if (itemToRender == null) {
        		itemToRender = new ItemStack(BioMechRegistry.ITEM_POWER_ARM.get());
        	}
        	Player renderEntity = event.getEntity();
        	if (renderEntity.isSpectator())
        		return;
        	AzArmorRenderer armorRenderer = AzArmorRendererRegistry.getOrNull(itemToRender.getItem());
        	//priorItem = null;
        	if (armorRenderer != null && renderEntity != null) {
        		
            	//AzArmorModelRenderer renderer = new AzArmorModelRenderer(armorRenderer.rendererPipeline());
        		
        		//playerModel.copyPropertiesTo(armorModel);
        		
        		try {
        			PlayerModel playerModel = event.getRenderer().getModel();
        			//armorRenderer.prepForRender(renderEntity, itemToRender, itemToRender.getEquipmentSlot(), playerModel);
        			AzArmorModel armorModel = armorRenderer.rendererPipeline().armorModel();
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
        		    
        		    priorItems.clear();
        		    BioMechPlayerData playerData = BioMech.globalPlayerData.get(renderEntity.getUUID());
        		    if (playerData != null) {
        		    	playerData.getAllSlots().forEach((slottedItem) -> {
        		    		if (!slottedItem.itemStack.isEmpty() && slottedItem.visible) {
        		    			ItemStack itemStackToRender = slottedItem.itemStack;
        		    			EquipmentSlot equipmentSlot = LivingEntity.getEquipmentSlotForItem(itemStackToRender);
        		    			if (equipmentSlot != null) {
        		    				ItemStack priorItem = event.getEntity().getItemBySlot(equipmentSlot);
        		    				
        		    				if (slottedItem.mechPart == MechPart.LeftArm) {
        		    					if (itemStackToRender.getItem() instanceof ArmorBase base) {
        		    						Item leftArmItem = base.getLeftArmItem();
        		    						if (leftArmItem != null) {
        		    							if (slottedItem.leftArmItemStack.isEmpty()) {
        		    								slottedItem.leftArmItemStack = new ItemStack(leftArmItem);
        		    							}
        		    							itemStackToRender = slottedItem.leftArmItemStack;
        		    						}
                            		    }
        		    				}
        		    				
        		    				ItemStack priorFeetItem = null;
        		    				event.getEntity().setItemSlot(equipmentSlot, itemStackToRender);
        		    				if (slottedItem.mechPart == MechPart.Leggings) {
        		    					priorFeetItem = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
        		    					event.getEntity().setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        		    				}
            		    			
                        		    hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, equipmentSlot, event.getPackedLight(), armorModel);
                        		    if (slottedItem.mechPart == MechPart.LeftArm || slottedItem.mechPart == MechPart.RightArm || slottedItem.mechPart == MechPart.Back) {
                        		    	event.getEntity().setItemSlot(equipmentSlot, priorItem);
                        		    } else {
                        		    	priorItems.putIfAbsent(equipmentSlot, priorItem);
                        		    	
                        		    	if (slottedItem.mechPart == MechPart.Leggings) {
                        		    		if (priorFeetItem != null)
                        		    			priorItems.putIfAbsent(EquipmentSlot.FEET, priorFeetItem);
            		    				}
                        		    }
                        		    
                        		    if (itemStackToRender.getItem() instanceof ArmorBase base) {
                        		    	if (base.shouldHidePlayerModel()) {
                        		    		List<ModelPart> parts = MechPartUtil.getCorrespondingModelParts(playerModel, base.getMechPart());
                    						for (ModelPart part : parts) {
                    							part.visible = false;
                    						}
                    					}
                        		    }
        		    			}
        		    		}
        		    	});
        		    }
        		    
        			//hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, EquipmentSlot.CHEST, event.getPackedLight(), armorModel);
        			//event.getEntity().setItemSlot(EquipmentSlot.CHEST, new ItemStack(BioMechRegistry.ITEM_POWER_LEFT_ARM.get()));
        			//hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, EquipmentSlot.CHEST, event.getPackedLight(), armorModel);
        			//event.getEntity().setItemSlot(EquipmentSlot.CHEST, priorItem);
        			poseStack.popPose();
        			
        			//delete this
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
            					}
            				}
            			}
            		}
        		} catch (Exception e) {
        			LOGGER.error("render error", e);
        		}
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
        	if (disableAllRenderingLogic)
        		return;
        	
        	if (itemToRender == null) {
        		itemToRender = new ItemStack(BioMechRegistry.ITEM_HOVERTECH_LEGGINGS.get());
        	}
        	Player renderEntity = event.getEntity();
        	AzArmorRenderer armorRenderer = AzArmorRendererRegistry.getOrNull(itemToRender.getItem());
        	if (armorRenderer != null && renderEntity != null) {
        		if (!priorItems.isEmpty()) {
        			for (Map.Entry<EquipmentSlot, ItemStack> priorItem : priorItems.entrySet()) {
        				event.getEntity().setItemSlot(priorItem.getKey(), priorItem.getValue());
        			}
        		}
        	}
        }
        
        @SuppressWarnings("unchecked")
		@SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
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
        	
        	AzItemRendererRegistry.register(BioMechStationItemRenderer::new, BioMechRegistry.ITEM_BIOMECH_STATION.get());
        	AzItemRendererRegistry.register(MiningLaserItemRenderer::new, BioMechRegistry.ITEM_MINING_LASER_ARM.get());
        	AzItemRendererRegistry.register(PowerArmItemRenderer::new, BioMechRegistry.ITEM_POWER_ARM.get());
        	
        	MenuScreens.register(BioMechRegistry.MENU_TYPE_BIOMECH_STATION.get(), BioMechStationScreen::new);
        	
        	AzIdentityRegistry.register(BioMechRegistry.ITEM_MINING_LASER_ARM.get(), BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get());
        }
        
    }
}

