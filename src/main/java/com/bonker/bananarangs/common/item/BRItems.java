package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.block.BRBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.bonker.bananarangs.Util.listOf;

public class BRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bananarangs.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB.location(), Bananarangs.MODID);

    public static final RegistryObject<Item> BANANA = ITEMS.register("banana",
            () -> new Item(props().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).build())));
    public static final RegistryObject<BananarangItem> BANANARANG = ITEMS.register("bananarang",
            () -> new BananarangItem(props().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BLANK_UPGRADE = ITEMS.register("blank_upgrade",
            () -> new Item(props()));
    public static final RegistryObject<BlockItem> BANANARANG_ANVIL = ITEMS.register("bananarang_anvil",
            () -> new BlockItem(BRBlocks.BANANARANG_ANVIL.get(), props()));
    public static final RegistryObject<TabIconItem> TAB_ICON = ITEMS.register("tab_icon",
            () -> new TabIconItem(props()));

    public static final RegistryObject<UpgradeItem> DAMAGE_UPGRADE_1 = ITEMS.register("damage_upgrade_1",
            () -> new UpgradeItem(props(), "damage_1", UpgradeItem.DAMAGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> DAMAGE_UPGRADE_2 = ITEMS.register("damage_upgrade_2",
            () -> new UpgradeItem(props(), "damage_2", UpgradeItem.DAMAGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> DAMAGE_UPGRADE_3 = ITEMS.register("damage_upgrade_3",
            () -> new UpgradeItem(props(), "damage_3", UpgradeItem.DAMAGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> FLAMING_UPGRADE = ITEMS.register("flaming_upgrade",
            () -> new UpgradeItem(props(), "flaming", UpgradeItem.EDGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> FLING_UPGRADE = ITEMS.register("fling_upgrade",
            () -> new UpgradeItem(props(), "fling", UpgradeItem.CENTER_UPGRADES));
    public static final RegistryObject<UpgradeItem> PIERCING_UPGRADE = ITEMS.register("piercing_upgrade",
            () -> new UpgradeItem(props(), "piercing", UpgradeItem.CENTER_UPGRADES));
    public static final RegistryObject<UpgradeItem.AttachItemUpgrade> PICKAXE_UPGRADE = ITEMS.register("pickaxe_upgrade",
            () -> new UpgradeItem.AttachItemUpgrade(props(), "pickaxe", UpgradeItem.POWER_UPGRADES, (stack) -> stack.getItem() instanceof PickaxeItem));
    public static final RegistryObject<UpgradeItem> POWER_UPGRADE_1 = ITEMS.register("power_upgrade_1",
            () -> new UpgradeItem(props(), "power_1", UpgradeItem.POWER_UPGRADES));
    public static final RegistryObject<UpgradeItem> POWER_UPGRADE_2 = ITEMS.register("power_upgrade_2",
            () -> new UpgradeItem(props(), "power_2", UpgradeItem.POWER_UPGRADES));
    public static final RegistryObject<UpgradeItem> POWER_UPGRADE_3 = ITEMS.register("power_upgrade_3",
            () -> new UpgradeItem(props(), "power_3", UpgradeItem.POWER_UPGRADES));
    public static final RegistryObject<UpgradeItem> STICKY_UPGRADE = ITEMS.register("sticky_upgrade",
            () -> new UpgradeItem(props(), "sticky", UpgradeItem.EDGE_UPGRADES));
    public static final RegistryObject<UpgradeItem> EXPLOSIVE_UPGRADE = ITEMS.register("explosive_upgrade",
            () -> new UpgradeItem(props(), "explosive", listOf("pickaxe")));

    public static Item.Properties props() {
        return new Item.Properties();
    }

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("bananarangs",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + Bananarangs.MODID + ".tab"))
                    .icon(() -> new ItemStack(TAB_ICON.get()))
                    .noScrollBar()
                    .displayItems((parameters, populator) -> {
                        populator.accept(BANANARANG.get());
                        populator.accept(BANANARANG_ANVIL.get());
                        populator.accept(DAMAGE_UPGRADE_1.get());
                        populator.accept(DAMAGE_UPGRADE_2.get());
                        populator.accept(DAMAGE_UPGRADE_3.get());
                        populator.accept(POWER_UPGRADE_1.get());
                        populator.accept(POWER_UPGRADE_2.get());
                        populator.accept(POWER_UPGRADE_3.get());
                        populator.accept(FLAMING_UPGRADE.get());
                        populator.accept(STICKY_UPGRADE.get());
                        populator.accept(PIERCING_UPGRADE.get());
                        populator.accept(FLING_UPGRADE.get());
                        populator.accept(PICKAXE_UPGRADE.get());
                        populator.accept(EXPLOSIVE_UPGRADE.get());
                        populator.accept(BANANA.get());
                        populator.accept(BLANK_UPGRADE.get());
                    })
                    .build());
}
