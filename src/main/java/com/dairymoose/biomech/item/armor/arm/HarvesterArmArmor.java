package com.dairymoose.biomech.item.armor.arm;

import java.util.List;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.DrillDispatcher;
import com.dairymoose.biomech.item.anim.HarvesterDispatcher;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class HarvesterArmArmor extends AbstractMiningArmArmor {

	public final HarvesterDispatcher dispatcher;

	public HarvesterArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new HarvesterDispatcher();
		
		this.blockReachMult = 1.0;
		this.energyPerSecMiss = 0.0f;
		
		this.minSpeedMult = 1.4f;
		this.maxSpeedMult = minSpeedMult;
		
		this.miningTool = new ItemStack(Items.IRON_HOE);

		this.xSize = 3;
		this.ySize = 2;
		this.zSize = 3;
		
		this.wrongToolPenalty = 2.2f;
	}

	private boolean isCrop(BlockState blockState) {
		if (blockState.getBlock() instanceof CropBlock crop) {
			return true;
		} else if (blockState.getBlock() instanceof NetherWartBlock wart) {
			return true;
		}
		return false;
	}
	
	private int getCropAge(BlockState state) {
		if (state.getBlock() instanceof CropBlock crop) {
			return crop.getAge(state);
		} else if (state.getBlock() instanceof NetherWartBlock wart) {
			return state.getValue(NetherWartBlock.AGE).intValue();
		}
		
		return 0;
	}
	
	@Override
	protected boolean matchesCorrectTool(Player player, Level level, BlockPos origin, BlockState originState) {
		return getHoeToolModifiedState(player, level, origin) != null || originState.is(Blocks.FARMLAND) || originState.is(Blocks.SOUL_SAND) || this.isCrop(originState);
	}
	
	protected void replantCrop(BlockPos blockPos, BlockState state, ServerLevel serverLevel) {
		Block cropBlock = state.getBlock();
		List<ItemStack> drops = Block.getDrops(state, serverLevel, blockPos, serverLevel.getBlockEntity(blockPos));

		ItemStack seedStack = null;
		if (cropBlock instanceof CropBlock crop) {
			seedStack = crop.getCloneItemStack(serverLevel, blockPos, state);
		} else if (cropBlock instanceof NetherWartBlock wart) {
			seedStack = wart.getCloneItemStack(serverLevel, blockPos, state);
		}
		
		if (seedStack != null) {
			Item seedItem = seedStack.getItem();
			
			ItemStack toRemove = null;
			boolean canReplant = false;
			for (ItemStack itemStack : drops) {
				if (itemStack.is(seedItem)) {
					canReplant = true;
					itemStack.shrink(1);
					if (itemStack.isEmpty()) {
						toRemove = itemStack;
					}
					break;
				}
			}
			if (toRemove != null) {
				drops.remove(toRemove);
			}
			
			drops.forEach((itemStack) -> {
				ItemEntity itemEntity = new ItemEntity(serverLevel, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, itemStack);
				itemEntity.setDefaultPickUpDelay();
				serverLevel.addFreshEntity(itemEntity);
			});
			
			
			if (canReplant) {
				BlockState replantedState = null;
				if (cropBlock instanceof CropBlock crop) {
					replantedState = state.setValue(CropBlock.AGE, 0);
				} else if (cropBlock instanceof NetherWartBlock wart) {
					replantedState = state.setValue(NetherWartBlock.AGE, 0);
				}
				
				serverLevel.setBlock(blockPos, replantedState, 3);
			} else {
				serverLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}
	
	@Override
	protected boolean doSpecialBlockMiningLogic(Player player, Level level, BlockState blockState, BlockPos blockPos) {
		int xDiff = xSize/2;
		int yDiff = ySize/2;
		int zDiff = zSize/2;
		Iterable<BlockPos> blocks = this.getAllMineableBlocks(blockPos, xDiff, yDiff, zDiff);
		
		boolean handled = false;
		for (BlockPos pos : blocks) {
			BlockState state = level.getBlockState(pos);
			
			BlockState toolModifiedState = getHoeToolModifiedState(player, level, pos);
			if (toolModifiedState != null) {
				if (!level.isClientSide) {
					level.setBlock(pos, toolModifiedState, 3);
				}
				
				handled = true;
			} else if (state.is(Blocks.FARMLAND) || state.is(Blocks.SOUL_SAND)) {
				handled = true;
			} else if (this.isCrop(state)) {
				int age = this.getCropAge(state);
				if (level instanceof ServerLevel serverLevel) {
					if (state.getBlock() instanceof CropBlock crop) {
						if (age >= crop.getMaxAge()) {
							this.replantCrop(pos, state, serverLevel);
						}
					} else if (state.getBlock() instanceof NetherWartBlock wart) {
						if (age >= NetherWartBlock.MAX_AGE) {
							this.replantCrop(pos, state, serverLevel);
						}
					}
				}
				
				handled = true;
			}
		}
		if (handled) {
			return true;
		}
		
		return false;
	}

	private BlockState getHoeToolModifiedState(Player player, Level level, BlockPos blockPos) {
		BlockState toolModifiedState = null;
		ItemStack currentMainHand = player.getMainHandItem();
		try {
			player.setItemInHand(InteractionHand.MAIN_HAND, this.miningTool);
			UseOnContext ctx = new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(blockPos.getCenter(), player.getDirection(), blockPos, false));
			toolModifiedState = level.getBlockState(blockPos).getToolModifiedState(ctx, net.minecraftforge.common.ToolActions.HOE_TILL, true);
		} finally {
			player.setItemInHand(InteractionHand.MAIN_HAND, currentMainHand);
		}
		return toolModifiedState;
	}
	
	public static float hoeDamage = 2.0f;
	
	@Override
	protected void playSound(Player player, int useTicks, boolean didHit) {
		float volume = 1.2f;
		float laserPitch = 1.20f;
		if (didHit) {
			laserPitch *= 1.10f;
		}
		//player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_MINING_DRILL.get(), SoundSource.PLAYERS, volume, laserPitch, false);
	}
	
	@Override
	protected void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living) {
		float damageMult = 1.0f;
		living.hurt(player.level().damageSources().playerAttack(player), damageMult*hoeDamage*miningPower);
	}
	
	@Override
	protected float getMiningPower(int useTicks) {
		return 1.0f;
	}
	
	@Override
	protected void passiveAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack, dispatcher.PASSIVE_COMMAND.cmd);
	}
	
	@Override
	protected void inertAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack, dispatcher.INERT_COMMAND.cmd);
	}
	
	@Override
	protected void startUsingAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack, dispatcher.START_USING_COMMAND.cmd);
	}
	
	@Override
	protected void miningAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack, dispatcher.MINING_COMMAND.cmd);
	}
	
	@Override
	protected void thirdPersonStartUsingAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack,
				dispatcher.START_USING_3D_COMMAND.cmd);
	}
	
	@Override
	protected void thirdPersonMiningAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack,
				dispatcher.MINING_3D_COMMAND.cmd);
	}
	
	@Override
	protected void onSpawnParticles(Player player, Vec3 startLoc, Vec3 endLoc, int useTicks, Vec3 viewVec) {
		
	}
	
	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_HARVESTER_LEFT_ARM.get();
	}


}
