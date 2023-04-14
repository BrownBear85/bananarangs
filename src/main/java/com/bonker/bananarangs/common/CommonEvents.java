package com.bonker.bananarangs.common;

import com.bonker.bananarangs.Bananarangs;
import net.minecraftforge.fml.common.Mod;

public class CommonEvents {

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID)
    public static class CommonModEvents {

    }

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonForgeEvent {

    }
}
