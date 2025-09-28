package com.dairymoose.biomech.block_entity;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block.BioMechStationBlock;
import com.dairymoose.biomech.block_entity.anim.BioMechStationDispatcher;
import com.dairymoose.biomech.menu.BioMechStationMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BioMechStationBlockEntity extends RandomizableContainerBlockEntity {
	
	private NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	public int walkToStationTicksMax = 30;
	public int turnAroundTicksMax = 8;
	public static final int WALK_TO_STATION_TICKS_MAX_DIST = 25;
	
	public int walkToStationTicks = -1;
	private int turnAroundTicks = -1;
	public Player walkToStationPlayer = null;
	
	public float playerStartYRot = 0.0f;
	public Vec3 playerStartLoc = null;
	
	public final BioMechStationDispatcher dispatcher;
	
	public boolean currentlyOpened = false;
	
	public static final BlockEntityType<BioMechStationBlockEntity> BIOMECH_STATION_BLOCK_ENTITY = BlockEntityType.Builder.of(BioMechStationBlockEntity::new, BioMechRegistry.BLOCK_BIOMECH_STATION.get()).build(null);

	@Override
	public void startOpen(Player player) {
		super.startOpen(player);
		currentlyOpened = true;
	}
	
	@Override
	public void stopOpen(Player player) {
		super.stopOpen(player);
		currentlyOpened = false;
	}
	
	@Override
	public boolean canOpen(Player player) {
		if (this.currentlyOpened) {
			return false;
		}
		return true;
	}
	
	public static void tick(Level level, BlockPos blockPos, BlockState blockState, BioMechStationBlockEntity blockEntity) {
		if (blockEntity.level != null && level.isClientSide()) {
            blockEntity.dispatcher.deploy();
        }
		
		if (blockEntity.walkToStationTicks >= 0 && blockEntity.walkToStationPlayer != null) {	
			blockEntity.turnAroundTicks = blockEntity.turnAroundTicksMax;
			
			Vec3 finalLoc = blockPos.getCenter().with(Axis.Y, blockPos.getY());
			Player player = blockEntity.walkToStationPlayer;
			
			double tickPct = (double)(blockEntity.walkToStationTicksMax - blockEntity.walkToStationTicks)/blockEntity.walkToStationTicksMax;
			double d0 = Mth.lerp(tickPct, blockEntity.playerStartLoc.x, finalLoc.x);
			double d1 = Mth.lerp(tickPct, blockEntity.playerStartLoc.y, finalLoc.y);
			double d2 = Mth.lerp(tickPct, blockEntity.playerStartLoc.z, finalLoc.z);
			player.setPos(new Vec3(d0, d1, d2));
			player.fallDistance = 0.0f;

			--blockEntity.walkToStationTicks;
		} else {
			Player player = blockEntity.walkToStationPlayer;
			
			if (blockEntity.turnAroundTicks >= 0 && blockEntity.walkToStationPlayer != null) {
				float finalYRot = blockState.getValue(BioMechStationBlock.FACING).toYRot();
				double tickPct = (double)(blockEntity.turnAroundTicksMax - blockEntity.turnAroundTicks)/blockEntity.turnAroundTicksMax;
				float yRot = (float) Mth.lerp(tickPct, blockEntity.playerStartYRot, finalYRot);
				player.setYRot(yRot);
				player.setYBodyRot(yRot);
				player.setXRot(22.0f);
				
				if (blockEntity.turnAroundTicks == 0) {
					blockEntity.walkToStationPlayer = null;
				}
				
				--blockEntity.turnAroundTicks;
			}
		}
	}
	
	public BioMechStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BIOMECH_STATION_BLOCK_ENTITY, blockPos, blockState);
		this.dispatcher = new BioMechStationDispatcher(this);
	}

	public void load(CompoundTag p_155588_) {
		super.load(p_155588_);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(p_155588_)) {
			ContainerHelper.loadAllItems(p_155588_, this.items);
		}
	}

	protected void saveAdditional(CompoundTag p_187502_) {
		super.saveAdditional(p_187502_);
		if (!this.trySaveLootTable(p_187502_)) {
			ContainerHelper.saveAllItems(p_187502_, this.items);
		}
	}
	
	@Override
	public int getContainerSize() {
		return 6;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> newItems) {
		this.items = newItems;
	}

	@Override
	protected Component getDefaultName() {
		return Component.literal("BioMech Station");
	}

	@Override
	protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
		return new BioMechStationMenu(p_58627_, p_58628_, this);
	}

}
