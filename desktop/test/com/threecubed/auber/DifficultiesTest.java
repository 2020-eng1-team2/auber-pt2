package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.ui.Difficulties;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class DifficultiesTest {

    private AuberGame game;
    private World world;
    private GameScreen gs;
    private Difficulties diffEasy;
    private Difficulties diffHard;

    @Before
    public void createWorld() {
        diffEasy = Difficulties.Easy;
        diffHard = Difficulties.Hard;
    }

    @Test
    public void easyNextDifficultyTest() {
        Assert.assertEquals(Difficulties.Hard, diffEasy.nextDifficulty(diffEasy));
    }

    @Test
    public void hardNextDifficultyTest() {
        Assert.assertEquals(Difficulties.Easy, diffHard.nextDifficulty(diffHard));
    }
}
