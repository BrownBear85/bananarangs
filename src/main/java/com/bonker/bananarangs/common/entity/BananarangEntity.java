package com.bonker.bananarangs.common.entity;

import com.bonker.bananarangs.common.item.BRItems;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BananarangEntity extends Projectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_RETURNING = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.BOOLEAN);

    public BananarangEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public static BananarangEntity shootFromEntity(LivingEntity shooter, ItemStack stack, double power) {
        BananarangEntity bananarang = new BananarangEntity(BREntities.BANANARANG.get(), shooter.level);
        bananarang.setItem(stack);
        bananarang.setPos(shooter.getEyePosition());
        Vec3 angle = shooter.getLookAngle();
        bananarang.setDeltaMovement(angle.multiply(power, power, power));
        shooter.level.addFreshEntity(bananarang);
        bananarang.setRot(shooter.getXRot(), shooter.getYRot());
        return bananarang;
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount > 1200) {
            discard();
        }

//        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
//        boolean flag = false;
//        if (hitresult.getType() == HitResult.Type.BLOCK) {
//            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
//            BlockState blockstate = this.level.getBlockState(blockpos);
//            if (blockstate.is(Blocks.NETHER_PORTAL)) {
//                this.handleInsidePortal(blockpos);
//                flag = true;
//            } else if (blockstate.is(Blocks.END_GATEWAY)) {
//                BlockEntity blockentity = this.level.getBlockEntity(blockpos);
//                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
//                    TheEndGatewayBlockEntity.teleportEntity(this.level, blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
//                }
//
//                flag = true;
//            }
//        }
//
//        if (hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
//            this.onHit(hitresult);
//        }

        this.move(MoverType.SELF, getDeltaMovement());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        discard();
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        entityData.define(DATA_RETURNING, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setItem(ItemStack.of(tag.getCompound("Item")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("Item", getItem().save(new CompoundTag()));
    }

    public double rotationSpeed() {
        Vec3 delta = getDeltaMovement();
        return 0;
    }

    protected Item getDefaultItem() {
        return BRItems.BANANA_RANG.get();
    }

    public ItemStack getItem() {
        ItemStack itemstack = entityData.get(DATA_ITEM_STACK);
        return itemstack.isEmpty() ? new ItemStack(getDefaultItem()) : itemstack;
    }

    public void setItem(ItemStack stack) {
        if (!stack.is(getDefaultItem()) || stack.hasTag()) {
            entityData.set(DATA_ITEM_STACK, Util.make(stack.copy(), (stack1) -> stack1.setCount(1)));
        }
    }

    public boolean isReturning() {
        return entityData.get(DATA_RETURNING);
    }

    public void setReturning(boolean returning) {
        entityData.set(DATA_RETURNING, returning);
    }
}
