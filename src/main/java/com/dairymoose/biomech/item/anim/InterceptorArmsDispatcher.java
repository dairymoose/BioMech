package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class InterceptorArmsDispatcher {
	public static final AnimCommand PASSIVE_COMMAND = new AnimCommand("base_controller", "animation.interceptor_arms.passive", AzPlayBehaviors.LOOP);
	public static final AnimCommand DEFLECT_COMMAND = new AnimCommand("base_controller", "animation.interceptor_arms.deflect", AzPlayBehaviors.LOOP);
	
    public void passive(Entity entity, ItemStack itemStack) {
    	PASSIVE_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void deflect(Entity entity, ItemStack itemStack) {
    	DEFLECT_COMMAND.sendForItem(entity, itemStack);
    }
    
}
