package com.threecubed.auber;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.PowerUp;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.ui.Difficulties;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class PowerUpTest {
    private World world;
    private AuberGame game;

    @Before
    public void createWorld() {
        game = mock(AuberGame.class);
        world = new World(game);
    }

    @Test
    public void invisibilityTest() {
        PowerUp pu = new PowerUp(0f, 0f, world, Abilities.Invisibility);
        world.queueEntityAdd(pu);
        world.updateEntities();
        pu.applyBuff();
        pu.update(world);
        Assert.assertEquals(true, world.player.invisible);
    }

    @Test
    public void invincibilityTest() {
        // Set player health
        world.player.health = 0.5f;
        PowerUp pu = new PowerUp(0f, 0f, world, Abilities.Invincibility);
        world.queueEntityAdd(pu);
        world.updateEntities();
        // Grant power up
        pu.applyBuff();
        pu.update(world);
        world.player.update(world);
        Assert.assertEquals(true, world.player.health == 1f);
    }

    @Test
    public void superSpeedTest() {
        PowerUp pu = new PowerUp(0f, 0f, world, Abilities.SuperSpeed);
        world.queueEntityAdd(pu);
        world.updateEntities();
        // Grant Power up
        pu.applyBuff();
        pu.update(world);
        world.player.update(world);
        Assert.assertEquals(true, world.player.maxSpeed == 4.8f);
    }

    @Test
    public void visionTest() {
        int mistake = 0;
        PowerUp pu = new PowerUp(0f, 0f, world, Abilities.Vision);
        world.queueEntityAdd(pu);
        world.updateEntities();
        pu.applyBuff();
        pu.update(world);
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Infiltrator) {
                if (!((Infiltrator) ent).exposed) {
                    // Infiltrator is not exposed
                    mistake++;
                    break;
                }
            }
        }
        Assert.assertEquals(0, mistake);
    }

    @Test
    public void buffsOnAttackEasyTest() {
        GameScreen gs = new GameScreen(game, Difficulties.Easy);
        world = gs.world;
        world.updateEntities();
        Infiltrator infiltrator = null;
        // Attack 1 system
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Infiltrator) {
                infiltrator = (Infiltrator) ent;
                break;
            }
        }
        if (infiltrator == null) {
            System.out.println("no infiltrators");
            Assert.assertTrue(false);
        }
        else {
            infiltrator.virtualAttackSystem(world);
            world.updateEntities();
            // Scan world for 5 power ups
            int countDooku = 0;
            for (GameEntity ent : world.getEntities()) {
                if (ent instanceof PowerUp) {
                    countDooku++;
                }
            }
            Assert.assertEquals(5, countDooku);
        }
    }

    @Test
    public void buffsOnAttackHardTest() {
        GameScreen gs = new GameScreen(game, Difficulties.Hard);
        world = gs.world;
        world.updateEntities();
        Infiltrator infiltrator = null;
        // Attack 1 system
        for (GameEntity ent : world.getEntities()) {
            if (ent instanceof Infiltrator) {
                infiltrator = (Infiltrator) ent;
                break;
            }
        }
        if (infiltrator == null) {
            System.out.println("no infiltrators");
            Assert.assertTrue(false);
        }
        else {
            infiltrator.virtualAttackSystem(world);
            world.updateEntities();
            // Scan world for 5 power ups
            int countDooku = 0;
            for (GameEntity ent : world.getEntities()) {
                if (ent instanceof PowerUp) {
                    countDooku++;
                }
            }
            Assert.assertEquals(2, countDooku);
        }
    }
}
