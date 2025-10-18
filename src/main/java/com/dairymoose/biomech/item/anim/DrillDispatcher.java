package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class DrillDispatcher {
	public static final AnimCommand PASSIVE_COMMAND = new AnimCommand("base_controller", "animation.drill_arm.passive", AzPlayBehaviors.LOOP);
	public static final AnimCommand START_USING_COMMAND = new AnimCommand("base_controller", "animation.drill_arm.start_using", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand START_USING_3D_COMMAND = new AnimCommand("base_controller", "animation.drill_arm.start_using_third_person", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand MINING_COMMAND = new AnimCommand("base_controller", "animation.drill_arm.mining", AzPlayBehaviors.LOOP);
	public static final AnimCommand MINING_3D_COMMAND = new AnimCommand("base_controller", "animation.drill_arm.mining_third_person", AzPlayBehaviors.LOOP);
	public static final AnimCommand INERT_COMMAND = new AnimCommand("base_controller", "animation.drill_arm.inert", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	
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
