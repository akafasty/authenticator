package com.github.akafasty.authenticator.childcommand;

import com.github.akafasty.authenticator.AuthenticatorPlugin;
import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.internal.commands.child.ChildCommand;
import com.github.akafasty.authenticator.internal.commands.context.CommandContext;
import com.github.akafasty.authenticator.util.CustomMapRender;
import com.github.akafasty.authenticator.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import java.util.concurrent.CompletableFuture;

public class RegisterCommand implements ChildCommand {

    private final AuthenticationController controller;
    private final FileConfiguration configuration;

    public RegisterCommand(AuthenticationController controller, FileConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @Override
    public void execute(CommandContext context) {

        if (!context.getSender().hasPermission("akaauthenticator.use")) {

            context.getSender().sendMessage("§cVocê não possui permissão para executar este comando.");
            return;

        }

        final Player player = (Player) context.getSender();
        final String playerName = player.getName().toLowerCase();

        if (controller.isRegistered(player)) {

            player.sendMessage("§cVocê já está registrado.");
            return;

        }

        controller.getCollection().getUnauthPlayers().add(playerName);
        controller.getCollection().getInventories().put(player.getName().toLowerCase(), player.getInventory().getContents());

        player.getInventory().clear();

        player.setItemInHand(ItemBuilder.of(Material.MAP)
                .name("§eEscaneie o código")
                .nbt("qrcode", true)
                .make()
        );

        CompletableFuture.runAsync(() -> controller.register(player))
                .whenComplete((unused, throwable) -> {

                    System.out.println("Bukkit.isPrimaryThread() = " + Bukkit.isPrimaryThread());

                    MapView view = Bukkit.getMap(player.getItemInHand().getDurability());

                    view.getRenderers().forEach(view::removeRenderer);
                    view.addRenderer(new CustomMapRender(controller::getImage));

                });

        Bukkit.getScheduler().runTaskLater(AuthenticatorPlugin.getInstance(), () -> {

            final MapView view = Bukkit.getMap(player.getItemInHand().getDurability());

            view.getRenderers().forEach(view::removeRenderer);
            view.addRenderer(new CustomMapRender(controller::getImage));

            CompletableFuture.runAsync(() -> controller.register(player));

        }, 2L);

        player.sendTitle(configuration.getString("login-title-title", "§a§lAUTHENTICATOR!").replace("&", "§"), configuration.getString("login-title-sub-title", "§7Use: /auth <código>").replace("&", "§"));
        player.sendMessage(configuration.getString("login-message", "§eDigite o código de verificação!").replace("&", "§"));

    }
}
