package com.bonker.bananarangs.common.networking;

import com.bonker.bananarangs.common.entity.BananarangEntity;
import com.bonker.bananarangs.common.item.BRItems;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BananarangC2SPacket {

    private final Vec3 delta;
    private final InteractionHand hand;

    public BananarangC2SPacket(Vec3 delta, InteractionHand hand) {
        this.delta = delta;
        this.hand = hand;
    }

    public BananarangC2SPacket(FriendlyByteBuf buffer) {
        this.delta = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeDouble(delta.x);
        buffer.writeDouble(delta.y);
        buffer.writeDouble(delta.z);
        buffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();
        if (sender == null) return;
        ServerLevel level = sender.getLevel();
        ItemStack stack = sender.getItemInHand(hand);
        BananarangEntity.shootFromEntity(level, sender, Util.make(stack.copy(), stack1 -> stack1.setCount(1)), 1.0F, delta);
        stack.shrink(1);
    }
}
