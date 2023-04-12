package com.bonker.bananarangs;

import com.bonker.bananarangs.common.entity.BREntities;
import com.bonker.bananarangs.common.item.BRItems;
import com.bonker.bananarangs.common.networking.BRNetworking;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BananarangsMod.MODID)
public class BananarangsMod {

    public static final String MODID = "bananarangs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BananarangsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BRNetworking.register();

        BRItems.ITEMS.register(bus);
        BREntities.ENTITY_TYPES.register(bus);
    }
}
