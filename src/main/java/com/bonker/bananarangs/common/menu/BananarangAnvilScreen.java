package com.bonker.bananarangs.common.menu;

import com.bonker.bananarangs.Bananarangs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BananarangAnvilScreen extends AbstractContainerScreen<BananarangAnvilMenu> {
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Bananarangs.MODID,"textures/gui/bananarang_anvil.png");

    public BananarangAnvilScreen(BananarangAnvilMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(graphics, pMouseX, pMouseY);
        if (!menu.hasBananarang()) renderPlaceBananarangText(graphics);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(BG_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        if (menu.hasBananarang()) {
            graphics.blit(BG_TEXTURE, x + 32, y + 24, 0, 166, 112, 18);
        }
        int attachedItemSlot = menu.getItemUpgradeSlot();
        if (attachedItemSlot != -1) {
            int slotX = x + (attachedItemSlot == 0 ? 32 : 126);
            int uOffset = attachedItemSlot == 0 ? 112 : 130;
            graphics.blit(BG_TEXTURE, slotX, y + 41, uOffset, 166, 18, 28);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {
        super.renderLabels(graphics, pMouseX, pMouseY);
    }

    private void renderPlaceBananarangText(GuiGraphics graphics) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        String text = Component.translatable("container.bananarang_anvil.place_bananarang").getString();
        graphics.drawString(font, text, x + 88 - font.width(text) / 2, y + 50, 0x909090, false);
    }
}
