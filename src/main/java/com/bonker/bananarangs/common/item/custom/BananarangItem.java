package com.bonker.bananarangs.common.item.custom;

import com.bonker.bananarangs.common.networking.BRNetworking;
import com.bonker.bananarangs.common.networking.BananarangC2SPacket;
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
        if (level.isClientSide) {
            BRNetworking.sendToServer(new BananarangC2SPacket(player.getDeltaMovement(), hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
