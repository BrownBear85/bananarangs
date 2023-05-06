package com.bonker.bananarangs;

import com.bonker.bananarangs.common.BRSounds;
import com.bonker.bananarangs.common.block.BRBlocks;
import com.bonker.bananarangs.common.entity.BREntities;
import com.bonker.bananarangs.common.item.BRItems;
import com.bonker.bananarangs.menu.BRMenus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bananarangs.MODID)
public class Bananarangs {

    public static final String MODID = "bananarangs";

    public Bananarangs() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BRItems.ITEMS.register(bus);
        BRBlocks.BLOCKS.register(bus);
        BREntities.ENTITY_TYPES.register(bus);
        BRMenus.MENUS.register(bus);
        BRSounds.SOUND_EVENTS.register(bus);
    }
}
