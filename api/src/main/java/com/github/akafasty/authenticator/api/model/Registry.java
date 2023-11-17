package com.github.akafasty.authenticator.api.model;

import org.json.simple.JSONObject;

public interface Registry {

    String getUsername();

    JSONObject getData();

    void setData(JSONObject jsonObject);

}
