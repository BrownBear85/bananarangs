package com.bonker.bananarangs.common;

import com.bonker.bananarangs.Bananarangs;
import com.bonker.bananarangs.common.block.BRBlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

public class CommonEvents {

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID)
    public static class CommonModEvents {

    }

    @Mod.EventBusSubscriber(modid = Bananarangs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonForgeEvents {

    }
}
