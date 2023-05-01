package com.bonker.bananarangs.common.block;

import com.bonker.bananarangs.Bananarangs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BRBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bananarangs.MODID);


    public static final RegistryObject<BananarangAnvilBlock> BANANARANG_ANVIL = BLOCKS.register("bananarang_anvil",
            () -> new BananarangAnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL).noOcclusion().isViewBlocking((state, level, pos) -> false)));
}
