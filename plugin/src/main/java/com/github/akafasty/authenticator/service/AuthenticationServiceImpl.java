package com.github.akafasty.authenticator.service;

import com.github.akafasty.authenticator.api.service.AuthenticationService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final String pairUrl = "https://www.authenticatorApi.com/pair.aspx?AppName=%s&AppInfo=%s&SecretCode=%s";
    private final String validateUrl = "https://www.authenticatorApi.com/validate.aspx?Pin=%s&SecretCode=%s";

    @Override
    public BufferedImage pair(String appName, String appInfo, String secretCode) {

        String finalUrl;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(String.format(pairUrl, appName, appInfo, secretCode)).openStream()))) {

            finalUrl = reader.readLine().split("'")[5];
            finalUrl = finalUrl.replace("300x300", "128x128");

            return ImageIO.read(new URL(finalUrl));

        }

        catch (Exception exception) { exception.printStackTrace(); return null; }

    }

    @Override
    public boolean validate(String pin, String secretCode) {

        String response = "False";

        pin = pin.replaceAll(" ", "");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(String.format(validateUrl, pin, secretCode)).openStream()))) {

            response = reader.readLine();

        }

        catch (Exception ignored) { ignored.printStackTrace(); }

        System.out.println("response = " + response);

        return response.equals("True");

    }
}