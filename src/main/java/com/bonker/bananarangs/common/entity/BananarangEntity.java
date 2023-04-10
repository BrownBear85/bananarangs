package com.bonker.bananarangs.common.entity;

import com.bonker.bananarangs.common.item.BRItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BananarangEntity extends Projectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_RETURNING = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.INT);

    private int age = 0;

    public BananarangEntity(EntityType<? extends BananarangEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static BananarangEntity shootFromEntity(ServerLevel level, LivingEntity shooter, ItemStack stack, double power) {
        BananarangEntity bananarang = BREntities.BANANARANG.get().spawn(level, null, (entity) -> {
            entity.setItem(stack);
            entity.setPos(shooter.getEyePosition().subtract(0, 0.3, 0));
            Vec3 angle = shooter.getLookAngle();
            entity.setDeltaMovement(angle.multiply(power, power, power));
        }, BlockPos.ZERO, MobSpawnType.SPAWN_EGG, false, false);
        if (bananarang != null) {
            bananarang.setRot(shooter.getXRot(), shooter.getYRot());
        }
        return bananarang;
    }

    @Override
    public void tick() {
        super.tick();


        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level.getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level, blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }

                flag = true;
            }
        }

        if (hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level.addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25D, d0 - vec3.y * 0.25D, d1 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double)f));

        this.setPos(d2, d0, d1);

//        if (isReturning()) {
//
//        }
//
//        this.move(MoverType.SELF, getDeltaMovement());

        if (++age >= 100) {
            drop();
        }
    }

    private void drop() {
        if (!level.isClientSide) {
            EntityType.ITEM.spawn((ServerLevel) level, (CompoundTag) null, (itemEntity) -> {
                itemEntity.setItem(getItem());
                itemEntity.setPos(position());
            }, BlockPos.ZERO, MobSpawnType.COMMAND, false, false);
        }
        discard();
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        entityData.define(DATA_RETURNING, false);
        entityData.define(DATA_AGE, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setItem(ItemStack.of(tag.getCompound("Item")));
        setReturning(tag.getBoolean("returning"));
        age = tag.getInt("Age");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("Item", getItem().save(new CompoundTag()));
        tag.putBoolean("returning", isReturning());
        tag.putInt("Age", age);
    }

    protected Item getDefaultItem() {
        return BRItems.BANANA_RANG.get();
    }

    public ItemStack getItem() {
        ItemStack itemstack = entityData.get(DATA_ITEM_STACK);
        return itemstack.isEmpty() ? new ItemStack(getDefaultItem()) : itemstack;
    }

    public void setItem(ItemStack stack) {
        if (!stack.is(getDefaultItem()) || stack.hasTag() || stack.getCount() > 0) {
            entityData.set(DATA_ITEM_STACK, stack.copy());
        }
    }

    public boolean isReturning() {
        return entityData.get(DATA_RETURNING);
    }

    public void setReturning(boolean returning) {
        entityData.set(DATA_RETURNING, returning);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
