package com.github.akafasty.authenticator.model;

import com.github.akafasty.authenticator.api.model.Registry;
import lombok.Builder;
import lombok.Data;
import org.json.simple.JSONObject;

@Data @Builder
public class RegistryImpl implements Registry {

    private final String username;
    private JSONObject data;

}
