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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class EmergencyForcefieldUnitArmor extends ArmorBase {

	public EmergencyForcefieldUnitArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 100;
		this.suitEnergyPerSec = 1.0f;
		this.hidePlayerModel = true;
		this.armDistance = 5.5f;
		this.backArmorTranslation = 0.12;
		this.mechPart = MechPart.Chest;
		
		this.forceFieldDuration = 5.0f;
		this.forceFieldCooldown = 90.0f;
	}
	
	public static class DurationInfo {
		public int remainingTicks = 0;
		public int cooldownRemaining = 0;
		public boolean appliedEfect = false;
	}
	public static Map<UUID, DurationInfo> durationMap = new HashMap<>();
	public static int forceFieldDurationTicks;
	public static int forceFieldCooldownTicks;
	@Override
	public void onHotkeyPressed(Player player, BioMechPlayerData playerData, boolean keyIsDown, int bonusData, boolean serverOriginator) {
		forceFieldDurationTicks = (int)(this.forceFieldDuration * 20);
		forceFieldCooldownTicks = (int)(this.forceFieldCooldown * 20);
		if (keyIsDown) {
			DurationInfo dura = durationMap.computeIfAbsent(player.getUUID(), (uuid) -> new DurationInfo());
			if (dura.remainingTicks == 0 && dura.cooldownRemaining == 0) {
				dura.remainingTicks = forceFieldDurationTicks;
				dura.cooldownRemaining = forceFieldCooldownTicks;
			} else {
				if (dura.remainingTicks <= 0 && dura.cooldownRemaining > 0) {
					if (player.level().isClientSide) {
						if (player.isLocalPlayer()) {
							player.sendSystemMessage(Component.literal("Forcefield Unit is recharging!  Wait " + (int)Math.ceil(dura.cooldownRemaining/20.0f) + " sec"));
							return;
						}
					}
				}
			}
			
			if (player.level().isClientSide) {
				if (dura.remainingTicks == forceFieldDurationTicks) {
					ParticleOptions forceFieldParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_FORCE_FIELD.get();
					Vec3 loc = player.position().add(0.0, player.getBbHeight()/2.0, 0.0);
					EmergencyForcefieldUnitArmor.currentPlayer = player;
					float sphereRadius = 1.7f;
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
			
			this.sendHotkeyToServer(player, keyIsDown, bonusData, BroadcastType.SEND_TO_ALL_CLIENTS, serverOriginator);
		}
		
		super.onHotkeyPressed(player, playerData, keyIsDown, bonusData, serverOriginator);
	}
	
	public static Player currentPlayer = null;
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_EMERGENCY_FORCEFIELD_UNIT.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living) {
					DurationInfo dura = durationMap.get(player.getUUID());
					if (dura != null) {
						if (!level.isClientSide) {
							if (!dura.appliedEfect) {
								dura.appliedEfect = true;
								
								player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, forceFieldDurationTicks, 3, false, false, false));
								player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, forceFieldDurationTicks, 1, false, false, false));
							}
						}
						
						if (FMLEnvironment.dist == Dist.CLIENT) {
							if (level.isClientSide) {
								--dura.remainingTicks;
							}
						} else {
							--dura.remainingTicks;
						}
						
						if (dura.remainingTicks <= 0) {
							if (FMLEnvironment.dist == Dist.CLIENT) {
								if (level.isClientSide) {
									--dura.cooldownRemaining;
								}
							} else {
								--dura.cooldownRemaining;
							}
							
							if (dura.cooldownRemaining <= 0) {
								if (level.isClientSide) {
									if (player.isLocalPlayer()) {
										player.sendSystemMessage(Component.literal("Forcefield Unit recharged!"));
									}
								}
								durationMap.remove(player.getUUID());
							}
						} else {
							if (player.tickCount % 2 == 0) {
								float volume = 0.5f;
								float pitch = 1.0f + 1.0f*(float)(Math.random());
								player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.AMETHYST_CLUSTER_STEP, SoundSource.PLAYERS, volume, pitch, false);
							}
						}
					}
				}
			}
		}
	}
	
}
