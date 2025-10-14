package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class ExtendoArmDispatcher {
	public static final AnimCommand PASSIVE_COMMAND = new AnimCommand("base_controller", "animation.extendo_arm.passive", AzPlayBehaviors.HOLD_ON_LAST_FRAME);
	public static final AnimCommand WALKING_COMMAND = new AnimCommand("base_controller", "animation.extendo_arm.walking", AzPlayBehaviors.LOOP);

    public void passive(Entity entity, ItemStack itemStack) {
    	PASSIVE_COMMAND.sendForItem(entity, itemStack);
    }

}
