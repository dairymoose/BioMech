package com.dairymoose.biomech;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.armor.renderer.BackJetpackRenderer;
import com.dairymoose.biomech.armor.renderer.BackScubaTankRenderer;
import com.dairymoose.biomech.armor.renderer.HovertechLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.LavastrideLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.MiningLaserLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.MiningLaserRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.NightVisionVisorRenderer;
import com.dairymoose.biomech.armor.renderer.PowerChestRenderer;
import com.dairymoose.biomech.armor.renderer.PowerHelmetRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.PowerRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.SpiderWalkersRenderer;
import com.dairymoose.biomech.block_entity.renderer.BioMechStationRenderer;
import com.dairymoose.biomech.client.screen.BioMechStationScreen;
import com.dairymoose.biomech.config.BioMechConfig;
import com.dairymoose.biomech.config.BioMechCraftingFlags;
import com.dairymoose.biomech.config.BioMechServerConfig;
import com.dairymoose.biomech.item.BioMechActivator;
import com.dairymoose.biomech.item.BioMechDeactivator;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MechPartUtil;
import com.dairymoose.biomech.item.renderer.BioMechStationItemRenderer;
import com.dairymoose.biomech.item.renderer.MiningLaserItemRenderer;
import com.dairymoose.biomech.item.renderer.PowerArmItemRenderer;
import com.dairymoose.biomech.packet.clientbound.ClientboundEnergySyncPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundUpdateSlottedItemPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundHandStatusPacket;
import com.dairymoose.biomech.particle.InstantSmokeParticle;
import com.dairymoose.biomech.particle.LaserParticle;
import com.dairymoose.biomech.particle.MaxLaserParticle;
import com.dairymoose.biomech.particle.ThickerLaserParticle;
import com.dairymoose.biomech.particle.ThickestLaserParticle;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.rewrite.animation.AzAnimator;
import mod.azure.azurelib.rewrite.animation.AzAnimatorAccessor;
import mod.azure.azurelib.rewrite.animation.cache.AzIdentityRegistry;
import mod.azure.azurelib.rewrite.animation.dispatch.AzDispatchSide;
import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
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
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;	
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
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

/*
 * - Adding new chest/back/etc armor:
 * Add .geo.json file
 * Add textures.item .png texture for 3d model
 * Draw custom image for icon in textures.item.icon package
 * Add custom image to minecraft:generated item in models.item
 * Add localization
 * Add renderer in armor.renderer package
 * Add ArmorBase class in item.armor package
 * Add new armor to BioMechRegistry
 * Add new renderer/items to bottom of BioMech in onClientSetup (AzArmorRendererRegistry)
 * 
 * - Adding new arms:
 * Add .geo.json file
 * Add textures.item .png texture for 3d model
 * <Arm Specific>: Copy .geo.json file to _item.geo.json
 * <Arm Specific>: Add animation in animations.item package
 * <Arm Specific>: Export display settings and put it models.item
 * Add localization
 * Add renderer in armor.renderer package
 * Add ArmorBase class in item.armor package
 * Add new armor to BioMechRegistry
 * <Arm Specific>: Add new renderer/items to bottom of BioMech in onClientSetup (AzArmorRendererRegistry & AzItemRendererRegistry)
 * <Arm Specific>: If animated: add to AzIdentityRegistry
 * 
 */

@Mod(BioMech.MODID)
public class BioMech
{
    public static final String MODID = "biomech";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    private static BioMechCraftingFlags craftingFlags;
    
    public BioMech(FMLJavaModLoadingContext context)
    {
		LOGGER.debug(BioMechRegistry.TAB_BIOMECH_CREATIVE.toString());
		
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);
        
        modEventBus.addListener(this::addItemsToCreativeTab);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);
        PARTICLES.register(modEventBus);
        SOUNDS.register(modEventBus);
        
        MinecraftForge.EVENT_BUS.register(this);

        craftingFlags = new BioMechCraftingFlags();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BioMechConfig.commonSpec);
	    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BioMechConfig.clientSpec);
	    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BioMechConfig.serverSpec);
	    
	    int msgId = 0;
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundUpdateSlottedItemPacket.class, ClientboundUpdateSlottedItemPacket::write, ClientboundUpdateSlottedItemPacket::new, ClientboundUpdateSlottedItemPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ServerboundHandStatusPacket.class, ServerboundHandStatusPacket::write, ServerboundHandStatusPacket::new, ServerboundHandStatusPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundHandStatusPacket.class, ClientboundHandStatusPacket::write, ClientboundHandStatusPacket::new, ClientboundHandStatusPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundEnergySyncPacket.class, ClientboundEnergySyncPacket::write, ClientboundEnergySyncPacket::new, ClientboundEnergySyncPacket::handle);
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
    
    @SubscribeEvent
    public void onCommand(RegisterCommandsEvent event) {
		BioMechCommand.register(event.getDispatcher());
	}
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        AzureLib.initialize();
    }
   
    @SubscribeEvent
    public void onStopServer(ServerStoppedEvent event) {
		lootBioMechInChest = null;
		lootBioMechInMineshaft = null;
		lootBioMechInDungeon = null;
		lootItemsToAdd.clear();
    }
    
    Float lootBioMechInChest = null;
    Float lootBioMechInMineshaft = null;
    Float lootBioMechInDungeon = null;
    List<Item> lootItemsToAdd = new ArrayList<>();
    @SubscribeEvent
    public void onAlterLootTable(LootTableLoadEvent event) {
    	
    	if (lootBioMechInChest == null) {
    		File file = BioMechConfig.getBiomechEarlyConfigFile();
    		CompoundTag tag = null;
    		try {
    			tag = NbtIo.read(file);
    		} catch (IOException e) {
    			BioMech.LOGGER.error("Failed to read early config file biomech.cfg");
    		}
    		lootBioMechInChest = (float) BioMechServerConfig.defaultChestLootChance;
    		lootBioMechInMineshaft = (float) BioMechServerConfig.defaultMineshaftLootChance;
    		lootBioMechInDungeon = (float) BioMechServerConfig.defaultDungeonLootChance;
    		boolean gotGlobalConfigValue = false;
    		boolean gotMineshaftConfigValue = false;
    		boolean gotDungeonConfigValue = false;
    		if (tag != null) {
    			if (tag.contains("lootBioMechInChest")) {
    				lootBioMechInChest = (float) tag.getDouble("lootBioMechInChest");
    				BioMech.LOGGER.info("Got lootBioMechInChest value of " + lootBioMechInChest + " from file");
    				gotGlobalConfigValue = true;
    			}
    			if (tag.contains("lootBioMechInMineshaft")) {
    				lootBioMechInMineshaft = (float) tag.getDouble("lootBioMechInMineshaft");
    				BioMech.LOGGER.info("Got lootBioMechInMineshaft value of " + lootBioMechInMineshaft + " from file");
    				gotMineshaftConfigValue = true;
    			}
    			if (tag.contains("lootBioMechInDungeon")) {
    				lootBioMechInDungeon = (float) tag.getDouble("lootBioMechInDungeon");
    				BioMech.LOGGER.info("Got lootBioMechInDungeon value of " + lootBioMechInDungeon + " from file");
    				gotDungeonConfigValue = true;
    			}
    		}
    		if (!gotGlobalConfigValue) {
    			BioMech.LOGGER.info("Using default lootBioMechInChest value of " + lootBioMechInChest);
    		}
    		if (!gotMineshaftConfigValue) {
    			BioMech.LOGGER.info("Using default lootBioMechInMineshaft value of " + lootBioMechInMineshaft);
    		}
    		if (!gotDungeonConfigValue) {
    			BioMech.LOGGER.info("Using default lootBioMechInDungeon value of " + lootBioMechInDungeon);
    		}
    		
    		Field[] allFields = BioMechRegistry.class.getDeclaredFields();
			for (Field f : allFields) {
				if (f.getType() == RegistryObject.class) {
					try {
						RegistryObject value = (RegistryObject) f.get(null);
						if (value != null) {
							if (value.get() instanceof ArmorBase ab) {
								if (ab.shouldAddToLootTable()) {
									lootItemsToAdd.add(ab);
									LOGGER.debug("Added loot is: " + value.get());
								}
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						LOGGER.error("Error registering items with creative tab", e);
					}
				}
			}
			
    	}
		
    	//LOGGER.info(ServerLifecycleHooks.getCurrentServer().getWorldPath(LevelResource.ROOT).toString());
    	if (event.getName().getPath().contains("chests")) {
    		String mineshaftText = "";
    		float chance = lootBioMechInChest;
    		if ("chests/abandoned_mineshaft".equals(event.getName().getPath())) {
    			chance = lootBioMechInMineshaft;
    			mineshaftText = " mineshaft";
    		} else if ("chests/simple_dungeon".equals(event.getName().getPath())) {
    			chance = lootBioMechInDungeon;
    			mineshaftText = " dungeon";
    		}
    		LOGGER.debug("alter" + mineshaftText + " loot table: " + event.getTable().getLootTableId().getPath());
    		
    		LootPool.Builder lootPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).when(LootItemRandomChanceCondition.randomChance(chance));
    		for (Item item : lootItemsToAdd) {
    			lootPool = lootPool.add(LootItem.lootTableItem(item));
    		}
    		
    		event.getTable().addPool(lootPool.build());
    	}
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
    public void onJump(LivingEvent.LivingJumpEvent event) {
    	if (event.getEntity() instanceof Player player) {
    		if (player.onGround() && localPlayerHoldingAlt) {
    			Vec3 delta = player.getDeltaMovement();
    			player.setDeltaMovement(delta.x, 0.0, delta.z);
    		}
    	}
    }
    
    //client-side energy drain tracking
    private static float suitEnergyLast = 0.0f;
	private static float suitEnergyDiffSum = 0.0f;
	private static int TICKS_TO_UPDATE_ENERGY_DIFF = 10;
	private static int TICKS_PER_SECOND = 20;
	private static float calcEnergyDiffOnePeriod = 0.0f;
	
    public static int RESYNC_ENERGY_TICK_PERIOD = 60;
    public static Map<UUID, HandActiveStatus> handActiveMap = new HashMap<>();
    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent event) {
    	if (event.phase == TickEvent.Phase.START) {
    		BioMechPlayerData playerData = globalPlayerData.get(event.player.getUUID());
    		if (playerData != null) {
    			playerData.tickEnergy(event.player);
    			tickInventoryForPlayer(event.player, playerData);
    			tickHandsForPlayer(event.player, playerData);
    			if (event.player.level().isClientSide) {
    				float suitEnergy = playerData.getSuitEnergy();
    				float oneTickEnergyDiff = suitEnergy - suitEnergyLast; 
					suitEnergyDiffSum += oneTickEnergyDiff;
					suitEnergyLast = suitEnergy;
					
					if (Minecraft.getInstance().player.tickCount % TICKS_TO_UPDATE_ENERGY_DIFF == 0) {
						BioMech.calcEnergyDiffOnePeriod = suitEnergyDiffSum / ((float)TICKS_TO_UPDATE_ENERGY_DIFF/TICKS_PER_SECOND);
						suitEnergyDiffSum = 0.0f;
					}
    			}
    			
				if (event.player.tickCount % RESYNC_ENERGY_TICK_PERIOD == 0) {
					if (event.player instanceof ServerPlayer sp) {
						BioMechNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), new ClientboundEnergySyncPacket(playerData.getSuitEnergy(), playerData.suitEnergyMax));
					}
    			}
    		}
    	}
    }

    //The concept of a first person arm itemstack only exists on the client for the local player
    //On the server, the passed in itemStack is returned unmodified
    @SuppressWarnings("deprecation")
	public ItemStack getFirstPersonArmItemStack(ItemStack itemStack, MechPart handPart) {
    	class ItemStackHolder {
			ItemStack itemStack;
		}
		ItemStackHolder ish = new ItemStackHolder();
		ish.itemStack = itemStack;
		final MechPart clientPart = handPart;
		DistExecutor.runWhenOn(Dist.CLIENT, () ->
			new Runnable() {
				public void run() {
					if (clientPart == MechPart.RightArm)
						ish.itemStack = ClientModEvents.mainHandRenderStack;
					else
						ish.itemStack = ClientModEvents.offHandRenderStack;
				}
			}
			
		);
		
		return ish.itemStack;
    }
    
    public static ItemStack getThirdPersonArmItemStack(BioMechPlayerData playerData, MechPart part) {
		ItemStack itemStack = ItemStack.EMPTY;
		
		SlottedItem slotted = playerData.getForSlot(part);
		itemStack = slotted.itemStack;
		if (slotted.mechPart == MechPart.LeftArm && !slotted.leftArmItemStack.isEmpty()) {
			itemStack = slotted.leftArmItemStack;
		}
		
		return itemStack;
	}
    
	private void tickHandsForPlayer(final Player player, BioMechPlayerData playerData) {
		InteractionHand[] hands = { InteractionHand.MAIN_HAND, InteractionHand.OFF_HAND };
		
		HandActiveStatus has = handActiveMap.computeIfAbsent(player.getUUID(), (uuid) -> new HandActiveStatus());
		for (InteractionHand hand : hands) {
			
			MechPart handPart = null;
			if (hand == InteractionHand.MAIN_HAND)
				handPart = MechPart.RightArm;
			else if (hand == InteractionHand.OFF_HAND)
				handPart = MechPart.LeftArm;
			
			boolean bothHandsInactive = false;
			boolean bothHandsActive = false;
			boolean currentArmActive = false;
			if (handPart == MechPart.RightArm && has.rightHandActive || handPart == MechPart.LeftArm && has.leftHandActive) {
				currentArmActive = true;
			}
			if (!has.leftHandActive && !has.rightHandActive) {
				bothHandsInactive = true;
			}
			if (has.leftHandActive && has.rightHandActive) {
				bothHandsActive = true;
			}
			
			ItemStack itemStack = playerData.getForSlot(handPart).itemStack;
			if (itemStack.getItem() instanceof ArmorBase base) {
				if (isMechArmActive(player, playerData, handPart)) {
					itemStack = getFirstPersonArmItemStack(itemStack, handPart);
					float partialTick = 1.0f;
					base.onHandTick(currentArmActive, itemStack, player, handPart, partialTick, bothHandsInactive, bothHandsActive);
				}
			}
		}
	}
	
	private void tickInventoryForPlayer(final Player player, BioMechPlayerData playerData) {
		List<SlottedItem> slottedItems = playerData.getAllSlots();
		for (SlottedItem slotted : slottedItems) {
			if (!slotted.itemStack.isEmpty()) {
								
				ItemStack tickingItem = getThirdPersonArmItemStack(playerData, slotted.mechPart);
				tickingItem.inventoryTick(player.level(), player, -1, slotted.mechPart == MechPart.LeftArm);
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

    //AzureLib bug: dispatcher doesn't work client side because it uses AzAnimatorAccessor instead of AzIdentifiableItemStackAnimatorCache
    public static void clientSideItemAnimation(ItemStack itemStack, AzCommand command) {
    	AzAnimator<ItemStack> anim = AzAnimatorAccessor.getOrNull(itemStack);
    	if (anim != null) {
    		command.actions().forEach(action -> action.handle(AzDispatchSide.CLIENT, anim));
    	}
    	//AzItemStackDispatchCommandPacket packet = new AzItemStackDispatchCommandPacket(itemStack.getTag().getUUID("az_id"), command);
		//packet.handle();
    }
    
    public static boolean isRightMechArmActive(Player player, BioMechPlayerData playerData) {
    	if (player == null)
    		return false;
    	if (player.getMainHandItem().getItem() instanceof BioMechActivator)
    		return true;
		return !playerData.getForSlot(MechPart.RightArm).itemStack.isEmpty() && player.getMainHandItem().isEmpty();
	}
	
	public static boolean isLeftMechArmActive(Player player, BioMechPlayerData playerData) {
		if (player == null)
    		return false;
		if (player.getMainHandItem().getItem() instanceof BioMechActivator || player.getOffhandItem().getItem() instanceof BioMechActivator)
    		return true;
		return !playerData.getForSlot(MechPart.LeftArm).itemStack.isEmpty() && player.getOffhandItem().isEmpty();
	}
	
	public static boolean isMechArmActive(Player player, BioMechPlayerData playerData, MechPart part) {
		if (part == MechPart.RightArm) {
			return BioMech.isRightMechArmActive(player, playerData);
		}
		else if (part == MechPart.LeftArm) {
			return BioMech.isLeftMechArmActive(player, playerData);
		}
		return false;
	}
    
	public static boolean primedForMidairJump = false;
	public static boolean localPlayerJumping = false;
	public static boolean localPlayerHoldingAlt = false;
	
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
    	
        public static final List<KeyMapping> allKeyMappings = List.of(HOTKEY_RIGHT_ARM, HOTKEY_LEFT_ARM, HOTKEY_ENABLE_ARM_FUNCTION);

        @SubscribeEvent
        public static void onRegisterParticle(RegisterParticleProvidersEvent event) {
        	event.registerSpriteSet(BioMechRegistry.PARTICLE_TYPE_LASER.get(), LaserParticle.Provider::new);
        	event.registerSpriteSet(BioMechRegistry.PARTICLE_TYPE_THICKER_LASER.get(), ThickerLaserParticle.Provider::new);
        	event.registerSpriteSet(BioMechRegistry.PARTICLE_TYPE_THICKEST_LASER.get(), ThickestLaserParticle.Provider::new);
        	event.registerSpriteSet(BioMechRegistry.PARTICLE_TYPE_MAX_LASER.get(), MaxLaserParticle.Provider::new);
        	event.registerSpriteSet(BioMechRegistry.PARTICLE_TYPE_INSTANT_SMOKE.get(), InstantSmokeParticle.Provider::new);
        }
        
    	@SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
    		for (KeyMapping mapping : allKeyMappings) {
				event.register(mapping);
			}
        }
    	
    	@SubscribeEvent
    	public void onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
    		HandActiveStatus has = this.getLocalHandActiveStatus();
    		
    		if (has != null) {
    			if (has.rightHandActive && event.isAttack() && Minecraft.getInstance().options.keyAttack.getKey().equals(HOTKEY_RIGHT_ARM.getKey())) {
        			event.setSwingHand(false);
        			event.setCanceled(true);
        		}
        		
        		if (has.leftHandActive && event.isUseItem() && Minecraft.getInstance().options.keyUse.getKey().equals(HOTKEY_LEFT_ARM.getKey())) {
        			event.setSwingHand(false);
        			event.setCanceled(true);
        		}
    		}
    	}

    	private ResourceLocation GUI_SUIT_ENERGY_LOCATION = new ResourceLocation(MODID, "textures/gui/suit_energy.png");
    	private int GUI_SUIT_ENERGY_TEX_SIZE = 32;
    	private int GUI_SUIT_ENERGY_BORDER_HEIGHT = 8;
		@SubscribeEvent
		public void renderOverlayEvent(RenderGuiOverlayEvent.Pre overlayEvent) {
			BioMechPlayerData playerData = this.getDataForLocalPlayer();

			if (playerData != null) {
				boolean hasAnyBioMechArmor = playerData.getAllSlots().stream()
						.anyMatch((slotted) -> !slotted.itemStack.isEmpty());
				if (hasAnyBioMechArmor) {
					float suitEnergy = playerData.getSuitEnergy();
					
					Window window = overlayEvent.getWindow();
					RenderSystem.setShader(GameRenderer::getPositionTexShader);
					float xScale = BioMechConfig.CLIENT.energySuitGuiXScale.get().floatValue();
					float yScale = BioMechConfig.CLIENT.energySuitGuiYScale.get().floatValue();
					if (xScale > 0.0f && yScale > 0.0f && playerData.suitEnergyMax > 0.0f) {
						float suitEnergyPct = suitEnergy/playerData.suitEnergyMax;
						float visibleThreshold = BioMechConfig.CLIENT.showEnergySuitGuiThreshold.get().floatValue();
						
						if (suitEnergyPct <= visibleThreshold) {
							float xScaleInv = 1.0f/xScale;
							float yScaleInv = 1.0f/yScale;
							int xStart = (int) ((window.getGuiScaledWidth() * BioMechConfig.CLIENT.energySuitGuiXPos.get().floatValue() * xScaleInv));
							int yStart = (int) ((window.getGuiScaledHeight() * BioMechConfig.CLIENT.energySuitGuiYPos.get().floatValue() * yScaleInv));
							float xStartTexture = 0.0f;
							float yStartTexture = 0.0f;
							
							//RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, BioMechConfig.CLIENT.energySuitGuiOpacity.get().floatValue());
							overlayEvent.getGuiGraphics().pose().pushPose();
							overlayEvent.getGuiGraphics().pose().scale(xScale, yScale, 1.0f);
							//draw border
							overlayEvent.getGuiGraphics().blit(this.GUI_SUIT_ENERGY_LOCATION, xStart, yStart, xStartTexture,
									yStartTexture, GUI_SUIT_ENERGY_TEX_SIZE, GUI_SUIT_ENERGY_BORDER_HEIGHT, GUI_SUIT_ENERGY_TEX_SIZE,
									GUI_SUIT_ENERGY_TEX_SIZE);
							//draw bar
							overlayEvent.getGuiGraphics().blit(this.GUI_SUIT_ENERGY_LOCATION, xStart, yStart, xStartTexture,
									(float) GUI_SUIT_ENERGY_BORDER_HEIGHT,
									1 + (int) Math.ceil((GUI_SUIT_ENERGY_TEX_SIZE - 2) * suitEnergyPct), GUI_SUIT_ENERGY_BORDER_HEIGHT,
									GUI_SUIT_ENERGY_TEX_SIZE, GUI_SUIT_ENERGY_TEX_SIZE);
							
							float textXScale = BioMechConfig.CLIENT.suitEnergyTextXScale.get().floatValue();
							float textYScale = BioMechConfig.CLIENT.suitEnergyTextYScale.get().floatValue();
							overlayEvent.getGuiGraphics().pose().scale(textXScale, textYScale, 1.0f);
							if (BioMechConfig.CLIENT.showSuitEnergyText.get().booleanValue()) {
								float textXScaleInv = 1.0f/textXScale;
								float textYScaleInv = 1.0f/textYScale;
								float barHeight = GUI_SUIT_ENERGY_BORDER_HEIGHT * yScale;
								int halfBarHeight = (int)(barHeight / 2.0f);
								int leftMargin = (int)(3.0f*xScale);
								
								float pixelXDiff = xStart*textXScaleInv - xStart;
								float pixelYDiff = yStart*textYScaleInv - yStart;
								overlayEvent.getGuiGraphics().pose().translate(pixelXDiff, pixelYDiff, 0.0f);
								overlayEvent.getGuiGraphics().pose().translate(leftMargin, halfBarHeight, 0.0f);
								//print suit energy
								Component component1 = MutableComponent
										.create(new LiteralContents(String.valueOf((int)suitEnergy)))
										.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)));
								overlayEvent.getGuiGraphics().drawString(Minecraft.getInstance().font, component1,
										xStart,
										yStart, 0, true);
							}
							if (BioMechConfig.CLIENT.showEnergyDrainRate.get().booleanValue()) {
								NumberFormat nf = new DecimalFormat("#.#");
								//print suit energy gain/loss
								Component component2 = null;
								if (calcEnergyDiffOnePeriod >= 0.0f) {
									component2 = MutableComponent
									.create(new LiteralContents(nf.format(calcEnergyDiffOnePeriod)))
									.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00)));
								} else {
									component2 = MutableComponent
									.create(new LiteralContents(nf.format(calcEnergyDiffOnePeriod)))
									.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)));
								}
								overlayEvent.getGuiGraphics().drawString(Minecraft.getInstance().font, component2,
										(int)(xStart + GUI_SUIT_ENERGY_TEX_SIZE*xScale - 0.2f*xScale),
										yStart, 0, true);
							}
							overlayEvent.getGuiGraphics().pose().scale(1.0f, 1.0f, 1.0f);
							
							overlayEvent.getGuiGraphics().pose().popPose();
							//RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
						}
					}
					
					//draw ENERGY text
//					RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
//					overlayEvent.getGuiGraphics().blit(this.GUI_SUIT_ENERGY_LOCATION, xStart, yStart, xStartTexture,
//							(float) GUI_SUIT_ENERGY_BORDER_HEIGHT * 2, GUI_SUIT_ENERGY_TEX_SIZE,
//							GUI_SUIT_ENERGY_BORDER_HEIGHT, GUI_SUIT_ENERGY_TEX_SIZE, GUI_SUIT_ENERGY_TEX_SIZE);
//
//					RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
				}
			}

		}
    	
    	public HandActiveStatus getLocalHandActiveStatus() {
    		if (Minecraft.getInstance().player != null) {
    			UUID localUuid = Minecraft.getInstance().player.getUUID();
        		HandActiveStatus has = handActiveMap.computeIfAbsent(localUuid, (uuid) -> new HandActiveStatus());
        		
        		return has;
    		}
    		
    		return null;
    	}
    	
    	public static boolean requireModifierKeyForArmUsage = true;
    	//boolean rightArmActive = false;
    	//boolean leftArmActive = false;
    	@SubscribeEvent
        public void onClientTick(final ClientTickEvent event) {
        	if (event.phase == TickEvent.Phase.START) {
        		HandActiveStatus has = this.getLocalHandActiveStatus();
        		if (has != null) {
        			boolean initialRight = has.rightHandActive;
            		boolean initialLeft = has.leftHandActive;
            		boolean initialModifier = has.modifierKeyActive;
            		boolean initialJump = has.jumpActive;
            		
            		BioMechPlayerData playerData = this.getDataForLocalPlayer();
            		if (playerData != null) {
            			Player localPlayer = Minecraft.getInstance().player;
            			
            			has.modifierKeyActive = HOTKEY_ENABLE_ARM_FUNCTION.isDown();
            			has.jumpActive = Minecraft.getInstance().player.input.jumping;
            			
            			if (Minecraft.getInstance().player != null) {
                			localPlayerHoldingAlt = HOTKEY_ENABLE_ARM_FUNCTION.isDown();
                			BioMech.localPlayerJumping = Minecraft.getInstance().player.input.jumping;
                			if (Minecraft.getInstance().player.onGround()) {
                				primedForMidairJump = false;
                			} else if (!BioMech.localPlayerJumping) {
                				primedForMidairJump = true;
                			}
                		}
            			
            			if (requireModifierKeyForArmUsage) {
                			if (HOTKEY_ENABLE_ARM_FUNCTION.isDown()) {
                    			if (isRightMechArmActive(localPlayer, playerData) && HOTKEY_RIGHT_ARM.isDown()) {
                    				while (HOTKEY_RIGHT_ARM.consumeClick());
                    				has.rightHandActive = true;
                        		}
                    			
                    			if (isLeftMechArmActive(localPlayer, playerData) && HOTKEY_LEFT_ARM.isDown()) {
                    				while (HOTKEY_LEFT_ARM.consumeClick());
                    				has.leftHandActive = true;
                        		}
                    		}
                    		if (!HOTKEY_RIGHT_ARM.isDown()) {
                    			has.rightHandActive = false;
                    		}
                    		
                    		if (!HOTKEY_LEFT_ARM.isDown()) {
                    			has.leftHandActive = false;
                    		}
                		} else {
                			if (isRightMechArmActive(localPlayer, playerData) && HOTKEY_RIGHT_ARM.isDown()) {
                				has.rightHandActive = true;
                    		} else {
                    			has.rightHandActive = false;
                    		}
                    		
                    		if (isLeftMechArmActive(localPlayer, playerData) && HOTKEY_LEFT_ARM.isDown()) {
                    			has.leftHandActive = true;
                    		} else {
                    			has.leftHandActive= false;
                    		}
                		}
            		}
            		
            		if (initialRight != has.rightHandActive || initialLeft != has.leftHandActive || initialModifier != has.modifierKeyActive || initialJump != has.jumpActive) {
            			BioMechNetwork.INSTANCE.sendToServer(new ServerboundHandStatusPacket(has));
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
        
        public static boolean disableAllRenderingLogic = false;
        
        public static ItemStack mainHandRenderStack = ItemStack.EMPTY;
        public static ItemStack offHandRenderStack = ItemStack.EMPTY;
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
            	
            	HandActiveStatus has = this.getLocalHandActiveStatus();
            	if (has != null) {
            		boolean currentArmActive = false;
                	if (handPart == MechPart.RightArm && has.rightHandActive || handPart == MechPart.LeftArm && has.leftHandActive) {
                		currentArmActive = true;
                	}
                	if (playerData != null && handPart != null && equipSlot != null) {
                		if (!(Minecraft.getInstance().player.getMainHandItem().getItem() instanceof BioMechActivator)) {
                			if ((Minecraft.getInstance().player.getMainHandItem().getItem() instanceof BioMechDeactivator && 
                    				(event.getHand() == InteractionHand.MAIN_HAND || (event.getHand() == InteractionHand.OFF_HAND && Minecraft.getInstance().player.getOffhandItem().isEmpty())))
                    				
                    				||
                    				
                    				(Minecraft.getInstance().player.getOffhandItem().getItem() instanceof BioMechDeactivator && event.getHand() == InteractionHand.OFF_HAND)) {
                    			event.setCanceled(true);
                    			return;
                    		}
                		}
                		if (isMechArmActive(Minecraft.getInstance().player, playerData, handPart) && playerData.getForSlot(handPart).visible) {
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
                			
                			if (handPart == MechPart.LeftArm && hideOffHandWhileInactive && !currentArmActive) {
                				return;
                			}
                			ItemInHandRenderer iihr = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
                			iihr.renderArmWithItem(Minecraft.getInstance().player, event.getPartialTick(), event.getInterpolatedPitch(), event.getHand(), 
                					event.getSwingProgress(), newRenderItem, event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
                		}
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
        			PlayerRenderer playerRenderer = event.getRenderer();
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
        			
    				//setupRotations accounts for yaw, elytra flight, swimming, etc
    				playerRenderer.setupRotations((AbstractClientPlayer)renderEntity, poseStack, 0.0f, f, event.getPartialTick());
        			poseStack.scale(-1.0F, -1.0F, 1.0F);
        		    this.scale((AbstractClientPlayer)renderEntity, poseStack, event.getPartialTick());
        		    poseStack.translate(0.0F, -1.501F, 0.0F);
        		    
        		    priorItems.clear();
        		    class ActivatorStatus {
        		    	boolean hasMainHandActivator = false;
            		    boolean hasOffHandActivator = false;
        		    }
        		    ActivatorStatus as = new ActivatorStatus();
        		    if (renderEntity.getMainHandItem().getItem() instanceof BioMechActivator || renderEntity.getMainHandItem().getItem() instanceof BioMechDeactivator) {
        		    	as.hasMainHandActivator = true;
        		    	priorItems.put(EquipmentSlot.MAINHAND, renderEntity.getMainHandItem());
        		    	renderEntity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        		    }
        		    if (renderEntity.getOffhandItem().getItem() instanceof BioMechActivator || renderEntity.getOffhandItem().getItem() instanceof BioMechDeactivator) {
        		    	as.hasOffHandActivator = true;
        		    	priorItems.put(EquipmentSlot.OFFHAND, renderEntity.getOffhandItem());
        		    	renderEntity.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        		    }
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
        		    				//this is required so AzureLib actually does any render at all in renderArmorPiece
        		    				event.getEntity().setItemSlot(equipmentSlot, itemStackToRender);
        		    				if (slottedItem.mechPart == MechPart.Leggings) {
        		    					priorFeetItem = event.getEntity().getItemBySlot(EquipmentSlot.FEET);
        		    					event.getEntity().setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        		    				}
            		    			
        		    				poseStack.pushPose();
        		    				HandActiveStatus has = handActiveMap.computeIfAbsent(event.getEntity().getUUID(), (uuid) -> new HandActiveStatus());
        		    				//activator item moves the arm forward slightly, we'll undo that here
        		    				if (slottedItem.mechPart == MechPart.RightArm && as.hasMainHandActivator) {
        		    					poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
        		    				}
        		    				if (slottedItem.mechPart == MechPart.LeftArm && as.hasOffHandActivator) {
        		    					poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
        		    				}
        		    				if (slottedItem.mechPart == MechPart.RightArm && has.rightHandActive) {
        		    					poseStack.mulPose(Axis.XP.rotationDegrees(renderEntity.getXRot()));
        		    				} else if (slottedItem.mechPart == MechPart.LeftArm && has.leftHandActive) {
        		    					poseStack.mulPose(Axis.XP.rotationDegrees(renderEntity.getXRot()));
        		    				}
                        		    hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, equipmentSlot, event.getPackedLight(), armorModel);
                        		    //this is required so that AzureLib does not re-render the same armor when it comes time to render the player via default MC logic
                        		    event.getEntity().setItemSlot(equipmentSlot, ItemStack.EMPTY);
                        		    poseStack.popPose();
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
        		    
        			poseStack.popPose();
        		} catch (Exception e) {
        			LOGGER.error("render error", e);
        		}
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
        	AzArmorRendererRegistry.register(BackScubaTankRenderer::new, BioMechRegistry.ITEM_BACK_SCUBA_TANK.get());
        	AzArmorRendererRegistry.register(BackJetpackRenderer::new, BioMechRegistry.ITEM_BACK_JETPACK.get());
        	AzArmorRendererRegistry.register(SpiderWalkersRenderer::new, BioMechRegistry.ITEM_SPIDER_WALKERS.get());
        	AzArmorRendererRegistry.register(NightVisionVisorRenderer::new, BioMechRegistry.ITEM_NIGHT_VISION_VISOR.get());
        	
        	AzItemRendererRegistry.register(BioMechStationItemRenderer::new, BioMechRegistry.ITEM_BIOMECH_STATION.get());
        	AzItemRendererRegistry.register(MiningLaserItemRenderer::new, BioMechRegistry.ITEM_MINING_LASER_ARM.get());
        	AzItemRendererRegistry.register(PowerArmItemRenderer::new, BioMechRegistry.ITEM_POWER_ARM.get());
        	
        	MenuScreens.register(BioMechRegistry.MENU_TYPE_BIOMECH_STATION.get(), BioMechStationScreen::new);
        	
        	AzIdentityRegistry.register(BioMechRegistry.ITEM_MINING_LASER_ARM.get(), BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get());
        }
        
    }
}


