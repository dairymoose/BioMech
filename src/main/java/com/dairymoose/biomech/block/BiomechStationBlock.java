 package com.dairymoose.biomech.block;

import com.dairymoose.biomech.block_entity.BiomechStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BiomechStationBlock extends Block implements EntityBlock {

	public BiomechStationBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BiomechStationBlockEntity.BIOMECH_STATION_BLOCK_ENTITY.create(blockPos, blockState);
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity p_49850_,
			ItemStack p_49851_) {
		super.setPlacedBy(level, blockPos, blockState, p_49850_, p_49851_);
		if (level.getBlockEntity(blockPos) instanceof BiomechStationBlockEntity be) {
			be.dispatcher.deploy();
		}
	}
	
//	@Override
//	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//		return BioMech.createTickerHelper(type, BiomechStationBlockEntity.BIOMECH_STATION_BLOCK_ENTITY, BiomechStationBlockEntity::tick);
//	}
	
}
