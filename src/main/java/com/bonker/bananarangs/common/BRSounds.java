package com.bonker.bananarangs.common;

import com.bonker.bananarangs.Bananarangs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BRSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Bananarangs.MODID);

    public static final RegistryObject<SoundEvent> BANANARANG_SLOT_PLACE = registerSound("anvil_bananarang_slot_place");
    public static final RegistryObject<SoundEvent> BANANARANG_SLOT_TAKE = registerSound("anvil_bananarang_slot_take");
    public static final RegistryObject<SoundEvent> UPGRADE_SLOT_INTERACT = registerSound("anvil_upgrade_slot_interact");
    public static final RegistryObject<SoundEvent> ITEM_SLOT_INTERACT = registerSound("anvil_item_slot_interact");
    public static final RegistryObject<SoundEvent> BANANARANG_THROW = registerSound("bananarang_throw");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Bananarangs.MODID, name)));
    }
}
