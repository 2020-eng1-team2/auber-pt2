package com.threecubed.auber.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Difficulties {
    Easy,
    Hard;

    public Difficulties nextDifficulty(Difficulties diff) {
        if (diff == Easy) {
            return Hard;
        }
        else {
            // diff == Hard
            return Easy;
        }
    }
}
