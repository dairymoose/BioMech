package com.dairymoose.biomech.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MuzzleFlashParticle extends TextureSheetParticle {
	private final SpriteSet sprites;
	
	public MuzzleFlashParticle(ClientLevel p_171904_, double p_171905_, double p_171906_, double p_171907_, float p_171908_, float p_171909_, float p_171910_, double p_171911_, double p_171912_, double p_171913_, float p_171914_, SpriteSet p_171915_, float p_171916_, int p_171917_, float p_171918_, boolean p_171919_) {
		super(p_171904_, p_171905_, p_171906_, p_171907_, 0.0D, 0.0D, 0.0D);
	      this.friction = 0.96F;
	      this.gravity = p_171918_;
	      this.speedUpWhenYMotionIsBlocked = false;
	      this.sprites = p_171915_;
	      this.xd *= (double)p_171908_;
	      this.yd *= (double)p_171909_;
	      this.zd *= (double)p_171910_;
	      this.xd += p_171911_;
	      this.yd += p_171912_;
	      this.zd += p_171913_;
	      this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F) * 2.0F;
	      this.quadSize *= 0.45F * p_171914_;
	      this.lifetime = 0;
	      this.hasPhysics = p_171919_;
	}

	@Override
	public void tick() {
		super.tick();
	}
	
	protected MuzzleFlashParticle(ClientLevel p_107685_, double p_107686_, double p_107687_, double p_107688_, double p_107689_, double p_107690_, double p_107691_, float p_107692_, SpriteSet p_107693_) {
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
			MuzzleFlashParticle muzzleFlashParticle = new MuzzleFlashParticle(p_105805_, p_105806_, p_105807_, p_105808_, p_105809_, p_105810_, p_105811_, 1.0f, sprite);
			muzzleFlashParticle.pickSprite(this.sprite);
			return muzzleFlashParticle;
      }
   }
}