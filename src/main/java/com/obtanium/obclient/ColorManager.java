package com.obtanium.obclient;

import java.awt.Color;

public class ColorManager {
    private static ColorManager instance = new ColorManager();

    // Default colors
    private int hudTextColor = 0x00FF00; // Green
    private int hudBackgroundColor = 0x80000000; // Semi-transparent black
    private boolean rainbowMode = false;
    private int rainbowOffset = 0;

    // Predefined color options (now includes rainbow)
    public static final int[] PRESET_COLORS = {
            0x00FF00, // Green
            0x4da6ff, // Blue
            0xFF0000, // Red
            0xFFFF00, // Yellow
            0xFF8000, // Orange
            0xFF00FF, // Magenta
            0x00FFFF, // Cyan
            0xFFFFFF, // White
            0x800080, // Purple
            0xFFA500, // Dark Orange
            0x000000  // Rainbow placeholder (special case)
    };

    public static final String[] COLOR_NAMES = {
            "Green", "Blue", "Red", "Yellow", "Orange",
            "Magenta", "Cyan", "White", "Purple", "Dark Orange", "Rainbow"
    };

    private ColorManager() {}

    public static ColorManager getInstance() {
        return instance;
    }

    public int getHudTextColor() {
        if (rainbowMode) {
            return getRainbowColor();
        }
        return hudTextColor;
    }

    public void setHudTextColor(int color) {
        // Check if this is the rainbow color (black placeholder)
        if (color == 0x000000) { // Rainbow mode uses black as placeholder
            this.rainbowMode = true;
            System.out.println("[ObClient] HUD text color changed to: Rainbow");
        } else {
            this.rainbowMode = false;
            this.hudTextColor = color;
            System.out.println("[ObClient] HUD text color changed to: " + Integer.toHexString(color));
        }
    }

    public void setRainbowMode(boolean enabled) {
        this.rainbowMode = enabled;
        if (enabled) {
            System.out.println("[ObClient] Rainbow mode enabled");
        } else {
            System.out.println("[ObClient] Rainbow mode disabled");
        }
    }

    public boolean isRainbowMode() {
        return rainbowMode;
    }



    private int getRainbowColor() {
        // Create smooth rainbow effect
        rainbowOffset += 2; // Speed of color change
        if (rainbowOffset >= 360) {
            rainbowOffset = 0;
        }

        return Color.HSBtoRGB(rainbowOffset / 360.0f, 1.0f, 1.0f);
    }

    public int getHudBackgroundColor() {
        return hudBackgroundColor;
    }

    public void setHudBackgroundColor(int color) {
        this.hudBackgroundColor = color;
        System.out.println("[ObClient] HUD background color changed to: " + Integer.toHexString(color));
    }

    public String getColorName(int color) {
        if (rainbowMode) {
            return "Rainbow";
        }

        for (int i = 0; i < PRESET_COLORS.length - 1; i++) { // Exclude rainbow from normal check
            if (PRESET_COLORS[i] == color) {
                return COLOR_NAMES[i];
            }
        }
        return "Custom";
    }

    public int getPresetColor(int index) {
        if (index >= 0 && index < PRESET_COLORS.length) {
            return PRESET_COLORS[index];
        }
        return PRESET_COLORS[0]; // Default to green
    }

    public boolean isRainbowIndex(int index) {
        return index == PRESET_COLORS.length - 1; // Last index is rainbow
    }
}