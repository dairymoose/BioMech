package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.BroadcastType;
import com.dairymoose.biomech.ToggledStatus;
import com.dairymoose.biomech.block.IlluminantBlock;
import com.dairymoose.biomech.item.anim.IlluminatorDispatcher;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class IlluminatorArmor extends ArmorBase {

	private final IlluminatorDispatcher dispatcher;
	
	public IlluminatorArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.mechPart = MechPart.Head;
		this.dispatcher = new IlluminatorDispatcher();
	}

	public static class IlluminantInfo {
		public BlockState oldBlockState = null;
		public BlockPos pos = null;
		public int id = -1;
	}
	
	public static long updateTickPeriod = 1;
	
	private Map<UUID, ToggledStatus> toggledOnMap = new HashMap<>();
	@Override
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown, int bonusData, boolean serverOriginator) {
		if (keyIsDown) {
			ToggledStatus status = toggledOnMap.get(player.getUUID());
			if (status != null) {
				if (bonusData == -1) {
					if (FMLEnvironment.dist == Dist.CLIENT) {
						if (player.level().isClientSide) {
							status.toggledOn = !status.toggledOn;
						}
					} else {
						status.toggledOn = !status.toggledOn;
					}
				} else {
					status.toggledOn = (bonusData == 1 ? true : false);
				}
				
				this.sendHotkeyToServer(player, keyIsDown, status.toggledOn ? 1 : 0, BroadcastType.SEND_TO_ALL_CLIENTS, serverOriginator);
			}
		}
		
		super.onHotkeyPressed(player, playerData, keyIsDown, bonusData, serverOriginator);
	}
	
	public static Map<UUID, List<IlluminantInfo>> illuminantMap = new HashMap<>();
	public static double illuminatorScale = 7.0;
	public static int illuminatorForwardBlocks = 5; //the first block will always be on top of the player (or 1-3 blocks forward)
	//the rest of the blocks will fan out in a cone
	public static int illuminatorConeBlocks = 2; //multiple of 2, 1 on each side
	public static float coneAngle = 15.0f;
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_ILLUMINATOR.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					if (level.isClientSide) {
						BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

						if (playerData != null) {
						
							if (playerData.tickCount % updateTickPeriod == 0) {
								ToggledStatus status = toggledOnMap.computeIfAbsent(player.getUUID(), (uuid) -> new ToggledStatus(true));
								
								if (!status.toggledOn) {
									BioMech.clientSideItemAnimation(itemStack, dispatcher.OFF_COMMAND.cmd);
									BioMech.removeIlluminantBlocks(player, playerData);
									return;
								}
								BioMech.clientSideItemAnimation(itemStack, dispatcher.ON_COMMAND.cmd);
								
								List<IlluminantInfo> infos = illuminantMap.computeIfAbsent(player.getUUID(), (uuid) -> new ArrayList<IlluminantInfo>());
								if (infos.isEmpty()) {
									int blocksCount = illuminatorForwardBlocks*(illuminatorConeBlocks + 1);
									for (int i=0; i<blocksCount; ++i) {
										infos.add(new IlluminantInfo());
									}
								}
								
								int currentId = -1;
								for (int i=0; i<illuminatorForwardBlocks; ++i) {
									++currentId;
									
									double currentScale = illuminatorScale*(i);
									Vec3 loc = player.getEyePosition().add(player.getViewVector(1.0f).scale(currentScale));
									if (i == 0) {
										//aim for the blockPos 3 blocks in front of the player
										//we do this to try to avoid flickering that occurs with certain shaders
										
										BlockPos capturedPositions[] = new BlockPos[3];
										int blocksForwardCount = 3;
										for (int b=0; b<blocksForwardCount; ++b) {
											BlockPos originalPos = BlockPos.containing(loc);
											BlockPos targetedPos = BlockPos.containing(loc);
											
											while (targetedPos.equals(originalPos)) {
												loc = loc.add(player.getViewVector(1.0f).scale(0.10));
												targetedPos = BlockPos.containing(loc);
											}
											capturedPositions[b] = targetedPos;
										}
										if (!locationCanBeIlluminated(level, capturedPositions[2].getCenter())) {
											loc = capturedPositions[1].getCenter();
											if (!locationCanBeIlluminated(level, capturedPositions[1].getCenter())) {
												loc = capturedPositions[0].getCenter();
											}
										}
									}
									recalcIlluminantBlock(level, infos, currentId, player, loc);
									
									if (i != 0) {
										for (int c=0; c<illuminatorConeBlocks/2; ++c) {
											Vec3 rightLoc = player.getEyePosition().add(player.getViewVector(1.0f).yRot(Mth.DEG_TO_RAD*coneAngle/(c+1)).scale(currentScale));
											Vec3 leftLoc = player.getEyePosition().add(player.getViewVector(1.0f).yRot(Mth.DEG_TO_RAD*-coneAngle/(c+1)).scale(currentScale));
											++currentId;
											recalcIlluminantBlock(level, infos, currentId, player, rightLoc);
											++currentId;
											recalcIlluminantBlock(level, infos, currentId, player, leftLoc);
										}
									}
								}
							}
							
						}
					}
				}
			}
		}
	}
	private void recalcIlluminantBlock(Level level, List<IlluminantInfo> infos, int currentId, Player player, Vec3 loc) {
		Vec3 originalLoc = loc;
		Vec3 startLoc = player.getEyePosition();
		
		Vec3 vecToEnd = originalLoc.subtract(startLoc);

		int tries = 10;
		//loop tries+1 times so that we always try scale of 0.0
		for (int i=0; i<=tries; ++i) {
			loc = startLoc.add(vecToEnd.scale((tries-i)*(1.0f/tries)));
			
			boolean didSet;
			if (locationCanBeIlluminated(level, loc)) {
				didSet = true;
				setIlluminantBlock(currentId, infos, level, loc);
			}
			else {
				didSet = false;
				unsetIlluminantBlock(currentId, infos, level, loc);
			}
			if (didSet)
				break;
		}
	}
	
	private boolean locationCanBeIlluminated(Level level, Vec3 loc) {
		BlockPos blockPos = BlockPos.containing(loc);
		BlockState currentState = level.getBlockState(blockPos);
		return currentState.is(BioMechRegistry.BLOCK_ILLUMINANT_BLOCK.get()) || currentState.isAir() || currentState.is(Blocks.WATER);
	}
	
	private void setIlluminantBlock(int id, List<IlluminantInfo> infos, Level level, Vec3 loc) {
		BlockPos blockPos = BlockPos.containing(loc);
		
		this.unsetIlluminantBlock(id, infos, level, loc);
		
		IlluminantInfo currentInfo = infos.get(id);
		BlockState currentState = level.getBlockState(blockPos);
		if (!currentState.is(BioMechRegistry.BLOCK_ILLUMINANT_BLOCK.get())) {
			currentInfo.id = id;
			currentInfo.oldBlockState = currentState;
			currentInfo.pos = blockPos;
		}
		
		FluidState fluidState = level.getFluidState(blockPos);
		level.setBlock(blockPos, BioMechRegistry.BLOCK_ILLUMINANT_BLOCK.get().defaultBlockState()
				.setValue(IlluminantBlock.WATERLOGGED, fluidState.getFluidType() == ForgeMod.WATER_TYPE.get())
				.setValue(IlluminantBlock.WATER_LEVEL, fluidState.isSource() ? 0 : fluidState.getAmount()), 0);
	}
	
	public static void unsetIlluminantBlock(int id, List<IlluminantInfo> infos, LevelAccessor level, Vec3 loc) {
		IlluminantInfo currentInfo = infos.get(id);
		if (currentInfo.pos != null) {
			BlockState currentState = level.getBlockState(currentInfo.pos);
			if (currentState.is(BioMechRegistry.BLOCK_ILLUMINANT_BLOCK.get())) {
				level.setBlock(currentInfo.pos, currentInfo.oldBlockState, 0);
			}
			currentInfo.pos = null;
		}
	}
	
}
