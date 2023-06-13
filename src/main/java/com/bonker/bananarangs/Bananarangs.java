package com.bonker.bananarangs;

import com.bonker.bananarangs.common.BRSounds;
import com.bonker.bananarangs.common.block.BRBlocks;
import com.bonker.bananarangs.common.entity.BREntities;
import com.bonker.bananarangs.common.item.BRItems;
import com.bonker.bananarangs.common.item.UpgradeItem;
import com.bonker.bananarangs.common.menu.BRMenus;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bananarangs.MODID)
public class Bananarangs {

    public static final String MODID = "bananarangs";

    public Bananarangs() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BRItems.ITEMS.register(bus);
        BRItems.TABS.register(bus);
        BRBlocks.BLOCKS.register(bus);
        BREntities.ENTITY_TYPES.register(bus);
        BRMenus.MENUS.register(bus);
        BRSounds.SOUND_EVENTS.register(bus);

        bus.addListener(Bananarangs::commonSetup);
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        for (UpgradeItem upgradeItem : UpgradeItem.UPGRADE_MAP.values()) {
            ImmutableList.Builder<Component> builder = new ImmutableList.Builder<>();
            for (UpgradeItem otherUpgradeItem : UpgradeItem.UPGRADE_MAP.values()) {
                if (upgradeItem != otherUpgradeItem && (!upgradeItem.isCompatible(otherUpgradeItem.getUpgrade()) || !otherUpgradeItem.isCompatible(upgradeItem.getUpgrade()))) {
                    builder.add(Component.literal(" - ").append(otherUpgradeItem.getName(ItemStack.EMPTY)).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
            upgradeItem.incompatibleTooltip = builder.build();
        }
    }
}
