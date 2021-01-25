package com.threecubed.auber.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.World;

/**
 * The power up entity that applies buffs to the player. Activated in {@link Player}
 *
 * */
public class PowerUp extends GameEntity {

    private String ability;

    private boolean beginTimer = false;
    private boolean pickup = true;

    private World world;

    private float time = 0f;
    private float countdown = 10f;

    /**
     * Initialises a power up
     *
     * @param x x-coord of power up
     * @param y y-coord of power up
     * @param world The game's world
     * @param ability The ability the power up is to grant
     * */
    public PowerUp(float x, float y, World world, World.Abilities ability) {
        super(x, y, world.atlas.createSprite(ability.toString()));
        this.ability = ability.toString();
        this.world = world;
        position = new Vector2(x, y);
    }

    /**
     * Ensures the power up is active throughout its duration.
     *
     * @param world The game's world
     * */
    @Override
    public void update(World world) {
        if (beginTimer) {
            time += Gdx.graphics.getDeltaTime();
            // This is in update so that when 2 of the same buffs are active, they won't end when the first one does
            switch (this.ability) {
                case ("Invisibility"):
                    this.world.player.invisible = true;
                    break;
                case ("Invincibility"):
                    this.world.player.invincible = true;
                    break;
                case ("SuperSpeed"):
                    this.world.player.superspeed = true;
                    break;
                case ("Vision"):
                    this.world.player.vision = true;
                    this.world.player.oneTimeVision = true;
                    break;
                case ("InstaBeam"):
                    this.world.player.insta_beam = true;
                    break;
            }
            if (time > countdown) {
                removeBuff();
            }
        }
    }

    /**
     * Returns the ability in string form
     *
     * @return The ability of the power up
     * */
    public String getAbility() {
        return this.ability;
    }

    /**
     * Returns the position of the power up in a Vector2
     *
     * @return The position of the power up
     * */
    public Vector2 getPosition() {
        return super.position;
    }

    /**
     * Begins timer, activates power up and hides the sprite from the player
     * */
    public void applyBuff() {
        // Start 10 sec timer, then remove after
        this.beginTimer = true;
        this.pickup = false;
        Gdx.app.log("timer", "start " + ability);
    }

    /**
     * Deactivates this power up and removes it from the Entity List in world
     * */
    public void removeBuff() {
        Gdx.app.log("timer", "finished, removing buff " + ability);
        switch (this.ability) {
            case ("Invisibility"):
                this.world.player.invisible = false;
                break;
            case ("Invincibility"):
                this.world.player.invincible = false;
                break;
            case ("SuperSpeed"):
                this.world.player.superspeed = false;
                break;
            case ("Vision"):
                this.world.player.vision = false;
                break;
            case ("InstaBeam"):
                this.world.player.insta_beam = false;
                break;
        }
        this.world.queueEntityRemove(this);
    }

    /**
     * Returns the objects canPickup bool (used to detect if the player has already picked it up)
     *
     * @return Whether the power up can be picked up
     * */
    public boolean canPickup() {
        return this.pickup;
    }
}
