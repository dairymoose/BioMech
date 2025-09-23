package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class MiningLaserDispatcher {
	private static final AzCommand PASSIVE_COMMAND = AzCommand.create("base_controller", "animation.mining_laser_right_arm.passive", AzPlayBehaviors.LOOP);
	private static final AzCommand START_USING_COMMAND = AzCommand.create("base_controller", "animation.mining_laser_right_arm.start_using", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	private static final AzCommand MINING_COMMAND = AzCommand.create("base_controller", "animation.mining_laser_right_arm.mining", AzPlayBehaviors.LOOP);
	private static final AzCommand INERT_COMMAND = AzCommand.create("base_controller", "animation.mining_laser_right_arm.inert", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    public void passive(Entity entity, ItemStack itemStack) {
    	PASSIVE_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void startUsing(Entity entity, ItemStack itemStack) {
    	START_USING_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void mining(Entity entity, ItemStack itemStack) {
    	MINING_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void inert(Entity entity, ItemStack itemStack) {
    	INERT_COMMAND.sendForItem(entity, itemStack);
    }
}
