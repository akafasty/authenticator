package com.github.akafasty.authenticator.listener;

import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

public class BlockListener implements Listener {

    private final AuthenticationController controller;

    public BlockListener(AuthenticationController controller) {
        this.controller = controller;
    }

    @EventHandler
    void on(PlayerInteractEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName())) return;

        event.setCancelled(true);

    }

    @EventHandler
    void on(PlayerCommandPreprocessEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName()) || event.getMessage().startsWith("/auth")) return;

        event.setCancelled(true);

    }

    @EventHandler
    void on(AsyncPlayerChatEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName()) || event.getMessage().startsWith("/auth")) return;

        event.setCancelled(true);

    }

    @EventHandler
    void on(PlayerMoveEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName())) return;

        Location to = event.getTo(),
                from  = event.getFrom();

        if (to.distance(from) <= 0.05) return;

        from.setYaw(to.getYaw());
        from.setPitch(to.getPitch());

        event.setTo(event.getFrom());

    }

    @EventHandler
    void on(PlayerDropItemEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName())) return;

        event.setCancelled(true);

    }

    @EventHandler
    void on(BlockPlaceEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName())) return;

        event.setCancelled(true);

    }

    @EventHandler
    void on(BlockBreakEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName())) return;

        event.setCancelled(true);

    }

    @EventHandler
    void on(PlayerItemHeldEvent event) {

        if (!controller.getCollection().getUnauthPlayers().contains(event.getPlayer().getName())) return;

        event.setCancelled(true);

    }

}
