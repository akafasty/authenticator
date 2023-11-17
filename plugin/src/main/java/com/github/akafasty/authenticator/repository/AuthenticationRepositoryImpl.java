package com.github.akafasty.authenticator.repository;

import com.github.akafasty.authenticator.api.model.Registry;
import com.github.akafasty.authenticator.api.repository.AuthenticationRepository;
import com.github.akafasty.authenticator.hikari.HikariWrapper;
import com.github.akafasty.authenticator.model.RegistryImpl;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private final HikariWrapper connectionWrapper;

    public AuthenticationRepositoryImpl(HikariWrapper connectionWrapper) {
        this.connectionWrapper = connectionWrapper;

        try (Connection connection = connectionWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement("create table if not exists google_authenticator(username varchar(32), data text);")) {

            statement.execute();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    @Override
    public void insertOne(Registry registry) {

        try (Connection connection = connectionWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into google_authenticator(username, data) values (?, ?);")) {

            statement.setString(1, registry.getUsername());
            statement.setString(2, registry.getData().toJSONString());

            statement.executeUpdate();

        }

        catch (Exception exception) {

            exception.printStackTrace();

            Bukkit.getLogger().log(Level.SEVERE, "An exception was thrown when saving auth data for " + registry.getUsername());

        }

    }

    @Override
    public void deleteOne(String username) {

        try (Connection connection = connectionWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from google_authenticator where username = ?;")) {

            statement.setString(1, username);
            statement.executeUpdate();

        }

        catch (Exception exception) {

            exception.printStackTrace();

            Bukkit.getLogger().log(Level.SEVERE, "An exception was thrown when searching auth data for " + username);

        }

    }

    @Override
    public Registry selectOne(String username) {

        Registry data = null;

        try (Connection connection = connectionWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from google_authenticator where username = ?;")) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next())
                    data = RegistryImpl.builder().username(username).data((JSONObject) JSONValue.parse(result.getString("data"))).build();

            }
        }

        catch (Exception exception) {

            exception.printStackTrace();

            Bukkit.getLogger().log(Level.SEVERE, "An exception was thrown when searching auth data for " + username);

        }

        return data;
    }

    @Override
    public Map<String, Registry> selectAll() {

        Map<String, Registry> registry = Maps.newHashMap();

        try (Connection connection = connectionWrapper.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from google_authenticator;")) {

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String username = result.getString("username");

                    registry.put(username, RegistryImpl.builder()
                            .username(username)
                            .data((JSONObject) JSONValue.parse(result.getString("data")))
                            .build()
                    );

                }
            }
        }

        catch (Exception exception) {

            exception.printStackTrace();

            Bukkit.getLogger().log(Level.SEVERE, "An exception was thrown when searching auth datas.");

        }

        return registry;
    }
}
