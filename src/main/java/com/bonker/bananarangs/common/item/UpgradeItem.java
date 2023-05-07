package com.bonker.bananarangs.common.item;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.client.ClientUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class UpgradeItem extends Item {

    private final String upgrade;
    private final List<String> incompatibleUpgrades;

    public static final List<String> DAMAGE_UPGRADES = List.of("damage_1", "damage_2", "damage_3");
    public static final List<String> CENTER_UPGRADES = List.of("fling", "piercing");
    public static final List<String> POWER_UPGRADES = List.of("power_1", "power_2", "power_3");
    public static final List<String> EDGE_UPGRADES = List.of("flaming, sticky");
    public static final List<String> NONE = List.of();
    public static final Map<String, UpgradeItem> UPGRADE_MAP = new HashMap<>();
    public static final Map<String, ResourceLocation> UPGRADE_MODEL_MAP = new HashMap<>();

    public UpgradeItem(Properties properties, String upgrade, List<String> incompatibleUpgrades) {
        super(properties.stacksTo(1));
        this.upgrade = upgrade;
        this.incompatibleUpgrades = incompatibleUpgrades;
        UPGRADE_MAP.put(upgrade, this);
        UPGRADE_MODEL_MAP.put(upgrade, new ResourceLocation(Bananarangs.MODID, "item/upgrades/" + upgrade));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pIsAdvanced) {
        ClientUtil.addUpgradeItemTooltip(pStack, pTooltip);
    }

    public String getUpgrade() {
        return upgrade;
    }

    public boolean isCompatible(String upgrade) {
        return !upgrade.equals(this.upgrade) && !incompatibleUpgrades.contains(upgrade);
    }

    public static boolean isValid(String upgrade) {
        return UPGRADE_MODEL_MAP.containsKey(upgrade);
    }

    public static class AttachItemUpgrade extends UpgradeItem {
        private final Predicate<ItemStack> validItemPredicate;

        public AttachItemUpgrade(Properties properties, String upgrade, List<String> incompatibleUpgrades, Predicate<ItemStack> validItemPredicate) {
            super(properties, upgrade, incompatibleUpgrades);
            this.validItemPredicate = validItemPredicate;
        }

        public boolean isValidItem(ItemStack stack) {
            return validItemPredicate.test(stack);
        }
    }
}
