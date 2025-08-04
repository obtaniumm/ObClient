package com.obtanium.obclient;

import java.awt.Color;

public class ColorManager {
    private static ColorManager instance = new ColorManager();

    // Default colors
    private int hudTextColor = 0x00FF00; // Green
    private int hudBackgroundColor = 0x80000000; // Semi-transparent black

    // Predefined color options
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
            0xFFA500  // Dark Orange
    };

    public static final String[] COLOR_NAMES = {
            "Green", "Blue", "Red", "Yellow", "Orange",
            "Magenta", "Cyan", "White", "Purple", "Dark Orange"
    };

    private ColorManager() {}

    public static ColorManager getInstance() {
        return instance;
    }

    public int getHudTextColor() {
        return hudTextColor;
    }

    public void setHudTextColor(int color) {
        this.hudTextColor = color;
        System.out.println("[ObClient] HUD text color changed to: " + Integer.toHexString(color));
    }

    public int getHudBackgroundColor() {
        return hudBackgroundColor;
    }

    public void setHudBackgroundColor(int color) {
        this.hudBackgroundColor = color;
        System.out.println("[ObClient] HUD background color changed to: " + Integer.toHexString(color));
    }

    public String getColorName(int color) {
        for (int i = 0; i < PRESET_COLORS.length; i++) {
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
}