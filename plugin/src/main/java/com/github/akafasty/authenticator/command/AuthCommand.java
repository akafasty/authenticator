package com.github.akafasty.authenticator.command;

import com.github.akafasty.authenticator.AuthenticatorPlugin;
import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.api.model.Registry;
import com.github.akafasty.authenticator.childcommand.RegisterCommand;
import com.github.akafasty.authenticator.childcommand.UnregisterCommand;
import com.github.akafasty.authenticator.childcommand.VerifyCommand;
import com.github.akafasty.authenticator.internal.commands.CommandBase;
import com.github.akafasty.authenticator.internal.commands.context.CommandContext;
import com.github.akafasty.authenticator.model.RegistryImpl;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.concurrent.CompletableFuture;

public class AuthCommand extends CommandBase {

    private final AuthenticationController controller;
    private final FileConfiguration configuration;

    public AuthCommand(AuthenticationController controller, FileConfiguration configuration) {

        super("auth", "Command usage for Google Authenticator.", "/auth", "akaauthenticator.use", "com/github/akafasty/authenticator");

        this.controller = controller;
        this.configuration = configuration;

        withChild(ImmutableMap.of(
                "register", new RegisterCommand(controller, configuration),
                "unregister", new UnregisterCommand(controller),
                "verify", new VerifyCommand(controller)
        ));

    }

    @Override
    public void executeEmpty(CommandContext context) {

        context.getSender().sendMessage("§e* Este servidor utiliza o AKAAuthenticator!");

    }

    @Override
    public void executeArgs(CommandContext context) {

        if (context.getSender() instanceof ConsoleCommandSender) return;

        Player player = (Player) context.getSender();
        String username = player.getName().toLowerCase();

        if (!controller.getCollection().getUnauthPlayers().contains(username)) {

            player.sendMessage("§cVocê já está autenticado.");
            return;

        }

        CompletableFuture.supplyAsync(() -> controller.checkCode(player, Strings.join(context.getArguments(), "")))
                .whenComplete((success, throwable) -> {

                    if (!success) {

                        player.kickPlayer(configuration.getString("kick-message", "§cVocê falhou na autenticação!").replace("&", "§"));
                        return;

                    }

                    if (!controller.getCollection().getRegisteredPlayers().containsKey(username)) {

                        Registry registry = RegistryImpl.builder().username(username).data(
                                new JSONObject(
                                        ImmutableMap.builder()
                                                .put("timestamp", System.currentTimeMillis())
                                                .put("address", player.getAddress().getHostName())
                                                .put("secret_code", controller.getSecretCode())
                                                .put("app_name", controller.getApplicationInfo())
                                                .build()
                                )
                        ).build();

                        controller.getCollection().getRegisteredPlayers().put(username, registry);
                        controller.getRepository().insertOne(registry);

                    }

                    player.sendTitle(configuration.getString("auth-title-title", "§aAutenticado com sucesso!").replace("&", "§"), configuration.getString("auth-title-sub-title", "").replace("&", "§"));
                    player.sendMessage(configuration.getString("auth-message", "§aVocê foi autenticado e agora pode agir normalmente.").replace("&", "§"));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1F, 1F);

                    controller.cleanUp(player);
                });

    }
}
