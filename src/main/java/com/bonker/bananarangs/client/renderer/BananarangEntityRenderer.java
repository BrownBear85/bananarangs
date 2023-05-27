package com.bonker.bananarangs.client.renderer;

import com.bonker.bananarangs.common.entity.BananarangEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class BananarangEntityRenderer<T extends BananarangEntity> extends ThrownItemRenderer<T> {

    private static final int degreesPerTick = 24;
    private final ItemRenderer itemRenderer;

    public BananarangEntityRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F, false);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);                                      // raise model to ~ center of hitbox
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90));                       // flatten model
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180 - entity.getYRot()));   // rotate to facing rotation
        poseStack.mulPose(Vector3f.XP.rotationDegrees(entity.getXRot()));         // tilt to facing rotation
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(                            // spin!!!
                ((entity.tickCount + partialTicks) * degreesPerTick) % 360)); // calculate spin
        this.itemRenderer.renderStatic(entity.getItem(), ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getId());
        poseStack.popPose();
    }
}