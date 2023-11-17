package com.github.akafasty.authenticator.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;

public class HikariWrapper {

    private final HikariDataSource dataSource;

    public HikariWrapper(FileConfiguration configuration) {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", configuration.getString("repo.host"), configuration.getInt("repo.port"), configuration.getString("repo.database")));
        hikariConfig.setUsername(configuration.getString("repo.user"));
        hikariConfig.setPassword(configuration.getString("repo.password"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize" , "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        hikariConfig.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2);

        dataSource = new HikariDataSource(hikariConfig);

    }

    public Connection getConnection() {

        try { return dataSource.getConnection(); }
        /*
        try (Connection connection = dataSource.getConnection()) {

            return connection;

        }
         */

        catch (Exception exception) { exception.printStackTrace(); }

        return null;

    }

    public void close() {

        try { dataSource.close(); }
        catch (Exception exception) { exception.printStackTrace(); }

    }

}
