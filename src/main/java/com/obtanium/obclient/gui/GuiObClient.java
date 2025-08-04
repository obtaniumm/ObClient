package com.obtanium.obclient.gui;

import com.obtanium.obclient.FeatureManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiObClient extends GuiScreen {

    private static final ResourceLocation LOGO_TEXTURE = new ResourceLocation("obclient", "textures/gui/logo.png");

    // Feature list matching your reference image
    private final String[] features = {
            "WatchdogBypass",
            "UnsafeWalk",
            "0CPSAutoclicker",
            "RealLag",
            "500+ FPS",
            "BrickWall",
            "FallOff"
    };

    // Colors - matching the blue theme from your image
    private static final int BLUE_COLOR = 0x4da6ff;
    private static final int LIGHT_BLUE_COLOR = 0x66b3ff;
    private static final int WHITE_COLOR = 0xFFFFFF;
    private static final int GREEN_COLOR = 0x00FF00;
    private static final int DARK_GRAY = 0x333333;
    private static final int BACKGROUND_OVERLAY = 0x90000000;

    private int animationTimer = 0;
    private boolean showSystemInfo = false;

    @Override
    public void initGui() {
        super.initGui();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int startY = this.height - 140;

        // Main menu buttons
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, startY, buttonWidth, buttonHeight, "Singleplayer"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, startY + 24, buttonWidth, buttonHeight, "Multiplayer"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, startY + 48, buttonWidth, buttonHeight, "Options"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, startY + 72, buttonWidth, buttonHeight, "Quit"));

        // System info toggle button (Linux-specific)
        this.buttonList.add(new GuiButton(5, 10, this.height - 30, 100, 20, "System Info"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animationTimer++;

        // Dark background
        this.drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, BACKGROUND_OVERLAY);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // Logo area - matching your reference image layout
        drawRect(15, 15, 57, 57, DARK_GRAY);
        drawRect(16, 16, 56, 56, 0xFF1a1a1a);

        // Draw logo or placeholder
        try {
            this.mc.getTextureManager().bindTexture(LOGO_TEXTURE);
            this.drawTexturedModalRect(20, 20, 0, 0, 32, 32);
        } catch (Exception e) {
            // Animated logo placeholder
            int pulseColor = BLUE_COLOR + (int)(Math.sin(animationTimer * 0.1) * 30) * 0x010101;
            drawRect(20, 20, 52, 52, pulseColor);
            this.fontRendererObj.drawString("NLE", 27, 32, WHITE_COLOR);
        }

        // Client name - matching your reference
        String clientName = "ObClient";
        this.fontRendererObj.drawStringWithShadow(clientName, 65, 25, WHITE_COLOR);

        // Platform indicator
        String platform = "Arch Linux Edition";
        this.fontRendererObj.drawString(platform, 65, 40, 0xAAAAAAAA);

        // Features section - main focus of your design
        drawFeaturesSection(mouseX, mouseY);

        // Statistics panel
        drawStatsPanel();

        // Linux system info (if enabled)
        if (showSystemInfo) {
            drawSystemInfo();
        }

        // Version info
        String version = "ObClient v1.0 - Minecraft 1.8.9";
        this.fontRendererObj.drawString(version, this.width - this.fontRendererObj.getStringWidth(version) - 10,
                this.height - 50, 0x888888);

        // Instructions
        String instructions = "Left-click: Toggle | Right-click: Options";
        this.fontRendererObj.drawString(instructions, 20, this.height - 70, 0x666666);

        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawFeaturesSection(int mouseX, int mouseY) {
        int startY = 80;
        int maxWidth = 0;

        // Calculate background width
        for (String feature : features) {
            String displayText = feature + FeatureManager.getInstance().getFeatureStatus(feature);
            int textWidth = this.fontRendererObj.getStringWidth(displayText);
            if (textWidth > maxWidth) maxWidth = textWidth;
        }

        // Features background - dark theme matching your image
        drawRect(15, startY - 10, 35 + maxWidth, startY + (features.length * 14) + 10, BACKGROUND_OVERLAY);
        drawRect(16, startY - 9, 34 + maxWidth, startY + (features.length * 14) + 9, 0x40333333);

        // Features title
        this.fontRendererObj.drawStringWithShadow("Features:", 20, startY - 20, WHITE_COLOR);

        // Draw each feature - exact layout from your reference
        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            String displayText = feature + FeatureManager.getInstance().getFeatureStatus(feature);
            boolean isEnabled = FeatureManager.getInstance().isFeatureEnabled(feature);
            int color = isEnabled ? GREEN_COLOR : BLUE_COLOR;

            // Hover detection
            int textWidth = this.fontRendererObj.getStringWidth(displayText);
            boolean isHovering = mouseX >= 20 && mouseX <= 20 + textWidth &&
                    mouseY >= startY + (i * 14) && mouseY <= startY + (i * 14) + 12;

            if (isHovering) {
                drawRect(18, startY + (i * 14) - 1, 22 + textWidth, startY + (i * 14) + 11, 0x40FFFFFF);
                color = LIGHT_BLUE_COLOR;
            }

            // Status indicator dot
            int dotColor = isEnabled ? GREEN_COLOR : 0x666666;
            drawRect(20, startY + (i * 14) + 4, 22, startY + (i * 14) + 6, dotColor);

            // Feature text
            this.fontRendererObj.drawString(displayText, 26, startY + (i * 14), color);
        }
    }

    private void drawStatsPanel() {
        int statsX = this.width - 200;
        int statsY = 80;

        drawRect(statsX - 10, statsY - 5, this.width - 10, statsY + 100, BACKGROUND_OVERLAY);
        drawRect(statsX - 9, statsY - 4, this.width - 11, statsY + 99, 0x40333333);

        this.fontRendererObj.drawStringWithShadow("Client Statistics", statsX, statsY, WHITE_COLOR);
        this.fontRendererObj.drawString("Active Features: " + FeatureManager.getInstance().getActiveFeatureCount(),
                statsX, statsY + 15, BLUE_COLOR);
        this.fontRendererObj.drawString("Runtime: " + formatTime(animationTimer), statsX, statsY + 25, BLUE_COLOR);
        this.fontRendererObj.drawString("Platform: Arch Linux", statsX, statsY + 35, BLUE_COLOR);
        this.fontRendererObj.drawString("Java: " + System.getProperty("java.version"), statsX, statsY + 45, BLUE_COLOR);
        this.fontRendererObj.drawString("Memory: " + getMemoryInfo(), statsX, statsY + 55, BLUE_COLOR);
    }

    private void drawSystemInfo() {
        int infoX = this.width / 2 - 150;
        int infoY = 150;

        drawRect(infoX - 10, infoY - 10, infoX + 300, infoY + 120, BACKGROUND_OVERLAY);
        drawRect(infoX - 9, infoY - 9, infoX + 299, infoY + 119, 0x60333333);

        this.fontRendererObj.drawStringWithShadow("System Information (Arch Linux)", infoX, infoY, WHITE_COLOR);
        this.fontRendererObj.drawString("OS: " + System.getProperty("os.name"), infoX, infoY + 15, BLUE_COLOR);
        this.fontRendererObj.drawString("Architecture: " + System.getProperty("os.arch"), infoX, infoY + 25, BLUE_COLOR);
        this.fontRendererObj.drawString("Java Home: " + System.getProperty("java.home"), infoX, infoY + 35, BLUE_COLOR);
        this.fontRendererObj.drawString("User: " + System.getProperty("user.name"), infoX, infoY + 45, BLUE_COLOR);
        this.fontRendererObj.drawString("Working Dir: " + System.getProperty("user.dir"), infoX, infoY + 55, BLUE_COLOR);

        // Linux-specific info
        try {
            String kernel = System.getProperty("os.version");
            this.fontRendererObj.drawString("Kernel: " + kernel, infoX, infoY + 65, BLUE_COLOR);
        } catch (Exception e) {
            this.fontRendererObj.drawString("Kernel: N/A", infoX, infoY + 65, BLUE_COLOR);
        }

        this.fontRendererObj.drawString("Click 'System Info' again to close", infoX, infoY + 85, 0x888888);
    }

    private String formatTime(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private String getMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        return usedMemory + "/" + maxMemory + " MB";
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: // Singleplayer
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2: // Multiplayer
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3: // Options
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 4: // Quit
                this.mc.shutdown();
                break;
            case 5: // System Info toggle
                showSystemInfo = !showSystemInfo;
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Handle feature clicks
        int startY = 80;
        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            String displayText = feature + FeatureManager.getInstance().getFeatureStatus(feature);
            int textWidth = this.fontRendererObj.getStringWidth(displayText);

            if (mouseX >= 20 && mouseX <= 20 + textWidth + 6 &&
                    mouseY >= startY + (i * 14) && mouseY <= startY + (i * 14) + 12) {

                if (mouseButton == 0) { // Left click
                    FeatureManager.getInstance().toggleFeature(feature);
                } else if (mouseButton == 1) { // Right click
                    System.out.println("[Nle Client] Right-clicked feature: " + feature);
                    // Add feature-specific options here
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}