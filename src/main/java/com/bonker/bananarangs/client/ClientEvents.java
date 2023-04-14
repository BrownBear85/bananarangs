package com.bonker.bananarangs.client;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.entity.BREntities;
import com.bonker.bananarangs.client.renderer.entity.BananarangRenderer;
import com.bonker.bananarangs.common.item.BRItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void buildCreativeTabs(CreativeModeTabEvent.BuildContents event) {
            if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                event.accept(BRItems.BANANARANG);
            }
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BREntities.BANANARANG.get(), BananarangRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }
}
