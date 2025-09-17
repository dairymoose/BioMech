package com.dairymoose.biomech.block_entity;

import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block_entity.anim.BiomechStationDispatcher;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BiomechStationBlockEntity extends BlockEntity {
	
	public final BiomechStationDispatcher dispatcher;
	
	public static final BlockEntityType<BiomechStationBlockEntity> BIOMECH_STATION_BLOCK_ENTITY = BlockEntityType.Builder.of(BiomechStationBlockEntity::new, BioMechRegistry.BLOCK_BIOMECH_STATION.get()).build(null);
//
//	public static void tick(Level level, BlockPos pos, BlockState state, BiomechStationBlockEntity blockEntity) {
//		if (blockEntity.level != null && level.isClientSide()) {
//            // This is where you now trigger an animation to play
//            blockEntity.dispatcher.deploy();
//        }
//	}
//	
	public BiomechStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BIOMECH_STATION_BLOCK_ENTITY, blockPos, blockState);
		this.dispatcher = new BiomechStationDispatcher(this);
	}

}
