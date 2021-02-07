package com.threecubed.auber;

import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(GdxTestRunner.class)
public class UtilsTest {

    Random rng;

    @Before
    public void createWorld() {
        rng = new Random();
    }

    @Test
    public void randomFloatTest() {
        float upperBound = 20f;
        float lowerBound = 10f;
        float test = Utils.randomFloatInRange(rng, lowerBound, upperBound);
        Assert.assertTrue(lowerBound <= test && test <= upperBound);
    }

    @Test
    public void randomIntegerTest() {
        int upperBound = 20;
        int lowerBound = 10;
        int test = Utils.randomIntInRange(rng, lowerBound, upperBound);
        Assert.assertTrue(lowerBound <= test && test <= upperBound);
    }
}
