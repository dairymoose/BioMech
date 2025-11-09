package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class TransformerModuleHelicopterDispatcher {
	public static final AnimCommand INACTIVE_COMMAND = new AnimCommand("base_controller", "animation.transformer_module_helicopter.inactive", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand ACTIVE_COMMAND = new AnimCommand("base_controller", "animation.transformer_module_helicopter.active", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    public void inactive(Entity entity, ItemStack itemStack) {
    	INACTIVE_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void active(Entity entity, ItemStack itemStack) {
    	ACTIVE_COMMAND.sendForItem(entity, itemStack);
    }

}
