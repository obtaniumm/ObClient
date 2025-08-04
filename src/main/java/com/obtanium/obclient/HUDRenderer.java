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
import org.lwjgl.opengl.GL11;

public class HUDRenderer {
    private Minecraft mc;
    private FeatureManager featureManager;

    // Colors matching your client theme
    private static final int GREEN_COLOR = 0x00FF00;
    private static final int BLUE_COLOR = 0x4da6ff;
    private static final int LIGHT_BLUE_COLOR = 0x66b3ff;
    private static final int WHITE_COLOR = 0xFFFFFF;
    private static final int BACKGROUND_COLOR = 0x80000000; // Semi-transparent black

    public HUDRenderer() {
        this.mc = Minecraft.getMinecraft();
        this.featureManager = FeatureManager.getInstance();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            if (!mc.gameSettings.showDebugInfo && mc.currentScreen == null) {
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

        if (enabledCount == 0) return; // Don't show anything if no features are enabled

        // Position settings - top right corner like your reference
        int xOffset = sr.getScaledWidth() - maxWidth - 6;
        int yOffset = 2;
        int lineHeight = fontRenderer.FONT_HEIGHT + 2;

        // Draw background for the entire list
        int bgHeight = (enabledCount * lineHeight) + 4;
        drawRect(xOffset - 4, yOffset - 2, sr.getScaledWidth() - 2, yOffset + bgHeight - 2, BACKGROUND_COLOR);

        // Draw each enabled feature
        int currentY = yOffset;
        for (String feature : allFeatures) {
            if (featureManager.isFeatureEnabled(feature)) {
                // Draw feature name with color (green for enabled, matching your theme)
                fontRenderer.drawStringWithShadow(feature, xOffset, currentY, GREEN_COLOR);
                currentY += lineHeight;
            }
        }
    }

    // Helper method to draw rectangles
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