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
import com.dairymoose.biomech.armor.renderer.DiamondMechArmorRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechHeadRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechLegsRenderer;
import com.dairymoose.biomech.armor.renderer.DiamondMechRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.DrillLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.DrillRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.ElytraMechChestplateRenderer;
import com.dairymoose.biomech.armor.renderer.HovertechLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechChestplateRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechHeadRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechLegsRenderer;
import com.dairymoose.biomech.armor.renderer.IronMechRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.LavastrideLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.MiningLaserLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.MiningLaserRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.MobilityTreadsRenderer;
import com.dairymoose.biomech.armor.renderer.NightVisionVisorRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechBodyRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechHeadRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechLegsRenderer;
import com.dairymoose.biomech.armor.renderer.PipeMechRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.PowerChestRenderer;
import com.dairymoose.biomech.armor.renderer.PowerHelmetRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeftArmRenderer;
import com.dairymoose.biomech.armor.renderer.PowerLeggingsRenderer;
import com.dairymoose.biomech.armor.renderer.PowerRightArmRenderer;
import com.dairymoose.biomech.armor.renderer.SpiderWalkersRenderer;
import com.dairymoose.biomech.armor.renderer.SpringLoadedLeggingsRenderer;
import com.dairymoose.biomech.block_entity.renderer.BioMechStationRenderer;
import com.dairymoose.biomech.client.screen.BioMechStationScreen;
import com.dairymoose.biomech.config.BioMechConfig;
import com.dairymoose.biomech.config.BioMechCraftingFlags;
import com.dairymoose.biomech.config.BioMechServerConfig;
import com.dairymoose.biomech.item.BioMechActivator;
import com.dairymoose.biomech.item.BioMechDeactivator;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.DrillLeftArmArmor;
import com.dairymoose.biomech.item.armor.ElytraMechChestplateArmor;
import com.dairymoose.biomech.item.armor.HovertechLeggingsArmor;
import com.dairymoose.biomech.item.armor.IronMechChestArmor;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.MechPartUtil;
import com.dairymoose.biomech.item.armor.MobilityTreadsArmor;
import com.dairymoose.biomech.item.armor.PipeMechBodyArmor;
import com.dairymoose.biomech.item.armor.PowerArmArmor;
import com.dairymoose.biomech.item.armor.PowerHelmetArmor;
import com.dairymoose.biomech.item.renderer.BioMechStationItemRenderer;
import com.dairymoose.biomech.item.renderer.DiamondMechArmItemRenderer;
import com.dairymoose.biomech.item.renderer.DrillItemRenderer;
import com.dairymoose.biomech.item.renderer.IronMechArmItemRenderer;
import com.dairymoose.biomech.item.renderer.MiningLaserItemRenderer;
import com.dairymoose.biomech.item.renderer.PipeMechArmItemRenderer;
import com.dairymoose.biomech.item.renderer.PowerArmItemRenderer;
import com.dairymoose.biomech.menu.BioMechStationMenu;
import com.dairymoose.biomech.packet.clientbound.ClientboundEnergySyncPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundHandStatusPacket;
import com.dairymoose.biomech.packet.clientbound.ClientboundUpdateSlottedItemPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundHandStatusPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundMobilityTreadsPacket;
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
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraftforge.client.event.ComputeFovModifierEvent;
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
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event.Result;
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
 * <Optional>: Add crafting recipe
 * <Optional>: Add tags to mech_part.json
 * 
 * - Adding new arms:
 * BB: Make new right arm model named: pipe_mech_right_arm
 * BB: Duplicate and flip arm model: export as pipe_mech_left_arm.geo.json
 * Add .geo.json file ("pipe_mech_right_arm.geo.json" and "pipe_mech_left_arm.geo.json")
 * Add textures.item .png texture for 3d model (right arm texture only)
 * <Arm Specific>: Copy right_arm .geo.json file to _item.geo.json
 * <Arm Specific / Animated>: Add animation in animations.item package
 * <Arm Specific>: Export display settings and put it models.item
 * Add localization ("pipe_mech_arm" and "pipe_mech_left_arm")
 * Add renderer in armor.renderer package (PipeMechRightArmRenderer & PipeMechLeftArmRenderer)
 * Add ArmorBase class in item.armor package (PipeMechArmArmor & PipeMechRightArmArmor & PipeMechLeftArmArmor)
 * Add new armor to BioMechRegistry (right & left arms)
 * Add new renderer/items to bottom of BioMech in onClientSetup (AzArmorRendererRegistry)
 * <Arm Specific>: Add AzItemRenderer class: PipeMechArmItemRenderer
 * <Arm Specific>: Add new item Renderer reference to bottom of BioMech in onClientSetup (AzItemRendererRegistry)
 * <Arm Specific / Animated>: If animated: add to AzIdentityRegistry
 * 
 */
//TODO: 
//	Add missing crafting recipes
//	Back: Battery Pack
//	Chest: Behemoth (full set?) hulkbuster theme
//	Head: Creeper, Enderman
//	Arm: Extendo-Arm (block reach +2, entity reach +0.5)
//	Arm: Grapple/zipline arm
//	Arm: Buzzsaw arm
//	Arm: Drill arm (3x3)
//	Head: light helmet?
//	Back: shield projector?
//	Legs: unicycle
//	Legs: skies
//	head: wall-e spyglass
//	claptrap suit?
//	Arm: flamethrower
//	Arm: gatling
//	Arm: shield
//	Arm: Claw arms
//	Robocop outfit?
//	Terminator outfit?
//	Wall-E outfit?
//	Iron giant outfit?
//	I robot Outfit?
//	Back: stimpack that injects buff for energy cost
//	Back: healing fluid that heals when holding hotkey, taking energy - cannot move while healing
//	Head: scanner that highlights nearby entities
//	Arm: ore seeker
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
    public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, MODID);
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    private static BioMechCraftingFlags craftingFlags;
    
    public static void INFO(String toLog) {
    	BioMech.LOGGER.info("[" + BioMech.MODID + "] " + toLog);
    }
    
    public static void DEBUG(String toLog) {
    	BioMech.LOGGER.debug("[" + BioMech.MODID + "] " + toLog);
    }
    
    public BioMech(FMLJavaModLoadingContext context)
    {
		LOGGER.debug(BioMechRegistry.TAB_BIOMECH_CREATIVE.toString());
		
        IEventBus modEventBus = context.getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addItemsToCreativeTab);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENUS.register(modEventBus);
        PARTICLES.register(modEventBus);
        SOUNDS.register(modEventBus);
        DAMAGE_TYPES.register(modEventBus);

        craftingFlags = new BioMechCraftingFlags();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BioMechConfig.commonSpec);
	    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BioMechConfig.clientSpec);
	    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BioMechConfig.serverSpec);
	    
	    int msgId = 0;
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundUpdateSlottedItemPacket.class, ClientboundUpdateSlottedItemPacket::write, ClientboundUpdateSlottedItemPacket::new, ClientboundUpdateSlottedItemPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ServerboundHandStatusPacket.class, ServerboundHandStatusPacket::write, ServerboundHandStatusPacket::new, ServerboundHandStatusPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundHandStatusPacket.class, ClientboundHandStatusPacket::write, ClientboundHandStatusPacket::new, ClientboundHandStatusPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ClientboundEnergySyncPacket.class, ClientboundEnergySyncPacket::write, ClientboundEnergySyncPacket::new, ClientboundEnergySyncPacket::handle);
		BioMechNetwork.INSTANCE.registerMessage(msgId++, ServerboundMobilityTreadsPacket.class, ServerboundMobilityTreadsPacket::write, ServerboundMobilityTreadsPacket::new, ServerboundMobilityTreadsPacket::handle);
    }
    
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> inputType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> tickerInterface) {
		return inputType == expectedType ? (BlockEntityTicker) tickerInterface : null;
	}
    
    public static void allowFlyingForPlayer(Player player) {
    	if (!player.level().isClientSide) {
			List<Connection> connections = player.getServer().getConnection().getConnections();
			for (int i=0; i<connections.size(); ++i) {
				if (connections.get(i).getPacketListener() instanceof ServerGamePacketListenerImpl sgpl) {
					if (sgpl.player != null && sgpl.player.getId() == player.getId()) {
						//prevent 'player is flying' disconnect error on server
						sgpl.aboveGroundTickCount = 0;
					}
				}
			}
		}
    }
    
    public void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
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
    public void onPlayerCalcBreakSpeed(PlayerEvent.BreakSpeed event) {
    	BioMechPlayerData playerData = BioMech.globalPlayerData.get(event.getEntity().getUUID());
		if (playerData != null) {
			if (playerData.getForSlot(MechPart.Leggings).itemStack.getItem() instanceof HovertechLeggingsArmor armor)
			if (!event.getEntity().onGround()) {
				//undo ground effect from player.getDigSpeed
				event.setNewSpeed(event.getNewSpeed() * 5.0f);
			}
		}
    }
    
    @SubscribeEvent
    public void onFovEvent(ComputeFovModifierEvent event) {
    	BioMechPlayerData playerData = BioMech.globalPlayerData.get(event.getPlayer().getUUID());
		if (playerData != null) {
			ItemStack itemStack = playerData.getForSlot(MechPart.Leggings).itemStack;
			if (itemStack.getItem() instanceof MobilityTreadsArmor armor) {
				float fovMod = 1.f + (MobilityTreadsArmor.SPEED_BOOST_SLOW + 1) * 0.1f;
				if (MobilityTreadsArmor.localPlayerSpeedBoosting) {
					fovMod = 1.f + (MobilityTreadsArmor.SPEED_BOOST_FAST + 1) * 0.1f;
				}
				
				float calcFov = event.getNewFovModifier();
				//try to avoid altering FOV but allow other legitimate FOV sources
				float fovAccountingForSpeed = (event.getNewFovModifier() / fovMod);
				if (fovAccountingForSpeed <= 1.182f) {
					calcFov = 1.0f;
				} else {
					BioMech.LOGGER.info("adjust fov=" + fovAccountingForSpeed);
				}

				//BioMech.LOGGER.info("fov calc=" + calcFov + " vs " + event.getNewFovModifier());
				event.setNewFovModifier(calcFov);
			}
		}
	}
    
    @SubscribeEvent
    public void onCommand(RegisterCommandsEvent event) {
		BioMechCommand.register(event.getDispatcher());
	}
    
    public void commonSetup(final FMLCommonSetupEvent event)
    {
        AzureLib.initialize();
    }
   
    @SuppressWarnings("deprecation")
	public static void resetBobView() {
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {
			@Override
			public void run() {
				if (originalBobView != null) {
		    		Minecraft.getInstance().options.bobView().set(originalBobView);
		    		BioMech.originalBobView = null;
		    	}
			}});
    	
    }
    
    public static Boolean originalBobView = null;
    @SubscribeEvent
    public void onStopServer(ServerStoppedEvent event) {
    	BioMech.resetBobView();
		lootBioMechInChest = null;
		lootItemsToAdd.clear();
		lootPoolChances.clear();
    }
    
    private static TagKey<Item> pickaxeBlockTag = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("minecraft", "pickaxes"));
    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event) {
    	BioMech.LOGGER.info("item crafted");
    	ItemStack crafted = event.getCrafting();
    	Container craftingGrid = event.getInventory();
    	if (crafted.is(BioMechRegistry.ITEM_BIOMECH_SCRAP.get())) {
			ItemStack pickaxeItem = null;
			int itemCount = 0;
			int pickaxeIndex = -1;
			for (int i = 0; i < craftingGrid.getContainerSize(); ++i) {
				ItemStack matrixItem = craftingGrid.getItem(i);
				if (!matrixItem.isEmpty())
					++itemCount;
				if (matrixItem.is(pickaxeBlockTag)) {
					pickaxeItem = matrixItem;
					pickaxeIndex = i;
				}
			}
			
			if (itemCount == 2) {
				if (pickaxeItem != null) {
					//use forbidden black magic to give back the pickaxe
					((RemainingPickaxeItem)BioMechRegistry.ITEM_REMAINING_PICKAXE.get()).remaining = pickaxeItem;
					craftingGrid.setItem(pickaxeIndex, new ItemStack(BioMechRegistry.ITEM_REMAINING_PICKAXE.get()));
				}
			}
		}
    }
    
    Float lootBioMechInChest = null;
    Float lootBioMechInMineshaft = null;
    Float lootBioMechInDungeon = null;
    Float lootBioMechInAncientCity = null;
    Float lootBioMechInShipwreck = null;
    Float lootBioMechInNetherFortress = null;
    List<Item> lootItemsToAdd = new ArrayList<>();
    class AddToLootPool {
		String lootTag;
		Float lootChance;
		String lootTablePath;
		boolean gotValueFromConfig = false;
	}
	
	List<AddToLootPool> lootPoolChances = new ArrayList<>();
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
    		lootBioMechInAncientCity = (float) BioMechServerConfig.defaultDungeonLootChance;
    		lootBioMechInShipwreck = (float) BioMechServerConfig.defaultDungeonLootChance;
    		lootBioMechInNetherFortress = (float) BioMechServerConfig.defaultDungeonLootChance;
    		
    		{
    			AddToLootPool atlp = new AddToLootPool();
    			atlp.lootTag = "lootBioMechInChest";
    			atlp.lootChance = lootBioMechInChest;
    			atlp.lootTablePath = "*";
    			lootPoolChances.add(atlp);
    		}
    		{
    			AddToLootPool atlp = new AddToLootPool();
    			atlp.lootTag = "lootBioMechInMineshaft";
    			atlp.lootChance = lootBioMechInMineshaft;
    			atlp.lootTablePath = "chests/abandoned_mineshaft";
    			lootPoolChances.add(atlp);
    		}
    		{
    			AddToLootPool atlp = new AddToLootPool();
    			atlp.lootTag = "lootBioMechInDungeon";
    			atlp.lootChance = lootBioMechInDungeon;
    			atlp.lootTablePath = "chests/simple_dungeon";
    			lootPoolChances.add(atlp);
    		}
    		{
    			AddToLootPool atlp = new AddToLootPool();
    			atlp.lootTag = "lootBioMechInAncientCity";
    			atlp.lootChance = lootBioMechInAncientCity;
    			atlp.lootTablePath = "chests/ancient_city";
    			lootPoolChances.add(atlp);
    		}
    		{
    			AddToLootPool atlp = new AddToLootPool();
    			atlp.lootTag = "lootBioMechInShipwreck";
    			atlp.lootChance = lootBioMechInShipwreck;
    			atlp.lootTablePath = "chests/shipwreck_treasure";
    			lootPoolChances.add(atlp);
    		}
    		{
    			AddToLootPool atlp = new AddToLootPool();
    			atlp.lootTag = "lootBioMechInNetherFortress";
    			atlp.lootChance = lootBioMechInNetherFortress;
    			atlp.lootTablePath = "chests/nether_bridge";
    			lootPoolChances.add(atlp);
    		}

    		boolean addElytraChestToLootPool = true;
    		if (tag != null) {
    			for (AddToLootPool atlp : lootPoolChances) {
    				if (tag.contains(atlp.lootTag)) {
        				atlp.lootChance = (float) tag.getDouble(atlp.lootTag);
        				BioMech.LOGGER.info("Got " + atlp.lootTag + " value of " + atlp.lootChance + " from file");
        				atlp.gotValueFromConfig = true;
    				}
    			}
    			
    			addElytraChestToLootPool = tag.getBoolean("ElytraMechChestplateCanBeLooted");
    		}
    		
    		for (AddToLootPool atlp : lootPoolChances) {
				if (!atlp.gotValueFromConfig) {
					BioMech.LOGGER.info("Using default lootBioMechInChest value of " + atlp.lootChance);
				}
			}
    		
    		Field[] allFields = BioMechRegistry.class.getDeclaredFields();
			for (Field f : allFields) {
				if (f.getType() == RegistryObject.class) {
					try {
						RegistryObject value = (RegistryObject) f.get(null);
						if (value != null) {
							if (value.get() instanceof ArmorBase ab) {
								if (ab.shouldAddToLootTable()) {
									boolean shouldAdd = true;
									if (ab instanceof ElytraMechChestplateArmor armor) {
										if (!addElytraChestToLootPool) {
											shouldAdd = false;
										}
									}
									if (shouldAdd) {
										lootItemsToAdd.add(ab);
										LOGGER.debug("Added loot is: " + value.get());
									}
								}
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						LOGGER.error("Error registering items with creative tab", e);
					}
				}
			}
			
    	}
		
    	if (event.getName().getPath().contains("chests")) {
    		String mineshaftText = "";
    		float chance = 0.0f;
    		for (AddToLootPool atlp : lootPoolChances) {
    			if ("*".equals(atlp.lootTablePath)) {
        			chance = atlp.lootChance;
        		} else if (atlp.lootTablePath.equals(event.getName().getPath())) {
        			chance = atlp.lootChance;
        			mineshaftText = " " + atlp.lootTablePath.substring(7);
        		}
    		}

    		LOGGER.debug("alter" + mineshaftText + " loot table: " + event.getTable().getLootTableId().getPath() + " with chance: " + chance);
    		
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
	
	private ItemStack elytraItemStackC = new ItemStack(Items.ELYTRA);
	private ItemStack tempChestItemC = null;
    public static int RESYNC_ENERGY_TICK_PERIOD = 60;
    public static Map<UUID, HandActiveStatus> handActiveMap = new HashMap<>();
    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent event) {
    	if (event.player == null) {
    		return;
    	}
    	
    	if (event.phase == TickEvent.Phase.START) {
    		BioMechPlayerData playerData = globalPlayerData.get(event.player.getUUID());
    		if (playerData != null) {
    			
    			playerData.tickEnergy(event.player);
    			tickInventoryForPlayer(event.player, playerData);
    			tickHandsForPlayer(event.player, playerData);
    			
    			HandActiveStatus has = BioMech.handActiveMap.get(event.player.getUUID());
    			checkForMidairJump(event.player, has);
    			
    			if (event.player.level().isClientSide && event.player.isLocalPlayer()) {
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
				
				if (playerData.getForSlot(MechPart.Chest).itemStack.getItem() instanceof ElytraMechChestplateArmor armor) {
					if (event.player.level().isClientSide) {
						elytraItemStackC.setDamageValue(0);
						tempChestItemC = event.player.getItemBySlot(EquipmentSlot.CHEST);
						event.player.setItemSlot(EquipmentSlot.CHEST, elytraItemStackC);
					}
				}
    		}
    	}
    	if (event.phase == TickEvent.Phase.END) {
    		if (tempChestItemC != null) {
    			if (event.player.level().isClientSide) {
    				event.player.setItemSlot(EquipmentSlot.CHEST, tempChestItemC);
    				tempChestItemC = null;
    			}
    		}
    	}
    }

    public static class MidAirJumpStatus {
    	public boolean primedForMidAirJump = false;
    	public boolean lastJumpActiveStatus = false;
    }
    public static Map<UUID, MidAirJumpStatus> primedForMidAirJumpMap = new HashMap<>();
    private void checkForMidairJump(Player player, HandActiveStatus has) {
    	MidAirJumpStatus maj = primedForMidAirJumpMap.computeIfAbsent(player.getUUID(), (uuid) -> new MidAirJumpStatus());
    	
    	if (player.onGround()) {
    		maj.primedForMidAirJump = false;
			//primedForMidairJump = false;
		} else if (!has.jumpActive && maj.lastJumpActiveStatus) {
			maj.primedForMidAirJump = true;
			//primedForMidairJump = true;
		}
    	
    	maj.lastJumpActiveStatus = has.jumpActive;
	}

	//The concept of a first person arm itemstack only exists on the client for the local player
    //On the server, the passed in itemStack is returned unmodified
    @SuppressWarnings("deprecation")
	public ItemStack getFirstPersonArmItemStack(BioMechPlayerData playerData, ItemStack itemStack, MechPart handPart) {
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
						ish.itemStack = playerData.getForSlot(handPart).clientTempHandStack;
					else
						ish.itemStack = playerData.getForSlot(handPart).clientTempHandStack;
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
					itemStack = getFirstPersonArmItemStack(playerData, itemStack, handPart);
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
    	if (itemStack == null) {
    		return;
    	}
    	if (itemStack.getOrCreateTag().contains(AzureLib.ITEM_UUID_TAG)) {
    		AzAnimator<ItemStack> anim = AzAnimatorAccessor.getOrNull(itemStack);
        	if (anim != null) {
        		command.actions().forEach(action -> action.handle(AzDispatchSide.CLIENT, anim));
        	}
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
	
	@SubscribeEvent
	public void onCriticalHit(CriticalHitEvent event) {
		if (!event.isVanillaCritical()) {
			BioMechPlayerData playerData = null;
	    	playerData = globalPlayerData.get(event.getEntity().getUUID());
	    	if (playerData != null) {
	    		SlottedItem headSlot = playerData.getForSlot(MechPart.Head);
	    		if (headSlot.itemStack.getItem() instanceof PowerHelmetArmor power) {
	    			float powerCritChance = 0.33f;
	    			double rnd = Math.random();
	    			if (rnd < powerCritChance) {
	    				event.setResult(Result.ALLOW);
	    				BioMech.LOGGER.debug("power helmet critical proc");
	    			}
	    		}
	    	}
		}
	}
	
	@SubscribeEvent
	public void onPlayerDealDamage(LivingAttackEvent event) {
		if (event.getSource().getDirectEntity() instanceof Player player) {
			if (!player.level().isClientSide) {
				if (event.getEntity() != null && event.getSource().type() == player.level().damageSources().playerAttack(player).type()) {
					BioMechPlayerData playerData = null;
		        	playerData = globalPlayerData.get(player.getUUID());
		        	if (playerData != null) {
		        		MechPart[] parts = { MechPart.RightArm, MechPart.LeftArm };
		        		for (MechPart part : parts) {
		        			ItemStack itemStack = playerData.getForSlot(part).itemStack;
			        		if (itemStack.getItem() instanceof PowerArmArmor arm) {
			        			boolean active = false;
			        			CompoundTag tag = itemStack.getOrCreateTag();
			        			if (tag.contains("ActiveArm")) {
			        				active = tag.getBoolean("ActiveArm");
			        			}
			        			if (!active) {
			        				if (!event.getEntity().isDeadOrDying() && !event.getEntity().isInvulnerable() && event.getEntity().attackable() && event.getEntity().invulnerableTime <= 0) {
			        					tag.putBoolean("ActiveArm", true);
					        			event.getEntity().hurt(player.level().damageSources().source(BioMechRegistry.BIOMECH_BONUS_DAMAGE), 2.0f);
					        			event.getEntity().invulnerableTime = 0;
					        			event.getEntity().hurtDuration = 0;
				        				event.getEntity().hurtTime = 0;
					        			tag.putBoolean("ActiveArm", false);
			        				}
			        			}
			        		}
		        		}
		        	}
				}
			}			
		}
	}
	
	//LivingHurtEvent - damage before armor/magic mitigation
	//LivingDamageEvent - damage after armor/magic mitigation
	//event incoming damage is reduced by mitigation from armor
	@SubscribeEvent
	public void onPlayerDamage(final LivingDamageEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			//BioMech.LOGGER.info("damage to non-player: " + event.getEntity() + " in amount of " + event.getAmount() + " of type=" + event.getSource());
		}
		if (event.getEntity() instanceof Player player) {
			BioMechPlayerData playerData = null;
        	playerData = globalPlayerData.get(event.getEntity().getUUID());
        	if (playerData != null) {
        		boolean hasAnyDamageAbsorb = false;
        		boolean hasAnyDamageAvoid = false;
        		List<SlottedItem> slottedItems = playerData.getAllSlots();
				for (SlottedItem slotted : slottedItems) {
					if (!slotted.itemStack.isEmpty()) {
						if (slotted.itemStack.getItem() instanceof ArmorBase base) {
							if (base.getDamageAvoidPercent() > 0.0f) {
								hasAnyDamageAvoid = true;
							}
							if (base.getDamageAbsorbPercent() > 0.0f) {
								hasAnyDamageAbsorb = true;
							}
							
							boolean cancel = base.onPlayerDamageTaken(event.getSource(), event.getAmount(), slotted.itemStack, player, slotted.mechPart);
							if (cancel) {
								event.setCanceled(true);
								return;
							}
						}
					}
				}
				
				//for global damage reduction or avoids
				if (hasAnyDamageAvoid) {
					if (!player.level().isClientSide) {
						if (playerData.getSuitEnergy() >= PipeMechBodyArmor.energyLostFromAvoidAttack) {
							if (PipeMechBodyArmor.avoidDirectAttack(PipeMechBodyArmor.getTotalDamageAvoidPct(player), event.getSource(), event.getAmount(), player)) {
								//call internalSpendSuitEnergy directly because this code does not ever run twice - it only runs on server-side whether it's CLIENT or DEDICATED_SERVER
								playerData.internalSpendSuitEnergy(player, PipeMechBodyArmor.energyLostFromAvoidAttack);
								BioMech.LOGGER.debug("avoided damage amount = " + event.getAmount() + " with avoid chance: " + PipeMechBodyArmor.getTotalDamageAvoidPct(player));
								if (event.getEntity() instanceof ServerPlayer sp) {
									BioMechNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), new ClientboundEnergySyncPacket(playerData.getSuitEnergy(), playerData.suitEnergyMax));
								}
								event.setCanceled(true);
								return;
							}
						}
					}
				}
				
				if (hasAnyDamageAbsorb) {
					float absorbPct = IronMechChestArmor.getTotalDamageAbsorbPct(player);
					float damageMitigated = IronMechChestArmor.getDamageMitigated(absorbPct, event.getAmount());
					float damageAfterMitigation = IronMechChestArmor.getDamageAfterMitigation(event.getAmount(), damageMitigated);
					float energyDamage = IronMechChestArmor.getEnergyDamageForAttack(damageMitigated);
					if (playerData.getSuitEnergy() >= energyDamage) {
						if (IronMechChestArmor.absorbDirectAttack(playerData, absorbPct, event.getSource(), event.getAmount(), player)) {
							playerData.spendSuitEnergy(player, energyDamage);
							//BioMech.LOGGER.debug("take damage: " + damageAfterMitigation + ", deal damage to energy: " + energyDamage + ", unmitigated damage was: " + event.getAmount() + " of type: " + event.getSource() + ", energyLeft=" + playerData.getSuitEnergy());
							event.setCanceled(true);
						}
					}
				}
				
        	}
		}
	}
    
	public static ItemStack currentRenderItemStackContext = null;
	
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
						
						int ticksSinceRecalculate = Minecraft.getInstance().player.tickCount - BioMechStationMenu.RECALCULATE_TICK;
						if (suitEnergyPct <= visibleThreshold || ticksSinceRecalculate <= 100) {
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
                			if (handPart == MechPart.RightArm && !ItemStack.isSameItem(playerData.getForSlot(handPart).clientTempHandStack, playerData.getForSlot(handPart).itemStack)) {
                				playerData.getForSlot(handPart).clientTempHandStack = new ItemStack(playerData.getForSlot(handPart).itemStack.getItem());
                			} else if (handPart == MechPart.LeftArm && !ItemStack.isSameItem(playerData.getForSlot(handPart).clientTempHandStack, playerData.getForSlot(handPart).itemStack)) {
                				playerData.getForSlot(handPart).clientTempHandStack = new ItemStack(playerData.getForSlot(handPart).itemStack.getItem());
                			}
                			ItemStack newRenderItem = playerData.getForSlot(handPart).clientTempHandStack;
                			if (handPart == MechPart.LeftArm) {
                				newRenderItem = playerData.getForSlot(handPart).clientTempHandStack;
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
        
        public static Map<EquipmentSlot, ItemStack> priorItems = new HashMap<>();
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
//        		    				if (slottedItem.mechPart == MechPart.RightArm && as.hasMainHandActivator) {
//        		    					poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
//        		    				}
//        		    				if (slottedItem.mechPart == MechPart.LeftArm && as.hasOffHandActivator) {
//        		    					poseStack.mulPose(Axis.XP.rotationDegrees(15.0f));
//        		    				}
        		    				if (slottedItem.mechPart == MechPart.RightArm && has.rightHandActive) {
        		    					poseStack.mulPose(Axis.XP.rotationDegrees(renderEntity.getXRot()));
        		    				} else if (slottedItem.mechPart == MechPart.LeftArm && has.leftHandActive) {
        		    					poseStack.mulPose(Axis.XP.rotationDegrees(renderEntity.getXRot()));
        		    				}
        		    				BioMech.currentRenderItemStackContext = itemStackToRender;
                        		    hal.renderArmorPiece(event.getPoseStack(), event.getMultiBufferSource(), renderEntity, equipmentSlot, event.getPackedLight(), armorModel);
                        		    BioMech.currentRenderItemStackContext = null;
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
                        		    	if (base.alwaysHidePlayerHat()) {
                        		    		playerModel.hat.visible = false;
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
        	AzArmorRendererRegistry.register(SpringLoadedLeggingsRenderer::new, BioMechRegistry.ITEM_SPRING_LOADED_LEGGINGS.get());
        	AzArmorRendererRegistry.register(MobilityTreadsRenderer::new, BioMechRegistry.ITEM_MOBILITY_TREADS.get());
        	AzArmorRendererRegistry.register(ElytraMechChestplateRenderer::new, BioMechRegistry.ITEM_ELYTRA_MECH_CHESTPLATE.get());
        	AzArmorRendererRegistry.register(DrillRightArmRenderer::new, BioMechRegistry.ITEM_DRILL_ARM.get());
        	AzArmorRendererRegistry.register(DrillLeftArmRenderer::new, BioMechRegistry.ITEM_DRILL_LEFT_ARM.get());
        	
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
        	
        	//BioMech Station only
        	AzItemRendererRegistry.register(BioMechStationItemRenderer::new, BioMechRegistry.ITEM_BIOMECH_STATION.get());
        	//BioMech Station only
        	
        	//------ Arm items - render item display ------
        	AzItemRendererRegistry.register(DrillItemRenderer::new, BioMechRegistry.ITEM_DRILL_ARM.get());
        	AzItemRendererRegistry.register(MiningLaserItemRenderer::new, BioMechRegistry.ITEM_MINING_LASER_ARM.get());
        	AzItemRendererRegistry.register(PowerArmItemRenderer::new, BioMechRegistry.ITEM_POWER_ARM.get());
        	AzItemRendererRegistry.register(PipeMechArmItemRenderer::new, BioMechRegistry.ITEM_PIPE_MECH_ARM.get());
        	AzItemRendererRegistry.register(IronMechArmItemRenderer::new, BioMechRegistry.ITEM_IRON_MECH_ARM.get());
        	AzItemRendererRegistry.register(DiamondMechArmItemRenderer::new, BioMechRegistry.ITEM_DIAMOND_MECH_ARM.get());
        	//------ Arm items - render item display ------
        	
        	//------ Arms / Animated ------
        	AzIdentityRegistry.register(BioMechRegistry.ITEM_MINING_LASER_ARM.get(), BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get());
        	AzIdentityRegistry.register(BioMechRegistry.ITEM_DRILL_ARM.get(), BioMechRegistry.ITEM_DRILL_LEFT_ARM.get());
        	//------ Arms / Animated ------
        	
        	
        	//------
        	
        	MenuScreens.register(BioMechRegistry.MENU_TYPE_BIOMECH_STATION.get(), BioMechStationScreen::new);
        }
        
    }
}


