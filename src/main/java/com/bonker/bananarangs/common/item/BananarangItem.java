package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.client.renderer.BananarangBEWLR;
import com.bonker.bananarangs.common.entity.BananarangEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BananarangItem extends Item {

    public BananarangItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            BananarangEntity.shootFromEntity((ServerLevel) level, player, Util.make(stack.copy(), stack1 -> stack1.setCount(1)), 0.9F);
//            stack.shrink(1);
            player.setItemInHand(hand, Util.make(stack, stack1 -> stack1.shrink(1)));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        String slot0 = getUpgradeInSlot(stack, 0);
        String slot1 = getUpgradeInSlot(stack, 1);
        boolean slot0valid = UpgradeItem.isValid(slot0);
        boolean slot1valid = UpgradeItem.isValid(slot1);
        String key0; String key1 = key0 = "item.bananarangs.bananarang.no_upgrade";
        if (slot0valid) {
            ResourceLocation slot0loc = ForgeRegistries.ITEMS.getKey(UpgradeItem.UPGRADE_MAP.get(getUpgradeInSlot(stack, 0)));
            key0 = slot0loc == null ? "null" : "item.bananarangs." + slot0loc.getPath();
        }
        if (slot1valid) {
            ResourceLocation slot1loc = ForgeRegistries.ITEMS.getKey(UpgradeItem.UPGRADE_MAP.get(getUpgradeInSlot(stack, 1)));
            key1 = slot1loc == null ? "null" : "item.bananarangs." + slot1loc.getPath();
        }

        float damage = 6.0F + 3.0F * damageUpgrade(stack);
        if (hasUpgrade(stack, "piercing")) damage *= 0.5;
        tooltip.add(Component.translatable("item.bananarangs.bananarang.damage").withStyle(ChatFormatting.GRAY).append(Component.literal(Float.toString(damage)).withStyle(ChatFormatting.GOLD)));

        ItemStack attachedItem = getAttachedItem(stack);
        if (!attachedItem.isEmpty()) tooltip.add(Component.translatable("item.bananarangs.bananarang.attachedItem").withStyle(ChatFormatting.GRAY).append(attachedItem.getDisplayName()));

        tooltip.add(Component.translatable(key0).withStyle(ChatFormatting.YELLOW));
        if (slot0valid && !key0.equals("null") && Screen.hasShiftDown())
            tooltip.add(Component.translatable(key0 + ".description").withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(Component.translatable(key1).withStyle(ChatFormatting.YELLOW));
        if (slot1valid && !key1.equals("null") && Screen.hasShiftDown())
            tooltip.add(Component.translatable(key1 + ".description").withStyle(ChatFormatting.DARK_GRAY));

        if (!Screen.hasShiftDown()) tooltip.add(Component.translatable("item.bananarangs.bananarang.shift").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BananarangBEWLR.INSTANCE;
            }
        });
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return getAttachedItem(pStack).isBarVisible();
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return getAttachedItem(pStack).getBarWidth();
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return getAttachedItem(pStack).getBarColor();
    }

    /** @param slot 0 or 1
     * @return the upgrade string or an empty string if no upgrade is in that slot */
    public static String getUpgradeInSlot(ItemStack stack, int slot) {
        if (slot != 0 && slot != 1) return "";
        return stack.getOrCreateTag().getString("slot" + slot); // slot0 or slot1
    }

    public static void setUpgradeInSlot(ItemStack stack, int slot, String upgrade) {
        if (upgrade.equals("") || UpgradeItem.isValid(upgrade)) {
            stack.getOrCreateTag().putString("slot" + slot, upgrade);
        }
    }

    public static boolean hasUpgrade(ItemStack stack, String upgrade) {
        if (!UpgradeItem.isValid(upgrade)) return false;
        if (upgrade.equals(getUpgradeInSlot(stack, 0))) return true;
        return upgrade.equals(getUpgradeInSlot(stack, 1));
    }

    public static ItemStack getAttachedItem(ItemStack bananarang) {
        return ItemStack.of(bananarang.getOrCreateTag().getCompound("attachedItem"));
    }

    public static void setAttachedItem(ItemStack bananarang, ItemStack stack) {
        bananarang.getOrCreateTag().put("attachedItem", stack.serializeNBT());
    }

    public static boolean hasPickaxe(ItemStack bananarang) {
        return getAttachedItem(bananarang).getItem() instanceof PickaxeItem
                && hasUpgrade(bananarang, "pickaxe");
    }

    public static int pickaxeEfficiency(ItemStack bananarang) {
        return getAttachedItem(bananarang).getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
    }

    public static int damageUpgrade(ItemStack bananarang) {
        if (hasUpgrade(bananarang, "damage_1")) return 1;
        if (hasUpgrade(bananarang, "damage_2")) return 2;
        if (hasUpgrade(bananarang, "damage_3")) return 3;
        return 0;
    }
}
