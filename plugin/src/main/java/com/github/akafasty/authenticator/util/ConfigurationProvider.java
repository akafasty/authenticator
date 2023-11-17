package com.github.akafasty.authenticator.util;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

@Data(staticConstructor = "of")
public class ConfigurationProvider {

    private final String file;
    private final Plugin plugin;

    public FileConfiguration getInstance() {

        plugin.saveResource(file + ".yml", false);

        File fileName = new File(plugin.getDataFolder() + File.separator + file + ".yml");

        return YamlConfiguration.loadConfiguration(fileName);
    }

}
