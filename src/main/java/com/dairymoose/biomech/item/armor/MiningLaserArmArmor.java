package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.MiningLaserDispatcher;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class MiningLaserArmArmor extends ArmorBase {

	public final MiningLaserDispatcher dispatcher;
	
	public MiningLaserArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new MiningLaserDispatcher();
	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get();
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isLeftArm) {
		if (entity instanceof Player player) {
			//this.dispatcher.mining(player, stack);
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_ARM.get()) || armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (player.getMainHandItem().isEmpty() && stack.getItem() instanceof MiningLaserRightArmArmor) {
						this.dispatcher.passive(entity, stack);
					}
					else if (player.getOffhandItem().isEmpty() && stack.getItem() instanceof MiningLaserLeftArmArmor) {
						this.dispatcher.passive(entity, stack);
					}
					else {
						this.dispatcher.inert(entity, stack);
					}
				}
			}
		}
	}

}
	