package com.dairymoose.biomech.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InstantSmokeParticle extends SmokeParticle {
	public InstantSmokeParticle(ClientLevel p_107685_, double p_107686_, double p_107687_, double p_107688_, double p_107689_, double p_107690_, double p_107691_, float p_107692_, SpriteSet p_107693_) {
		super(p_107685_, p_107686_, p_107687_, p_107688_, 0.1F, 0.1F, 0.1F, p_107692_, p_107693_);
		this.lifetime = 0;
	}

   @OnlyIn(Dist.CLIENT)
   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprite;

      public Provider(SpriteSet p_105793_) {
         this.sprite = p_105793_;
      }
      
      @Override
      public TextureSheetParticle createParticle(SimpleParticleType p_105804_, ClientLevel p_105805_, double p_105806_, double p_105807_, double p_105808_, double p_105809_, double p_105810_, double p_105811_) {
			InstantSmokeParticle instantSmokeParticle = new InstantSmokeParticle(p_105805_, p_105806_, p_105807_, p_105808_, p_105809_, p_105810_, p_105811_, 1.0f, sprite);
			return instantSmokeParticle;
      }
   }
}