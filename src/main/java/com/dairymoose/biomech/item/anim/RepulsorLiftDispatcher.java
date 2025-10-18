package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class RepulsorLiftDispatcher {
	public static final AnimCommand PASSIVE_COMMAND = new AnimCommand("base_controller", "animation.repulsor_lift.passive", AzPlayBehaviors.LOOP);

    public void passive(Entity entity, ItemStack itemStack) {
    	PASSIVE_COMMAND.sendForItem(entity, itemStack);
    }

}
