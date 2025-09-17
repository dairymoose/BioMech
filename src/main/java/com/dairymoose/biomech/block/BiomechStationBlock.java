 package com.dairymoose.biomech.block;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BioMechStationBlock extends Block implements EntityBlock, AlmostFullBlock {

	public BioMechStationBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BioMechRegistry.BLOCK_ENTITY_BIOMECH_STATION.get().create(blockPos, blockState);
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity p_49850_,
			ItemStack p_49851_) {
		super.setPlacedBy(level, blockPos, blockState, p_49850_, p_49851_);
		if (level.getBlockEntity(blockPos) instanceof BioMechStationBlockEntity be) {
			be.dispatcher.deploy();
		}
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return BioMech.createTickerHelper(type, BioMechStationBlockEntity.BIOMECH_STATION_BLOCK_ENTITY, BioMechStationBlockEntity::tick);
	}
	
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
			CollisionContext p_220053_4_) {
		return Block.box(0.01D, 0.0D, 0.01D, 15.99D, 16.0D, 15.99D);
	}
	
}
