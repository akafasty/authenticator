package com.github.akafasty.authenticator.conversation;

import com.github.akafasty.authenticator.AuthenticatorPlugin;
import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class AuthPrompt extends StringPrompt {

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return "§e* Digite o código de autenticação.";
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {

        AuthenticationController controller = AuthenticatorPlugin.getInstance().getController();
        Player player = (Player) conversationContext.getForWhom();

        CompletableFuture.runAsync(() -> {

            player.setItemInHand(null);

            if (controller.checkCode(player, s)) {

                //controller.getCollection().getRegisteredPlayers().add(player.getName());
                //controller.getRepository().insertOne(player.getName());

                player.sendRawMessage("§aVocê foi autenticado com sucesso!");

            }

            else player.kickPlayer("§cVocê falhou na autenticação!");

        });

        return END_OF_CONVERSATION;
    }
}
