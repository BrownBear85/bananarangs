package com.bonker.bananarangs.common;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.entity.BananarangEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BRDamage {
    public static final ResourceKey<DamageType> BANANARANG = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Bananarangs.MODID, "bananarang"));
    public static final ResourceKey<DamageType> PIERCING_BANANARANG = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Bananarangs.MODID, "piercing_bananarang"));
    public static final ResourceKey<DamageType> EXPLOSIVE_BANANARANG = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Bananarangs.MODID, "explosive_bananarang"));

    public static class BananarangDamageSource extends DamageSource {
        public BananarangDamageSource(BRDamage.Type type, BananarangEntity direct, @Nullable Entity thrower) {
            super(direct.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(type.get()), direct, thrower);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
            if (getDirectEntity() instanceof BananarangEntity bananarang) {
                ItemStack stack = bananarang.getItem();
                if (stack.hasCustomHoverName()) {
                    Component attackerName = getEntity() == null ? bananarang.getDisplayName() : getEntity().getDisplayName();
                    return Component.translatable("death.attack." + this.type().msgId() + ".item", pLivingEntity.getDisplayName(), attackerName, stack.getDisplayName());
                }
            }
            return super.getLocalizedDeathMessage(pLivingEntity);
        }

    }

    public enum Type {
        NORMAL(BANANARANG), PIERCING(PIERCING_BANANARANG), EXPLOSIVE(EXPLOSIVE_BANANARANG);

        private final ResourceKey<DamageType> damageType;

        Type(ResourceKey<DamageType> damageType) {
            this.damageType = damageType;
        }

        public ResourceKey<DamageType> get() {
            return damageType;
        }
    }
}
