package com.threecubed.auber;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class SaveLoadTest {
    private World world;

    @Before
    public void createWorld() {
        world = new World(null);
    }

    @Test
    public void saveLoadTest() {
        Json json = new Json();
        String result = json.toJson(world);
        // Empty systems for testing
        world.systems.clear();
        JsonValue data = new JsonReader().parse(result);
        world.read(json, data);
        Assert.assertTrue(world.systems.size() > 0);
    }
}
