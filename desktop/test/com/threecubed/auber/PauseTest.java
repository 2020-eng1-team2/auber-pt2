package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.screens.GameScreen;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class PauseTest {

    private AuberGame game;
    private World world;
    private GameScreen gs;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        gs = new GameScreen(game, false);
        world = gs.world;
    }

    @Test
    public void pauseTest() {
        // pick entity, log position, pause, resume, compare position
        Civilian n = new Civilian(world);
        n.update(world);
        Vector2 oldPos = n.position.cpy();
        // Pause
        gs.paused = true;
        // Update entities
        world.updateEntities();
        // Unpause
        gs.paused = false;
        // Compare
        Assert.assertEquals(n.position, oldPos);
    }
}
