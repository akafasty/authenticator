package com.github.akafasty.authenticator.collection;

import com.github.akafasty.authenticator.api.collection.AuthenticationCollection;
import com.github.akafasty.authenticator.api.model.Registry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class AuthenticationCollectionImpl implements AuthenticationCollection {

    private final Map<String, BufferedImage> cachedImages = Maps.newHashMap();
    private final Map<String, Registry> registered = Maps.newHashMap();
    private final Map<String, ItemStack[]> inventories = Maps.newHashMap();
    private final List<String> unAuth = Lists.newArrayList();

    @Override
    public List<String> getUnauthPlayers() {
        return unAuth;
    }

    @Override
    public Map<String, Registry> getRegisteredPlayers() {
        return registered;
    }

    @Override
    public Map<String, BufferedImage> getCachedImages() {
        return cachedImages;
    }

    @Override
    public Map<String, ItemStack[]> getInventories() {
        return inventories;
    }
}
