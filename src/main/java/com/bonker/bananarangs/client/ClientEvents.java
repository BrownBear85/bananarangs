package com.bonker.bananarangs.client;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.client.renderer.BananarangBEWLR;
import com.bonker.bananarangs.common.entity.BREntities;
import com.bonker.bananarangs.client.renderer.BananarangEntityRenderer;
import com.bonker.bananarangs.common.item.custom.UpgradeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            BananarangBEWLR.INSTANCE = new BananarangBEWLR(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }

        @SubscribeEvent
        public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
            event.register(BananarangBEWLR.BASE_LOC);
            for (ResourceLocation model : UpgradeItem.UPGRADE_MODEL_MAP.values()) {
                event.register(model);
            }
        }

        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BREntities.BANANARANG.get(), BananarangEntityRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

    }
}
