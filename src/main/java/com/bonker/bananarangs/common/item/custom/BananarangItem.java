package com.bonker.bananarangs.common.item.custom;

import com.bonker.bananarangs.common.entity.BananarangEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BananarangItem extends Item {

    public BananarangItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            BananarangEntity.shootFromEntity((ServerLevel) level, player, stack, 0.2F);
        }
        return InteractionResultHolder.success(stack);
    }
}
