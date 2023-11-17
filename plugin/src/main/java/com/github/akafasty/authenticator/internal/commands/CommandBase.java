package com.github.akafasty.authenticator.internal.commands;

import com.github.akafasty.authenticator.internal.commands.child.ChildCommand;
import com.github.akafasty.authenticator.internal.commands.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.Arrays;
import java.util.Map;

public abstract class CommandBase extends Command {

    private final String permission;
    private Map<String, ChildCommand> childCommandMap;

    public CommandBase(String name, String description, String usage, String permission, String... aliases) {

        super(name, description, usage, Arrays.asList(aliases));

        this.permission = permission;

        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        SimpleCommandMap simpleCommandMap = craftServer.getCommandMap();

        simpleCommandMap.register("akafasty", this);

    }

    public abstract void executeEmpty(CommandContext context);
    public abstract void executeArgs(CommandContext context);

    public void withChild(Map<String, ChildCommand> childCommandMap) {
        this.childCommandMap = childCommandMap;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if (!commandSender.hasPermission(permission)) {

            commandSender.sendMessage("§cVocê não possui acesso a este comando.");
            return false;

        }

        if (strings.length == 0) {
            executeEmpty(CommandContext.of(s, commandSender, strings, this));
            return true;
        }

        ChildCommand command = childCommandMap == null ? null : childCommandMap.get(strings[0].toLowerCase());

        if (command == null) executeArgs(CommandContext.of(s, commandSender, strings, this));
        else command.execute(CommandContext.of(s, commandSender, Arrays.copyOfRange(strings, 1, strings.length), this));

        return false;
    }

}
