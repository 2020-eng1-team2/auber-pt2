package com.threecubed.auber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Abilities {
    SuperSpeed,
    Invisibility,
    Invincibility,
    InstaBeam,
    Vision;

    private static final List<Abilities> ABILITIES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int length = ABILITIES.size();
    private static final Random rng = new Random();

    public static Abilities randomAbility() {
        return ABILITIES.get(rng.nextInt(length));
    }
}
