package com.github.akafasty.authenticator.util;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public class CustomMapRender extends MapRenderer {

    private final Function<Player, BufferedImage> imageFunction;
    private boolean isRendered = false;

    public CustomMapRender(Function<Player, BufferedImage> imageFunction) {
        this.imageFunction = imageFunction;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {

        if (isRendered) return;

        BufferedImage image = imageFunction.apply(player);

        if (image == null) return;

        mapView.setScale(MapView.Scale.CLOSEST);
        mapCanvas.drawImage(0, 0, image);

        isRendered = true;

    }
}
