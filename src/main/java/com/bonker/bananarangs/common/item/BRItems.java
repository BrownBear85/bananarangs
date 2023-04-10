package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.BananarangsMod;
import com.bonker.bananarangs.common.item.custom.BananarangItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BananarangsMod.MODID);


    public static final RegistryObject<Item> BANANA_RANG = ITEMS.register("bananarang", () -> new BananarangItem(props()));


    public static Item.Properties props() {
        return new Item.Properties();
    }
}
