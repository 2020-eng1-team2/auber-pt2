package com.threecubed.auber;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * A holding class for game assets.
 */
public class Assets {
    public static TiledMap map;
    public static TiledMapTileSet tileset;
    public static TextureAtlas atlas;

    static void create() {
        atlas = new TextureAtlas("auber.atlas");
        map = new TmxMapLoader().load("map.tmx");
        tileset = map.getTileSets().getTileSet(0);
    }

    static void dispose() {
        map.dispose();
        atlas.dispose();
    }
}
