package com.github.akafasty.authenticator.childcommand;

import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.api.model.Registry;
import com.github.akafasty.authenticator.internal.commands.child.ChildCommand;
import com.github.akafasty.authenticator.internal.commands.context.CommandContext;

public class VerifyCommand implements ChildCommand {

    private final AuthenticationController controller;

    public VerifyCommand(AuthenticationController controller) {
        this.controller = controller;
    }

    @Override
    public void execute(CommandContext context) {

        if (!context.getSender().hasPermission("akaauthenticator.verify")) {

            context.getSender().sendMessage("§cVocê não possui permissão para executar este comando.");
            return;

        }

        if (context.getArguments().length != 1) {

            context.getSender().sendMessage("§c/auth verify <player>");
            return;

        }

        String playerName = context.getAsString(0).toLowerCase();
        Registry registry = controller.getCollection().getRegisteredPlayers().getOrDefault(playerName, null);
        StringBuilder stringBuilder = new StringBuilder("\n");

        stringBuilder.append(" §7Registrado: ").append(registry != null ? "§asim" : "§cnão");

        if (registry != null) {
            stringBuilder.append("\n \n");
            stringBuilder.append("§e Dados brutos: ").append("\n ");
            stringBuilder.append(registry.getData().toJSONString());
        }

        stringBuilder.append("\n");

        context.getSender().sendMessage(stringBuilder.toString());

    }
}
