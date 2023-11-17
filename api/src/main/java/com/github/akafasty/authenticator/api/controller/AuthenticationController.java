package com.github.akafasty.authenticator.api.controller;

import com.github.akafasty.authenticator.api.collection.AuthenticationCollection;
import com.github.akafasty.authenticator.api.repository.AuthenticationRepository;
import com.github.akafasty.authenticator.api.service.AuthenticationService;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public interface AuthenticationController {

    AuthenticationService getService();
    AuthenticationCollection getCollection();
    AuthenticationRepository getRepository();

    boolean isRegistered(Player player);
    boolean checkCode(Player player, String code);

    BufferedImage getImage(Player player);

    String getSecretCode();
    String getApplicationInfo();

    void cleanUp(Player player);
    void register(Player player);

}
