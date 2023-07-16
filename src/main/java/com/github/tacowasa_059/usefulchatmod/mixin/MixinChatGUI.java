package com.github.tacowasa_059.usefulchatmod.mixin;

import com.github.tacowasa_059.usefulchatmod.events.RenderGUIEvents;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(NewChatGui.class)
public class MixinChatGUI extends AbstractGui {
    @Shadow @Final private Minecraft mc;

    @Shadow @Final private List<ChatLine<IReorderingProcessor>> drawnChatLines;

    @Inject(at=@At("HEAD"),method="func_238494_b_(DD)Lnet/minecraft/util/text/Style;",cancellable=true)
    private void func_238494_b_(double p_238494_1_, double p_238494_3_, CallbackInfoReturnable<Style> cir){
        Minecraft mc=Minecraft.getInstance();
        if((mc.currentScreen instanceof ChatScreen)) {
            if(p_238494_3_<Minecraft.getInstance().getMainWindow().getScaledHeight()-40-20*9.0 * (Minecraft.getInstance().gameSettings.chatLineSpacing + 1.0))cir.cancel();
            if ((Object) this != RenderGUIEvents.Chatgui && !RenderGUIEvents.allChat) {
                Style style=RenderGUIEvents.Chatgui.func_238494_b_(p_238494_1_, p_238494_3_);
                cir.setReturnValue(style);
            }
        }
    }
    @Inject(at=@At("HEAD"),method="clearChatMessages(Z)V",cancellable = true)
    private void clearChatMessages(boolean p_146231_1_,CallbackInfo info){
        if((Object)this!= RenderGUIEvents.Chatgui)RenderGUIEvents.Chatgui.clearChatMessages(p_146231_1_);
    }
    @Inject(at = @At("HEAD"), method ="func_238492_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;I)V",cancellable = true)
    private void func_238492_a_(MatrixStack p_238492_1_, int p_238492_2_,CallbackInfo info) {
        Minecraft mc=Minecraft.getInstance();
        if((mc.currentScreen instanceof ChatScreen)){
            if((Object)this!= RenderGUIEvents.Chatgui&&! RenderGUIEvents.allChat){
                info.cancel();
                //System.out.println(this.drawnChatLines.size());
                 RenderGUIEvents.Chatgui.func_238492_a_(p_238492_1_,p_238492_2_);
            }
            RenderChatTab(p_238492_1_, mc);
        }
    }

    private static void RenderChatTab(MatrixStack p_238492_1_, Minecraft mc) {
        double lvt_9_1_ = mc.gameSettings.chatOpacity * 0.8999999761581421 + 0.10000000149011612;
        double lvt_11_1_ = mc.gameSettings.accessibilityTextBackgroundOpacity;
        double lvt_13_1_ = 9.0 * (mc.gameSettings.chatLineSpacing + 1.0);
        double lvt_15_1_ = -8.0 * (mc.gameSettings.chatLineSpacing + 1.0) + 4.0 * mc.gameSettings.chatLineSpacing;
        int lvt_23_2_ = (int)(255.0  * lvt_9_1_);
        int lvt_24_2_ = (int)(255.0* lvt_11_1_);

        p_238492_1_.push();
        //横・縦の順
        String text1=TextFormatting.BOLD+ I18n.format("usefulchatmod.category.all");
        String text2=TextFormatting.BOLD+I18n.format("usefulchatmod.category.at");

        if( RenderGUIEvents.allChat)text1=TextFormatting.UNDERLINE+text1;
        else text2=TextFormatting.UNDERLINE+text2;

        int width1= mc.fontRenderer.getStringWidth(text1);
        int width2= mc.fontRenderer.getStringWidth(text2);
        int height= mc.fontRenderer.FONT_HEIGHT+2;
        int f1= RenderGUIEvents.allChat?lvt_24_2_ << 24:0xFF696969;
        int f2= RenderGUIEvents.allChat?0xFF696969:lvt_24_2_ << 24;
        fill(p_238492_1_, 0, (int)(lvt_13_1_), 2+width1, (int)(lvt_13_1_)+height, f1);
        fill(p_238492_1_, 2+width1, (int)(lvt_13_1_), 2+width1+2+width2, (int)(  lvt_13_1_)+height, f2);
        RenderSystem.enableBlend();
        //横・縦の順
        mc.fontRenderer.drawTextWithShadow(p_238492_1_, new TranslationTextComponent(text1), 2F, (float)((int)(lvt_15_1_+2*  lvt_13_1_)), 16777215 + (lvt_23_2_ << 24));
        mc.fontRenderer.drawTextWithShadow(p_238492_1_, new TranslationTextComponent(text2), 2F+2+width1, (float)((int)(lvt_15_1_+2*  lvt_13_1_)), 16777215 + (lvt_23_2_ << 24));
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
        p_238492_1_.pop();
    }


}
