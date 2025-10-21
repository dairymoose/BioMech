package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class IlluminatorDispatcher {
	public static final AnimCommand ON_COMMAND = new AnimCommand("base_controller", "animation.illuminator.on", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand OFF_COMMAND = new AnimCommand("base_controller", "animation.illuminator.off", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    public void on(Entity entity, ItemStack itemStack) {
    	ON_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void off(Entity entity, ItemStack itemStack) {
    	OFF_COMMAND.sendForItem(entity, itemStack);
    }
    
}
