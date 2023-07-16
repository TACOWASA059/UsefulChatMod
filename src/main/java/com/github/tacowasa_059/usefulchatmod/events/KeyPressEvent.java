package com.github.tacowasa_059.usefulchatmod.events;

import com.github.tacowasa_059.usefulchatmod.Useful_chat_mod;
import com.github.tacowasa_059.usefulchatmod.config.ConfigScreen;
import com.github.tacowasa_059.usefulchatmod.keybind.KeyManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Useful_chat_mod.MOD_ID,bus= Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class KeyPressEvent {
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        on_input(mc, event.getKey());
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.world == null) return;
        on_input(mc, event.getButton());
    }

    private static void on_input(Minecraft mc, int key) {
        if (mc.currentScreen == null && KeyManager.keyBinding.isPressed()) {
            mc.displayGuiScreen(new ConfigScreen(null));
        }
    }
}