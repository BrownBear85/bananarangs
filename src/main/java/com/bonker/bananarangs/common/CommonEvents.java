package com.bonker.bananarangs.common;

import com.bonker.bananarangs.BananarangsMod;
import net.minecraftforge.fml.common.Mod;

public class CommonEvents {

    @Mod.EventBusSubscriber(modid = BananarangsMod.MODID)
    public static class CommonModEvents {

    }

    @Mod.EventBusSubscriber(modid = BananarangsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonForgeEvent {

    }
}
