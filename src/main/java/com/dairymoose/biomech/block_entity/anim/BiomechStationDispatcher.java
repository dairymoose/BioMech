package com.dairymoose.biomech.block_entity.anim;

import mod.azure.azurelib.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BioMechStationDispatcher {
    private static final AzCommand DEPLOY_ANIM = AzCommand.create("base_controller", "animation.biomech_station.deploy", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    private final BlockEntity blockEntity;

    public BioMechStationDispatcher(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public void deploy() {
        DEPLOY_ANIM.sendForBlockEntity(blockEntity);
    }
}