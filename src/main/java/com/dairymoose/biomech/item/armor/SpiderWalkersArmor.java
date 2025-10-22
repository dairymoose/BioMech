package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class SpiderWalkersArmor extends ArmorBase {
	
	public SpiderWalkersArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.mechPart = MechPart.Leggings;
	}

	public static float energyPerSec = 1.0f;
	public static float energyPerTick = energyPerSec/20.0f;
	
	public static float energyPerSecHover = 1.0f;
	public static float energyPerTickHover = energyPerSecHover/20.0f;
	
	@SuppressWarnings("deprecation")
	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_SPIDER_WALKERS.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());
					if (has != null) {
						if (has.modifierKeyActive) {
							BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
							if (playerData != null) {
								class ImpulseChecker {
									boolean hasImpulse = false;
									Vec2 movementVec = null;
								}
								ImpulseChecker ic = new ImpulseChecker();
								DistExecutor.runWhenOn(Dist.CLIENT, () -> new Runnable() {

									@Override
									public void run() {
										ic.hasImpulse = Minecraft.getInstance().player.input.forwardImpulse > 1.0E-5F ||
										Minecraft.getInstance().player.input.forwardImpulse < -1.0E-5F ||
										Minecraft.getInstance().player.input.right || Minecraft.getInstance().player.input.left;
										
										ic.movementVec = Minecraft.getInstance().player.input.getMoveVector();
									}});
								boolean didClimb = false;
								if (player.isLocalPlayer() && ic.hasImpulse && ic.movementVec != null) {
									boolean hasAnyMatch = false;
									if (!player.horizontalCollision) {
										double xComp = player.getSpeed() * ic.movementVec.y * -Math.sin(Math.toRadians(player.getYRot())) + player.getSpeed() * ic.movementVec.x * -Math.sin(Math.toRadians(player.getYRot()-90.0));
										double zComp = player.getSpeed() * ic.movementVec.y * Math.cos(Math.toRadians(player.getYRot())) + player.getSpeed() * ic.movementVec.x * Math.cos(Math.toRadians(player.getYRot()-90.0));
										Vec3 deltaNoY = new Vec3(xComp, 0.0, zComp);
										//check block at player's feet
										hasAnyMatch = deltaMovementWouldCollide(player, 0.0, deltaNoY, 1.2f);
										if (!hasAnyMatch) {
											//check block near player's head
											hasAnyMatch = deltaMovementWouldCollide(player, 1.05, deltaNoY, 1.2f);
										}
									} else {
										hasAnyMatch = true;
									}
									
									if (hasAnyMatch) {
										//climb while colliding
										if (playerData.getSuitEnergy() >= energyPerTick) {
											didClimb = true;
											playerData.spendSuitEnergy(player, energyPerTick);
											
											Vec3 delta = player.getDeltaMovement();
											player.setDeltaMovement(delta.x, 0.35, delta.z);
											player.resetFallDistance();
										}
									}
								}
								
								if (!didClimb) {
									boolean hasNearbyMatch = false;
									
									float gripRadius = 1.40f;
									hasNearbyMatch = hasBlocksNearby(level, player, gripRadius);
									
									if (hasNearbyMatch) {
										playerData.spendSuitEnergy(player, energyPerTickHover);
										
										Vec3 delta = player.getDeltaMovement();
										player.setDeltaMovement(delta.x, 0.0, delta.z);
										player.resetFallDistance();
										player.setOnGround(true);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private boolean deltaMovementWouldCollide(Player player, double yAdjustment, Vec3 delta, float deltaScale) {
		float halfWidth = player.getBbWidth()/2.0f;
		Vec3 result = player.position().add(0.0, yAdjustment, 0.0).add(delta.normalize().scale(halfWidth).add(delta.scale(deltaScale)));
		
		BlockPos pos = BlockPos.containing(result);
		BlockState state = player.level().getBlockState(pos);
		VoxelShape collisionShape = state.getCollisionShape(player.level(), pos);

		BioMech.LOGGER.info("deltaMov=" + delta + " and bbWidth=" + halfWidth + " with deltaBB=" + (delta.normalize().scale(halfWidth)) + " has blockPos=" + pos + " with state=" + state + " with result=" + result);
		if (!collisionShape.isEmpty()) {
			return true;
		}
		
		return false;
	}

	private boolean hasBlocksNearby(Level level, Player player, float gripRadius) {
		boolean hasAnyMatch = false;
		for (float x=-gripRadius; x<=gripRadius; ++x) {
			for (float y=0.0f; y<=0.0f; ++y) {
				for (float z=-gripRadius; z<=gripRadius; ++z) {
					BlockPos pos = BlockPos.containing(player.position().add(x, y, z));
					BlockState state = level.getBlockState(pos);
					if (!state.isAir()) {
						hasAnyMatch = true;
						break;
					}
				}
			}
		}
		return hasAnyMatch;
	}
	
}
	