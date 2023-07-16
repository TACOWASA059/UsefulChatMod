package com.github.tacowasa_059.usefulchatmod.events;

import com.github.tacowasa_059.usefulchatmod.Useful_chat_mod;
import com.github.tacowasa_059.usefulchatmod.config.ChatDisplayConfig;
import com.github.tacowasa_059.usefulchatmod.sounds.SoundRegistrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.github.tacowasa_059.usefulchatmod.events.RenderGUIEvents.Chatgui;

@Mod.EventBusSubscriber(modid= Useful_chat_mod.MOD_ID)
public class ChatEventHandler{

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        System.out.println(event.getMessage());
        if (event.getType() == ChatType.CHAT) {//chatのとき
            ITextComponent component=event.getMessage().deepCopy();
            if (component instanceof TranslationTextComponent) {
                component=modify_itextComponent(component, Color.fromTextFormatting(TextFormatting.getValueByName(ChatDisplayConfig.chatColor.get())), ChatDisplayConfig.displayTimestamp.get(),true);
                System.out.println(component);
                event.setMessage(component);
            }
            else if(component instanceof TextComponent){//プラグイン出力の場合
                TextComponent textComponent=(TextComponent) component;
                String text=textComponent.getUnformattedComponentText();
                if(textComponent.getSiblings().size()==0)return;

                String text2=textComponent.getSiblings().get(0).getUnformattedComponentText();
                if(text.charAt(0) == '<' && text2.charAt(0) == '>') {
                    text += ">";
                    text2 = text2.substring(1);
                }
                if(text.charAt(0) == '<' && text.charAt(text.length() - 1) == '>') {
                    TranslationTextComponent translationTextComponent;

                    if(textComponent.getStyle().getColor()!=null){
                        text=TextFormatting.getValueByName(textComponent.getStyle().getColor().getName())+text;
                        text=TextFormatting.BOLD+text;
                    }else{
                        text=TextFormatting.WHITE+text;
                        text=TextFormatting.BOLD+text;
                    }
                    if(ChatDisplayConfig.displayTimestamp.get()){
                        translationTextComponent= new TranslationTextComponent((TextFormatting.getValueByName(ChatDisplayConfig.TimestampColor.get())+getDate())+text);
                    }
                    else translationTextComponent= new TranslationTextComponent(text);
                    String name = text.substring(1, text.length() - 1);
                    if (translationTextComponent.getStyle().getClickEvent() == null) {
                        translationTextComponent.setStyle(translationTextComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + name)));
                    }
                    TranslationTextComponent translationTextComponent1 = new TranslationTextComponent(text2);
                    translationTextComponent1.setStyle(textComponent.getSiblings().get(0).getStyle());
                    translationTextComponent1.setStyle(translationTextComponent1.getStyle().setColor(Color.fromTextFormatting(TextFormatting.getValueByName(ChatDisplayConfig.chatColor.get()))).setBold(false));
                    if(ChatDisplayConfig.enableCopy.get()) translationTextComponent1.setStyle(translationTextComponent1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text2.trim())).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent(text2.trim()))));
                    //第3階層以降はそのまま入れる
                    for (ITextComponent iTextComponent : textComponent.getSiblings().get(0).getSiblings()) {
                        translationTextComponent1.appendSibling(iTextComponent);
                    }
                    translationTextComponent.appendSibling(translationTextComponent1);
                    event.setMessage(translationTextComponent);
                    System.out.println(event.getMessage());
                }
            }
        }

        else if(event.getType()==ChatType.SYSTEM){//tellなど
            ITextComponent component=event.getMessage().deepCopy();
            System.out.println(component.getString());

            if (component instanceof TranslationTextComponent) {//tellrawをはじく
                TranslationTextComponent translationComponent = (TranslationTextComponent) component;
                if(translationComponent.getKey().equals("commands.message.display.outgoing")){
                    Chatgui.func_238493_a_(event.getMessage(),0,0,true);
                }
                if (translationComponent.getKey().equals("commands.message.display.incoming")) {//messageが送られてくるときのみ
                    try {
                        component = modify_itextComponent(component, Color.fromTextFormatting(TextFormatting.getValueByName(ChatDisplayConfig.tellColor.get())),ChatDisplayConfig.displayTimestamp.get(),false);
                        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundRegistrator.NOTIFICATION, 1.0f));
                    } catch (ClassCastException e) {
                        component = event.getMessage().deepCopy();
                    }
                    System.out.println(component);

                    event.setMessage(component);
                    Chatgui.func_238493_a_(event.getMessage(),0,0,true);
                }
            }
        }
    }
    private static Color getColor(ITextComponent textcomponent){
        Color color=textcomponent.getStyle().getColor();
        for(ITextComponent component:textcomponent.getSiblings()){
            Color color1=getColor(component);
            if(color1!=null&&color==null)color=color1;
        }
        return color;
    }
    private static TranslationTextComponent modify_itextComponent(ITextComponent component,Color textcolor,boolean addTimestamp,boolean ischat){
        if (component instanceof TranslationTextComponent) {
            TranslationTextComponent translationComponent = (TranslationTextComponent) component;
            if(translationComponent.getFormatArgs().length==0)return (TranslationTextComponent) component;

            //一つ目はsenderのプレイヤー名がどこかに入る
            if(translationComponent.getFormatArgs()[0] instanceof TextComponent){
                TextComponent firstArg =(TextComponent) translationComponent.getFormatArgs()[0];
                String name=firstArg.getString();
                String text;
                if(ischat)text=TextFormatting.BOLD+("<"+name+"> ");
                else text=TextFormatting.BOLD+(name);

                Color color=getColor(firstArg);
                if(color!=null){
                    TextFormatting formatting=TextFormatting.getValueByName(color.getName());
                    text=formatting+text;
                }else text=TextFormatting.WHITE+text;
                if(!ischat){
                    text=(TextFormatting.GOLD+"☑")+text+TextFormatting.GRAY+ TextFormatting.ITALIC + I18n.format("usefulchatmod.message.display.incoming");
                }
                TranslationTextComponent translationTextComponent;
                if(addTimestamp)translationTextComponent=new TranslationTextComponent(TextFormatting.getValueByName(ChatDisplayConfig.TimestampColor.get())+getDate()+text);
                else translationTextComponent=new TranslationTextComponent(text);
                translationTextComponent.setStyle(translationTextComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/tell "+name+" ")));
                if(translationComponent.getFormatArgs()[1] instanceof TextComponent){
                    TextComponent Arg =(TextComponent) translationComponent.getFormatArgs()[1];//参照元Arg
                    String text1= Arg.getUnformattedComponentText();//参照元のtext
                    TranslationTextComponent translationTextComponent1=new TranslationTextComponent(text1);//代入する用

                    if(Arg.getSiblings().size()!=0){//siblingのコピー
                        if(!text1.equals("")){//外側のtextをsiblingsの中に入れる(上位の色が優先されるため)
                            TranslationTextComponent translationTextComponent2=new TranslationTextComponent("");
                            SetText(translationTextComponent1,Arg,textcolor);
                            translationTextComponent2.appendSibling(translationTextComponent1);
                            translationTextComponent1=translationTextComponent2;
                        }
                        for(int j=0;j<Arg.getSiblings().size();j++){
                            ITextComponent textComponent=Arg.getSiblings().get(j);
                            TranslationTextComponent sibling=new TranslationTextComponent(textComponent.getString());
                            SetText(sibling,textComponent,textcolor);
                            translationTextComponent1.appendSibling(sibling);
                        }
                    }
                    else{
                        SetText(translationTextComponent1,Arg,textcolor);
                    }
                    translationTextComponent.appendSibling(translationTextComponent1);
                }
                else{
                    System.out.println(translationComponent.getFormatArgs()[1]);
                    translationTextComponent.appendSibling((ITextComponent) translationComponent.getFormatArgs()[1]);
                }
                return translationTextComponent;
            }
            return translationComponent;
        }
        return (TranslationTextComponent) component;
    }
    //現在時刻の取得
    private static String getDate() {
        // 現在日時を取得
        LocalDateTime date1 = LocalDateTime.now();
        // 表示形式を指定
        DateTimeFormatter dformat =
                DateTimeFormatter.ofPattern(ChatDisplayConfig.TimestampFormat.get());

        String fdate1 = dformat.format(date1); //表示形式+Stringに変換
        return "["+fdate1+"]";
    }

    //ItextComponent Argから、TranslationTextComponent translationTextComponentの設定を行う。
    private static void  SetText(TranslationTextComponent translationTextComponent,ITextComponent Arg,Color textcolor){
        String text= Arg.getUnformattedComponentText();//参照元のテキスト
        if(Arg.getStyle().getClickEvent()==null){
            if(ChatDisplayConfig.enableCopy.get())translationTextComponent.setStyle(translationTextComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,text.trim())));
            if(Arg.getStyle().getHoverEvent()==null){//hover_eventも定義されていないとき
                if(ChatDisplayConfig.enableCopy.get())translationTextComponent.setStyle(translationTextComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new TranslationTextComponent(text.trim()))));
            }else{
                translationTextComponent.setStyle(translationTextComponent.getStyle().setHoverEvent(Arg.getStyle().getHoverEvent()));
            }
            if(Arg.getStyle().getColor()==null)translationTextComponent.setStyle(translationTextComponent.getStyle().setColor(textcolor));
        }else{
            translationTextComponent.setStyle(Arg.getStyle());//click_eventが既に定義されているときはスタイルをコピーするだけ
        }
    }

}