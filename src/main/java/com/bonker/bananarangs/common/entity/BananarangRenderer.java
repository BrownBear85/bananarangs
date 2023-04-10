package com.bonker.bananarangs.common.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class BananarangRenderer<T extends BananarangEntity> extends ThrownItemRenderer<T> {

    private static final int degreesPerTick = 24;
    private final ItemRenderer itemRenderer;

    public BananarangRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F, false);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getYRot()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(((entity.tickCount + partialTicks) * (!entity.isReturning() ? -degreesPerTick : degreesPerTick)) % 360));
        this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level, entity.getId());
        poseStack.popPose();
    }
}