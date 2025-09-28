package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;
import com.dairymoose.biomech.item.anim.MiningLaserDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

	class DestroyBlockProgress {
		BlockPos pos;
		float progress = 0.0f;
		public static float progressMax = 100.0f;
	}

	public float getLaserPower(int useTicks) {
		return Math.min(useTicks / (float) (SECONDS_UNTIL_MAX_LASER * 20), 1.0f);
	}

	public int SECONDS_UNTIL_MAX_LASER = 10;
	public static float minSpeedMult = 2.5f;
	public static float maxSpeedMult = minSpeedMult * 5.0f;
	public static double blockReachMult = 1.4;
	private static ItemStack miningTool = new ItemStack(Items.IRON_PICKAXE);
	public static int START_USING_TICK_COUNT = 5;
	Map<Player, DestroyBlockProgress> dbpMap = new HashMap<>();

	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick,
			boolean bothHandsInactive) {

		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

		if (playerData != null) {
			ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);
			if (active) {
				int useTicks = thirdPersonItemStack.getTag().getInt("useTicks");
				if (FMLEnvironment.dist == Dist.CLIENT) {
					if (player.level().isClientSide)
						++useTicks;
				} else {
					++useTicks;
				}

				thirdPersonItemStack.getTag().putInt("useTicks", useTicks);
				if (player.level().isClientSide) {
					if (useTicks <= START_USING_TICK_COUNT) {
						// BioMech.LOGGER.info("useTicks=" + useTicks);
						BioMech.clientSideItemAnimation(itemStack, MiningLaserDispatcher.START_USING_COMMAND.cmd);
						// send packet to server asking for start_using anim

						BioMech.clientSideItemAnimation(thirdPersonItemStack,
								MiningLaserDispatcher.START_USING_3D_COMMAND.cmd);
						player.setYBodyRot(player.getYHeadRot());
					} else {
						BioMech.clientSideItemAnimation(itemStack, MiningLaserDispatcher.MINING_COMMAND.cmd);
						// send packet to server asking for mining anim
						BioMech.clientSideItemAnimation(thirdPersonItemStack,
								MiningLaserDispatcher.MINING_3D_COMMAND.cmd);
						player.setYBodyRot(player.getYHeadRot());

						HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
								(e) -> !(e instanceof ItemEntity) && !e.isSpectator(),
								player.getBlockReach() * blockReachMult);

						double handMult = 1.0;
						if (handPart == MechPart.RightArm)
							handMult = -1.0;
						Vec3 viewVec = player.getViewVector(partialTick);
						Vec3 perpendicular = new Vec3(viewVec.z, 0.0, -viewVec.x);
						Vec3 startLoc = player.getEyePosition(partialTick).add(0.0, -0.28, 0.0)
								.add(perpendicular.scale(0.32f * handMult));
						Vec3 endLoc = hitResult.getLocation();
						if (hitResult instanceof EntityHitResult ehr) {
							Vec3 vecToEntity = ehr.getLocation().subtract(player.position());
							endLoc = player.getEyePosition(partialTick).add(viewVec.scale(vecToEntity.length()));
						}
						Vec3 endToStartVec = endLoc.subtract(startLoc);
						int max = (int) (endToStartVec.length() * 16);
						double startDist = 0.07;
						for (int i = 0; i < max; ++i) {
							double vecScale = startDist + (i + 1) * 1.0f / max;
							Vec3 loc = startLoc.add(endToStartVec.scale(vecScale));

							float power = this.getLaserPower(useTicks);
							ParticleOptions laserParticle = null;
							if (power <= 0.33f) {
								laserParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_LASER.get();
							} else if (power <= 0.66f) {
								laserParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_THICKER_LASER.get();
							} else if (power <= 0.99f) {
								laserParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_THICKEST_LASER.get();
							} else {
								laserParticle = (ParticleOptions) BioMechRegistry.PARTICLE_TYPE_MAX_LASER.get();
							}
							Minecraft.getInstance().level.addParticle(laserParticle, loc.x, loc.y, loc.z,
									viewVec.scale(vecScale).x, viewVec.scale(vecScale).y, viewVec.scale(vecScale).z);
						}

						if (hitResult instanceof BlockHitResult bhr) {
							BlockPos pos = bhr.getBlockPos();
							BlockState state = Minecraft.getInstance().level.getBlockState(pos);
							if (!state.isAir()) {
								float blockDestroySpeed = state.getDestroySpeed(player.level(), pos);

								ParticleType particles = ForgeRegistries.PARTICLE_TYPES
										.getValue(ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.BLOCK));
								if (particles != null && blockDestroySpeed > 0.0f) {
									BlockParticleOption blockParticle = new BlockParticleOption(ParticleTypes.BLOCK,
											state);
									for (int i = 0; i < 3; ++i) {
										Minecraft.getInstance().level.addParticle(blockParticle,
												endLoc.x + (Math.random() * 1.0 - 0.5),
												endLoc.y + (Math.random() * 1.0 - 0.5),
												endLoc.z + (Math.random() * 1.0 - 0.5), 0.0D, 0.0D, 0.0D);
									}
								}
							}
						}
					}
				}

				if (!player.level().isClientSide) {
					if (active && useTicks >= START_USING_TICK_COUNT) {
						HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
								(e) -> !(e instanceof ItemEntity) && !e.isSpectator(),
								player.getBlockReach() * blockReachMult);
						if (hitResult instanceof BlockHitResult bhr) {
							BlockState blockState = player.level().getBlockState(bhr.getBlockPos());
							if (!blockState.isAir()) {
								DestroyBlockProgress dbp = dbpMap.computeIfAbsent(player,
										(p) -> new DestroyBlockProgress());
								if (dbp.pos == null || !dbp.pos.equals(bhr.getBlockPos())) {
									dbp.pos = bhr.getBlockPos();
									dbp.progress = 0;
								}
								float blockDestroySpeed = blockState.getDestroySpeed(player.level(), dbp.pos);
								float toolSpeed = miningTool.getDestroySpeed(blockState);
								float miningSpeed = (toolSpeed / blockDestroySpeed);
								float speedMult = Mth.lerp(this.getLaserPower(useTicks), minSpeedMult, maxSpeedMult);
								dbp.progress += miningSpeed * speedMult;
								player.level().destroyBlockProgress(0, dbp.pos,
										(int) (10 * (float) dbp.progress / DestroyBlockProgress.progressMax));
								if (dbp.progress >= DestroyBlockProgress.progressMax) {
									player.level().destroyBlockProgress(0, dbp.pos, 10);
									player.level().destroyBlock(dbp.pos, true);
									dbp.pos = null;
									dbp.progress = 0;
								}
							}
						} else if (hitResult instanceof EntityHitResult ehr) {
							Entity e = ehr.getEntity();
							if (e instanceof LivingEntity living) {
								if (!living.isInvulnerable() && !living.fireImmune()) {
									living.hurt(player.level().damageSources().onFire(), 1.0f);
									if (living.getRemainingFireTicks() <= 40) {
										living.setRemainingFireTicks(40);
									}
								}
							}
						}
					} else {
						if (bothHandsInactive) {
							DestroyBlockProgress dbp = dbpMap.get(player);
							if (dbp != null && dbp.pos != null) {
								player.level().destroyBlockProgress(0, dbp.pos, 10);
								dbp.pos = null;
								dbp.progress = 0;
							}
						}
					}
				}
			} else {
				if (thirdPersonItemStack.getTag() != null && thirdPersonItemStack.getTag().contains("useTicks")) {
					thirdPersonItemStack.getTag().putInt("useTicks", 0);
					if (player.level().isClientSide) {
						BioMech.clientSideItemAnimation(itemStack, MiningLaserDispatcher.PASSIVE_COMMAND.cmd);
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
				// this.dispatcher.mining(player, stack);
				List<Item> armorItems = new ArrayList<Item>();
				player.getArmorSlots().forEach((itemStack) -> armorItems.add(itemStack.getItem()));
				if (armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_ARM.get())
						|| armorItems.contains(BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get()) || slotId == -1) {
					if (entity instanceof LivingEntity living && !living.isSpectator()) {
						if (level.isClientSide) {
							if (player.getMainHandItem().isEmpty()
									&& stack.getItem() instanceof MiningLaserRightArmArmor) {
								BioMech.clientSideItemAnimation(stack, MiningLaserDispatcher.PASSIVE_COMMAND.cmd);
							} else if (player.getOffhandItem().isEmpty()
									&& stack.getItem() instanceof MiningLaserLeftArmArmor) {
								BioMech.clientSideItemAnimation(stack, MiningLaserDispatcher.PASSIVE_COMMAND.cmd);
							} else {
								BioMech.clientSideItemAnimation(stack, MiningLaserDispatcher.INERT_COMMAND.cmd);
							}
						}
					}
				}
			}
		}
	}

}
