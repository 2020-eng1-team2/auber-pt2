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
public class GameOverUI {
    World world;
    AuberGame game;

    Button quitButton;
    Button playButton;
    OrthogonalTiledMapRenderer renderer;
    SpriteBatch spriteBatch;

    /**
     * Instantiate the screen with the {@link AuberGame} object. Set the buttons up to be
     * rendered.
     *
     * @param game The game object
     * */
    public GameOverUI(final AuberGame game) {
        this.game = game;

        spriteBatch = new SpriteBatch();

        Runnable onQuitClick = new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MenuScreen(game));
            }
        };

        // Button sprites are now drawn from the bottom left rather than center
        quitButton = new Button(
                new Vector2(0f, Gdx.graphics.getHeight() / 4 - 125f),
                1f, World.atlas.createSprite("quitButton"), World.atlas.createSprite("quitButtonPressed"), game, onQuitClick);

        Runnable onPlayClick = new Runnable() {
            @Override
            public void run() {
                game.setScreen(new GameScreen(game, MenuScreen.difficulty));
            }
        };

        playButton = new Button(
                new Vector2(Gdx.graphics.getWidth() - 400f, Gdx.graphics.getHeight() / 4 - 125f),
                1f, World.atlas.createSprite("playButtonRight"), World.atlas.createSprite("playButtonRightPressed"), game, onPlayClick);

    }

    public void render(SpriteBatch spriteBatch) {

        spriteBatch.begin();

        quitButton.render(spriteBatch);
        playButton.render(spriteBatch);

        spriteBatch.end();
    }
}
