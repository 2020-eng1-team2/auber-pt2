package com.threecubed.auber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class GameTest {
    protected AuberGame game;
    protected Application app;

    @BeforeEach
    void gameTestBeforeEach() {
        Gdx.gl = mock(GL20.class);

        game = new AuberGame();
        app = new HeadlessApplication(game);
        System.out.println("GameTest initialised");
    }
}
