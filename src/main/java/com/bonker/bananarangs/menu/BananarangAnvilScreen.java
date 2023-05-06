package com.bonker.bananarangs.menu;

import com.bonker.bananarangs.Bananarangs;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("SuspiciousNameCombination")
public class BananarangAnvilScreen extends AbstractContainerScreen<BananarangAnvilMenu> {
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Bananarangs.MODID,"textures/gui/bananarang_anvil.png");

    public BananarangAnvilScreen(BananarangAnvilMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
        if (!menu.hasBananarang()) renderPlaceBananarangText(pPoseStack);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        if (menu.hasBananarang()) {
            blit(pPoseStack, x + 32, y + 24, 0, 166, 112, 18);
        }
        int attachedItemSlot = menu.getItemUpgradeSlot();
        if (attachedItemSlot != -1) {
            int slotX = x + (attachedItemSlot == 0 ? 32 : 126);
            int uOffset = attachedItemSlot == 0 ? 112 : 130;
            blit(pPoseStack, slotX, y + 41, uOffset, 166, 18, 28);
        }
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }

    private void renderPlaceBananarangText(PoseStack poseStack) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        String text = Component.translatable("container.bananarang_anvil.place_bananarang").getString();
        font.draw(poseStack, text, x + 88 - font.width(text) / 2F, y + 50, 0x909090);
    }
}
