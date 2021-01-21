package com.threecubed.auber;

/**
 * Holds various constants used throughout the game.
 */
public class Constants {
    /** Coordinates for the bottom left and top right tiles of the brig. */
    public static final float[][] BRIG_BOUNDS = {{240f, 608f}, {352f, 640f}};
    /** Coordinates for the medbay teleporter. */
    public static final float[] MEDBAY_COORDINATES = {96f, 640f};
    /** The rate at which the teleporter ray charges. */
    public static final float AUBER_CHARGE_RATE = 0.05f;
    /** The time the ray should visibly render for. */
    public static final float AUBER_RAY_TIME = 0.25f;
    /** The time a debuff should last for (with the exception of blindness). */
    public static final float AUBER_DEBUFF_TIME = 5f;
    /** The rate at which auber should heal. */
    public static final float AUBER_HEAL_RATE = 0.005f;
    /** The amount of time it takes for an infiltrator to sabotage a system. */
    public static final float SYSTEM_BREAK_TIME = 5f;
    /** The chance an infiltrator will sabotage after pathfinding to a system. */
    public static final float SYSTEM_SABOTAGE_CHANCE = 0.6f;
    /** The distance the infiltrator can see. Default: 5 tiles */
    public static final float INFILTRATOR_SIGHT_RANGE = 80f;
    /** The speed at which infiltrator projectiles should travel. */
    public static final float INFILTRATOR_PROJECTILE_SPEED = 4f;
    /** Maximum infiltrators in a full game of Auber (including defated ones). */
    public static final int MAX_INFILTRATORS = 8;
    /** The interval at which the infiltrator should attack the player when exposed. */
    public static final float INFILTRATOR_FIRING_INTERVAL = 5f;
    /** The damage a projectile should do. */
    public static final float INFILTRATOR_PROJECTILE_DAMAGE = 0.2f;
    /**
     * Max infiltrators alive at a given point, Should always be greater or equal to
     * {@link Constants#MAX_INFILTRATORS}.
     * */
    public static final int MAX_INFILTRATORS_IN_GAME = 3;
    /** The amount of variance there should be between the speeds of different NPCs. */
    public static final float[] NPC_SPEED_VARIANCE = {0.8f, 1.2f};
    /** The maximum amount of time (in seconds) an NPC should flee for. */
    public static final float NPC_FLEE_TIME = 10f;
    /** The speed multiplier an NPC should receive when fleeing. */
    public static final float NPC_FLEE_MULTIPLIER = 1.2f;
    /** The shortest distance an NPC should move from its current position when fleeing. */
    public static final float NPC_MIN_FLEE_DISTANCE = 80f;
    /** The distance an NPC can here the teleporter ray shoot from. */
    public static final float NPC_EAR_STRENGTH = 80f;
    /** The number of NPCs in the game. */
    public static final int NPC_COUNT = 24;
}
