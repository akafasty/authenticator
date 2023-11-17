package com.github.akafasty.authenticator.api.service;

import java.awt.image.BufferedImage;

public interface AuthenticationService {

    BufferedImage pair(String appName, String appInfo, String secretCode);
    boolean validate(String pin, String secretCode);

}
