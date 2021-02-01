package com.threecubed.auber.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.World;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.screens.MenuScreen;


/**
 * The Pause UI is shown when the player pauses the game.
 *
 * @author Joseph Krystek-Walton
 * @version 1.0
 * @since 1.0
 * */
public class PauseUI {
    World world;
    AuberGame game;

    Button resumeButton;
    Button quitButton;
    Button saveButton;
    OrthogonalTiledMapRenderer renderer;
    SpriteBatch spriteBatch;

    /**
     * Instantiate the screen with the {@link AuberGame} object. Set the buttons up to be
     * rendered.
     *
     * @param game The game object
     * */
    public PauseUI(final AuberGame game, World world) {
        this.game = game;
        this.world = world;

        spriteBatch = new SpriteBatch();

        Runnable onQuitClick = new Runnable() {
            @Override
            public void run() {
                GameScreen.paused = false;
                game.setScreen(new MenuScreen(game));
            }
        };

        // Button sprites are now drawn from the bottom left rather than center
        quitButton = new Button(
                new Vector2(0f, Gdx.graphics.getHeight() / 4 - 125f),
                1f, World.atlas.createSprite("quitButton"), World.atlas.createSprite("quitButtonPressed"), game, onQuitClick);

        Runnable onResumeClick = new Runnable() {
            @Override
            public void run() {
                GameScreen.paused = false;
            }
        };

        resumeButton = new Button(
                new Vector2(Gdx.graphics.getWidth() - 400f, Gdx.graphics.getHeight() / 4 - 125f),
                1f, World.atlas.createSprite("resumeButton"), World.atlas.createSprite("resumeButtonPressed"), game, onResumeClick);

        Runnable onSaveClick = new Runnable() {
            @Override
            public void run() {
                world.saveGame();
                GameScreen.paused = false;
                game.setScreen(new MenuScreen(game));
            }
        };

        saveButton = new Button(
                new Vector2(Gdx.graphics.getWidth() / 2 - 200f, Gdx.graphics.getHeight() / 4 - 125f),
                1f, World.atlas.createSprite("saveButton"), World.atlas.createSprite("saveButtonPressed"), game, onSaveClick);
    }

    public void render(World world, SpriteBatch spriteBatch) {

        spriteBatch.begin();

        quitButton.render(spriteBatch);
        resumeButton.render(spriteBatch);
        saveButton.render(spriteBatch);

        spriteBatch.end();
    }
}
