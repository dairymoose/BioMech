package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.BroadcastType;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EmergencyForcefieldUnitArmor extends ArmorBase {

	public EmergencyForcefieldUnitArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 100;
		this.suitEnergyPerSec = 1.0f;
		this.hidePlayerModel = true;
		this.armDistance = 5.5f;
		this.backArmorTranslation = 0.12;
		this.mechPart = MechPart.Chest;
		
		this.forceFieldDuration = 6.0f;
	}
	
	class DurationInfo {
		int remainingTicks = 0;
	}
	Map<UUID, DurationInfo> durationMap = new HashMap<>();
	public static int forceFieldDurationTicks;
	@Override
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown, boolean serverOriginator) {
		forceFieldDurationTicks = (int)(this.forceFieldDuration * 20);
		if (keyIsDown) {
			DurationInfo dura = durationMap.computeIfAbsent(player.getUUID(), (uuid) -> new DurationInfo());
			
			if (player.level().isClientSide) {
				dura.remainingTicks = -1;
				if (dura.remainingTicks == -1) {
					dura.remainingTicks = forceFieldDurationTicks;
				}
				if (dura.remainingTicks > 0) {
					--dura.remainingTicks;
					
					BioMech.LOGGER.info("spawn particles");
					ParticleOptions forceFieldParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_FORCE_FIELD.get();
					Vec3 loc = player.position().add(0.0, player.getBbHeight()/2.0, 0.0);
					EmergencyForcefieldUnitArmor.currentPlayer = player;
					float sphereRadius = 1.5f;
					double pitch = 0.0f;
					double yaw = 0.0f;
					int segments = 30;
					for (int a=0; a<segments; ++a) {
						pitch = a * 360.0/segments;
						for (int b=0; b<segments; ++b) {
							yaw = b * 360.0/segments;
							
							double y = sphereRadius*Math.sin(Math.toRadians(pitch));
							double projection = sphereRadius*Math.cos(Math.toRadians(pitch));
							
							double x = projection*Math.sin(Math.toRadians(yaw));
							double z = projection*Math.cos(Math.toRadians(yaw));
					
							loc = player.position().add(0.0, player.getBbHeight()/2.0, 0.0);
							loc = loc.add(x, y, z);
							
							player.level().addParticle(forceFieldParticle, loc.x, loc.y, loc.z, 0.0, 0.0, 0.0);
						}
					}
				}
			}
		}
		this.sendHotkeyToServer(player, keyIsDown, BroadcastType.SEND_TO_ALL_CLIENTS, serverOriginator);
		super.onHotkeyPressed(player, playerData, keyIsDown, serverOriginator);
	}
	
	public static Player currentPlayer = null;
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_EMERGENCY_FORCEFIELD_UNIT.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					
				}
			}
		}
	}
	
}
