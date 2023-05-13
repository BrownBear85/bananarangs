package com.bonker.bananarangs.client.renderer;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.item.BRItems;
import com.bonker.bananarangs.common.item.BananarangItem;
import com.bonker.bananarangs.common.item.UpgradeItem;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;

public class BananarangBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final ResourceLocation BASE_LOC = new ResourceLocation(Bananarangs.MODID, "item/upgrades/bananarang_base");
    public static BananarangBEWLR INSTANCE;
    public static IClientItemExtensions EXTENSION() {return new IClientItemExtensions() {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return BananarangBEWLR.INSTANCE;
        }
    };}
    public static final List<String> BOTTOM_UPGRADES = List.of("flaming", "sticky", "explosive");
    public static final List<String> TOP_UPGRADES = List.of("piercing", "fling", "pickaxe");

    private List<String> upgrades;
    private int upgrade1 = 0;
    private int upgrade2 = 0;
    boolean upgrade1switched = false;
    long lastUpgrade2switched = 0;

    public BananarangBEWLR(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    private void fillUpgradeList() {
        if (upgrades == null) upgrades = new ImmutableList.Builder<String>().addAll(UpgradeItem.UPGRADE_MAP.keySet()).build();
    }

    private int nextUpgrade(int currentIndex, String otherUpgrade) {
        UpgradeItem upgrade = UpgradeItem.UPGRADE_MAP.get(otherUpgrade);
        if (++currentIndex == upgrades.size()) currentIndex = 0;
        String nextUpgrade = upgrades.get(currentIndex);
        while (upgrade == null || !upgrade.isCompatible(nextUpgrade)) {
            if (++currentIndex == upgrades.size()) currentIndex = 0;
            nextUpgrade = upgrades.get(currentIndex);
        }
        return currentIndex;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.popPose(); // remove translations from ItemRenderer
        poseStack.pushPose();

        String layer1, layer2;
        if (stack.getItem() == BRItems.TAB_ICON.get()) {
            fillUpgradeList();

            long seconds = System.currentTimeMillis() / 1000;
            if (seconds % 5 == 0 && !upgrade1switched) {
                upgrade1 = nextUpgrade(upgrade1, upgrades.get(upgrade2));
                upgrade1switched = true;
            } else {
                upgrade1switched = seconds % 5 == 0;
                if (seconds % 5 != 0 && seconds > lastUpgrade2switched) {
                    upgrade2 = nextUpgrade(upgrade2, upgrades.get(upgrade1));
                    lastUpgrade2switched = seconds;
                }
            }

            layer1 = upgrades.get(upgrade1);
            layer2 = upgrades.get(upgrade2);
        } else {
            layer1 = BananarangItem.getUpgradeInSlot(stack, 0);
            layer2 = BananarangItem.getUpgradeInSlot(stack, 1);
        }
        if (BOTTOM_UPGRADES.contains(layer2) || TOP_UPGRADES.contains(layer1)) { // swap upgrade render layers if you need to
            String temp = layer2;
            layer2 = layer1;
            layer1 = temp;
        }

        boolean shouldRender1 = UpgradeItem.isValid(layer1);
        boolean shouldRender2 = UpgradeItem.isValid(layer2);

        int topLayer = 0; // figure out which layer is on top and should therefore render the item glint
        if (shouldRender1) topLayer = 1;
        if (shouldRender2) topLayer = 2;

        render(stack, BASE_LOC, displayContext, poseStack, bufferSource, RenderType.solid(), topLayer == 0, packedLight, packedOverlay);
        if (shouldRender1) render(stack, UpgradeItem.UPGRADE_MODEL_MAP.get(layer1), displayContext, poseStack, bufferSource, RenderType.translucent(), topLayer == 1, packedLight, packedOverlay);
        if (shouldRender2) render(stack, UpgradeItem.UPGRADE_MODEL_MAP.get(layer2), displayContext, poseStack, bufferSource, RenderType.translucent(), topLayer == 2, packedLight, packedOverlay); // if layer 2 is valid, it will always have glint
    }

    private static void render(ItemStack stack, ResourceLocation modelLoc, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, boolean topLayer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        model = model.applyTransform(displayContext, poseStack, isLeftHand(displayContext));
        poseStack.translate(-0.5, -0.5, -0.5); // replicate ItemRenderer translation

        renderType = RenderTypeHelper.getEntityRenderType(renderType, true);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource, renderType, true, stack.hasFoil() && topLayer);
        itemRenderer.renderModelLists(model, ItemStack.EMPTY, packedLight, packedOverlay, poseStack, vertexConsumer);

        boolean inGui = displayContext == ItemDisplayContext.GUI;
        if (inGui) {
            Lighting.setupForFlatItems();
        }

        if (topLayer) {
            ItemStack attachedItem = BananarangItem.getAttachedItem(stack);
            if (!attachedItem.isEmpty()) {
                if (displayContext == ItemDisplayContext.GUI) {
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                    poseStack.translate(-0.7, -0.7, 0);
                    poseStack.scale(0.7F, 0.7F, 0.7F);
                } else {
                    poseStack.translate(0.125, 0.232, 0.531);
                    poseStack.scale(0.7F, 0.7F, 0.7F);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                }
                itemRenderer.renderStatic(attachedItem, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, null, 42);
            }
        }

        if (inGui) {
            ((MultiBufferSource.BufferSource) bufferSource).endBatch();
            Lighting.setupFor3DItems();
        }

        poseStack.popPose();
    }

    private static boolean isLeftHand(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }
}
