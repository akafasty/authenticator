package com.github.akafasty.authenticator.listener;

import com.github.akafasty.authenticator.AuthenticatorPlugin;
import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.util.CustomMapRender;
import com.github.akafasty.authenticator.util.ItemBuilder;
import com.nickuc.login.api.event.bukkit.auth.AuthenticateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.map.MapView;

import java.util.concurrent.CompletableFuture;

public class ConnectionListener implements Listener {

    private final AuthenticationController controller;
    private final FileConfiguration configuration;

    public ConnectionListener(AuthenticationController controller, FileConfiguration configuration) {
        this.controller = controller;
        this.configuration = configuration;
    }

    @EventHandler
    void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        controller.cleanUp(player);

    }

    @EventHandler
    void on(AuthenticateEvent event) {

        Player player = event.getPlayer();
        String username = player.getName().toLowerCase();

        if (!controller.getCollection().getRegisteredPlayers().containsKey(username) && !player.hasPermission("akaauthenticator.force")) return;

        controller.getCollection().getUnauthPlayers().add(username);

        Bukkit.getScheduler().runTask(AuthenticatorPlugin.getInstance(), () -> {

            controller.getCollection().getInventories().put(username, player.getInventory().getContents());
            player.getInventory().clear();

            player.sendTitle(configuration.getString("login-title-title", "§a§lAUTHENTICATOR!").replace("&", "§"), configuration.getString("login-title-sub-title", "§7Use: /auth <código>").replace("&", "§"));
            player.sendMessage(configuration.getString("login-message", "§eDigite o código de verificação!").replace("&", "§"));

            if (player.hasPermission("akaauthenticator.force") && !controller.isRegistered(player)) {

                player.setItemInHand(ItemBuilder.of(Material.MAP)
                        .name("§eEscaneie o código")
                        .nbt("qrcode", true)
                        .flags(ItemFlag.HIDE_ATTRIBUTES)
                        .make()
                );

                Bukkit.getScheduler().runTaskLater(AuthenticatorPlugin.getInstance(), () -> {

                    MapView view = Bukkit.getMap(player.getItemInHand().getDurability());

                    view.getRenderers().forEach(view::removeRenderer);
                    view.addRenderer(new CustomMapRender(controller::getImage));

                    CompletableFuture.runAsync(() -> controller.register(player));

                }, 1L);

            }
        });
    }
}
