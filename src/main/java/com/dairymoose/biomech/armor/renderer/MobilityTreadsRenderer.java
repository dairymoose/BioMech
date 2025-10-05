package com.dairymoose.biomech.armor.renderer;

import com.dairymoose.biomech.BioMech;

import mod.azure.azurelib.cache.texture.AnimatableTexture;
import mod.azure.azurelib.rewrite.render.AzRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRenderer;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.rewrite.render.armor.AzArmorRendererPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MobilityTreadsRenderer extends AzArmorRenderer {
    private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/mobility_treads.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/mobility_treads_flipbook.png"
    );

    static class AnimatedRendererPipeline extends AzArmorRendererPipeline {

    	protected int currentFrame = 0;
    	protected int lastTick = 0;
    	
    	protected long lastMillis = 0;
    	
		public AnimatedRendererPipeline(AzRendererConfig<ItemStack> config, AzArmorRenderer armorRenderer) {
			super(config, armorRenderer);
		}
		
		@Override
		protected void updateAnimatedTextureFrame(ItemStack animatable) {
			var currentEntity = context().currentEntity();

	        if (currentEntity != null) {
	        	float currentSpeed = 0.0f;
	        	boolean reverse = false;
	        	
	        	int tickUpdatePeriod = 0;
	        	if (BioMech.currentRenderItemStackContext != null) {
	        		CompoundTag tag = BioMech.currentRenderItemStackContext.getTag();

	        		if (tag.contains("CurrentSpeed")) {
						currentSpeed = tag.getFloat("CurrentSpeed");
						if (currentSpeed < 0.0f) {
							reverse = true;
							currentSpeed = -currentSpeed;
						}
					}
	        		
	        		if (Math.abs(currentSpeed) <= 1E-2 || Minecraft.getInstance().isPaused()) {
	        			tickUpdatePeriod = 0;
	        		} else {
	        			//2.357 walking
	        			tickUpdatePeriod = Math.max(10, (int)(100.0f / currentSpeed));
	        		}
	        		//BioMech.LOGGER.info("animatable=" + BioMech.currentRenderItemStackContext + " with TUP=" + tickUpdatePeriod + " and currentSpeed=" + currentSpeed);
	        	}
	        	
	        	if (tickUpdatePeriod > 0) {
	        		long milliTime = System.currentTimeMillis();
	        		long tickDiff = milliTime - lastMillis;
		        	if (tickDiff >= tickUpdatePeriod) {
		        		//BioMech.LOGGER.info("tickDiff=" + tickDiff + " is greater than " + tickUpdatePeriod);
		        		lastMillis = milliTime;
		        		//lastTick = tick;
		        		if (reverse)
		        			--currentFrame;
		        		else
		        			++currentFrame;
		        	}
	        	}

	            AnimatableTexture.setAndUpdate(config.textureLocation(animatable), currentFrame);
	        }
		}
    	
    }
    
    @Override
    protected AzArmorRendererPipeline createPipeline(AzRendererConfig config) {
    	//return super.createPipeline(config);
    	return new AnimatedRendererPipeline(config, this);
    }
    
    public MobilityTreadsRenderer() {
        super(AzArmorRendererConfig.builder(GEO, TEX)
        		.build());
    }
}