package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.anim.MiningLaserDispatcher;

import mod.azure.azurelib.rewrite.animation.AzAnimator;
import mod.azure.azurelib.rewrite.animation.AzAnimatorAccessor;
import mod.azure.azurelib.rewrite.animation.primitive.AzBakedAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class MiningLaserArmArmor extends ArmorBase {

	public final MiningLaserDispatcher dispatcher;
	
	public MiningLaserArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new MiningLaserDispatcher();
	}

	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick) {
		if (player.level().isClientSide) {
			BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());
			
			if (playerData != null) {
				ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);
				if (active) {
					//if third person view is active, this will work
					AzAnimator<ItemStack> anim = AzAnimatorAccessor.getOrNull(thirdPersonItemStack);
					if (anim == null) {
						//if first person view is active, this will work
						anim = AzAnimatorAccessor.getOrNull(itemStack);
					}
					if (anim != null) {
						AzBakedAnimation startUsing = anim.getAnimation(itemStack, MiningLaserDispatcher.START_USING_COMMAND.animationName);
						int useTicks = thirdPersonItemStack.getTag().getInt("useTicks");
						if (FMLEnvironment.dist == Dist.CLIENT) {
							if (player.level().isClientSide)
								++useTicks;
						} else {
							++useTicks;
						}

						thirdPersonItemStack.getTag().putInt("useTicks", useTicks);
						if (useTicks <= (int) startUsing.length()) {
							//BioMech.LOGGER.info("useTicks=" + useTicks);
							BioMech.clientSideItemAnimation(itemStack, MiningLaserDispatcher.START_USING_COMMAND.command);
							// send packet to server asking for start_using anim
							
							BioMech.clientSideItemAnimation(thirdPersonItemStack, MiningLaserDispatcher.START_USING_COMMAND.command);
						} else {
							BioMech.clientSideItemAnimation(itemStack, MiningLaserDispatcher.MINING_COMMAND.command);
							// send packet to server asking for mining anim
							BioMech.clientSideItemAnimation(thirdPersonItemStack, MiningLaserDispatcher.MINING_COMMAND.command);

							HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
									(e) -> true, player.getBlockReach() * 1.2);

							double handMult = 1.0;
							if (handPart == MechPart.RightArm)
								handMult = -1.0;
							Vec3 viewVec = player.getViewVector(partialTick);
							Vec3 perpendicular = new Vec3(viewVec.z, 0.0, -viewVec.x);
							Vec3 startLoc = player.getEyePosition(partialTick).add(0.0, -0.2, 0.0)
									.add(perpendicular.scale(0.22f * handMult));
							Vec3 endLoc = hitResult.getLocation();
							if (hitResult instanceof EntityHitResult ehr) {
								endLoc = ehr.getEntity().getPosition(1.0f).add(0.0, ehr.getEntity().getEyeHeight() / 2.0, 0.0);
							}
							Vec3 endToStartVec = endLoc.subtract(startLoc);
							int max = (int) (endToStartVec.length() * 16);
							double startDist = 0.075;
							for (int i = 0; i < max; ++i) {
								double vecScale = startDist + (i + 1) * 1.0f / max;
								Vec3 loc = startLoc.add(endToStartVec.scale(vecScale));
								Minecraft.getInstance().level.addParticle(
										(ParticleOptions) BioMechRegistry.PARTICLE_TYPE_LASER.get(), loc.x, loc.y, loc.z,
										viewVec.scale(vecScale).x, viewVec.scale(vecScale).y, viewVec.scale(vecScale).z);
							}

							if (hitResult instanceof BlockHitResult bhr) {
								BlockPos pos = bhr.getBlockPos();
								BlockState state = Minecraft.getInstance().level.getBlockState(pos);
								if (!state.isAir()) {
									ParticleType particles = ForgeRegistries.PARTICLE_TYPES
											.getValue(ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.BLOCK));
									if (particles != null) {
										BlockParticleOption blockParticle = new BlockParticleOption(particles, state);
										for (int i = 0; i < 10; ++i) {
											Minecraft.getInstance().level.addParticle(blockParticle,
													endLoc.x + (Math.random() * 1.0 - 0.5), endLoc.y + (Math.random() * 1.0 - 0.5),
													endLoc.z + (Math.random() * 1.0 - 0.5), 0.0D, 0.0D, 0.0D);
										}
									}
									// this.mineIfPossible(player, world, pos, mInfo);
								}
							}
						}
					} else {
						BioMech.LOGGER.error("Could not get animator for item: " + itemStack);
					}
				} else {
					if (thirdPersonItemStack.getTag() != null && thirdPersonItemStack.getTag().contains("useTicks")) {
						thirdPersonItemStack.getTag().putInt("useTicks", 0);
						BioMech.clientSideItemAnimation(itemStack, MiningLaserDispatcher.PASSIVE_COMMAND.command);
						//send packet to server asking for passive anim
					}
				}
			}
		}
	}
	
	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get();
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isLeftArm) {
		if (entity instanceof Player player) {
			HandActiveStatus has = BioMech.handActiveMap.get(player.getUUID());
			
			if (has != null && (!has.leftHandActive && isLeftArm || !has.rightHandActive && !isLeftArm)) {
				//this.dispatcher.mining(player, stack);
				List<Item> armorItems = new ArrayList<Item>();
				player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
				if (armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_ARM.get()) || armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get()) || slotId == -1) {
					if (entity instanceof LivingEntity living && !living.isSpectator()) {
						if (level.isClientSide) {
							if (player.getMainHandItem().isEmpty() && stack.getItem() instanceof MiningLaserRightArmArmor) {
								BioMech.clientSideItemAnimation(stack, MiningLaserDispatcher.PASSIVE_COMMAND.command);
								//this.dispatcher.passive(entity, stack);
							}
							else if (player.getOffhandItem().isEmpty() && stack.getItem() instanceof MiningLaserLeftArmArmor) {
								BioMech.clientSideItemAnimation(stack, MiningLaserDispatcher.PASSIVE_COMMAND.command);
								//this.dispatcher.passive(entity, stack);
							}
							else {
								BioMech.clientSideItemAnimation(stack, MiningLaserDispatcher.INERT_COMMAND.command);
								//this.dispatcher.inert(entity, stack);
							}
						}
					}
				}
			}
		}
	}

}
	