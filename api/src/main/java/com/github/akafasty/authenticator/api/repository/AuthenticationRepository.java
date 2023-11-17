package com.github.akafasty.authenticator.api.repository;

import com.github.akafasty.authenticator.api.model.Registry;

import java.util.Map;

public interface AuthenticationRepository {

    void insertOne(Registry registry);
    void deleteOne(String username);

    Registry selectOne(String username);

    Map<String, Registry> selectAll();

}
