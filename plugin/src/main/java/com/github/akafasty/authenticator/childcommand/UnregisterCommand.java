package com.github.akafasty.authenticator.childcommand;

import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.internal.commands.child.ChildCommand;
import com.github.akafasty.authenticator.internal.commands.context.CommandContext;
import org.bukkit.entity.Player;

public class UnregisterCommand implements ChildCommand {

    private final AuthenticationController controller;

    public UnregisterCommand(AuthenticationController controller) {
        this.controller = controller;
    }

    @Override
    public void execute(CommandContext context) {

        if (context.getSender() instanceof Player) {

            context.getSender().sendMessage("§cEste comando só pode ser executado pelo console.");
            return;

        }

        if (context.getArguments().length != 1) {

            context.getSender().sendMessage("§c/auth unregister <player>");
            return;

        }

        String playerName = context.getAsString(0).toLowerCase();

        if (!controller.getCollection().getRegisteredPlayers().containsKey(playerName)) {

            context.getSender().sendMessage("§cEste usuário não está registrado.");
            return;

        }

        controller.getRepository().deleteOne(playerName);
        controller.getCollection().getRegisteredPlayers().remove(playerName);

        context.getSender().sendMessage(String.format("§aVocê desregistrou %s com sucesso.", playerName));

    }
}
