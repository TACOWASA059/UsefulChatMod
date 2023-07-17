package com.github.tacowasa_059.usefulchatmod.mixin;

import com.github.tacowasa_059.usefulchatmod.config.ChatDisplayConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static net.minecraft.client.gui.screen.Screen.hasShiftDown;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow protected abstract void insertText(String p_231155_1_, boolean p_231155_2_);

    @Inject(at=@At("HEAD"),method="handleComponentClicked(Lnet/minecraft/util/text/Style;)Z",cancellable=true)
    private void handleComponentClicked(@Nullable Style p_230455_1_, CallbackInfoReturnable<Boolean> infoReturnable){
        if (p_230455_1_ == null) {
            infoReturnable.setReturnValue(false);
        } else {
            ClickEvent clickevent = p_230455_1_.getClickEvent();
            if (clickevent != null && clickevent.getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD) {
                if (!ChatDisplayConfig.enableCopy.get()) infoReturnable.setReturnValue(false);
                else{
                    if (!hasShiftDown()&&Minecraft.getInstance().currentScreen instanceof ChatScreen) {
                        ChatScreen screen=(ChatScreen)Minecraft.getInstance().currentScreen;
                        screen.inputField.setText(screen.inputField.getText()+clickevent.getValue());
                        infoReturnable.setReturnValue(false);
                    }else{
                        Minecraft.getInstance().keyboardListener.setClipboardString(clickevent.getValue());
                        infoReturnable.setReturnValue(true);
                    }
                }
            }
        }
    }
    @Inject(at=@At("HEAD"),method="renderComponentHoverEffect(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/text/Style;II)V",cancellable=true)
    private void renderComponentHoverEffect(MatrixStack p_238653_1_, @Nullable Style p_238653_2_, int p_238653_3_, int p_238653_4_, CallbackInfo info){
        if (p_238653_2_ != null && p_238653_2_.getHoverEvent() != null&&p_238653_2_.getClickEvent()!=null){
            if(p_238653_2_.getClickEvent().getAction()==ClickEvent.Action.COPY_TO_CLIPBOARD&&p_238653_2_.getHoverEvent().getAction()== HoverEvent.Action.SHOW_TEXT){
                if (!ChatDisplayConfig.enableCopy.get())info.cancel();
            }
        }
    }
}
