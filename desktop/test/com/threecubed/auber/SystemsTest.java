package com.threecubed.auber;

import com.threecubed.auber.screens.GameScreen;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class SystemsTest {

    AuberGame game;
    GameScreen gs;
    World world;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        gs = new GameScreen(game, false);
        world = gs.world;
    }

    @Test
    public void keySystemsTest() {
        world.updateEntities();
        Assert.assertTrue(world.systems.size() >= 15);
    }
}
