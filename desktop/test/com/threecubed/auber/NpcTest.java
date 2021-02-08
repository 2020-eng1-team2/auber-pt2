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

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class NpcTest {

    private AuberGame game;
    private GameScreen gs;
    private World world;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        gs = new GameScreen(game, Difficulties.Easy);
        world = gs.world;    }

    @Test
    public void updateMovedTest() {
        Civilian n = new Civilian(world);
        Vector2 oldPos = n.position.cpy();
        n.update(world);
        Assert.assertNotEquals(n.position, oldPos);
    }

    @Test
    public void destinationReachedTest() {
        Civilian n = new Civilian(world);
        n.handleDestinationReached(world);
        Assert.assertEquals(n.getState(), Npc.States.IDLE);
    }

    @Test
    public void nonHostileTest() {
        // 24 non hostile NPCs on map creation as per requirements
        world.updateEntities();
        int counter = 0;
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Civilian) {
                counter++;
            }
        }
        Assert.assertEquals(24, counter);
    }

    @Test
    public void civilianSpawnTest() throws ClassNotFoundException {
        world.updateEntities();
        ArrayList<Civilian> civilians = new ArrayList<>();
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Civilian) {
                civilians.add((Civilian) ent);
            }
        }
        if (civilians.isEmpty()) {
            // No infiltrators
            throw new ClassNotFoundException("No Civilians on map");
        }
        for (Civilian civilian : civilians) {
            for (Civilian civilian1 : civilians) {
                if (civilian.position == civilian1.position && !(civilian.equals(civilian1))) {
                    // Two infiltrators have spawned in the same position
                    Assert.assertTrue(false);
                }
            }
        }
        Assert.assertTrue(true);
    }
}
