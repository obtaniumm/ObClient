package com.obtanium.obclient;

import java.util.HashMap;
import java.util.Map;

public class FeatureManager {
    private static FeatureManager instance = new FeatureManager();
    private Map<String, Boolean> features = new HashMap<>();

    private FeatureManager() {
        // Initialize features as disabled
        features.put("Watchdog Bypass", false);
        features.put("Fully safe walk", false);
        features.put("0Cps Disable", false);
        features.put("Literal Lag", false);
        features.put("250+ Fps", false);
        features.put("Brick Wall", false);
        features.put("Fall off", false);
    }

    public static FeatureManager getInstance() {
        return instance;
    }

    public boolean isFeatureEnabled(String featureName) {
        return features.getOrDefault(featureName, false);
    }

    public void toggleFeature(String featureName) {
        boolean newState = !features.getOrDefault(featureName, false);
        features.put(featureName, newState);
        System.out.println("[ObClient] Feature '" + featureName + "' is now " +
                (newState ? "ENABLED" : "DISABLED"));

        // Feature implementations
        if (featureName.equals("250+ Fps")) {
            if (newState) {
                System.setProperty("java.awt.headless", "true");
            }
        } else if (featureName.equals("Fully safe walk")) {
            if (newState) {
                System.out.println("[ObClient] Safe walk activated");
            }
        }
    }

    public String getFeatureStatus(String featureName) {
        return isFeatureEnabled(featureName) ? " [ON]" : " [OFF]";
    }

    public String[] getAllFeatures() {
        return features.keySet().toArray(new String[0]);
    }

    public int getActiveFeatureCount() {
        int count = 0;
        for (Boolean enabled : features.values()) {
            if (enabled) count++;
        }
        return count;
    }
}