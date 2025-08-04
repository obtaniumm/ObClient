package com.obtanium.obclient.commands;

import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;

public class ClientCommandHandler {

    public static void registerCommands() {
        // Register the /obclient command
        ClientCommandHandler.instance.registerCommand(new ObClientCommand());

        System.out.println("[ObClient] Registered client commands");
    }
}