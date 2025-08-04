package com.obtanium.obclient;

import com.obtanium.obclient.gui.GuiObClient;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ObClient.MODID, version = ObClient.VERSION, name = ObClient.NAME)
public class ObClient {
    public static final String MODID = "obclient";
    public static final String NAME = "ObClient";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static ObClient instance;

    private HUDRenderer hudRenderer;
    private KeyBindHandler keyBindHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("ObClient initializing...");
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("ObClient initialized successfully!");

        // Initialize managers
        ColorManager.getInstance(); // Initialize color settings

        // Initialize and register HUD renderer
        hudRenderer = new HUDRenderer();
        MinecraftForge.EVENT_BUS.register(hudRenderer);

        // Initialize and register keybind handler
        keyBindHandler = new KeyBindHandler();
        MinecraftForge.EVENT_BUS.register(keyBindHandler);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        // Replace main menu with custom GUI
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new GuiObClient();
        }
    }
}