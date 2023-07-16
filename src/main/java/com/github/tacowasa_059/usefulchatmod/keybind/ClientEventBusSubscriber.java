package com.github.tacowasa_059.usefulchatmod.keybind;

import com.github.tacowasa_059.usefulchatmod.Useful_chat_mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Useful_chat_mod.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        KeyManager.register(event);
    }
}
