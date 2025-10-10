package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.BioMech.OutlinedSpawnerInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OpticsUnitArmor extends ArmorBase {

	public OpticsUnitArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.alwaysHidePlayerHat = false;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Head;
	}
	
	public static int SCAN_TICK_PERIOD = 30;
	
	public static int XZ_SIZE = 30;
	public static int Y_SIZE = 10;
	
	public static boolean unopenedChestsOnly = true;
	
	public static boolean canEverHighlightSpawners = true;
	public static boolean canEverHighlightChests = true;

	public static Set<BlockPos> capturedSpawners = new HashSet<>();
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_OPTICS_UNIT.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (player.tickCount % SCAN_TICK_PERIOD == 0) {
						BlockPos pos = player.blockPosition();
						int scanXz = XZ_SIZE;
						int scanY = Y_SIZE;
						for (int x=-scanXz; x<=scanXz; ++x) {
							for (int y=-scanY; y<=scanY; ++y) {
								for (int z=-scanXz; z<=scanXz; ++z) {
									pos = player.blockPosition();
									pos = pos.relative(Axis.X, x).relative(Axis.Y, y).relative(Axis.Z, z);
									BlockState blockState = level.getBlockState(pos);
									BlockEntity blockEntity = level.getBlockEntity(pos);
									if (canEverHighlightSpawners && blockState.is(Blocks.SPAWNER)) {
										if (!capturedSpawners.contains(pos)) {
											capturedSpawners.add(pos);
											BioMech.OutlinedSpawnerInfo info = new BioMech.OutlinedSpawnerInfo();
											info.pos = pos;
											info.type = BioMech.OutlinerType.SPAWNER;
											synchronized (BioMech.outlinedSpawners) {
												BioMech.outlinedSpawners.add(info);
											}
										}
									} else if (canEverHighlightChests && blockEntity instanceof RandomizableContainerBlockEntity rnd) {
										if (!capturedSpawners.contains(pos)) {
											//only highlight the chest if the loot is not yet generated
											if (!unopenedChestsOnly || rnd.lootTable != null) {
												capturedSpawners.add(pos);
												BioMech.OutlinedSpawnerInfo info = new BioMech.OutlinedSpawnerInfo();
												info.pos = pos;
												info.type = BioMech.OutlinerType.CHEST;
												synchronized (BioMech.outlinedSpawners) {
													BioMech.outlinedSpawners.add(info);
												}
											}
										}
									} else {
										//if this location no longer contains a spawner or container, remove it
										if (capturedSpawners.contains(pos)) {
											if (!BioMech.outlinedSpawners.isEmpty()) {
												synchronized (BioMech.outlinedSpawners) {
													List<OutlinedSpawnerInfo> toRemove = new ArrayList<>();
													for (OutlinedSpawnerInfo info : BioMech.outlinedSpawners) {
														if (info.pos != null && info.pos.equals(pos)) {
															toRemove.add(info);
														}
													}
													for (OutlinedSpawnerInfo info : toRemove) {
														BioMech.outlinedSpawners.remove(info);
							            				OpticsUnitArmor.capturedSpawners.remove(info.pos);
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
			}
		}
	}
	
}
	