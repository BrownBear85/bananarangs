package com.bonker.bananarangs.common;

import com.bonker.bananarangs.common.entity.BananarangEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BRDamage {
    public static class BananarangDamageSource extends EntityDamageSource {
        public BananarangDamageSource(BRDamage.Type type, BananarangEntity direct, @Nullable Entity thrower) {
            super(type.id(), thrower == null ? direct : thrower);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
            if (getDirectEntity() instanceof BananarangEntity bananarang) {
                ItemStack stack = bananarang.getItem();
                if (stack.hasCustomHoverName()) {
                    Component attackerName = getEntity() == null ? bananarang.getDisplayName() : getEntity().getDisplayName();
                    return Component.translatable("death.attack." + this.getMsgId() + ".item", pLivingEntity.getDisplayName(), attackerName, stack.getDisplayName());
                }
            }
            return super.getLocalizedDeathMessage(pLivingEntity);
        }
    }

    public enum Type {
        NORMAL("bananarang"), PIERCING("piercingBananarang"), EXPLOSIVE("explosiveBananarang");

        private final String id;

        Type(String id) {
            this.id = id;
        }

        public String id() {
            return id;
        }
    }
}
