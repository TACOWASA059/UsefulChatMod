package com.github.tacowasa_059.usefulchatmod.mixin;

import com.github.tacowasa_059.usefulchatmod.config.ChatDisplayConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static net.minecraft.client.gui.screen.Screen.*;

@Mixin(Screen.class)
public abstract class MixinScreen {


    @Shadow protected abstract void openLink(URI p_231156_1_);

    @Inject(at=@At(value = "INVOKE",target = "Lnet/minecraft/client/gui/screen/Screen;hasShiftDown()Z"),
            method="handleComponentClicked(Lnet/minecraft/util/text/Style;)Z",locals = LocalCapture.CAPTURE_FAILHARD,cancellable=true)
    private void handleComponentClicked(@Nullable Style p_230455_1_, CallbackInfoReturnable<Boolean> infoReturnable,ClickEvent clickevent) {
        if (clickevent != null && clickevent.getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD) {
            if (!ChatDisplayConfig.enableCopy.get()) infoReturnable.setReturnValue(false);

            //shiftが押されているときはクリップボードにコピー
            if(hasShiftDown()){
                Minecraft.getInstance().keyboardListener.setClipboardString(clickevent.getValue());
                infoReturnable.setReturnValue(true);
            }
            //altが押されているときはGoogle検索
            else if(hasAltDown()) extracted(infoReturnable, clickevent,"https://www.google.com/search?q=");
            else if(hasControlDown())extracted(infoReturnable, clickevent,"https://twitter.com/search?q=");
                //shiftもaltも押されてないときは入力欄に入れる
            else {
                ChatScreen screen=(ChatScreen)Minecraft.getInstance().currentScreen;
                if(screen!=null){
                    screen.inputField.setText(screen.inputField.getText()+clickevent.getValue());
                    infoReturnable.setReturnValue(false);
                }
            }

        }
    }
    @ModifyArg(
            method = "renderComponentHoverEffect(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/text/Style;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;trimStringToWidth(Lnet/minecraft/util/text/ITextProperties;I)Ljava/util/List;"),
            index=0
    )
    private ITextProperties injected(ITextProperties p_238425_1_) {
        String s = p_238425_1_.getString();
        ITextComponent itextcomponent;
        if (hasShiftDown()) {
            itextcomponent = new TranslationTextComponent(s + TextFormatting.GRAY + " <クリップボードにコピー>");
        } else if (hasAltDown()) {
            itextcomponent = new TranslationTextComponent(s + TextFormatting.GRAY + " <Google検索>");
        } else if (hasControlDown()) {
            itextcomponent = new TranslationTextComponent(s + TextFormatting.GRAY + " <Twitter検索>");
        } else {
            itextcomponent = new TranslationTextComponent(s + TextFormatting.GRAY + " <入力欄に追加>");
        }
        return itextcomponent;
    }
    @Inject(at=@At("HEAD"),method="renderComponentHoverEffect(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/text/Style;II)V",cancellable=true)
    private void renderComponentHoverEffect(MatrixStack p_238653_1_, @Nullable Style p_238653_2_, int p_238653_3_, int p_238653_4_, CallbackInfo info){
        if (p_238653_2_ != null && p_238653_2_.getHoverEvent() != null&&p_238653_2_.getClickEvent()!=null){
            if(p_238653_2_.getClickEvent().getAction()==ClickEvent.Action.COPY_TO_CLIPBOARD&&p_238653_2_.getHoverEvent().getAction()== HoverEvent.Action.SHOW_TEXT){
                if (!ChatDisplayConfig.enableCopy.get())info.cancel();
            }
        }
    }

    private void extracted(CallbackInfoReturnable<Boolean> infoReturnable, ClickEvent clickevent,String s) {
        URL url;
        try{
            url=new URL(s+ clickevent.getValue());
            this.openLink(url.toURI());
        }catch (MalformedURLException | URISyntaxException exception){
            infoReturnable.setReturnValue(false);
        }
        infoReturnable.setReturnValue(true);
    }
}
