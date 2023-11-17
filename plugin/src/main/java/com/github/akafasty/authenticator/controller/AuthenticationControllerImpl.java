package com.github.akafasty.authenticator.controller;

import com.github.akafasty.authenticator.api.collection.AuthenticationCollection;
import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.api.repository.AuthenticationRepository;
import com.github.akafasty.authenticator.api.service.AuthenticationService;
import com.github.akafasty.authenticator.collection.AuthenticationCollectionImpl;
import com.github.akafasty.authenticator.hikari.HikariWrapper;
import com.github.akafasty.authenticator.repository.AuthenticationRepositoryImpl;
import com.github.akafasty.authenticator.service.AuthenticationServiceImpl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;

public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService service = new AuthenticationServiceImpl();
    private final AuthenticationCollection collection = new AuthenticationCollectionImpl();

    private final AuthenticationRepository repository;
    private final String secretCode, appName;

    public AuthenticationControllerImpl(FileConfiguration configuration, HikariWrapper connectionWrapper) {

        secretCode = configuration.getString("secret-code", "5OuhsBLm66jg6ZTT");
        appName = configuration.getString("app-name", "AkaAuthenticator");
        repository = new AuthenticationRepositoryImpl(connectionWrapper);

        collection.getRegisteredPlayers().putAll(repository.selectAll());

    }

    @Override
    public AuthenticationService getService() {
        return service;
    }

    @Override
    public AuthenticationCollection getCollection() {
        return collection;
    }

    @Override
    public AuthenticationRepository getRepository() {
        return repository;
    }

    @Override
    public boolean isRegistered(Player player) {
        return collection.getRegisteredPlayers().containsKey(player.getName().toLowerCase());
    }

    @Override
    public BufferedImage getImage(Player player) {
        return collection.getCachedImages().get(player.getName());
    }

    @Override
    public String getSecretCode() {
        return secretCode;
    }

    @Override
    public String getApplicationInfo() {
        return appName;
    }

    @Override
    public void cleanUp(Player player) {

        String username = player.getName().toLowerCase();
        ItemStack[] contents = collection.getInventories().get(username);

        if (contents != null) {
            player.getInventory().clear();
            player.getInventory().setContents(contents);
        }

        collection.getUnauthPlayers().remove(username);
        collection.getCachedImages().remove(username);
        collection.getInventories().remove(username);
    }

    @Override
    public void register(Player player) {

        if (collection.getCachedImages().containsKey(player.getName())) return;

        collection.getCachedImages().put(player.getName(), service.pair(appName, player.getName(), secretCode + player.getName()));
    }

    @Override
    public boolean checkCode(Player player, String code) {
        return service.validate(code, secretCode + player.getName());
    }
}
