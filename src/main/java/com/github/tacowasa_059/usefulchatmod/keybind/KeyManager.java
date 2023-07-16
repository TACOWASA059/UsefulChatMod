package com.github.tacowasa_059.usefulchatmod.keybind;

import com.github.tacowasa_059.usefulchatmod.Useful_chat_mod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class KeyManager {
    public static KeyBinding keyBinding;
    public static void register(final FMLClientSetupEvent event){
        keyBinding=keybindCreate("setting", 298);
        ClientRegistry.registerKeyBinding(keyBinding);
    }
    private static KeyBinding keybindCreate(String name ,int key){
        return new KeyBinding("key."+ Useful_chat_mod.MOD_ID+"."+name,key,"key.category."+Useful_chat_mod.MOD_ID);
    }
}