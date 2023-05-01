package com.bonker.bananarangs.menu;

import com.bonker.bananarangs.Bananarangs;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BRMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Bananarangs.MODID);

    public static final RegistryObject<MenuType<BananarangAnvilMenu>> BANANARANG_ANVIL = MENUS.register("bananarang_anvil",
            () -> IForgeMenuType.create(BananarangAnvilMenu::create));
}
