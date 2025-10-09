package com.dairymoose.biomech.item.armor;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.GatlingDispatcher;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public abstract class GatlingArmArmor extends AbstractMiningArm {

	public final GatlingDispatcher dispatcher;

	public static float gatlingEnergyPerSec = 10.0f;
	public static float gatlingMinFalloff = 0.5f;
	
	public GatlingArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new GatlingDispatcher();
		
		this.blockReachMult = 100.0;
		
		this.minSpeedMult = 0.75f;
		this.maxSpeedMult = minSpeedMult;
		
		this.wrongToolPenalty = 10.0f;
		
		this.miningTool = new ItemStack(Items.IRON_SHOVEL);
		
		this.soundTickPeriod = 1;
		this.instantDestroyLeaves = true;
		
		this.particleYFirstPerson = 1.265;
		this.particleDistanceFromPlayerFirstPerson = 0.18;
		this.particlePerpendicularDistanceFirstPerson = 0.42;
		
		this.particleYThirdPerson = 1.44;
		this.particleDistanceFromPlayerThirdPerson = 0.25;
		this.particlePerpendicularDistanceThirdPerson = 0.35;
	}

	public static float gatlingDamage = 30.0f;
	
	@Override
	protected void beginHandTick(Player player) {
		this.energyPerSec = gatlingEnergyPerSec;
		this.energyPerSecMiss = gatlingEnergyPerSec;
	}
	
	@Override
	protected void startUsingSound(Player player) {
		float volume = 1.0f;
		float pitch = 1.0f;
		player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_GATLING_SPIN_UP.get(), SoundSource.PLAYERS, volume, pitch, false);
	}
	
	@Override
	protected void playSound(Player player, int useTicks, boolean didHit) {
		float volume = 1.4f;
		float laserPitch = 1.0f;
		player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_GATLING_FIRING.get(), SoundSource.PLAYERS, volume, laserPitch, false);
	}
	
	@Override
	protected void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living) {
		float damageMult = 1.0f;
		if (bothHandsActive) {
			//damageMult = 2.0f;
		}
		
		double damageFalloffFactor = 1.0;
		double distTo = player.distanceTo(living);
		
		//as distTo multiplier is decreased, falloff also decreases
		damageFalloffFactor = Math.min(1.0, Math.max(gatlingMinFalloff, 1.0/Math.log10(distTo*0.60)));
		
		//living.hurt(player.level().damageSources().playerAttack(player), damageMult*drillDamage*miningPower);
		living.hurt(player.level().damageSources().source(BioMechRegistry.BIOMECH_BONUS_DAMAGE, player), damageMult*gatlingDamage*miningPower/20.0f);
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
		BioMech.clientSideItemAnimation(itemStack, dispatcher.SPIN_UP_COMMAND.cmd);
	}
	
	@Override
	protected void miningAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack, dispatcher.FIRING_COMMAND.cmd);
	}
	
	@Override
	protected void thirdPersonStartUsingAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack,
				dispatcher.SPIN_UP_3D_COMMAND.cmd);
	}
	
	@Override
	protected void thirdPersonMiningAnimation(ItemStack itemStack) {
		BioMech.clientSideItemAnimation(itemStack,
				dispatcher.FIRING_3D_COMMAND.cmd);
	}
	
	@Override
	protected void onSpawnParticles(Player player, Vec3 startLoc, Vec3 endLoc, int useTicks, Vec3 viewVec) {
		//double vecScale = 0.60;
		double vecScale = 0.0;
		Vec3 loc = startLoc.add(viewVec.scale(vecScale));
		
		player.level().addParticle((ParticleOptions) BioMechRegistry.PARTICLE_TYPE_MUZZLE_FLASH.get(), loc.x, loc.y, loc.z,
				0.0, 0.0, 0.0);
		
		//flamethrower?
		//player.level().addParticle(ParticleTypes.SMALL_FLAME, loc.x, loc.y, loc.z,
				//viewVec.scale(vecScale).x, viewVec.scale(vecScale).y, viewVec.scale(vecScale).z);
	}
	
	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_GATLING_LEFT_ARM.get();
	}


}
