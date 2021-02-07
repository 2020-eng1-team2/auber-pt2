package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.entities.Npc;
import com.threecubed.auber.screens.GameOverScreen;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class endGameTest {

    private World world;
    private AuberGame game;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        world = new World(game);
    }

    @Test
    public void winConditionTest() {
        world.infiltratorCount = -1;
        world.checkForEndState();
        Assert.assertEquals(true, GameOverScreen.userWon);
    }

    @Test
    public void loseConditionTest() {
        // Set systems to empty list
        world.systems = new ArrayList<>();
        world.checkForEndState();
        Assert.assertEquals(false, GameOverScreen.userWon);
    }
}
