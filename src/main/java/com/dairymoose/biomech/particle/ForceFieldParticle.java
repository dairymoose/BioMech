package com.dairymoose.biomech.particle;

import com.dairymoose.biomech.item.armor.EmergencyForcefieldUnitArmor;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ForceFieldParticle extends TextureSheetParticle {
	private final SpriteSet sprites;
	
	private Player player = null;
	private float savedQuadSize = 0.0f;
	private double initialDeltaX = 0.0;
	private double initialDeltaY = 0.0;
	private double initialDeltaZ = 0.0;
	public ForceFieldParticle(ClientLevel p_171904_, double x, double y, double z, float p_171908_, float p_171909_, float p_171910_, double d4, double d5, double d6, float p_171914_, SpriteSet p_171915_, float p_171916_, int p_171917_, float p_171918_, boolean p_171919_) {
		super(p_171904_, x, y, z, 0.0D, 0.0D, 0.0D);
		player = EmergencyForcefieldUnitArmor.currentPlayer;
		
		initialDeltaX = player.getX() - this.x;
		initialDeltaY = player.getY() - this.y;
		initialDeltaZ = player.getZ() - this.z;
		
		 //this.friction = 0.96F;
		this.friction = 1.0f;
		//this.friction = 0.0f;
		// this.gravity = p_171918_;
		this.gravity = 0.0f;
		this.speedUpWhenYMotionIsBlocked = false;
		this.sprites = p_171915_;
		this.xd = d4;
		this.yd = d5;
		this.zd = d6;
		this.xd *= (double) p_171908_;
		this.yd *= (double) p_171909_;
		this.zd *= (double) p_171910_;
		
		//this.xd += inputDeltaX;
		//this.yd += inputDeltaY;
		//this.zd += inputDeltaZ;

		this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F) * 2.5F;
		this.quadSize *= 0.55F * p_171914_;
		this.savedQuadSize = this.quadSize;
		
		//initially, the particle is not visible - this avoids a visual artifact
		this.quadSize = 0.0f;
		
		this.lifetime = EmergencyForcefieldUnitArmor.forceFieldDurationTicks;
		// this.hasPhysics = p_171919_;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		if (player != null) {
			this.xd = 0.0;
			this.zd = 0.0;
		}
		
		super.tick();
		if (player != null) {
			double deltaX = player.getX() - this.x - initialDeltaX;
			double deltaY = player.getY() - this.y - initialDeltaY;
			double deltaZ = player.getZ() - this.z - initialDeltaZ;
			if (deltaX != 0.0 || deltaY != 0.0 || deltaZ != 0.0)
				this.move(deltaX, deltaY, deltaZ);
		}
		
		if (this.age == 2) {
			this.quadSize = this.savedQuadSize;
		}
		this.quadSize *= 0.988;
	}
	
	protected ForceFieldParticle(ClientLevel p_107685_, double p_107686_, double p_107687_, double p_107688_, double p_107689_, double p_107690_, double p_107691_, float p_107692_, SpriteSet p_107693_) {
		this(p_107685_, p_107686_, p_107687_, p_107688_, 0.1F, 0.1F, 0.1F, p_107689_, p_107690_, p_107691_, p_107692_, p_107693_, 0.3F, 8, -0.1F, true);
	}
	
	public ParticleRenderType getRenderType() {
		return LaserParticle.PARTICLE_SHEET_LIT_TRANSLUCENT;
	}

	@Override
	protected int getLightColor(float p_107249_) {
		return LightTexture.FULL_BRIGHT;
	}
	
   @OnlyIn(Dist.CLIENT)
   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprite;

      public Provider(SpriteSet p_105793_) {
         this.sprite = p_105793_;
      }
      
      @Override
      public TextureSheetParticle createParticle(SimpleParticleType p_105804_, ClientLevel p_105805_, double p_105806_, double p_105807_, double p_105808_, double p_105809_, double p_105810_, double p_105811_) {
			ForceFieldParticle forceFieldParticle = new ForceFieldParticle(p_105805_, p_105806_, p_105807_, p_105808_, p_105809_, p_105810_, p_105811_, 1.0f, sprite);
			forceFieldParticle.pickSprite(this.sprite);
			return forceFieldParticle;
      }
   }
}