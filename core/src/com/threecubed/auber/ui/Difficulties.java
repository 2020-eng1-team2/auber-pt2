package com.threecubed.auber.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Difficulties {
    Easy,
    Normal,
    Hard;

    private static final List<Difficulties> DIFFICULTIES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int length = DIFFICULTIES.size();

    public Difficulties nextDifficulty(Difficulties diff) {
        if (diff == Easy) {
            return Normal;
        }
        else if (diff == Normal) {
            return Hard;
        }
        else {
            // diff == Hard
            return Easy;
        }
    }
}
