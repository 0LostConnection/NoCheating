package win.lt404.nocheating.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.MultiplayerOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NocheatingClient implements ClientModInitializer {
    public static final String MOD_ID = "nocheating";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final String WORLD_OPTIONS_BUTTON_KEY = "options.worldOptions.button";
    private static final String GAME_MODE_BUTTON_KEY = "selectWorld.gameMode";
    private static final String ALLOW_COMMANDS_BUTTON_KEY = "selectWorld.allowCommands";

    private static final Tooltip WORLD_OPTIONS_DISABLED_TOOLTIP =
        Tooltip.create(Component.translatable("nocheating.options.worldOptions.disabled"));
    private static final Tooltip GAME_MODE_DISABLED_TOOLTIP =
        Tooltip.create(Component.translatable("nocheating.selectWorld.gameMode.disabled"));
    private static final Tooltip ALLOW_COMMANDS_DISABLED_TOOLTIP =
        Tooltip.create(Component.translatable("nocheating.selectWorld.allowCommands.disabled"));

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initialized");
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!shouldLockCheatControls(client)) {
                return;
            }

            if (screen instanceof OptionsScreen) {
                disableMatchingWidget(screen, WORLD_OPTIONS_BUTTON_KEY, WORLD_OPTIONS_DISABLED_TOOLTIP);
            } else if (screen instanceof MultiplayerOptionsScreen) {
                disableMatchingWidget(screen, GAME_MODE_BUTTON_KEY, GAME_MODE_DISABLED_TOOLTIP);
                disableMatchingWidget(screen, ALLOW_COMMANDS_BUTTON_KEY, ALLOW_COMMANDS_DISABLED_TOOLTIP);
            }
        });
    }

    private static boolean shouldLockCheatControls(Minecraft client) {
        IntegratedServer server = client.getSingleplayerServer();
        if (server == null) {
            return false;
        }

        LevelSettings settings = server.getWorldData().getLevelSettings();
        return !settings.allowCommands() && settings.gameType() != GameType.CREATIVE;
    }

    private static void disableMatchingWidget(Screen screen, String translationKey, Tooltip tooltip) {
        for (AbstractWidget widget : Screens.getWidgets(screen)) {
            if (!translationKey.equals(primaryTranslationKey(widget.getMessage()))) {
                continue;
            }

            widget.active = false;
            widget.setTooltip(tooltip);
        }
    }

    private static String primaryTranslationKey(Component component) {
        if (!(component.getContents() instanceof TranslatableContents contents)) {
            return "";
        }

        if ("options.generic_value".equals(contents.getKey()) && contents.getArgs().length > 0 && contents.getArgs()[0] instanceof Component name) {
            return primaryTranslationKey(name);
        }

        return contents.getKey();
    }
}
