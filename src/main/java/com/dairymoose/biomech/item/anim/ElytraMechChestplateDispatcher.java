package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class ElytraMechChestplateDispatcher {
	public static final AnimCommand IDLE_COMMAND = new AnimCommand("base_controller", "animation.elytra_mech_chestplate.idle", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand FLY_COMMAND = new AnimCommand("base_controller", "animation.elytra_mech_chestplate.fly", AzPlayBehaviors.HOLD_ON_LAST_FRAME);

    public void idle(Entity entity, ItemStack itemStack) {
    	IDLE_COMMAND.sendForItem(entity, itemStack);
    }
    
    public void fly(Entity entity, ItemStack itemStack) {
    	FLY_COMMAND.sendForItem(entity, itemStack);
    }

}
