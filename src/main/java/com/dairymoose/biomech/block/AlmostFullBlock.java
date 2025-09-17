package com.dairymoose.biomech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface AlmostFullBlock {


	default public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
			CollisionContext p_220053_4_) {
		return Block.box(0.01D, 0.0D, 0.01D, 15.99D, 16.0D, 15.99D);
	}
}