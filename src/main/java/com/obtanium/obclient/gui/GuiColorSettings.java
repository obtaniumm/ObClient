package com.obtanium.obclient.gui;

import com.obtanium.obclient.ColorManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

public class GuiColorSettings extends GuiScreen {

    private final GuiScreen parentScreen;
    private ColorManager colorManager;

    // Colors
    private static final int WHITE_COLOR = 0xFFFFFF;
    private static final int DARK_GRAY = 0x333333;
    private static final int BACKGROUND_OVERLAY = 0xCC000000;

    public GuiColorSettings(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.colorManager = ColorManager.getInstance();
    }

    @Override
    public void initGui() {
        super.initGui();

        int buttonWidth = 150;
        int buttonHeight = 20;
        int startX = this.width / 2 - buttonWidth / 2;
        int startY = 80;

        // Color preset buttons
        for (int i = 0; i < ColorManager.PRESET_COLORS.length; i++) {
            int row = i / 2;
            int col = i % 2;
            int x = startX + (col * 160) - 75;
            int y = startY + (row * 25);

            this.buttonList.add(new GuiButton(i, x, y, buttonWidth, buttonHeight,
                    ColorManager.COLOR_NAMES[i]));
        }

        // Back button
        this.buttonList.add(new GuiButton(99, this.width / 2 - 50, this.height - 40, 100, 20, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Semi-transparent background
        drawRect(0, 0, this.width, this.height, BACKGROUND_OVERLAY);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // Main panel background
        int panelWidth = 400;
        int panelHeight = 350;
        int panelX = this.width / 2 - panelWidth / 2;
        int panelY = this.height / 2 - panelHeight / 2;

        drawRect(panelX, panelY, panelX + panelWidth, panelY + panelHeight, DARK_GRAY);
        drawRect(panelX + 1, panelY + 1, panelX + panelWidth - 1, panelY + panelHeight - 1, 0xFF1a1a1a);

        // Title
        String title = "HUD Color Settings";
        int titleWidth = this.fontRendererObj.getStringWidth(title);
        this.fontRendererObj.drawStringWithShadow(title, this.width / 2 - titleWidth / 2, panelY + 15, WHITE_COLOR);

        // Current color info
        String currentColorName = colorManager.getColorName(colorManager.getHudTextColor());
        String currentColorText = "Current Color: " + currentColorName;
        int currentColorWidth = this.fontRendererObj.getStringWidth(currentColorText);
        this.fontRendererObj.drawString(currentColorText, this.width / 2 - currentColorWidth / 2, panelY + 35, 0xAAAAAA);

        // Color preview
        String previewText = "Preview Text";
        int previewWidth = this.fontRendererObj.getStringWidth(previewText);
        int previewX = this.width / 2 - previewWidth / 2;
        int previewY = panelY + 50;

        // Draw preview background
        drawRect(previewX - 5, previewY - 2, previewX + previewWidth + 5, previewY + 12,
                colorManager.getHudBackgroundColor());

        // Draw preview text
        this.fontRendererObj.drawStringWithShadow(previewText, previewX, previewY,
                colorManager.getHudTextColor());

        // Instructions
        String instructions = "Click a color to change HUD text color";
        int instrWidth = this.fontRendererObj.getStringWidth(instructions);
        this.fontRendererObj.drawString(instructions, this.width / 2 - instrWidth / 2, panelY + panelHeight - 60, 0x888888);

        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 99) {
            // Back button
            this.mc.displayGuiScreen(parentScreen);
        } else if (button.id >= 0 && button.id < ColorManager.PRESET_COLORS.length) {
            // Color button
            int selectedColor = colorManager.getPresetColor(button.id);
            colorManager.setHudTextColor(selectedColor);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC key
            this.mc.displayGuiScreen(parentScreen);
        }
        super.keyTyped(typedChar, keyCode);
    }
}