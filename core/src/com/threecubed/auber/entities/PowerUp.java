package com.threecubed.auber.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.threecubed.auber.World;
import com.threecubed.auber.Utils;

public class PowerUp extends GameEntity {

    // TODO: Move this enum to class where PowerUp() is called, pass Abilities.toString() into constructor
    // Randomization needs to be done before construction
    public static enum Abilities {
        superspeed,
        vision,
        invincibility,
        invisibility,
        insta_beam
    }

    private static final int NUMBER_OF_ABILITIES = 5;

    public PowerUp(float x, float y, World world, String ability) {
        super(x, y, world.atlas.createSprite(ability));
    }

    @Override
    public void update(World world) {
        // TODO: Check if player has picked up ability

        //RectangleMapObject nearbyObject = getNearbyObjects(world.map);

        //if (nearbyObject != null) {
        //   Gdx.app.log("nearby", nearbyObject.getName());
        //}
    }
}
