package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Npc;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.ui.Difficulties;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class InfiltratorTest {

    private AuberGame game;
    private GameScreen gs;
    private World world;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        gs = new GameScreen(game, Difficulties.Easy);
        world = gs.world;
    }

    @Test
    public void infiltratorsSpawnedTest() {
        int counter = 0;
        world.updateEntities();
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Infiltrator) {
                counter++;
            }
        }
        Assert.assertEquals(world.MAX_INFILTRATORS_IN_GAME, counter);
    }

    @Test
    public void playerInfiltratorSpeedTest() {
        world.updateEntities();
        Infiltrator infiltrator = null;
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Infiltrator) {
                infiltrator = (Infiltrator) ent;
                break;
            }
        }
        if (infiltrator == null) {
            // No infiltrators on map :(
            Assert.assertTrue(false);
        }
        else {
            Assert.assertEquals(infiltrator.speed, world.player.speed, 0);
        }
    }
}
