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
public class PlayerTest {

    private AuberGame game;
    private GameScreen gs;
    private World world;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        gs = new GameScreen(game, Difficulties.Easy);
        world = gs.world;
    }

    // This is handled in GameScreen.render() which testing doesn't like
    //@Test
    //public void cameraPositionTest() {
    //    int counter = 0;
    //    world.updateEntities();
    //    world.camera.update();
    //    Assert.assertEquals(world.camera.position, world.player.position);
    //}

    @Test
    public void playerSpawnTest() {
        AuberGame newGame = mock(AuberGame.class);
        GameScreen newGS = new GameScreen(newGame, Difficulties.Easy);
        World newWorld = newGS.world;
        Assert.assertEquals(newWorld.player.position, world.player.position);
    }
}
