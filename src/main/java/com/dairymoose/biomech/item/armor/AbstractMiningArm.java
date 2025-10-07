package com.dairymoose.biomech.item.armor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechPlayerData;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.HandActiveStatus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

public abstract class AbstractMiningArm extends ArmorBase {

	public AbstractMiningArm(ArmorMaterial material, Type type, Properties props) {
		super(material, type, props);
	}
	
	class DestroyBlockProgressList {
		DestroyBlockProgressList() {
			list = new ArrayList<>();
		}
		
		List<DestroyBlockProgress> list;
	}
	
	class DestroyBlockProgress {
		BlockPos pos;
		float progress = 0.0f;
		public static float progressMax = 100.0f;
	}

	public int SECONDS_UNTIL_MAX_LASER = 12;
	protected float minSpeedMult = 2.5f;
	protected float maxSpeedMult = minSpeedMult * 6.0f;
	protected double blockReachMult = 1.6;
	protected ItemStack miningTool = new ItemStack(Items.IRON_PICKAXE);
	public static int START_USING_TICK_COUNT = 5;
	
	protected float energyPerSec = 4.0f;
	protected float energyPerTick;
	
	protected float energyPerSecMiss = 1.0f;
	protected float energyPerTickMiss;
	
	protected float wrongToolPenalty = 1.0f;
	protected boolean instantDestroyLeaves = false;
	protected boolean onlyMinesMatchingBlocks = false;
	
	private static int SOUND_TICK_DURATION = 3;
	Map<Player, DestroyBlockProgressList> dbpMap = new HashMap<>();
	protected int xSize = 1;
	protected int ySize = 1;
	protected int zSize = 1;
	
	protected float minMiningProgress = 0.0f;

	protected abstract float getMiningPower(int useTicks);
	
	protected abstract void passiveAnimation(ItemStack itemStack);
	protected abstract void inertAnimation(ItemStack itemStack);
	protected abstract void startUsingAnimation(ItemStack itemStack);
	protected abstract void miningAnimation(ItemStack itemStack);
	protected abstract void thirdPersonStartUsingAnimation(ItemStack itemStack);
	protected abstract void thirdPersonMiningAnimation(ItemStack itemStack);
	
	@Override
	public void onHandTick(boolean active, ItemStack itemStack, Player player, MechPart handPart, float partialTick,
			boolean bothHandsInactive, boolean bothHandsActive) {

		BioMechPlayerData playerData = BioMech.globalPlayerData.get(player.getUUID());

		if (playerData != null) {
			this.energyPerTick = energyPerSec / 20.0f;
			this.energyPerTickMiss = energyPerSecMiss / 20.0f;
			
			ItemStack thirdPersonItemStack = BioMech.getThirdPersonArmItemStack(playerData, handPart);
			
			if (playerData.getSuitEnergy() < energyPerTick) {
				active = false;
			}
			
			if (active) {
				int useTicks = thirdPersonItemStack.getTag().getInt("useTicks");
				if (FMLEnvironment.dist == Dist.CLIENT) {
					if (player.level().isClientSide)
						++useTicks;
				} else {
					++useTicks;
				}
				
				thirdPersonItemStack.getTag().putInt("useTicks", useTicks);

				boolean didHit = false;
				if (player.level().isClientSide) {
					if (useTicks <= START_USING_TICK_COUNT) {
						// BioMech.LOGGER.info("useTicks=" + useTicks);
						this.startUsingAnimation(itemStack);
						// send packet to server asking for start_using anim

						this.thirdPersonStartUsingAnimation(thirdPersonItemStack);
						player.setYBodyRot(player.getYHeadRot());
					} else {
						this.miningAnimation(itemStack);
						
						// send packet to server asking for mining anim
						this.thirdPersonMiningAnimation(thirdPersonItemStack);
						player.setYBodyRot(player.getYHeadRot());

						HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
								(e) -> (e instanceof LivingEntity) && !e.isSpectator(),
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
						this.onSpawnParticles(player, startLoc, endLoc, useTicks, viewVec);

						if (hitResult instanceof BlockHitResult bhr) {
							BlockPos pos = bhr.getBlockPos();
							BlockState blockState = player.level().getBlockState(pos);
							if (!blockState.isAir() && !blockState.getFluidState().isSource()) {
								didHit = true;
								float blockDestroySpeed = blockState.getDestroySpeed(player.level(), pos);

								ParticleType particles = ForgeRegistries.PARTICLE_TYPES
										.getValue(ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.BLOCK));
								if (particles != null && blockDestroySpeed > 0.0f) {
									BlockParticleOption blockParticle = new BlockParticleOption(ParticleTypes.BLOCK,
											blockState);
									for (int i = 0; i < 3; ++i) {
										player.level().addParticle(blockParticle,
												endLoc.x + (Math.random() * 1.0 - 0.5),
												endLoc.y + (Math.random() * 1.0 - 0.5),
												endLoc.z + (Math.random() * 1.0 - 0.5), 0.0D, 0.0D, 0.0D);
									}
								}
							}
						} else if (hitResult instanceof EntityHitResult ehr) {
							didHit = true;
						}
						
						if (player.tickCount % SOUND_TICK_DURATION == 0) {
							playSound(player, useTicks, didHit);
						}
					}
				}

				if (!player.level().isClientSide) {
					if (active && useTicks >= START_USING_TICK_COUNT) {
						HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player,
								(e) -> (e instanceof LivingEntity) && !e.isSpectator(),
								player.getBlockReach() * blockReachMult);
						
						float miningPower = this.getMiningPower(useTicks);
						if (hitResult instanceof BlockHitResult bhr) {
							BlockState blockState = player.level().getBlockState(bhr.getBlockPos());
							
							if (!blockState.isAir() && !blockState.getFluidState().isSource()) {
								didHit = true;
								DestroyBlockProgressList dbpList = dbpMap.computeIfAbsent(player,
										(p) -> new DestroyBlockProgressList());
								BlockPos origin = bhr.getBlockPos();
								int xDiff = xSize/2;
								int yDiff = ySize/2;
								int zDiff = zSize/2;
								BlockPos minPos = origin.relative(Axis.X, -xDiff).relative(Axis.Y, -yDiff).relative(Axis.Z, -zDiff);
								BlockPos maxPos = origin.relative(Axis.X, xDiff).relative(Axis.Y, yDiff).relative(Axis.Z, zDiff);
								Iterable<BlockPos> blocks = BlockPos.betweenClosed(minPos, maxPos);
								int expectedSize = xSize*ySize*zSize;
								int dbpIndex = 0;
								
								for (BlockPos pos : blocks) {
									if (dbpList.list.size() < expectedSize)
										dbpList.list.add(new DestroyBlockProgress());
									DestroyBlockProgress dpb = dbpList.list.get(dbpIndex);
									//new BlockPos is required because 'pos' is a MutableBlockPos
									populateDestroyBlockProgress(player, miningPower, new BlockPos(pos), dpb, pos.equals(origin));
									
									++dbpIndex;
								}
							}
						} else if (hitResult instanceof EntityHitResult ehr) {
							didHit = true;
							Entity e = ehr.getEntity();
							if (e instanceof LivingEntity living) {
								if (!living.isInvulnerable() && !living.isDeadOrDying()) {
									dealEntityDamage(player, bothHandsActive, miningPower, living);
								}
							}
						}
					} else {
						if (bothHandsInactive) {
							DestroyBlockProgressList dbpList = dbpMap.get(player);
							for (DestroyBlockProgress dbp : dbpList.list) {
								if (dbp != null && dbp.pos != null) {
									player.level().destroyBlockProgress(0, dbp.pos, 10);
									dbp.pos = null;
									dbp.progress = 0;
								}
							}
						}
					}
				}
				
				if (didHit) {
					if (energyPerTick > 0.0f)
						playerData.spendSuitEnergy(player, energyPerTick);
				} else {
					if (energyPerTickMiss > 0.0f)
						playerData.spendSuitEnergy(player, energyPerTickMiss);
				}
				
				
			} else {
				if (thirdPersonItemStack.getTag() != null && thirdPersonItemStack.getTag().contains("useTicks")) {
					thirdPersonItemStack.getTag().putInt("useTicks", 0);
					if (player.level().isClientSide) {
						this.passiveAnimation(itemStack);
					}
				}
			}
		}
	}

	private void populateDestroyBlockProgress(Player player, float miningPower, BlockPos pos, DestroyBlockProgress dbp, boolean isOrigin) {
		if (dbp.pos == null || !dbp.pos.equals(pos)) {
			dbp.pos = pos;
			dbp.progress = 0;
		}
		
		BlockState blockState = player.level().getBlockState(pos);
		float blockDestroySpeed = blockState.getDestroySpeed(player.level(), dbp.pos);
		float toolSpeed = miningTool.getDestroySpeed(blockState);
		boolean isCorrect = miningTool.isCorrectToolForDrops(blockState);
		float penaltyMod = isCorrect ? 1.0f : wrongToolPenalty;
		float miningSpeed = penaltyMod * (toolSpeed / blockDestroySpeed);
		float speedMult = Mth.lerp(miningPower, minSpeedMult, maxSpeedMult);
		
		if (miningSpeed > 0.0f) {
			if (!onlyMinesMatchingBlocks || (isOrigin || isCorrect))
				dbp.progress += Math.max(minMiningProgress, miningSpeed * speedMult);
			
			if (blockState.is(BlockTags.LEAVES)) {
				if (instantDestroyLeaves) {
					dbp.progress = dbp.progressMax;
				}
			}
		}
		
		if (isOrigin) {
			player.level().destroyBlockProgress(0, dbp.pos,
					(int) (10 * (float) dbp.progress / DestroyBlockProgress.progressMax));
		}
		if (dbp.progress >= DestroyBlockProgress.progressMax) {
			if (isOrigin) {
				player.level().destroyBlockProgress(0, dbp.pos, 10);
			}
			player.level().destroyBlock(dbp.pos, true);
			dbp.pos = null;
			dbp.progress = 0;
		}
	}

	protected abstract void playSound(Player player, int useTicks, boolean didHit);
	
	protected abstract void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living);

	protected abstract void onSpawnParticles(Player player, Vec3 startLoc, Vec3 endLoc, int useTicks, Vec3 viewVec);

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
								this.passiveAnimation(stack);
							} else if (player.getOffhandItem().isEmpty()
									&& stack.getItem() instanceof MiningLaserLeftArmArmor) {
								this.passiveAnimation(stack);
							} else {
								this.inertAnimation(stack);
							}
						}
					}
				}
			}
		}
	}

}
