package com.threecubed.auber;

import com.badlogic.gdx.math.Vector3;
import com.threecubed.auber.entities.Civilian;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GameEntityTest {
    private World world;

    @Before
    public void createWorld() {
        world = new World(null);
    }

    @Test
    public void testEntityOnScreen() {
        world.camera.viewportWidth = 100;
        world.camera.viewportHeight = 100;
        world.camera.position.set(0, 0, 0);

        // size is known to be 14x14
        Civilian ent = new Civilian( world);
        ent.position.set(0, 0);
        Assert.assertTrue(ent.entityOnScreen(world));

        ent.position.set(200, 0);
        Assert.assertFalse(ent.entityOnScreen(world));
    }
}
