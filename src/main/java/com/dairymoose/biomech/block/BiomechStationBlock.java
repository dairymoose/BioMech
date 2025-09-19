 package com.dairymoose.biomech.block;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BioMechStationBlock extends Block implements EntityBlock, AlmostFullBlock {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public BioMechStationBlock(Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
	
	public BlockState rotate(BlockState p_54094_, Rotation p_54095_) {
		return p_54094_.setValue(FACING, p_54095_.rotate(p_54094_.getValue(FACING)));
	}

	public BlockState mirror(BlockState p_54091_, Mirror p_54092_) {
		return p_54091_.rotate(p_54092_.getRotation(p_54091_.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54097_) {
		p_54097_.add(FACING);
	}
	
	public InteractionResult use(BlockState p_54071_, Level p_54072_, BlockPos p_54073_, Player p_54074_,
			InteractionHand p_54075_, BlockHitResult p_54076_) {
		if (p_54072_.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockentity = p_54072_.getBlockEntity(p_54073_);
			if (blockentity instanceof BioMechStationBlockEntity be) {
				p_54074_.openMenu(be);
			}

			return InteractionResult.CONSUME;
		}
	}

}
