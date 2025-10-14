package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.packet.clientbound.ClientboundEnergySyncPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class IronMechChestArmor extends ArmorBase {

	public IronMechChestArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 40;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Chest;
		this.armDistance = 8.0f;
		this.addToLootTable = false;
		this.damageAbsorbPct = 0.04f;
		this.backArmorTranslation = 0.1;
	}

	public static float getAverageAbsorbedDamageEnergyMult(Player player) {
		float absorbMultTotal = 0.0f;
		
		int absorbItems = 0;
		BioMechPlayerData playerData = null;
    	playerData = BioMech.globalPlayerData.get(player.getUUID());
    	if (playerData != null) {
    		List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						if (base.damageAbsorbPct > 0.0f) {
							absorbMultTotal += base.absorbedDamageEnergyMult;
							++absorbItems;
						}
					}
				}
			}
    	}
    	
    	return absorbMultTotal/absorbItems;
	}
	
	public static float getEnergyDamageForAttack(float damageMitigated, float absorbedDamageMult) {
		float energyDamage = damageMitigated * absorbedDamageMult;
		return energyDamage;
	}
	
	public static float getDamageMitigated(float absorbPct, float amount) {
		float damageMitigated = absorbPct * amount;
		return damageMitigated;
	}
	
	public static float getDamageAfterMitigation(float amount, float damageMitigated) {
		float damageAfterMitigation = amount - damageMitigated;
		return damageAfterMitigation;
	}
	
	public static boolean absorbDirectAttack(BioMechPlayerData playerData, float absorbedDamageMult, float absorbPct, DamageSource damageSource, float amount, Player player, boolean consumesEnergy) {
		if (!damageSource.getMsgId().equals(player.level().damageSources().source(BioMechRegistry.BIOMECH_ABSORB).getMsgId()) && PipeMechBodyArmor.damageSourceIsDirect(amount, damageSource, player)) {
			float damageMitigated = IronMechChestArmor.getDamageMitigated(absorbPct, amount);
			float damageAfterMitigation = IronMechChestArmor.getDamageAfterMitigation(amount, damageMitigated);
			//player.hurt(new DamageSource(Holder.direct(bioMechAbsorbDamageType)), damageAfterMitigation);
			float unmitigatedPredictedHp = player.getHealth() - amount;
			float mitigatedPredictedHp = player.getHealth() - damageAfterMitigation;
			if (unmitigatedPredictedHp <= 0.0f && mitigatedPredictedHp > 0.0f) {
				//prevent the damage entirely and apply biomech_absorb damage
				//this case only occurs if the damage would immediately kill the player - but we predicted they should be saved instead
				player.hurt(player.level().damageSources().source(BioMechRegistry.BIOMECH_ABSORB), damageAfterMitigation);
				BioMech.LOGGER.debug("inflict: " + damageAfterMitigation + " damage, avoid killing blow with Absorb, damage source was: " + damageSource + " with amount=" + amount + " unmitigatedHp=" + unmitigatedPredictedHp + ", mitigated=" + mitigatedPredictedHp + "/currentHp=" + player.getHealth());
				float energyDamage = IronMechChestArmor.getEnergyDamageForAttack(damageMitigated, absorbedDamageMult);
				if (consumesEnergy) {
					playerData.spendSuitEnergy(player, energyDamage);
					if (player instanceof ServerPlayer sp) {
						BioMechNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), new ClientboundEnergySyncPacket(playerData.getSuitEnergy(), playerData.suitEnergyMax, playerData.remainingTicksForEnergyRegen()));
					}
				}

				return true;
			} else {
				float energyDamage = IronMechChestArmor.getEnergyDamageForAttack(damageMitigated, absorbedDamageMult);
				//damage is only processed here on server side
				if (consumesEnergy) {
					if (playerData != null) {
						playerData.internalSpendSuitEnergy(player, energyDamage);
					}
					
					if (player instanceof ServerPlayer sp) {
						BioMechNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sp), new ClientboundEnergySyncPacket(playerData.getSuitEnergy(), playerData.suitEnergyMax, playerData.remainingTicksForEnergyRegen()));
					}
				}
				//BioMech.LOGGER.debug("heal amount = " + damageMitigated + " from raw damage = " + amount + " with energyDamage=" + energyDamage);
				player.heal(damageMitigated);
				return false;
			}
		} else {
			//BioMech.LOGGER.info("INDIRECT damage source was: " + damageSource + " with amount=" + amount);
		}
		
		return false;
	}
	
	public static float getTotalDamageAbsorbPct(Player player) {
		float absorbPct = 0.0f;
		
		BioMechPlayerData playerData = null;
    	playerData = BioMech.globalPlayerData.get(player.getUUID());
    	if (playerData != null) {
    		List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						absorbPct += base.getDamageAbsorbPercent();
					}
				}
			}
    	}
    	
    	return absorbPct;
	}
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_IRON_MECH_CHESTPLATE.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					;
				}
			}
		}
	}
	
}
