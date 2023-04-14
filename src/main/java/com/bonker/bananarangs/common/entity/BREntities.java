package com.bonker.bananarangs.common.entity;

import com.bonker.bananarangs.Bananarangs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BREntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bananarangs.MODID);


    public static final RegistryObject<EntityType<BananarangEntity>> BANANARANG = ENTITY_TYPES.register("bananarang",
            () -> EntityType.Builder.of(BananarangEntity::new, MobCategory.MISC)
                    .sized(0.3F, 0.3F)
                    .updateInterval(4)
                    .clientTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(true)
                    .build(new ResourceLocation(Bananarangs.MODID, "bananarang").toString()));
}
