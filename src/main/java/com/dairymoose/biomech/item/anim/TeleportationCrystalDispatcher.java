package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class TeleportationCrystalDispatcher {
	public static final AnimCommand PASSIVE_COMMAND = new AnimCommand("base_controller", "animation.teleportation_crystal.passive", AzPlayBehaviors.LOOP);
	
    public void passive(Entity entity, ItemStack itemStack) {
    	PASSIVE_COMMAND.sendForItem(entity, itemStack);
    }
    
}
