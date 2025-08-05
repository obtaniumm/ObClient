package com.obtanium.obclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDRenderer {
    private Minecraft mc;
    private FeatureManager featureManager;
    private ColorManager colorManager;

    private static boolean hudVisible = true;

    public HUDRenderer() {
        this.mc = Minecraft.getMinecraft();
        this.featureManager = FeatureManager.getInstance();
        this.colorManager = ColorManager.getInstance();
    }

    public static void toggleVisibility() {
        hudVisible = !hudVisible;
        System.out.println("[ObClient] HUD visibility: " + (hudVisible ? "ON" : "OFF"));
    }

    public static boolean isVisible() {
        return hudVisible;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            if (!mc.gameSettings.showDebugInfo && mc.currentScreen == null && hudVisible) {
                renderModuleList();
            }
        }
    }

    private void renderModuleList() {
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer fontRenderer = mc.fontRendererObj;

        String[] allFeatures = featureManager.getAllFeatures();

        // Count enabled features and get the longest text width
        int enabledCount = 0;
        int maxWidth = 0;

        for (String feature : allFeatures) {
            if (featureManager.isFeatureEnabled(feature)) {
                enabledCount++;
                int textWidth = fontRenderer.getStringWidth(feature);
                if (textWidth > maxWidth) {
                    maxWidth = textWidth;
                }
            }
        }

        if (enabledCount == 0) return;

        // Position settings - top right corner, moved more to the left
        int rightOffset = 5; // Move 20 pixels to the left from the edge
        int xOffset = sr.getScaledWidth() - maxWidth - 6 - rightOffset;
        int yOffset = 2;
        int lineHeight = fontRenderer.FONT_HEIGHT + 2;

        // Draw background for the entire list only if enabled
        if (colorManager.isBackgroundEnabled()) {
            int bgHeight = (enabledCount * lineHeight) + 4;
            drawRect(xOffset - 4, yOffset - 2, sr.getScaledWidth() - 2 - rightOffset, yOffset + bgHeight - 2,
                    colorManager.getHudBackgroundColor());
        }

        // Draw each enabled feature
        int currentY = yOffset;
        for (String feature : allFeatures) {
            if (featureManager.isFeatureEnabled(feature)) {
                fontRenderer.drawStringWithShadow(feature, xOffset, currentY, colorManager.getHudTextColor());
                currentY += lineHeight;
            }
        }
    }

    private void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int temp = left;
            left = right;
            right = temp;
        }

        if (top < bottom) {
            int temp = top;
            top = bottom;
            bottom = temp;
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(
                (float)(color >> 16 & 255) / 255.0F,
                (float)(color >> 8 & 255) / 255.0F,
                (float)(color & 255) / 255.0F,
                (float)(color >> 24 & 255) / 255.0F
        );

        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldRenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldRenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldRenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}