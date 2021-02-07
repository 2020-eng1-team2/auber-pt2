package com.threecubed.auber.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.Abilities;
import com.threecubed.auber.Utils;
import com.threecubed.auber.World;

/**
 * The power up entity that applies buffs to the player. Activated in {@link Player}
 *
 * */
public class PowerUp extends GameEntity {

    private final Abilities ability;

    private boolean beginTimer = false;
    private boolean pickup = true;

    public transient World world;

    private float time = 0f;
    private float countdown;

    /**
     * Initialises a power up
     *
     * @param x x-coord of power up
     * @param y y-coord of power up
     * @param world The game's world
     * @param ability The ability the power up is to grant
     * */
    public PowerUp(float x, float y, World world, Abilities ability) {
        super(x, y, Gdx.app.getType() == Application.ApplicationType.HeadlessDesktop
                ? Utils.createMockSprite(14, 14)
                : World.atlas.createSprite(
                ability.toString()));
        this.ability = ability;
        this.world = world;
        position = new Vector2(x, y);
        this.countdown = world.AUBER_BUFF_TIME;
    }

    /**
     * This constructor is necessary for save-loading to work.
     * It should NEVER be used directly by a human!
     */
    public PowerUp() {
        // Set a random ability here, then when we've been deserialized we'll replace the sprite
        super(
                0f,
                0f,
                Gdx.app.getType() == Application.ApplicationType.HeadlessDesktop
                        ? Utils.createMockSprite(14, 14)
                        : World.atlas.createSprite(Abilities.randomAbility().toString())
        );
        this.ability = Abilities.randomAbility();
    }

    public void recreateSprite() {
        this.sprite = World.atlas.createSprite(this.ability.toString());
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
                case Invisibility:
                    this.world.player.invisible = true;
                    break;
                case Invincibility:
                    this.world.player.invincible = true;
                    break;
                case SuperSpeed:
                    this.world.player.superspeed = true;
                    break;
                case Vision:
                    this.world.player.vision = true;
                    this.world.player.oneTimeVision = true;
                    break;
                case InstaBeam:
                    this.world.player.insta_beam = true;
                    break;
            }
            if (time > countdown) {
                removeBuff();
            }
        }
        if (!canPickup()) {
            this.sprite.setAlpha(0f);
        }
    }

    /**
     * Returns the ability in string form
     *
     * @return The ability of the power up
     * */
    public Abilities getAbility() {
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
            case Invisibility:
                this.world.player.invisible = false;
                break;
            case Invincibility:
                this.world.player.invincible = false;
                break;
            case SuperSpeed:
                this.world.player.superspeed = false;
                break;
            case Vision:
                this.world.player.vision = false;
                break;
            case InstaBeam:
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
