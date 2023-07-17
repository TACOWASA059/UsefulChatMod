package com.github.tacowasa_059.usefulchatmod.config;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.sql.Array;
import java.util.*;

public final class ConfigScreen extends Screen {
    /** Distance from top of the screen to this GUI's title */
    private static final int TITLE_HEIGHT = 8;
    /** Distance from top of the screen to the options row list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    /** List of options rows shown on the screen */
    // Not a final field because this cannot be initialized in the constructor,
    // as explained below
    private OptionsRowList optionsRowList;

    /** The parent screen of this screen */
    private final Screen parentScreen;
    private static final List<String> timestamp_format_text=new ArrayList<>(Arrays.asList(ChatDisplayConfig.defaultTimestampFormat,"yyyy/MM/dd HH:mm:ss","MM/dd HH:mm:ss","MM/dd HH:mm:ss.SSS","HH:mm:ss.SSS"));

    private static final List<String> color_text;

    private static boolean[] flags = {true, true, true, true, true, true};
    static{
        List<String> color=new ArrayList<>();
        for (TextFormatting formatting : EnumSet.allOf(TextFormatting.class)) {
            color. add(formatting.getFriendlyName());
        }
        color_text=color.subList(0,16);
    }

    public ConfigScreen(Screen parentScreen) {
        super(new TranslationTextComponent("usefulchatmod.configGui.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        // Create the options row list
        // It must be created in this method instead of in the constructor,
        // or it will not be displayed properly
        this.optionsRowList = new OptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );
        this.optionsRowList.addOptions(new AbstractOption[]{
                new BooleanOption(
                        "usefulchatmod.config.DisplayTimestamp",
                        // GameSettings argument unused for both getter and setter
                        unused -> ChatDisplayConfig.displayTimestamp.get(),
                        (unused, newValue) -> ChatDisplayConfig.displayTimestamp.set(newValue)
                ),
                new IteratableOption(
                        "usefulchatmod.config.Timestamp_Color",
                        (unused, newValue) ->
                        {ChatDisplayConfig.TimestampColor.set(color_text.get
                                ((color_text.indexOf(ChatDisplayConfig.TimestampColor.get()) + newValue)% color_text.size()));
                        },
                        (unused, option) -> ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent("usefulchatmod.config.Timestamp_Color").getString()+": "+TextFormatting.getValueByName(ChatDisplayConfig.TimestampColor.get())+new TranslationTextComponent(ChatDisplayConfig.TimestampColor.get()).getString())
                )
        });

        this.optionsRowList.addOption(new IteratableOption(
                "usefulchatmod.config.Timestamp_Format",
                (unused, newValue) ->
                {ChatDisplayConfig.TimestampFormat.set(timestamp_format_text.get
                            ((timestamp_format_text.indexOf(ChatDisplayConfig.TimestampFormat.get()) + newValue)% timestamp_format_text.size()));
                },
                (unused, option) -> ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent("usefulchatmod.config.Timestamp_Format").getString()+": ["+new TranslationTextComponent(ChatDisplayConfig.TimestampFormat.get()).getString()+"]")
        ));
        this.optionsRowList.addOption(new IteratableOption(
                "usefulchatmod.config.chatColor",
                        (unused, newValue) ->
                        {ChatDisplayConfig.chatColor.set(color_text.get
                                ((color_text.indexOf(ChatDisplayConfig.chatColor.get()) + newValue)% color_text.size()));
                        },
                        (unused, option) -> ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent("usefulchatmod.config.chatColor").getString()+": "+TextFormatting.getValueByName(ChatDisplayConfig.chatColor.get())+new TranslationTextComponent(ChatDisplayConfig.chatColor.get()).getString())
                )

        );
        this.optionsRowList.addOption(new IteratableOption(
                "usefulchatmod.config.tellColor",
                        (unused, newValue) ->
                        {ChatDisplayConfig.tellColor.set(color_text.get
                                ((color_text.indexOf(ChatDisplayConfig.tellColor.get()) + newValue)% color_text.size()));
                        },
                        (unused, option) -> ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent("usefulchatmod.config.tellColor").getString()+": "+TextFormatting.getValueByName(ChatDisplayConfig.tellColor.get())+new TranslationTextComponent(ChatDisplayConfig.tellColor.get()).getString())
                )

        );
        this.optionsRowList.addOption(new BooleanOption(
                "usefulchatmod.config.copy_chat",
                        // GameSettings argument unused for both getter and setter
                        unused -> ChatDisplayConfig.enableCopy.get(),
                        (unused, newValue) -> ChatDisplayConfig.enableCopy.set(newValue)
                )

        );
        this.optionsRowList.addOption(new BooleanOption(
                        "usefulchatmod.config.enablebutton",
                        // GameSettings argument unused for both getter and setter
                        unused -> ChatDisplayConfig.enableSwitchButton.get(),
                        (unused, newValue) -> ChatDisplayConfig.enableSwitchButton.set(newValue)
                )

        );
        this.optionsRowList.addOption(new BooleanOption(
                "usefulchatmod.config.debug_output",
                        // GameSettings argument unused for both getter and setter
                        unused -> ChatDisplayConfig.debugoutput.get(),
                        (unused, newValue) -> ChatDisplayConfig.debugoutput.set(newValue)
                )

        );



        this.children.add(this.optionsRowList);

        // Add the "Done" button
        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                // Text shown on the button
                new TranslationTextComponent("usefulchatmod.config.gui.done"),
                // Action performed when the button is pressed
                (button) -> {
                    ChatDisplayConfig.spec.save();
                    this.minecraft.displayGuiScreen(parentScreen);// Display the parent screen
                }
        ));
    }
    @Override
    public void onClose() {
        ChatDisplayConfig.spec.save();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        // Options row list must be rendered here,
        // otherwise the GUI will be broken
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}