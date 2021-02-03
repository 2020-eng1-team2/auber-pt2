package com.threecubed.auber;

import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.ui.Difficulties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NpcTest extends GameTest {
    private Civilian npc;
    private GameScreen screen;

    @BeforeEach
    void beforeEach() {
        screen = new GameScreen(game, Difficulties.Easy);
        game.setScreen(screen);
        System.out.println("NpcTest initialised");
    }

    @Test
    void testSmoke() {
        npc = new Civilian(0, 0, screen.world);
    }
}
