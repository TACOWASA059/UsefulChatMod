package com.github.tacowasa_059.usefulchatmod.sounds;

import com.github.tacowasa_059.usefulchatmod.Useful_chat_mod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundRegistrator {
    public static final SoundEvent NOTIFICATION;

    static {
        NOTIFICATION = addSoundsToRegistry("notification");
    }

    private static SoundEvent addSoundsToRegistry(String soundId) {
        ResourceLocation shotSoundLocation = new ResourceLocation(Useful_chat_mod.MOD_ID, soundId);
        SoundEvent soundEvent = new SoundEvent(shotSoundLocation);
        soundEvent.setRegistryName(shotSoundLocation);
        return soundEvent;
    }
}