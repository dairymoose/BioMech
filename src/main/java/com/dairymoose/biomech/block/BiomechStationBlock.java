 package com.dairymoose.biomech.block;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block_entity.BioMechStationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BioMechStationBlock extends HorizontalDirectionalBlock implements EntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty MULTIBLOCK = BooleanProperty.create("multiblock");
	private static boolean instantTeleportToStation = false;
	public static boolean configWalkToBioMechStation = true;
	
	public BioMechStationBlock(Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MULTIBLOCK, false));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BioMechRegistry.BLOCK_ENTITY_BIOMECH_STATION.get().create(blockPos, blockState);
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity living,
			ItemStack itemStack) {
		super.setPlacedBy(level, blockPos, blockState, living, itemStack);
		if (level.getBlockEntity(blockPos) instanceof BioMechStationBlockEntity be) {
			be.dispatcher.deploy();
		}
		if (!level.isClientSide) {
	         BlockPos abovePos = blockPos.above();
	         level.setBlock(abovePos, blockState.setValue(MULTIBLOCK, true), 3);
	         level.blockUpdated(blockPos, Blocks.AIR);
	         blockState.updateNeighbourShapes(level, blockPos, 3);
	      }
	}
	
	private static Direction getNeighbourDirection(Boolean multipart) {
		if (multipart.booleanValue()) {
			return Direction.DOWN;
		}
		return Direction.UP;
	}
	
	public BlockState updateShape(BlockState p_49525_, Direction p_49526_, BlockState p_49527_, LevelAccessor p_49528_,
			BlockPos p_49529_, BlockPos p_49530_) {
		if (p_49526_ == getNeighbourDirection(p_49525_.getValue(MULTIBLOCK))) {
			if (!(p_49527_.is(this) && p_49527_.getValue(MULTIBLOCK) != p_49525_.getValue(MULTIBLOCK)))
					return Blocks.AIR.defaultBlockState();
		}
		return super.updateShape(p_49525_, p_49526_, p_49527_, p_49528_, p_49529_, p_49530_);
	}
	
	public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
	      if (!level.isClientSide && player.isCreative()) {
	         Boolean multiblock = blockState.getValue(MULTIBLOCK);
	         //if (!multiblock.booleanValue()) {
	            BlockPos neighborPos = blockPos.relative(getNeighbourDirection(multiblock));
	            BlockState neighborState = level.getBlockState(neighborPos);
	            if (neighborState.is(this) && neighborState.getValue(MULTIBLOCK).booleanValue() != multiblock) {
	               level.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 35);
	               level.levelEvent(player, 2001, neighborPos, Block.getId(neighborState));
	            }
	         //}
	      }

	      super.playerWillDestroy(level, blockPos, blockState, player);
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
	public VoxelShape getShape(BlockState blockState, BlockGetter p_220053_2_, BlockPos p_220053_3_,
			CollisionContext p_220053_4_) {
		if (blockState.is(this) && blockState.getValue(MULTIBLOCK).booleanValue()) {
			return Block.box(1.0D, 0.0D, 0.00D, 15.0D, 9.0D, 16.0D);
		}
		return Block.box(1.0D, 0.0D, 0.00D, 15.0D, 16.0D, 16.0D);
	}
	
	@Override
	public VoxelShape getVisualShape(BlockState blockState, BlockGetter p_60480_, BlockPos p_60481_,
			CollisionContext p_60482_) {
		if (blockState.is(this) && blockState.getValue(MULTIBLOCK).booleanValue()) {
			return Shapes.empty();
		}
		return super.getVisualShape(blockState, p_60480_, p_60481_, p_60482_);
	}
	
	public BlockState rotate(BlockState p_54094_, Rotation p_54095_) {
		return p_54094_.setValue(FACING, p_54095_.rotate(p_54094_.getValue(FACING)));
	}

	public BlockState mirror(BlockState p_54091_, Mirror p_54092_) {
		return p_54091_.rotate(p_54092_.getRotation(p_54091_.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54097_) {
		p_54097_.add(FACING, MULTIBLOCK);
	}
	
	private InteractionResult useBlock(BlockState blockState, Level level, BlockPos blockPos, Player player,
			InteractionHand hand, BlockHitResult hitResult) {
		if (configWalkToBioMechStation) {
			if (instantTeleportToStation) {
				player.setPos(blockPos.getCenter().with(Axis.Y, blockPos.getY()));
				float finalYRot = blockState.getValue(FACING).toYRot();
				player.setYRot(finalYRot);
				player.setYHeadRot(finalYRot);
				player.setXRot(22.0f);
			} else {
				BlockEntity blockentity = level.getBlockEntity(blockPos);
				if (blockentity instanceof BioMechStationBlockEntity be) {
					if (be.walkToStationTicks == -1 && be.walkToStationPlayer == null) {
						be.playerStartLoc = player.getPosition(1.0f);
						be.playerStartYRot = player.getYRot();
						be.walkToStationPlayer = player;
						Vec3 blockEntityPos = blockentity.getBlockPos().getCenter().with(Axis.Y, blockentity.getBlockPos().getY());
						double dist = blockEntityPos.distanceTo(be.playerStartLoc);
						be.walkToStationTicksMax = Math.min((int)(dist/3.0 * be.WALK_TO_STATION_TICKS_MAX_DIST), be.WALK_TO_STATION_TICKS_MAX_DIST);
						if (be.walkToStationTicksMax <= 0) {
							be.walkToStationTicksMax = 1;
						}
						be.walkToStationTicks = be.walkToStationTicksMax;
					}
				}
			}
		}
		
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			BlockEntity blockentity = level.getBlockEntity(blockPos);
			if (blockentity instanceof BioMechStationBlockEntity be) {
				player.openMenu(be);
			}

			return InteractionResult.CONSUME;
		}
	}
	
	public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
			InteractionHand hand, BlockHitResult hitResult) {
		if (blockState.is(this) && blockState.getValue(MULTIBLOCK).booleanValue()) {
			BlockPos belowPos = blockPos.below();
			BlockState belowState = level.getBlockState(belowPos);
			if (belowState.is(this) && !belowState.getValue(MULTIBLOCK).booleanValue()) {
				return this.useBlock(belowState, level, belowPos, player, hand, hitResult);
			}
		} else {
			return this.useBlock(blockState, level, blockPos, player, hand, hitResult);
		}
		return InteractionResult.PASS;
	}

}
