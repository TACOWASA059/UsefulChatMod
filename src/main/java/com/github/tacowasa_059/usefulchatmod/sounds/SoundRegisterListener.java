package com.github.tacowasa_059.usefulchatmod.sounds;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundRegisterListener {
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SoundRegistrator.NOTIFICATION);
    }
}