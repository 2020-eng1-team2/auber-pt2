package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.entities.Npc;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class NpcTest {

    private World world;

    @Before
    public void createWorld() {
        world = new World(null);
    }

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
}
