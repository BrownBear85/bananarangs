package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.block.BRBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Bananarangs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bananarangs.MODID);


    public static final RegistryObject<BananarangItem> BANANARANG = ITEMS.register("bananarang",
            () -> new BananarangItem(props().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BLANK_UPGRADE = ITEMS.register("blank_upgrade",
            () -> new Item(props()));
    public static final RegistryObject<UpgradeItem> DAMAGE_UPGRADE_1 = ITEMS.register("damage_upgrade_1",
            () -> new UpgradeItem(props(), "damage_1", UpgradeItem.EDGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> DAMAGE_UPGRADE_2 = ITEMS.register("damage_upgrade_2",
            () -> new UpgradeItem(props(), "damage_2", UpgradeItem.EDGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> DAMAGE_UPGRADE_3 = ITEMS.register("damage_upgrade_3",
            () -> new UpgradeItem(props(), "damage_3", UpgradeItem.EDGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> FLAMING_UPGRADE = ITEMS.register("flaming_upgrade",
            () -> new UpgradeItem(props(), "flaming", UpgradeItem.NONE));
    public static final RegistryObject<UpgradeItem> FLING_UPGRADE = ITEMS.register("fling_upgrade",
            () -> new UpgradeItem(props(), "fling", UpgradeItem.CENTER_UPGRADES));
    public static final RegistryObject<UpgradeItem> PIERCING_UPGRADE = ITEMS.register("piercing_upgrade",
            () -> new UpgradeItem(props(), "piercing", UpgradeItem.CENTER_UPGRADES));
    public static final RegistryObject<UpgradeItem.AttachItemUpgrade> PICKAXE_UPGRADE = ITEMS.register("pickaxe_upgrade",
            () -> new UpgradeItem.AttachItemUpgrade(props(), "pickaxe", UpgradeItem.NONE, (stack) -> stack.getItem() instanceof PickaxeItem));
    public static final RegistryObject<BlockItem> BANANARANG_ANVIL = ITEMS.register("bananarang_anvil",
            () -> new BlockItem(BRBlocks.BANANARANG_ANVIL.get(), props()));


    public static Item.Properties props() {
        return new Item.Properties();
    }

    @SubscribeEvent
    public static void registerCreativeTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(Bananarangs.MODID, "bananarangs"),
                builder -> builder.title(Component.translatable("item_group." + Bananarangs.MODID + ".tab"))
                        .icon(() -> new ItemStack(BANANARANG.get()))
                        .noScrollBar()
                        .displayItems((parameters, populator) -> {
                            populator.accept(BANANARANG.get());
                            populator.accept(BANANARANG_ANVIL.get());
                            populator.accept(BLANK_UPGRADE.get());
                            populator.accept(FLAMING_UPGRADE.get());
                            populator.accept(FLING_UPGRADE.get());
                            populator.accept(PIERCING_UPGRADE.get());
                            populator.accept(DAMAGE_UPGRADE_1.get());
                            populator.accept(DAMAGE_UPGRADE_2.get());
                            populator.accept(DAMAGE_UPGRADE_3.get());
                            populator.accept(PICKAXE_UPGRADE.get());
                        }));
    }
}
