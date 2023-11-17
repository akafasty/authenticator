package com.github.akafasty.authenticator;

import com.github.akafasty.authenticator.api.controller.AuthenticationController;
import com.github.akafasty.authenticator.command.AuthCommand;
import com.github.akafasty.authenticator.controller.AuthenticationControllerImpl;
import com.github.akafasty.authenticator.hikari.HikariWrapper;
import com.github.akafasty.authenticator.listener.BlockListener;
import com.github.akafasty.authenticator.listener.ConnectionListener;
import com.github.akafasty.authenticator.util.ConfigurationProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AuthenticatorPlugin extends JavaPlugin {

    private AuthenticationController controller;
    private FileConfiguration lang;
    private HikariWrapper connectionWrapper;

    public static AuthenticatorPlugin getInstance() {
        return JavaPlugin.getPlugin(AuthenticatorPlugin.class);
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        ConfigurationProvider.of("permissions", this).getInstance();

        lang = ConfigurationProvider.of("lang", this).getInstance();

        connectionWrapper = new HikariWrapper(getConfig());

        controller = new AuthenticationControllerImpl(getConfig(), connectionWrapper);

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(controller, lang), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(controller), this);

        new AuthCommand(controller, lang);

    }

    @Override
    public void onDisable() {
        if (connectionWrapper != null)
            connectionWrapper.close();
    }
}
