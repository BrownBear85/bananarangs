package com.bonker.bananarangs.common.item.custom;

import com.bonker.bananarangs.Bananarangs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.*;

public class UpgradeItem extends Item {

    private final String upgrade;
    private final List<String> incompatibleUpgrades;

    public static final List<String> EDGE_UPGRADES = List.of("damage_1", "damage_2", "damage_3");
    public static final List<String> CENTER_UPGRADES = List.of("fling", "piercing");
    public static final List<String> NONE = List.of();
    public static final Map<String, ResourceLocation> UPGRADE_MODEL_MAP = new HashMap<>();

    public UpgradeItem(Properties properties, String upgrade, List<String> incompatibleUpgrades) {
        super(properties.stacksTo(1));
        this.upgrade = upgrade;
        this.incompatibleUpgrades = incompatibleUpgrades;
        UPGRADE_MODEL_MAP.put(upgrade, new ResourceLocation(Bananarangs.MODID, "item/upgrades/" + upgrade));
    }

    public String getUpgrade() {
        return upgrade;
    }

    public boolean isCompatible(String upgrade) {
        return !incompatibleUpgrades.contains(upgrade);
    }

    public static boolean isValid(String upgrade) {
        return UPGRADE_MODEL_MAP.containsKey(upgrade);
    }
}
