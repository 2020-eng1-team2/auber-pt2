package com.threecubed.auber.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.World;
import com.threecubed.auber.Utils;

public class PowerUp extends GameEntity {

    private String ability;

    public PowerUp(float x, float y, World world, String ability) {
        super(x, y, world.atlas.createSprite(ability));
        this.ability = ability;
        position = new Vector2(x, y);
    }

    @Override
    public void update(World world) {

    }

    public String getAbility() {
        return this.ability;
    }

    public Vector2 getPosition() {
        return super.position;
    }
}
