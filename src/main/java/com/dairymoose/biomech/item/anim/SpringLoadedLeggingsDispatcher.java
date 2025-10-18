package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class SpringLoadedLeggingsDispatcher {
	public static final AnimCommand BOUNCE_COMMAND = new AnimCommand("base_controller", "animation.spring_loaded_leggings.bounce", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	
	public static final AnimCommand INERT_COMMAND = new AnimCommand("base_controller", "animation.spring_loaded_leggings.inert", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	
    public void bounce(Entity entity, ItemStack itemStack) {
    	BOUNCE_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void inert(Entity entity, ItemStack itemStack) {
    	INERT_COMMAND.sendForItem(entity, itemStack);
    }
    
}
