package com.dairymoose.biomech.item.anim;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehavior;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class AnimCommand {

	public final AzCommand cmd;
	public final String controllerName;
	public final String animationName;
	public final AzPlayBehavior playBehavior;
	
	AnimCommand(String controllerName, String animationName, AzPlayBehavior playBehavior) {
		this.controllerName = controllerName;
		this.animationName = animationName;
		this.playBehavior = playBehavior;
		cmd = AzCommand.create(controllerName, animationName, playBehavior);
	}
	
	public void sendForItem(Entity entity, ItemStack itemStack) {
		cmd.sendForItem(entity, itemStack);
	}
	
}
