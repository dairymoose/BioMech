package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class GrappleArmDispatcher {
	public static final AnimCommand INERT_COMMAND = new AnimCommand("base_controller", "animation.grapple_arm.inert", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand LAUNCH_COMMAND = new AnimCommand("base_controller", "animation.grapple_arm.launch", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	
    public void inert(Entity entity, ItemStack itemStack) {
    	INERT_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void launch(Entity entity, ItemStack itemStack) {
    	LAUNCH_COMMAND.sendForItem(entity, itemStack);
    }
    
}
