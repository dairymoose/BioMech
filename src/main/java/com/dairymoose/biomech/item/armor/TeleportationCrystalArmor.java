package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.TeleportationCrystalDispatcher;
import com.dairymoose.biomech.packet.serverbound.ServerboundTeleportationCrystalPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TeleportationCrystalArmor extends ArmorBase {

	public final TeleportationCrystalDispatcher dispatcher;
	public static int TELEPORT_HOLD_TIME_TICKS = 4*20;
	
	public TeleportationCrystalArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Back;
		this.dispatcher = new TeleportationCrystalDispatcher();
	}

	@Override
	public void onHotkeyHeld(Player player, BioMechPlayerData playerData) {
		++BioMech.holdingTeleportTicks;
		
		float pitch = 1.0f + 0.8f * (float)BioMech.holdingTeleportTicks/TeleportationCrystalArmor.TELEPORT_HOLD_TIME_TICKS;
		Minecraft.getInstance().player.playSound(SoundEvents.ALLAY_DEATH, 0.2f, pitch);
		if (playerData.tickCount % 2 == 0) {
			int particleCount = 6;
			for (int i=0; i<particleCount; ++i) {
				Vec3 loc = Minecraft.getInstance().player.position().add(new Vec3(2.0 * (Math.random() - 0.5), 1.3 + 0.25 * Math.random(), 2.0 * (Math.random() - 0.5)));
    			Minecraft.getInstance().player.level().addParticle(ParticleTypes.ELECTRIC_SPARK, loc.x, loc.y, loc.z, 0.0f, 0.0f, 0.0f);
			}
		}
		
		if (BioMech.holdingTeleportTicks >= TeleportationCrystalArmor.TELEPORT_HOLD_TIME_TICKS) {
			BioMech.holdingTeleportTicks = 0;
			BioMechNetwork.INSTANCE.sendToServer(new ServerboundTeleportationCrystalPacket());
		}
	}
	
	@Override
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown) {
		if (!keyIsDown)
			BioMech.holdingTeleportTicks = 0;
	}
	
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_TELEPORTATION_CRYSTAL.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					BioMech.clientSideItemAnimation(itemStack, this.dispatcher.PASSIVE_COMMAND.cmd);
				}
			}
		}
	}
	
}
