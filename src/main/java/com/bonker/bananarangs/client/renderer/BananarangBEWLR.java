package com.bonker.bananarangs.client.renderer;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.item.BananarangItem;
import com.bonker.bananarangs.common.item.UpgradeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeHelper;

public class BananarangBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final ResourceLocation BASE_LOC = new ResourceLocation(Bananarangs.MODID, "item/upgrades/bananarang_base");
    public static BananarangBEWLR INSTANCE;

    public BananarangBEWLR(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.popPose(); // remove translations from ItemRenderer
        poseStack.pushPose();

        String layer1 = BananarangItem.getUpgradeInSlot(stack, 0);
        String layer2 = BananarangItem.getUpgradeInSlot(stack, 1);
        if (layer2.equals("flaming")) { // always render flaming below other upgrades
            layer2 = layer1;
            layer1 = "flaming";
        }

        boolean layer1valid = UpgradeItem.isValid(layer1);
        boolean layer2valid = UpgradeItem.isValid(layer2);

        int glintLayer = -1;
        if (stack.hasFoil()) { // figure out which layer is on top and should therefore render the item glint
            glintLayer = 0;
            if (layer1valid) glintLayer = 1;
            if (layer2valid) glintLayer = 2;
        }

        renderItemModel(stack, BASE_LOC, displayContext, poseStack, bufferSource, RenderType.solid(), glintLayer == 0, packedLight, packedOverlay);
        if (layer1valid) renderItemModel(stack, UpgradeItem.UPGRADE_MODEL_MAP.get(layer1), displayContext, poseStack, bufferSource, RenderType.translucent(), glintLayer == 1, packedLight, packedOverlay);
        if (layer2valid) renderItemModel(stack, UpgradeItem.UPGRADE_MODEL_MAP.get(layer2), displayContext, poseStack, bufferSource, RenderType.translucent(), glintLayer == 2, packedLight, packedOverlay); // if layer 2 is valid, it will always have glint
    }

    private static void renderItemModel(ItemStack stack, ResourceLocation modelLoc, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, boolean glint, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        model = model.applyTransform(displayContext, poseStack, isLeftHand(displayContext));
        poseStack.translate(-0.5, -0.5, -0.5); // replicate ItemRenderer translation

        if (displayContext != ItemDisplayContext.GUI) // if in gui, use default solid/translucent render type, otherwise
            renderType = RenderTypeHelper.getEntityRenderType(renderType, true); // use entity render type
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource, renderType, true, glint);
        itemRenderer.renderModelLists(model, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vertexConsumer);

        poseStack.popPose();
    }

    private static boolean isLeftHand(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }
}
