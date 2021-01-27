// THESE TESTS ARE BROKEN, WILL HOPEFULLY BE FIXED SOON

package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.PowerUp;
import com.threecubed.auber.World;
import org.junit.jupiter.api.Test;

import static com.threecubed.auber.Abilities.SuperSpeed;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {
    Abilities abilityCheck  = SuperSpeed;
    AuberGame game = new AuberGame();
    World wrld = new World(game);
    float xIn = 26f;
    float yIn = 457f;
    PowerUp powerUp = new PowerUp(xIn, yIn, wrld, abilityCheck);

    @Test
    public void AbilityTest() {
        assertEquals(abilityCheck, powerUp.getAbility());
    }

    @Test
    public void positionTest() {
        assertEquals(new Vector2(xIn, yIn), powerUp.getPosition());
    }
}
