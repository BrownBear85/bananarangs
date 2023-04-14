package com.bonker.bananarangs.common.entity;

import com.bonker.bananarangs.common.damage.BRDamageSources;
import com.bonker.bananarangs.common.item.BRItems;
import com.bonker.bananarangs.common.item.custom.BananarangItem;
import com.bonker.bananarangs.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class BananarangEntity extends Projectile implements ItemSupplier {

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> DATA_RETURNING = SynchedEntityData.defineId(BananarangEntity.class, EntityDataSerializers.BOOLEAN);
    private static final double deceleration = 0.96;
    private static final double speedThreshold = 0.15;
    private static final double maxSpeed = 2.0;

    private int age = 0;

    private boolean hasPickaxe = false;
    private ItemStack attachedItem = ItemStack.EMPTY;
    private float pickaxeLevel = -1;
    private float pickaxeEfficiency = 0;
    private boolean flaming = false;
    private boolean piercing = false;
    private boolean fling = false;
    private int damageUpgrade = 0;

    public BananarangEntity(EntityType<? extends BananarangEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static void shootFromEntity(ServerLevel level, LivingEntity shooter, ItemStack stack, double power, Vec3 delta) {
        BREntities.BANANARANG.get().spawn(level, null, (entity) -> {
            entity.setOwner(shooter);
            entity.setItem(stack);
            entity.setPos(shooter.getEyePosition().subtract(0, 0.2, 0));
            entity.setRot(shooter.getXRot(), shooter.getYRot());
            entity.setDeltaMovement(shooter.getLookAngle()
                    .add(delta.multiply(3, 1, 3))
                    .multiply(power, power, power));
        }, BlockPos.ZERO, MobSpawnType.COMMAND, false, false);
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = getOwner();
        if (owner != null) {
            Vec3 targetPos = owner.getEyePosition().subtract(0, 1.0, 0);
            if (this.isReturning()) {                           // if it is coming back,
                if (shouldDrop(targetPos)) {                    // and it's close to the player,
                    if (owner instanceof ServerPlayer player) { //
                        if (player.addItem(getItem())) {        // then give  it back to the player
                            discard();                          // and delete the entity
                        } else {                                //
                            drop();                             // or drop it as an item
                        }
                        player.getCooldowns().addCooldown(BRItems.BANANARANG.get(), 20);
                    }
                }

                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add( // stolen from ThrownTrident.java
                        targetPos.subtract(this.position())                     // calculate the trajectory to the player and
                        .normalize().scale(0.05D)));                            // set the delta movement to it
            } else {
                scaleDelta(deceleration);
                if (shouldReturn()) {
                    setReturning(true);
                }
            }
        }

        Vec3 posOld = position();
        setPos(posOld.add(getDeltaMovement()));

        HitResult hitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
            onHit(hitResult);
        }

        setPos(posOld);
        move(MoverType.SELF, getDeltaMovement());

        if (++age >= 200) {
            drop();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Entity owner = getOwner();
        if (entity != owner) {
            float damage = 6.0F + 3.0F * damageUpgrade; // each damage upgrade increases damage by 1.5 heart
            if (piercing || hasPickaxe) { // nerf piercing damage for balance and decrease damage if it has a pickaxe
                damage *= 0.5;
            }
            entity.hurt(new BRDamageSources.BananarangDamageSource(piercing, this, getOwner()), damage);
            Vec3 delta = getDeltaMovement().multiply(1, 0, 1).normalize(); // remove the vertical delta of the bananarang
            if (fling) {
                delta = delta.scale(2); // knockback is 5 times more if fling
            } else { // fling doesn't care about knockback resistance, so only calculate it if fling is false
                double knockbackResistance = entity instanceof LivingEntity livingEntity ? Math.max(0, 1 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) : 0;
                delta = delta.scale(0.1 * knockbackResistance);
            }
            entity.push(delta.x, fling ? 0.5 : 0.01, delta.z); // if fling, fling the entity in the air, otherwise do normal vertical knockback
            if (isOnFire()) {
                entity.setSecondsOnFire(5);
            }
            setReturning(true);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (hasPickaxe) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            double destroySpeed = state.getDestroySpeed(level, pos);
            if (canMine(state) && destroySpeed <= 5 + pickaxeEfficiency * 10) {
                if (!level.isClientSide) {
                    level.destroyBlock(hitResult.getBlockPos(), true);
                }
            } else {
                setDeltaMovement(Vec3.ZERO);
            }
            scaleDelta(1 - 1 / (pickaxeEfficiency + 1)); //TODO figure out how to incorporate block strength to make destroying stronger blocks slower
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096;
    }

    public void setUpgrades(ItemStack bananarang) {
        hasPickaxe = BananarangItem.hasPickaxe(bananarang);
        attachedItem = BananarangItem.getAttachedItem(bananarang);
        pickaxeLevel = BananarangItem.pickaxeLevel(bananarang);
        pickaxeEfficiency = BananarangItem.pickaxeEfficiency(bananarang);
        flaming = BananarangItem.flaming(bananarang);
        if (flaming) setSecondsOnFire(100);
        piercing = BananarangItem.piercing(bananarang);
        fling = BananarangItem.fling(bananarang);
        damageUpgrade = BananarangItem.damageUpgrade(bananarang);
    }

    private boolean canMine(BlockState state) {
        if (attachedItem.getItem() instanceof DiggerItem diggerItem) {
            return diggerItem.isCorrectToolForDrops(attachedItem, state);
        }
        return false;
    }

    private boolean shouldReturn() {
        Vec3 delta = getDeltaMovement();
        return Math.abs(delta.x) < speedThreshold && Math.abs(delta.y) < speedThreshold && Math.abs(delta.z) < speedThreshold;
    }

    private boolean shouldDrop(Vec3 targetPos) {
        Entity owner = getOwner();
        return owner != null && position().distanceToSqr(targetPos) < 2.0;
    }

    private void scaleDelta(double factor) {
        setDeltaMovement(MathUtil.clamp(getDeltaMovement().multiply(factor, factor, factor), -maxSpeed, maxSpeed));
    }

    private void drop() {
        if (!level.isClientSide) {
            spawnAtLocation(getItem());
        }
        discard();
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        entityData.define(DATA_RETURNING, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setItem(ItemStack.of(tag.getCompound("Item")));
        setReturning(tag.getBoolean("returning"));
        age = tag.getInt("age");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Item", getItem().save(new CompoundTag()));
        tag.putBoolean("returning", isReturning());
        tag.putInt("age", age);
    }

    protected Item getDefaultItem() {
        return BRItems.BANANARANG.get();
    }

    public ItemStack getItem() {
        ItemStack itemstack = entityData.get(DATA_ITEM_STACK);
        return itemstack.isEmpty() ? new ItemStack(getDefaultItem()) : itemstack;
    }

    public void setItem(ItemStack stack) {
        if (!stack.is(getDefaultItem()) || stack.hasTag() || stack.getCount() > 0) {
            entityData.set(DATA_ITEM_STACK, stack.copy());
            setUpgrades(stack);
        }
    }

    public boolean isReturning() {
        return entityData.get(DATA_RETURNING);
    }

    public void setReturning(boolean returning) {
        entityData.set(DATA_RETURNING, returning);
    }
}
