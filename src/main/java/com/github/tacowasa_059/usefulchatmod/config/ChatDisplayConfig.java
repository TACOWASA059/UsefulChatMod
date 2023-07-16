package com.github.tacowasa_059.usefulchatmod.config;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

public class ChatDisplayConfig {
    public static final ForgeConfigSpec.Builder buider=new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec spec;


    public static  ForgeConfigSpec.ConfigValue<Boolean> displayTimestamp;
    public static  ForgeConfigSpec.ConfigValue<String> TimestampFormat;
    public static  ForgeConfigSpec.ConfigValue<String> TimestampColor;

    public static  ForgeConfigSpec.ConfigValue<String> chatColor;
    public static  ForgeConfigSpec.ConfigValue<String> tellColor;
    public static  ForgeConfigSpec.ConfigValue<Boolean> enableCopy;

    public static  final Boolean defaultdisplayTimestamp=true;
    public static  final String defaultTimestampFormat="HH:mm:ss";
    public static  final String defaultTimestampColor=TextFormatting.GRAY.getFriendlyName();

    public static  final String defaultchatColor=TextFormatting.GREEN.getFriendlyName();
    public static  final String defaulttellColor=TextFormatting.RED.getFriendlyName();
    public static  final Boolean defaultenableCopy=true;
    static {
        buider.push("Config for useful_chat_mod");
        displayTimestamp=buider.comment("Whether to add a timestamp").define("DisplayTimestamp",defaultdisplayTimestamp);
        TimestampFormat=buider.comment("Time stamp format").define("Timestamp_Format",defaultTimestampFormat);
        TimestampColor=buider.comment("Time stamp color").define("Timestamp_Color",defaultTimestampColor);
        chatColor=buider.comment("chat color").define("chat_color",defaultchatColor);
        tellColor=buider.comment("tell color").define("tell_color",defaulttellColor);
        enableCopy=buider.comment("Enable click to copy chat").define("copy_chat",defaultenableCopy);

        buider.pop();
        spec= buider.build();
    }
}
