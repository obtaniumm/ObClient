package com.obtanium.obclient;

import com.obtanium.obclient.gui.GuiInGameObClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyBindHandler {

    // Define keybinds with proper category for Minecraft controls menu
    public static KeyBinding openMenu;
    public static KeyBinding toggleHUD;
    public static KeyBinding quickToggleWatchdog;
    public static KeyBinding quickToggleSafeWalk;

    // Category name that will appear in Minecraft's controls menu
    private static final String KEYBIND_CATEGORY = "ObClient";

    public KeyBindHandler() {
        // Initialize keybinds with descriptions that will show in controls menu
        openMenu = new KeyBinding(
                "key.obclient.openmenu",     // Translation key
                Keyboard.KEY_O,              // Default key
                KEYBIND_CATEGORY            // Category in controls menu
        );

        toggleHUD = new KeyBinding(
                "key.obclient.togglehud",    // Translation key
                Keyboard.KEY_H,              // Default key
                KEYBIND_CATEGORY            // Category in controls menu
        );

        quickToggleWatchdog = new KeyBinding(
                "key.obclient.watchdog",     // Translation key
                Keyboard.KEY_NONE,           // No default key (user can set)
                KEYBIND_CATEGORY            // Category in controls menu
        );

        quickToggleSafeWalk = new KeyBinding(
                "key.obclient.safewalk",     // Translation key
                Keyboard.KEY_NONE,           // No default key (user can set)
                KEYBIND_CATEGORY            // Category in controls menu
        );

        // Register keybinds with Minecraft
        ClientRegistry.registerKeyBinding(openMenu);
        ClientRegistry.registerKeyBinding(toggleHUD);
        ClientRegistry.registerKeyBinding(quickToggleWatchdog);
        ClientRegistry.registerKeyBinding(quickToggleSafeWalk);

        System.out.println("[ObClient] Registered keybinds in 'ObClient' category");
        System.out.println("[ObClient] Default keys: O (menu), H (toggle HUD)");
        System.out.println("[ObClient] Additional keys can be set in Controls menu");
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Only process keybinds when not in a menu (except for menu opening key)
        if (openMenu.isPressed()) {
            // Open menu works anywhere
            mc.displayGuiScreen(new GuiInGameObClient());
            return;
        }

        // Other keybinds only work when in-game (no GUI open)
        if (mc.currentScreen == null) {
            if (toggleHUD.isPressed()) {
                // Toggle HUD visibility
                HUDRenderer.toggleVisibility();
            }

            if (quickToggleWatchdog.isPressed()) {
                // Quick toggle for Watchdog Bypass
                FeatureManager.getInstance().toggleFeature("Watchdog Bypass");
            }

            if (quickToggleSafeWalk.isPressed()) {
                // Quick toggle for Safe Walk
                FeatureManager.getInstance().toggleFeature("Fully safe walk");
            }
        }
    }

    // Getter methods for accessing keybinds from other classes
    public static KeyBinding getOpenMenuKey() {
        return openMenu;
    }

    public static KeyBinding getToggleHUDKey() {
        return toggleHUD;
    }

    public static KeyBinding getQuickToggleWatchdogKey() {
        return quickToggleWatchdog;
    }

    public static KeyBinding getQuickToggleSafeWalkKey() {
        return quickToggleSafeWalk;
    }
}