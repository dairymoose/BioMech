package com.dairymoose.biomech.item.armor;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.BioMechRegistry;
import com.dairymoose.biomech.item.anim.MiningLaserDispatcher;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public abstract class MiningLaserArmArmor extends AbstractMiningArm {

	public final MiningLaserDispatcher dispatcher;

	public MiningLaserArmArmor(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
		super(p_40386_, p_266831_, p_40388_);
		this.suitEnergy = 10;
		this.hidePlayerModel = true;
		this.dispatcher = new MiningLaserDispatcher();
		
		this.minSpeedMult = 2.5f;
		this.maxSpeedMult = minSpeedMult * 6.0f;
	}

	public static float laserDamageAtMaxPower = 6.0f;
	
	@Override
	protected void playSound(Player player, int useTicks, boolean didHit) {
		float volume = 0.45f;
		float laserPitch = 0.85f + this.getMiningPower(useTicks)*0.3f;
		if (didHit) {
			laserPitch *= 1.04f;
		}
		player.level().playLocalSound(player.position().x, player.position().y, player.position().z, BioMechRegistry.SOUND_EVENT_LASER_LOOP.get(), SoundSource.PLAYERS, volume, laserPitch, false);
	}
	
	@Override
	protected void dealEntityDamage(Player player, boolean bothHandsActive, float miningPower, LivingEntity living) {
		float damageMult = 1.0f;
		if (bothHandsActive) {
			//damageMult = 2.0f;
		}
		//living.hurt(player.level().damageSources().playerAttack(player), damageMult*laserDamageAtMaxPower*miningPower);
		
		if (miningPower < 0.33f) {
			miningPower = 0.33f;;
		}
		
		living.hurt(player.level().damageSources().source(BioMechRegistry.BIOMECH_BONUS_DAMAGE, player), damageMult*laserDamageAtMaxPower*miningPower/20.0f);
		if (living.getRemainingFireTicks() <= 30) {
			living.setRemainingFireTicks(30);
		}
	}
	
	//power from 0.0 to 1.0
	public float getLaserPower(int useTicks) {
		return Math.min(useTicks / (float) (SECONDS_UNTIL_MAX_LASER * 20), 1.0f);
	}
	
	@Override
	protected float getMiningPower(int useTicks) {
		return this.getLaserPower(useTicks);
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
		Vec3 endToStartVec = endLoc.subtract(startLoc);
		int max = (int) (endToStartVec.length() * 16);
		double startDist = 0.00;
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
			player.level().addParticle(laserParticle, loc.x, loc.y, loc.z,
					viewVec.scale(vecScale).x, viewVec.scale(vecScale).y, viewVec.scale(vecScale).z);
		}
	}
	
	@Override
	public Item getLeftArmItem() {
		return BioMechRegistry.ITEM_MINING_LASER_LEFT_ARM.get();
	}


}
