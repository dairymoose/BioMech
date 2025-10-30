package com.dairymoose.biomech.item.armor.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechNetwork;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechPlayerData.SlottedItem;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.ExtendoArmDispatcher;
import com.dairymoose.biomech.item.anim.GrappleArmDispatcher;
import com.dairymoose.biomech.item.armor.ArmorBase;
import com.dairymoose.biomech.item.armor.MechPart;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.BlockTargetInfo;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.DestroyBlockProgress;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.DestroyBlockProgressList;
import com.dairymoose.biomech.item.armor.arm.AbstractMiningArmArmor.EntityTargetInfo;
import com.dairymoose.biomech.item.armor.arm.ArmUtil.BoostInstance;
import com.dairymoose.biomech.packet.serverbound.ServerboundMiningArmBlockTargetPacket;
import com.dairymoose.biomech.packet.serverbound.ServerboundMiningArmEntityTargetPacket;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class GrappleArmArmor extends ArmorBase {

	public final GrappleArmDispatcher dispatcher;
	
	protected static final String USE_TICKS = "useTicks";
	
	public GrappleArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new GrappleArmDispatcher();
	}

	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_GRAPPLE_LEFT_ARM.get();
	}
	
	public static int energyToLaunch = 30;
	protected int startUsingTickCount = 3;
	
	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick, boolean bothHandsInactive, boolean bothHandsActive) {
		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

		if (playerData != null) {
			ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);

			if (playerData.getSuitEnergy() < energyToLaunch) {
				active = false;
			}
			
			if (active) {
				int useTicks = thirdPersonItemStack.getTag().getInt(USE_TICKS);
				if (FMLEnvironment.dist == Dist.CLIENT) {
					if (player.level().isClientSide)
						++useTicks;
				} else {
					++useTicks;
				}
				
				thirdPersonItemStack.getTag().putInt(USE_TICKS, useTicks);

				boolean didHit = false;
				if (player.level().isClientSide) {
					if (useTicks <= startUsingTickCount) {
						if (useTicks == 1) {
							//this.startUsingSound(player);
						}
						// BioMech.LOGGER.info("useTicks=" + useTicks);
						//this.startUsingAnimation(itemStack);
						BioMech.clientSideItemAnimation(itemStack, this.dispatcher.LAUNCH_COMMAND.cmd);
						// send packet to server asking for start_using anim

						//this.thirdPersonStartUsingAnimation(thirdPersonItemStack);
						player.setYBodyRot(player.getYHeadRot());
					} else {
						//this.miningAnimation(itemStack);
						
						// send packet to server asking for mining anim
						//this.thirdPersonMiningAnimation(thirdPersonItemStack);
						player.setYBodyRot(player.getYHeadRot());
					}
				}

				if (!player.level().isClientSide) {
					if (active && useTicks > startUsingTickCount) {
						//create grapple hook entity
					}
				}
				
//				if (useTicks > startUsingTickCount) {
//					if (didHit) {
//						if (energyPerTick > 0.0f)
//							playerData.spendSuitEnergy(player, energyPerTick);
//					} else {
//						if (energyPerTickMiss > 0.0f)
//							playerData.spendSuitEnergy(player, energyPerTickMiss);
//					}
//				}
				
				
			} else {
				if (thirdPersonItemStack.getTag() != null && thirdPersonItemStack.getTag().contains(USE_TICKS)) {
					thirdPersonItemStack.getTag().putInt(USE_TICKS, 0);
				}
				if (player.level().isClientSide) {
					BioMech.clientSideItemAnimation(itemStack, this.dispatcher.INERT_COMMAND.cmd);
				}
			}
		}
	}

	@Override
	public void biomechInventoryTick(SlottedItem slottedItem, ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity instanceof Player player) {
			List<Item> armorItems = new ArrayList<Item>();
			player.getArmorSlots().forEach((armorItemStack) -> armorItems.add(((armorItemStack).getItem())));
			if (armorItems.contains(BioMechRegistry.ITEM_GRAPPLE_ARM.get()) || slotId == -1) {
				if (entity instanceof LivingEntity living && !living.isSpectator()) {
					
				}
			}
		}
	}

}
	