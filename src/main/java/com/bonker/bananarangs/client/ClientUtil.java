package com.bonker.bananarangs.client;

import com.bonker.bananarangs.common.item.BananarangItem;
import com.bonker.bananarangs.common.item.UpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ClientUtil {
    public static void addBananarangTooltip(ItemStack stack, List<Component> tooltip) {
        String slot0 = BananarangItem.getUpgradeInSlot(stack, 0);
        String slot1 = BananarangItem.getUpgradeInSlot(stack, 1);
        boolean slot0valid = UpgradeItem.isValid(slot0);
        boolean slot1valid = UpgradeItem.isValid(slot1);
        String key0; String key1 = key0 = "item.bananarangs.bananarang.no_upgrade";
        if (slot0valid) {
            ResourceLocation slot0loc = ForgeRegistries.ITEMS.getKey(UpgradeItem.UPGRADE_MAP.get(BananarangItem.getUpgradeInSlot(stack, 0)));
            key0 = slot0loc == null ? "null" : "item.bananarangs." + slot0loc.getPath();
        }
        if (slot1valid) {
            ResourceLocation slot1loc = ForgeRegistries.ITEMS.getKey(UpgradeItem.UPGRADE_MAP.get(BananarangItem.getUpgradeInSlot(stack, 1)));
            key1 = slot1loc == null ? "null" : "item.bananarangs." + slot1loc.getPath();
        }

        float damage = 6.0F + 3.0F * BananarangItem.damageUpgrade(stack);
        if (BananarangItem.hasUpgrade(stack, "piercing")) damage *= 0.5;
        tooltip.add(Component.translatable("item.bananarangs.bananarang.damage").withStyle(ChatFormatting.GRAY).append(Component.literal(Float.toString(damage)).withStyle(ChatFormatting.GOLD)));

        ItemStack attachedItem = BananarangItem.getAttachedItem(stack);
        if (!attachedItem.isEmpty()) {
            tooltip.add(Component.translatable("item.bananarangs.bananarang.attachedItem").withStyle(ChatFormatting.GRAY).append(attachedItem.getDisplayName()));
            if (Screen.hasShiftDown()) {
                List<Component> lines = attachedItem.getTooltipLines(null, TooltipFlag.NORMAL);
                lines = lines.subList(1, lines.size());
                for (Component line : lines) {
                    if (line.getContents() == ComponentContents.EMPTY) continue;
                    tooltip.add(Component.literal("|   ").append(MutableComponent.create(line.getContents())).withStyle(ChatFormatting.GRAY));
                }
            }
        }

        tooltip.add(Component.translatable(key0).withStyle(ChatFormatting.YELLOW));
        if (slot0valid && !key0.equals("null") && Screen.hasShiftDown())
            tooltip.add(Component.translatable(key0 + ".description").withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.translatable(key1).withStyle(ChatFormatting.YELLOW));
        if (slot1valid && !key1.equals("null") && Screen.hasShiftDown())
            tooltip.add(Component.translatable(key1 + ".description").withStyle(ChatFormatting.DARK_GRAY));

        if (!Screen.hasShiftDown() && BananarangItem.isUpgraded(stack)) tooltip.add(Component.translatable("item.bananarangs.bananarang.shift").withStyle(ChatFormatting.DARK_GRAY));
    }

    public static void addUpgradeItemTooltip(ItemStack stack, List<Component> tooltip) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (loc == null) return;
        tooltip.add(Component.translatable("item.bananarangs." + loc.getPath() + ".description").withStyle(ChatFormatting.GRAY));
    }
}
