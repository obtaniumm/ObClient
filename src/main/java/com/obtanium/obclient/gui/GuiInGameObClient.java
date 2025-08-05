package com.obtanium.obclient.gui;

import com.obtanium.obclient.FeatureManager;
import com.obtanium.obclient.KeyBindHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiInGameObClient extends GuiScreen {

    // Colors matching your client theme
    private static final int BLUE_COLOR = 0x4da6ff;
    private static final int WHITE_COLOR = 0xFFFFFF;
    private static final int GREEN_COLOR = 0x00FF00;
    private static final int DARK_GRAY = 0x333333;
    private static final int BACKGROUND_OVERLAY = 0xCC000000;

    private final String[] features = {
            "WatchdogBypass",
            "UnsafeWalk",
            "0CPSAutoclicker",
            "RealLag",
            "500+ FPS",
            "BrickWall",
            "FallOff"
    };

    @Override
    public void initGui() {
        super.initGui();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int startX = this.width / 2 - 100;
        int startY = 50; // Moved up to make room for text below

        // Create toggle buttons for each feature
        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            boolean isEnabled = FeatureManager.getInstance().isFeatureEnabled(feature);
            String buttonText = feature + (isEnabled ? " [ON]" : " [OFF]");

            this.buttonList.add(new GuiButton(i, startX, startY + (i * 25), buttonWidth, buttonHeight, buttonText));
        }

        // Color settings button
        this.buttonList.add(new GuiButton(98, this.width / 2 - 100, startY + (features.length * 25) + 10, 200, 20, "Color Settings"));

        // Close button
        this.buttonList.add(new GuiButton(99, this.width / 2 - 50, this.height - 40, 100, 20, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Semi-transparent background
        drawRect(0, 0, this.width, this.height, BACKGROUND_OVERLAY);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // Main panel background
        int panelWidth = 400;
        int panelHeight = 480;
        int panelX = this.width / 2 - panelWidth / 2;
        int panelY = this.height / 2 - panelHeight / 2;

        drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, DARK_GRAY);
        drawRect(panelX + 1, panelY + 1, panelX + panelWidth - 1, panelY + panelHeight - 1, 0xFF1a1a1a);

        // Title
        String title = "ObClient Settings";
        int titleWidth = this.fontRendererObj.getStringWidth(title);
        this.fontRendererObj.drawStringWithShadow(title, this.width / 2 - titleWidth / 2, panelY + 15, WHITE_COLOR);

        // Information text below buttons
        int textStartY = panelY + 270; // Below the button area

        // Statistics
        int activeCount = FeatureManager.getInstance().getActiveFeatureCount();
        String stats = "Active Features: " + activeCount + "/" + features.length;
        int statsWidth = this.fontRendererObj.getStringWidth(stats);
        this.fontRendererObj.drawString(stats, this.width / 2 - statsWidth / 2, textStartY, BLUE_COLOR);

        // Keybind information
        String menuKey = getKeyDisplayString(KeyBindHandler.getOpenMenuKey());
        String hudKey = getKeyDisplayString(KeyBindHandler.getToggleHUDKey());

        this.fontRendererObj.drawString("Keybinds:", panelX + 10, textStartY + 20, WHITE_COLOR);
        this.fontRendererObj.drawString("• Open Menu: " + menuKey, panelX + 10, textStartY + 35, 0xAAAAA);
        this.fontRendererObj.drawString("• Toggle HUD: " + hudKey, panelX + 10, textStartY + 50, 0xAAAAA);

        // Feature descriptions
        this.fontRendererObj.drawString("Feature Information:", panelX + 10, textStartY + 70, WHITE_COLOR);
        this.fontRendererObj.drawString("• Click feature buttons to toggle ON/OFF", panelX + 10, textStartY + 85, 0x888888);
        this.fontRendererObj.drawString("• Green [ON] means feature is active", panelX + 10, textStartY + 100, 0x888888);
        this.fontRendererObj.drawString("• Gray [OFF] means feature is disabled", panelX + 10, textStartY + 115, 0x888888);

        // Instructions for keybinds
        String instructions = "Change keybinds in Options > Controls > ObClient";
        int instrWidth = this.fontRendererObj.getStringWidth(instructions);
        this.fontRendererObj.drawString(instructions, this.width / 2 - instrWidth / 2, textStartY + 140, 0x666666);

        // Additional help text
        String helpText = "Press ESC or click Close to exit this menu";
        int helpWidth = this.fontRendererObj.getStringWidth(helpText);
        this.fontRendererObj.drawString(helpText, this.width / 2 - helpWidth / 2, textStartY + 155, 0x666666);

        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String getKeyDisplayString(net.minecraft.client.settings.KeyBinding keyBinding) {
        if (keyBinding.getKeyCode() == Keyboard.KEY_NONE) {
            return "Not Set";
        }
        return keyBinding.getKeyDescription();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 99) {
            // Close button
            this.mc.displayGuiScreen(null);
        } else if (button.id == 98) {
            // Color settings button
            this.mc.displayGuiScreen(new GuiColorSettings(this));
        } else if (button.id >= 0 && button.id < features.length) {
            // Feature toggle button
            String feature = features[button.id];
            FeatureManager.getInstance().toggleFeature(feature);

            // Update button text
            boolean isEnabled = FeatureManager.getInstance().isFeatureEnabled(feature);
            button.displayString = feature + (isEnabled ? " [ON]" : " [OFF]");
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC key
            this.mc.displayGuiScreen(null);
        }
        super.keyTyped(typedChar, keyCode);
    }
}