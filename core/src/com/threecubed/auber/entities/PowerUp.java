package com.threecubed.auber.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.World;

public class PowerUp extends GameEntity {

    private String ability;

    private boolean beginTimer = false;
    private boolean pickup = true;

    private World world;

    private float time = 0f;
    private float countdown = 10f;

    public PowerUp(float x, float y, World world, String ability) {
        super(x, y, world.atlas.createSprite(ability));
        this.ability = ability;
        this.world = world;
        position = new Vector2(x, y);
    }

    @Override
    public void update(World world) {
        if (beginTimer) {
            time += Gdx.graphics.getDeltaTime();
            // This is in update so that when 2 of the same buffs are active, they won't end when the first one does
            switch (this.ability) {
                case ("invisibility"):
                    this.world.player.invisible = true;
                    break;
                case ("invincibility"):
                    this.world.player.invincible = true;
                    break;
                case ("superspeed"):
                    this.world.player.superspeed = true;
                    break;
                case ("vision"):
                    this.world.player.vision = true;
                    this.world.player.oneTimeVision = true;
                    break;
                case ("insta_beam"):
                    this.world.player.insta_beam = true;
                    break;
            }
            if (time > countdown) {
                removeBuff();
            }
        }
    }

    public String getAbility() {
        return this.ability;
    }

    public Vector2 getPosition() {
        return super.position;
    }

    public void applyBuff() {
        // Start 10 sec timer, then remove after
        this.beginTimer = true;
        this.pickup = false;
        Gdx.app.log("timer", "start " + ability);
    }

    public void removeBuff() {
        Gdx.app.log("timer", "finished, removing buff " + ability);
        switch (this.ability) {
            case ("invisibility"):
                this.world.player.invisible = false;
                break;
            case ("invincibility"):
                this.world.player.invincible = false;
                break;
            case ("superspeed"):
                this.world.player.superspeed = false;
                break;
            case ("vision"):
                this.world.player.vision = false;
                break;
            case ("insta_beam"):
                this.world.player.insta_beam = false;
                break;
        }
        this.world.queueEntityRemove(this);
    }

    public boolean canPickup() {
        return this.pickup;
    }
}
