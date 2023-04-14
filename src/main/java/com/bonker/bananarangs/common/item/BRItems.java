package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.item.custom.BananarangItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bananarangs.MODID);


    public static final RegistryObject<Item> BANANARANG = ITEMS.register("bananarang",
            () -> new BananarangItem(props().stacksTo(1)));


    public static Item.Properties props() {
        return new Item.Properties();
    }
}
