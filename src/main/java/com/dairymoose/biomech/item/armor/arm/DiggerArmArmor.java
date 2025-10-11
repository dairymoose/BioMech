package com.dairymoose.biomech.item.armor.arm;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.DiggerDispatcher;
import com.dairymoose.biomech.item.anim.DrillDispatcher;
import com.dairymoose.biomech.item.armor.AbstractMiningArm;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public abstract class DiggerArmArmor extends AbstractMiningArm {

	public final DiggerDispatcher dispatcher;

	public DiggerArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new DiggerDispatcher();
		
		this.blockReachMult = 1.0;
		this.energyPerSecMiss = 0.0f;
		
		this.minSpeedMult = 1.2f;
		this.maxSpeedMult = minSpeedMult;
		
		this.xSize = 3;
		this.ySize = 3;
		this.zSize = 3;
		
		this.miningTool = new ItemStack(Items.IRON_SHOVEL);
		
		this.wrongToolPenalty = 2.2f;
		this.onlyMinesMatchingBlocks = true;
		
		this.soundTickPeriod = 10;
	}

	public static float bucketDamage = 2.0f;
	
	@Override
	protected void playSound(Player player, int useTicks, boolean didHit) {
		float volume = 0.7f;
		float pitch = 1.0f;
		if (didHit) {
			//player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_SHOVEL_DIG.get(), SoundSource.PLAYERS, volume, pitch, false);
		}
		player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_SHOVEL_DIG.get(), SoundSource.PLAYERS, volume, pitch, false);
	}
	
	@Override
	protected void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living) {
		float damageMult = 1.0f;
		living.hurt(player.level().damageSources().playerAttack(player), damageMult*bucketDamage*miningPower);
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
		return BioMechRegistry.ITEM_DIGGER_LEFT_ARM.get();
	}


}
