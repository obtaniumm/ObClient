package com.obtanium.obclient.gui;

import com.obtanium.obclient.FeatureManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

public class GuiInGameObClient extends GuiScreen {

    // Colors matching your client theme
    private static final int BLUE_COLOR = 0x4da6ff;
    private static final int LIGHT_BLUE_COLOR = 0x66b3ff;
    private static final int WHITE_COLOR = 0xFFFFFF;
    private static final int GREEN_COLOR = 0x00FF00;
    private static final int RED_COLOR = 0xFF4444;
    private static final int DARK_GRAY = 0x333333;
    private static final int BACKGROUND_OVERLAY = 0xCC000000;

    private final String[] features = {
            "Watchdog Bypass",
            "Fully safe walk",
            "0Cps Disable",
            "Literal Lag",
            "250+ Fps",
            "Brick Wall",
            "Fall off"
    };

    private int animationTimer = 0;

    @Override
    public void initGui() {
        super.initGui();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int startX = this.width / 2 - 100;
        int startY = 60;

        // Create toggle buttons for each feature
        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            boolean isEnabled = FeatureManager.getInstance().isFeatureEnabled(feature);
            String buttonText = feature + (isEnabled ? " [ON]" : " [OFF]");

            this.buttonList.add(new GuiButton(i, startX, startY + (i * 25), buttonWidth, buttonHeight, buttonText));
        }

        // Close button
        this.buttonList.add(new GuiButton(99, this.width / 2 - 50, this.height - 40, 100, 20, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animationTimer++;

        // Semi-transparent background
        drawRect(0, 0, this.width, this.height, BACKGROUND_OVERLAY);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // Main panel background
        int panelWidth = 300;
        int panelHeight = 400;
        int panelX = this.width / 2 - panelWidth / 2;
        int panelY = this.height / 2 - panelHeight / 2;

        drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, DARK_GRAY);
        drawRect(panelX + 1, panelY + 1, panelX + panelWidth - 1, panelY + panelHeight - 1, 0xFF1a1a1a);

        // Title
        String title = "ObClient Settings";
        int titleWidth = this.fontRendererObj.getStringWidth(title);
        this.fontRendererObj.drawStringWithShadow(title, this.width / 2 - titleWidth / 2, panelY + 15, WHITE_COLOR);

        // Subtitle
        String subtitle = "Toggle features on/off";
        int subtitleWidth = this.fontRendererObj.getStringWidth(subtitle);
        this.fontRendererObj.drawString(subtitle, this.width / 2 - subtitleWidth / 2, panelY + 30, 0xAAAAAA);

        // Statistics
        int activeCount = FeatureManager.getInstance().getActiveFeatureCount();
        String stats = "Active Features: " + activeCount + "/" + features.length;
        int statsWidth = this.fontRendererObj.getStringWidth(stats);
        this.fontRendererObj.drawString(stats, this.width / 2 - statsWidth / 2, panelY + panelHeight - 60, BLUE_COLOR);

        // Instructions
        String instructions = "Click buttons to toggle features";
        int instrWidth = this.fontRendererObj.getStringWidth(instructions);
        this.fontRendererObj.drawString(instructions, this.width / 2 - instrWidth / 2, panelY + panelHeight - 75, 0x888888);

        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 99) {
            // Close button
            this.mc.displayGuiScreen(null);
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
        return false; // Don't pause the game when this GUI is open
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        // Close GUI when ESC is pressed
        if (keyCode == 1) { // ESC key
            this.mc.displayGuiScreen(null);
        }
        super.keyTyped(typedChar, keyCode);
    }
}