package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.*;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.ui.Difficulties;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class InfirmaryTest {

    private AuberGame game;
    private GameScreen gs;
    private World world;
    private Player player;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        gs = new GameScreen(game, Difficulties.Easy);
        world = gs.world;
        player = world.player;
    }

    @Test
    public void healTest() {
        float startHealth = 0.5f;
        player.health = startHealth;
        // Teleport Auber to Infirmary
        player.position.set(world.MEDBAY_COORDINATES[0], world.MEDBAY_COORDINATES[1]);
        world.updateEntities();
        player.update(world);
        Assert.assertNotEquals(startHealth, player.health);
    }

    @Test
    public void respawnTest() {
        world.updateEntities();
        player.health = -1f;
        player.update(world);
        world.updateEntities();
        player.update(world);
        Assert.assertTrue(world.medbay.getRectangle().contains(player.position.x, player.position.y));
    }
}
