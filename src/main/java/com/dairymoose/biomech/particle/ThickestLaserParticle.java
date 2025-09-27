package com.dairymoose.biomech.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ThickestLaserParticle extends LaserParticle {
	public ThickestLaserParticle(ClientLevel p_105773_, double p_105774_, double p_105775_, double p_105776_, double p_105777_, double p_105778_, double p_105779_) {
      super(p_105773_, p_105774_, p_105775_, p_105776_, p_105777_, p_105778_, p_105779_);
      this.quadSize *= 2.1f;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprite;

      public Provider(SpriteSet p_105793_) {
         this.sprite = p_105793_;
      }
      
      @Override
      public TextureSheetParticle createParticle(SimpleParticleType p_105804_, ClientLevel p_105805_, double p_105806_, double p_105807_, double p_105808_, double p_105809_, double p_105810_, double p_105811_) {
			ThickestLaserParticle laserParticle = new ThickestLaserParticle(p_105805_, p_105806_, p_105807_, p_105808_, p_105809_, p_105810_, p_105811_);
			laserParticle.pickSprite(this.sprite);
			return laserParticle;
      }
   }
}