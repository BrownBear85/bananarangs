package com.bonker.bananarangs.common.item.custom;

import com.bonker.bananarangs.common.networking.BRNetworking;
import com.bonker.bananarangs.common.networking.BananarangC2SPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.TierSortingRegistry;

public class BananarangItem extends Item {

    public BananarangItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            BRNetworking.sendToServer(new BananarangC2SPacket(player.getDeltaMovement(), hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static ItemStack getAttachedItem(ItemStack bananarang) {
        return ItemStack.of(bananarang.getOrCreateTag().getCompound("attachedItem"));
    }

    public static boolean hasPickaxe(ItemStack bananarang) {
        return getAttachedItem(bananarang).getItem() instanceof PickaxeItem;
    }

    public static int pickaxeEfficiency(ItemStack bananarang) {
        return getAttachedItem(bananarang).getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
    }

    public static boolean flaming(ItemStack bananarang) {
        return bananarang.getOrCreateTag().getBoolean("flaming");
    }

    public static boolean piercing(ItemStack bananarang) {
        return bananarang.getOrCreateTag().getBoolean("piercing");
    }

    public static boolean fling(ItemStack bananarang) {
        return bananarang.getOrCreateTag().getBoolean("fling");
    }

    public static int damageUpgrade(ItemStack bananarang) {
        return bananarang.getOrCreateTag().getInt("damageUpgrade");
    }
}
