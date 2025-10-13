package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.packet.serverbound.ServerboundMobilityTreadsPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class MobilityTreadsArmor extends ArmorBase {
	
	public MobilityTreadsArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 20;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
		
		this.viewBobDisabled = true;
		this.viewBobArmSwayModifier = 1/7.0f;
	}

	public static int SECONDS_UNTIL_SPEED_BOOST = 5;
	public static int TICKS_PER_SEC = 20;
	
	private boolean requestedSpeedBoost = false;
	public static boolean localPlayerSpeedBoosting = false;
	
	public static int SPEED_BOOST_SLOW = 0;
	public static int SPEED_BOOST_FAST = 2;
	
	@SuppressWarnings("deprecation")
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
			if (armorItems.contains(BioMechRegistry.ITEM_MOBILITY_TREADS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					if (level.isClientSide) {
						int lastCollisionTick = 0;
						if (stack.getTag().contains("LastCollisionTick")) {
							lastCollisionTick = stack.getTag().getInt("LastCollisionTick");
						}
						
						class InputChecker {
							boolean hasForwardImpulse = false;
							boolean hasBackwardsImpulse = false;
						}
						InputChecker ic = new InputChecker();
						DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {
							@Override
							public void run() {
								ic.hasForwardImpulse = Minecraft.getInstance().player.input.hasForwardImpulse();
								ic.hasBackwardsImpulse = Minecraft.getInstance().player.input.forwardImpulse < 0.0f;
							}});
						
						float currentSpeed = (float) entity.getDeltaMovement().horizontalDistance() * 20.0f;
						if (!player.onGround()) {
							currentSpeed = 0.0f;
						}
						if (ic.hasBackwardsImpulse) {
							currentSpeed = -currentSpeed;
						}
						stack.getOrCreateTag().putFloat("CurrentSpeed", currentSpeed);
						
						if (!ic.hasForwardImpulse) {
							if (requestedSpeedBoost) {
								localPlayerSpeedBoosting = false;
								
								requestedSpeedBoost = false;
								BioMechNetwork.INSTANCE.sendToServer(new ServerboundMobilityTreadsPacket(false));
							}
							lastCollisionTick = player.tickCount;
							stack.getTag().putInt("LastCollisionTick", lastCollisionTick);
						} else {
							int tickDiff = player.tickCount - lastCollisionTick;
							if (!requestedSpeedBoost && tickDiff >= (SECONDS_UNTIL_SPEED_BOOST * TICKS_PER_SEC) && player.onGround()) {
								localPlayerSpeedBoosting = true;
								
								requestedSpeedBoost = true;
								BioMechNetwork.INSTANCE.sendToServer(new ServerboundMobilityTreadsPacket(true));
							}
						}
					} else {
						CompoundTag tag = stack.getOrCreateTag();
						boolean speedBoost = false;
						if (tag.contains("WantSpeedBoost")) {
							speedBoost = stack.getOrCreateTag().getBoolean("WantSpeedBoost");
						}
						
						MobEffectInstance speedBuff = player.getEffect(MobEffects.MOVEMENT_SPEED);
						if (speedBuff == null || speedBuff.endsWithin(4)) {
							player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 8, speedBoost ? SPEED_BOOST_FAST : SPEED_BOOST_SLOW, false, false, false));
						}
					}
				}
			}
		}
	}
	
}
	