package com.github.tacowasa_059.usefulchatmod.events;

import com.github.tacowasa_059.usefulchatmod.Useful_chat_mod;
import com.github.tacowasa_059.usefulchatmod.config.ChatDisplayConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.CommandSuggestionHelper;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber(modid= Useful_chat_mod.MOD_ID)
public class RenderGUIEvents {
    public static NewChatGui Chatgui=new NewChatGui(Minecraft.getInstance());
    public static boolean allChat=true;
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMouseClickEvent(GuiScreenEvent.MouseClickedEvent event) {
        if(!ChatDisplayConfig.enableSwitchButton.get())return;
        double mouseX=event.getMouseX();
        double mouseY=event.getMouseY();
        Minecraft mc=Minecraft.getInstance();
        double lvt_13_1_ = mc.getMainWindow().getScaledHeight()-40;
        //横・縦の順
        String text1= TextFormatting.BOLD+ I18n.format("usefulchatmod.category.all");
        String text2=TextFormatting.BOLD+I18n.format("usefulchatmod.category.at");
        int width1=mc.fontRenderer.getStringWidth(text1);
        int width2=mc.fontRenderer.getStringWidth(text2);
        int height=mc.fontRenderer.FONT_HEIGHT+2;
        if(mouseX>=0&&mouseX<=2+width1&&mouseY>=(int)(lvt_13_1_)&&mouseY<=(int)(lvt_13_1_)+height){
            allChat=true;
        }
        else if(mouseX>=2+width1&&mouseX<=2+width1+2+width2&&mouseY>=(int)(lvt_13_1_)&&mouseY<=(int)(lvt_13_1_)+height){
            allChat=false;
        }
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMouseScrollEvent(GuiScreenEvent.MouseScrollEvent event) {
        if(!allChat&&Minecraft.getInstance().currentScreen instanceof ChatScreen){
            double delta=event.getScrollDelta();
            Chatgui.addScrollPos(delta);
        }
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onGUIOpenEvent(GuiOpenEvent event) {
        if(event.getGui() instanceof ChatScreen){
            Chatgui.resetScroll();
            allChat=true;
        }
    }
}