package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class GatlingDispatcher {
	public static final AnimCommand PASSIVE_COMMAND = new AnimCommand("base_controller", "animation.gatling_arm.inert", AzPlayBehaviors.LOOP);
	public static final AnimCommand SPIN_UP_COMMAND = new AnimCommand("base_controller", "animation.gatling_arm.spin_up", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand SPIN_UP_3D_COMMAND = new AnimCommand("base_controller", "animation.gatling_arm.spin_up_third_person", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand FIRING_COMMAND = new AnimCommand("base_controller", "animation.gatling_arm.firing", AzPlayBehaviors.LOOP);
	public static final AnimCommand FIRING_3D_COMMAND = new AnimCommand("base_controller", "animation.gatling_arm.firing_third_person", AzPlayBehaviors.LOOP);
	public static final AnimCommand INERT_COMMAND = new AnimCommand("base_controller", "animation.gatling_arm.inert", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	
    public void passive(Entity entity, ItemStack itemStack) {
    	PASSIVE_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void spinUp(Entity entity, ItemStack itemStack) {
    	SPIN_UP_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void mining(Entity entity, ItemStack itemStack) {
    	FIRING_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void inert(Entity entity, ItemStack itemStack) {
    	INERT_COMMAND.sendForItem(entity, itemStack);
    }
}
