package com.dairymoose.biomech.block_entity;

import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block_entity.anim.BioMechStationDispatcher;
import com.dairymoose.biomech.menu.BioMechArmorMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BioMechStationBlockEntity extends RandomizableContainerBlockEntity {
	
	private NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	
	public final BioMechStationDispatcher dispatcher;
	
	public static final BlockEntityType<BioMechStationBlockEntity> BIOMECH_STATION_BLOCK_ENTITY = BlockEntityType.Builder.of(BioMechStationBlockEntity::new, BioMechRegistry.BLOCK_BIOMECH_STATION.get()).build(null);

	public static void tick(Level level, BlockPos pos, BlockState state, BioMechStationBlockEntity blockEntity) {
		if (blockEntity.level != null && level.isClientSide()) {
            blockEntity.dispatcher.deploy();
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
		return 5;
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
		return new BioMechArmorMenu(p_58627_, p_58628_, this);
	}

}
