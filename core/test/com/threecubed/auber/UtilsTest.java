package com.threecubed.auber;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.threecubed.auber.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    Random rng = new Random();

    @Test
    public void randomFloatInRangeTest() {
        float testCase = randomFloatInRange(rng, 10f, 70f);
        assertEquals(testCase >= 10f, testCase <= 70f);
    }

    @Test
    public void randomIntInRangeTest() {
        int testCase = randomIntInRange(rng, 10, 70);
        assertEquals(testCase >= 10, testCase <= 70);
    }
}
