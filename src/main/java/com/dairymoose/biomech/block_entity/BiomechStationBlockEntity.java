package com.dairymoose.biomech.block_entity;

import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.block_entity.anim.BioMechStationDispatcher;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BioMechStationBlockEntity extends BlockEntity {
	
	public final BioMechStationDispatcher dispatcher;
	
	public static final BlockEntityType<BioMechStationBlockEntity> BIOMECH_STATION_BLOCK_ENTITY = BlockEntityType.Builder.of(BioMechStationBlockEntity::new, BioMechRegistry.BLOCK_BIOMECH_STATION.get()).build(null);

	public static void tick(Level level, BlockPos pos, BlockState state, BioMechStationBlockEntity blockEntity) {
		if (blockEntity.level != null && level.isClientSide()) {
            // This is where you now trigger an animation to play
            blockEntity.dispatcher.deploy();
        }
	}
	
	public BioMechStationBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(BIOMECH_STATION_BLOCK_ENTITY, blockPos, blockState);
		this.dispatcher = new BioMechStationDispatcher(this);
	}

}
