package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.client.renderer.BananarangBEWLR;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class TabIconItem extends Item {
    public TabIconItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BananarangBEWLR.EXTENSION());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.bananarangs.???").withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_PURPLE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = new ItemStack(BRItems.BANANARANG.get());

        if (!pLevel.isClientSide) {
            List<String> upgrades = new ImmutableList.Builder<String>().addAll(UpgradeItem.UPGRADE_MAP.keySet()).build();

            UpgradeItem upgrade = UpgradeItem.UPGRADE_MAP.get(upgrades.get(pLevel.random.nextInt(upgrades.size())));
            boolean twoUpgrades = pLevel.random.nextFloat() > 0.5;
            String upgrade2 = null;
            if (twoUpgrades) {
                while (upgrade2 == null || !upgrade.isCompatible(upgrade2)) {
                    upgrade2 = upgrades.get(pLevel.random.nextInt(upgrades.size()));
                }
            }

            BananarangItem.setUpgradeInSlot(stack, 0, upgrade.getUpgrade());
            if (twoUpgrades) BananarangItem.setUpgradeInSlot(stack, 1, upgrade2);

            pPlayer.addItem(stack);
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
