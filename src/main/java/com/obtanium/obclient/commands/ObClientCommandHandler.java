package com.obtanium.obclient.commands;

import net.minecraftforge.client.ClientCommandHandler;

public class ObClientCommandHandler {
    public static void registerCommands() {
        ClientCommandHandler.instance.registerCommand(new ObClientCommand());
    }
}
