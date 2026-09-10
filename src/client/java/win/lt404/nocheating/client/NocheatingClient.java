package win.lt404.nocheating.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.world.GameMode;
import net.minecraft.world.SaveProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NocheatingClient implements ClientModInitializer {
    public static final String MOD_ID = "nocheating";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final String GAME_MODE_BUTTON_KEY = "selectWorld.gameMode";
    private static final String ALLOW_COMMANDS_BUTTON_KEY = "selectWorld.allowCommands";

    private static final Tooltip GAME_MODE_DISABLED_TOOLTIP =
        Tooltip.of(Text.translatable("nocheating.selectWorld.gameMode.disabled"));
    private static final Tooltip ALLOW_COMMANDS_DISABLED_TOOLTIP =
        Tooltip.of(Text.translatable("nocheating.selectWorld.allowCommands.disabled"));

    @Override
    public void onInitializeClient() {
        logStartup();
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!shouldLockCheatControls(client)) {
                return;
            }

            if (screen instanceof OpenToLanScreen) {
                disableMatchingWidget(screen, GAME_MODE_BUTTON_KEY, GAME_MODE_DISABLED_TOOLTIP);
                disableMatchingWidget(screen, ALLOW_COMMANDS_BUTTON_KEY, ALLOW_COMMANDS_DISABLED_TOOLTIP);
            }
        });
        LOGGER.info("Initialized");
    }

    private static void logStartup() {
        FabricLoader loader = FabricLoader.getInstance();
        ModMetadata meta = loader.getModContainer(MOD_ID).map(ModContainer::getMetadata).orElse(null);
        String name = meta != null ? meta.getName() : "NoCheating";
        String version = meta != null ? meta.getVersion().getFriendlyString() : "unknown";

        LOGGER.info("Starting {} {}", name, version);
        LOGGER.info("Minecraft {} | Fabric Loader {}", modVersion(loader, "minecraft"), modVersion(loader, "fabricloader"));
        LOGGER.info("Running on {}", loader.getEnvironmentType().name().toLowerCase());
    }

    private static String modVersion(FabricLoader loader, String modId) {
        return loader.getModContainer(modId)
            .map(ModContainer::getMetadata)
            .map(metadata -> metadata.getVersion().getFriendlyString())
            .orElse("unknown");
    }

    private static boolean shouldLockCheatControls(MinecraftClient client) {
        IntegratedServer server = client.getServer();
        if (server == null) {
            return false;
        }

        SaveProperties properties = server.getSaveProperties();
        return !properties.areCommandsAllowed() && properties.getGameMode() != GameMode.CREATIVE;
    }

    private static void disableMatchingWidget(Screen screen, String translationKey, Tooltip tooltip) {
        for (ClickableWidget widget : Screens.getButtons(screen)) {
            if (!translationKey.equals(primaryTranslationKey(widget.getMessage()))) {
                continue;
            }

            widget.active = false;
            widget.setTooltip(tooltip);
        }
    }

    private static String primaryTranslationKey(Text text) {
        if (!(text.getContent() instanceof TranslatableTextContent contents)) {
            return "";
        }

        if ("options.generic_value".equals(contents.getKey()) && contents.getArgs().length > 0 && contents.getArgs()[0] instanceof Text name) {
            return primaryTranslationKey(name);
        }

        return contents.getKey();
    }
}
