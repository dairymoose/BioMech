package com.dairymoose.entity.renderer;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import com.dairymoose.biomech.BioMech;
import com.dairymoose.biomech.entity.GrapplingHook;
import com.dairymoose.biomech.item.armor.MechPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookEntityRenderer extends AzEntityRenderer<GrapplingHook> {

	private static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(
        BioMech.MODID,
        "geo/item/grapple_hook.geo.json"
    );

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
    	BioMech.MODID,
        "textures/item/grapple_arm.png"
    );
	    
	protected GrapplingHookEntityRenderer(AzEntityRendererConfig<GrapplingHook> config, Context context) {
		super(config, context);
	}
	
	public GrapplingHookEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<GrapplingHook>builder(GEO, TEX)
                .build(),
            context
        );
    }
	
	@Override
	public boolean shouldRender(GrapplingHook p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
		return true;
	}
	
	int x=44;
	@Override
	public void render(@NotNull GrapplingHook entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
		if (entity.tickCount <= 1) {
			return;
		}
		
		if (entity.didHit) {
			//override lightning because sometimes, after we hit, it goes inside a block and becomes fully dark
			packedLight = LightTexture.FULL_BRIGHT;
		}
		poseStack.pushPose();
		float xRot = entity.getXRot() + 90.0f;
		float spinSpeed = 10.0f;
		poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));
		poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
		long clientTick = entity.clientHitTick;
		float partialRot = 0.0f;
		if (entity.clientHitTick == -1) {
			clientTick = BioMech.clientTick;
			partialRot = spinSpeed * partialTick;
		}
		poseStack.mulPose(Axis.YP.rotationDegrees(clientTick*spinSpeed + partialRot));
		if (entity.clientHitTick == -1 && entity.didHit) {
			entity.clientHitTick = BioMech.clientTick;
		}
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		poseStack.popPose();
		
		if (!entity.isRemoved())
			this.renderLeash(entity, partialTick, poseStack, bufferSource, Minecraft.getInstance().player);
	}

	public Vec3 getRopeHoldPositionForPlayer(Entity grappleHook, Entity holder, float partialTick) {
      if (holder.isControlledByLocalInstance() && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
         float f = Mth.lerp(partialTick * 0.5F, holder.getYRot(), holder.yRotO) * ((float)Math.PI / 180F);
         float f1 = Mth.lerp(partialTick * 0.5F, holder.getXRot(), holder.xRotO) * ((float)Math.PI / 180F);
         boolean rightArm = true;
         if (grappleHook instanceof GrapplingHook hook) {
        	 if (hook.mechPart == MechPart.LeftArm) {
        		 rightArm = false;
        	 }
         }
         double d0 = rightArm ? -1.0D : 1.0D;
         Vec3 vec3 = new Vec3(0.39D * d0, -0.6D, 0.3D);
         return vec3.xRot(-f1).yRot(-f).add(holder.getEyePosition(partialTick));
      	} else {
      		return holder.getRopeHoldPosition(partialTick);
      	}
	}
	
	private <E extends Entity> void renderLeash(Entity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, E holder) {
		poseStack.pushPose();
		//Vec3 vec3 = holder.getRopeHoldPosition(partialTick);
		Vec3 vec3 = this.getRopeHoldPositionForPlayer(entity, holder, partialTick);
		double d0 = (double) (entity.getYRot() * ((float) Math.PI / 180F)) + (Math.PI / 2D);
		Vec3 vec31 = entity.getLeashOffset(partialTick);
		double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
		double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
		double d3 = Mth.lerp((double) partialTick, entity.xo, entity.getX()) + d1;
		double d4 = Mth.lerp((double) partialTick, entity.yo, entity.getY()) + vec31.y;
		double d5 = Mth.lerp((double) partialTick, entity.zo, entity.getZ()) + d2;
		poseStack.translate(d1, vec31.y, d2);
		float f = (float) (vec3.x - d3);
		float f1 = (float) (vec3.y - d4);
		float f2 = (float) (vec3.z - d5);
		float f3 = 0.025F;
		VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.leash());
		Matrix4f matrix4f = poseStack.last().pose();
		float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
		float f5 = f2 * f4;
		float f6 = f * f4;
		BlockPos blockpos = BlockPos.containing(entity.getEyePosition(partialTick));
		BlockPos blockpos1 = BlockPos.containing(holder.getEyePosition(partialTick));
		int playerBrightness = entity.level().getMaxLocalRawBrightness(blockpos1);
		int targetBrightness = entity.level().getMaxLocalRawBrightness(blockpos);
		// int i = this.getBlockLightLevel(p_115462_, blockpos);
		// int j =
		// this.entityRenderDispatcher.getRenderer(p_115466_).getBlockLightLevel(p_115466_,
		// blockpos1);
		// int k = p_115462_.level().getBrightness(LightLayer.SKY, blockpos);
		// int l = p_115462_.level().getBrightness(LightLayer.SKY, blockpos1);
		
		//int i = targetBrightness;
		//int k = targetBrightness;
		int i = playerBrightness;
		int k = playerBrightness;
		
		int j = playerBrightness;
		int l = playerBrightness;

		for (int i1 = 0; i1 <= 24; ++i1) {
			addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
		}

		for (int j1 = 24; j1 >= 0; --j1) {
			addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
		}

		poseStack.popPose();
	}

	private static void addVertexPair(VertexConsumer p_174308_, Matrix4f p_254405_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
		float f = (float) p_174321_ / 24.0F;
		int i = (int) Mth.lerp(f, (float) p_174313_, (float) p_174314_);
		int j = (int) Mth.lerp(f, (float) p_174315_, (float) p_174316_);
		int k = LightTexture.pack(i, j);
		float f1 = p_174321_ % 2 == (p_174322_ ? 1 : 0) ? 0.5F : 0.6F;
		float f2 = 0.8F * f1;
		float f3 = 0.8F * f1;
		float f4 = 0.8F * f1;
		float f5 = p_174310_ * f;
		
		float bendFactor = 1.1f;
		
		float inverseF = (1.0f - f);
		
		float exponentialYp = (float) Math.pow(f, bendFactor);
		float exponentialInverseYp = (float) Math.pow(inverseF, bendFactor);
		float f6 = p_174311_ > 0.0F ? p_174311_ * exponentialYp : p_174311_ - p_174311_ * exponentialInverseYp;
		
		//float f6 = p_174311_ * f;
		float f7 = p_174312_ * f;
		p_174308_.vertex(p_254405_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
		p_174308_.vertex(p_254405_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
	}

}
