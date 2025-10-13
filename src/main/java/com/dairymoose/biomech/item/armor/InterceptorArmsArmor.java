package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.InterceptorArmsDispatcher;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class InterceptorArmsArmor extends ArmorBase {

	InterceptorArmsDispatcher dispatcher;
	
	public InterceptorArmsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.suitEnergyPerSec = 0.5f;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Back;
		this.projectileAvoidPct = 0.5f;
		this.dispatcher = new InterceptorArmsDispatcher();
	}

	public static float getProjectileAvoidPct(Player player) {
		float avoidPct = 0.0f;

		BioMechPlayerData playerData = null;
		playerData = BioMech.globalPlayerData.get(player.getUUID());
		if (playerData != null) {
			List<SlottedItem> slottedItems = playerData.getAllSlots();
			for (SlottedItem slotted : slottedItems) {
				if (!slotted.itemStack.isEmpty()) {
					if (slotted.itemStack.getItem() instanceof ArmorBase base) {
						avoidPct += base.getProjectileAvoidPercent();
					}
				}
			}
		}

		return avoidPct;
	}

	public static Set<Player> dodgedProjectileSet = new HashSet<>();
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_INTERCEPTOR_ARMS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					boolean setHasEntity = dodgedProjectileSet.contains(entity);
					if ((itemStack.getTag() != null && itemStack.getTag().contains("DodgeTicks")) || setHasEntity) {
						if (setHasEntity)
							BioMech.clientSideItemAnimation(itemStack, this.dispatcher.PASSIVE_COMMAND.cmd);
						dodgedProjectileSet.remove(entity);
						
						CompoundTag tag = itemStack.getOrCreateTag();
						if (player.level().isClientSide) {
							int ticks = 5;
							if (tag.contains("DodgeTicks")) {
								ticks = tag.getInt("DodgeTicks");
							}
							--ticks;
							if (ticks <= 0) {
								tag.remove("DodgeTicks");
								BioMech.clientSideItemAnimation(itemStack, this.dispatcher.PASSIVE_COMMAND.cmd);
							} else {
								tag.putInt("DodgeTicks", ticks);
								BioMech.clientSideItemAnimation(itemStack, this.dispatcher.DEFLECT_COMMAND.cmd);
							}
						}
					} else {
						BioMech.clientSideItemAnimation(itemStack, this.dispatcher.PASSIVE_COMMAND.cmd);
					}
				}
			}
		}
	}
	
}
