package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.client.renderer.BananarangBEWLR;
import com.bonker.bananarangs.common.entity.BananarangEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BananarangBEWLR.INSTANCE;
            }
        });
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
