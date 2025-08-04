package com.obtanium.obclient.commands;

import com.obtanium.obclient.gui.GuiInGameObClient;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;

public class ObClientCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "obclient";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/obclient - Opens ObClient settings menu";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Anyone can use this command
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // Open the in-game GUI
        Minecraft.getMinecraft().displayGuiScreen(new GuiInGameObClient());
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return Arrays.asList("menu", "gui", "settings");
    }
}