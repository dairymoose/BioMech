package com.dairymoose.biomech.item.armor.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.BuzzsawDispatcher;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public abstract class BuzzsawArmArmor extends AbstractMiningArmArmor {

	public final BuzzsawDispatcher dispatcher;

	public BuzzsawArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new BuzzsawDispatcher();
		
		this.blockReachMult = 1.0;
		this.energyPerSecMiss = 0.0f;
		
		this.minSpeedMult = 4.2f;
		this.maxSpeedMult = minSpeedMult;
		
		this.xSize = 3;
		this.ySize = 3;
		this.zSize = 3;
		this.onlyMinesMatchingBlocks = true;
		
		this.miningTool = new ItemStack(Items.IRON_AXE);
		this.wrongToolPenalty = 1.0f;
		this.instantDestroyLeaves = true;
	}

	public static float drillDamage = 5.5f;
	
	@Override
	protected void playSound(Player player, int useTicks, boolean didHit) {
		float volume = 1.2f;
		float laserPitch = 1.0f;
		if (didHit) {
			laserPitch *= 1.10f;
		}
		player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_BUZZSAW_LOOP.get(), SoundSource.PLAYERS, volume, laserPitch, false);
	}
	
	@Override
	protected void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living) {
		float damageMult = 1.0f;
		if (bothHandsActive) {
			//damageMult = 2.0f;
		}
		//living.hurt(player.level().damageSources().playerAttack(player), damageMult*drillDamage*miningPower);
		living.hurt(player.level().damageSources().source(BioMechRegistry.BIOMECH_BONUS_DAMAGE, player), damageMult*drillDamage*miningPower/20.0f);
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
		return BioMechRegistry.ITEM_BUZZSAW_LEFT_ARM.get();
	}


}
