package com.github.akafasty.authenticator.api.collection;

import com.github.akafasty.authenticator.api.model.Registry;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface AuthenticationCollection {

    List<String> getUnauthPlayers();
    Map<String, Registry> getRegisteredPlayers();
    Map<String, BufferedImage> getCachedImages();
    Map<String, ItemStack[]> getInventories();

}
