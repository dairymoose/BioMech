package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PowerChestArmor extends ArmorBase {

	public PowerChestArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 100;
		this.suitEnergyPerSec = 1.0f;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Chest;
	}

	public static int SECONDS_BETWEEN_PROC = 30;
	
	public static final int TICKS_PER_SEC = 20;
	
	@Override
	public boolean onPlayerDamageTaken(DamageSource damageSource, float amount, ItemStack itemStack, Player player, MechPart handPart) {
		boolean cancel = false;

		if (PipeMechBodyArmor.damageSourceIsDirect(damageSource, player) && amount > 0.0f && !player.level().isClientSide) {
			int lastProc = -1;
			CompoundTag tag = itemStack.getOrCreateTag();
			if (tag.contains("LastProc")) {
				lastProc = tag.getInt("LastProc");
			}
			int tickDiff = player.tickCount - lastProc;
			if (lastProc == -1 || tickDiff < 0 || tickDiff >= (SECONDS_BETWEEN_PROC*TICKS_PER_SEC)) {
				tag.putInt("LastProc", player.tickCount);
				float healAmount = amount * 0.5f;
				BioMech.LOGGER.debug("power armor proc for player=" + player.getName().getString() + " at tick=" + player.tickCount + ", heal=" + healAmount);
				player.level().playSound(null, player.blockPosition(), SoundEvents.CHAIN_BREAK, SoundSource.PLAYERS, 2.0f, 1.2f);
				//player.heal(healAmount);
				return IronMechChestArmor.absorbDirectAttack(null, 0.50f, damageSource, amount, player, false);
			}
		}
		
		return cancel;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_POWER_CHEST.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}
	
}
