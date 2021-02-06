package com.threecubed.auber;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.threecubed.auber.entities.PowerUp;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class PowerUpTest {
    private World world;

    @Before
    public void createWorld() {
        world = new World(null);
    }

    @Test
    public void invisTest() {
        PowerUp pu = new PowerUp(0f, 0f, world, Abilities.Invisibility);
        world.queueEntityAdd(pu);
        world.updateEntities();
        pu.applyBuff();
        pu.update(world);
        Assert.assertEquals(true, world.player.invisible);
    }
}
