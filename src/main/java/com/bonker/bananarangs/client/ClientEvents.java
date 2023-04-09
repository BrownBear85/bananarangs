package com.bonker.bananarangs.client;

import com.bonker.bananarangs.BananarangsMod;
import com.bonker.bananarangs.common.entity.BREntities;
import com.bonker.bananarangs.common.entity.BananarangRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = BananarangsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BREntities.BANANARANG.get(), BananarangRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = BananarangsMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }
}
