package com.threecubed.auber.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.headless.mock.input.MockInput;
import com.threecubed.auber.World;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class PlayerTest {
    private World world = mock(World.class);
    private Player player;
    
    @BeforeEach
    public void reset() {
        this.player = new Player(0, 0, this.world);
    }
    
    @Nested
    @DisplayName("calculateNewVelocity")
    class CalculateNewVelocity {
        @BeforeEach
        public void reset() {
            // Mock out the Gdx.input
            Gdx.input = mock(MockInput.class);
            world.auberTeleporterCharge = 0f;
        }

        @Test
        public void testKeyboardMovement() {
            when(Gdx.input.isKeyJustPressed(Input.Keys.W)).thenReturn(true);
            when(Gdx.input.isKeyJustPressed(Input.Keys.A)).thenReturn(false);
            when(Gdx.input.isKeyJustPressed(Input.Keys.S)).thenReturn(false);
            when(Gdx.input.isKeyJustPressed(Input.Keys.D)).thenReturn(false);
            player.calculateNewVelocity(world);
            assertEquals(player.velocity.y, 1);
        }
    }
}
