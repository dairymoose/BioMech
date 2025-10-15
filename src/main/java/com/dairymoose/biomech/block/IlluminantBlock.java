package com.dairymoose.biomech.block;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.item.armor.IlluminatorArmor;
import com.dairymoose.biomech.item.armor.IlluminatorArmor.IlluminantInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;

public class IlluminantBlock extends Block implements SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 8);
	
	public IlluminantBlock(Properties props) {
		super(props.noCollission().noOcclusion().replaceable().lightLevel((state) -> 15));
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(WATER_LEVEL, 0));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED).add(WATER_LEVEL);
		super.createBlockStateDefinition(builder);
	}
	
	@Override
	public ItemStack pickupBlock(LevelAccessor level, BlockPos blockPos, BlockState p_154562_) {
		for (Map.Entry<UUID, List<IlluminantInfo>> entry : IlluminatorArmor.illuminantMap.entrySet()) {
			for (IlluminantInfo info : entry.getValue()) {
				if (info.pos != null && info.pos.equals(blockPos)) {
					BioMech.LOGGER.debug("unset illuminant block at " + info.pos);
					IlluminatorArmor.unsetIlluminantBlock(info.id, entry.getValue(), level, new Vec3(info.pos.getX(), info.pos.getY(), info.pos.getZ()));
					break;
				}
			}
		}
		return SimpleWaterloggedBlock.super.pickupBlock(level, blockPos, p_154562_);
	}
	
	@Override
	public FluidState getFluidState(BlockState blockState) {
		return (Boolean) blockState.getValue(WATERLOGGED)
				? blockState.getValue(WATER_LEVEL) == 0 ? Fluids.WATER.getSource(false) : Fluids.WATER.getFlowing(blockState.getValue(WATER_LEVEL), false)
				: super.getFluidState(blockState);
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getFluidType() == ForgeMod.WATER_TYPE.get());
	}
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return Shapes.empty();
	}

}
