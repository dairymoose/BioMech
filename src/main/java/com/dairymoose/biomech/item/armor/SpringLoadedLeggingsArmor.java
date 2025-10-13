package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.anim.SpringLoadedLeggingsDispatcher;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpringLoadedLeggingsArmor extends ArmorBase {

	public SpringLoadedLeggingsDispatcher dispatcher;
	
	public SpringLoadedLeggingsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 5;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
		this.dispatcher = new SpringLoadedLeggingsDispatcher();
	}

	public static int JUMP_BOOST_POWER_LEVEL = 1;
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_SPRING_LOADED_LEGGINGS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					CompoundTag tag = itemStack.getOrCreateTag();
					if (player.level().isClientSide) {
						if (tag != null && tag.contains("BounceTicks")) {
							int ticks = tag.getInt("BounceTicks");
							--ticks;
							if (ticks == 0) {
								BioMech.clientSideItemAnimation(itemStack, this.dispatcher.INERT_COMMAND.cmd);
								tag.remove("BounceTicks");
							} else {
								tag.putInt("BounceTicks", ticks);
							}
						}
					}
					
					HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());
					if (has != null) {
						if (!level.isClientSide) {
							MobEffectInstance jumpBoost = living.getEffect(MobEffects.JUMP);
							if (jumpBoost == null || jumpBoost.endsWithin(10)) {
								living.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, JUMP_BOOST_POWER_LEVEL, false, false, false));
							}
						}
						
						if (has.jumpActive) {
							int jumpsLeft;
							if (!tag.contains("JumpsLeft")) {
								tag.putInt("JumpsLeft", 1);
							}
							jumpsLeft = tag.getInt("JumpsLeft");
							
							BioMech.MidAirJumpStatus maj = BioMech.primedForMidAirJumpMap.get(entity.getUUID());
							if (maj.primedForMidAirJump && jumpsLeft > 0) {
								--jumpsLeft;
								tag.putInt("JumpsLeft", jumpsLeft);
								player.setOnGround(true);
								//BioMech.primedForMidairJump = false;
							}
						} else {
							if (player.onGround()) {
								tag.putInt("JumpsLeft", 1);
							}
						}
					}
				}
			}
		}
	}
	
}
	