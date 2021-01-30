package com.threecubed.auber.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.World;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.ui.Button;


/**
 * The menu screen is the first screen that shows in the game and is responsible for controlling
 * when the game begins.
 *
 * @author Joseph Krystek-Walton
 * @version 1.0
 * @since 1.0
 * */
public class MenuUI {
  World world;
  AuberGame game;

  Button playButton;
  Button diffButton;
  OrthogonalTiledMapRenderer renderer;
  Sprite instructions;
  Sprite title;
  SpriteBatch spriteBatch;

  /**
   * Instantiate the screen with the {@link AuberGame} object. Set the title and button up to be
   * rendered.
   *
   * @param game The game object
   * */
  public MenuUI(final AuberGame game) {
    this.game = game;

    spriteBatch = new SpriteBatch();

    instructions = game.atlas.createSprite("instructions");
    title = game.atlas.createSprite("auber_logo");

    Runnable onPlayClick = new Runnable() {
      @Override
      public void run() {
        game.setScreen(new GameScreen(game, false));
      }
    };

    // Button sprites are now drawn from the bottom left rather than center
    playButton = new Button(
        new Vector2(0f, Gdx.graphics.getHeight() / 4),
        1f, game.atlas.createSprite("playButton"), game.atlas.createSprite("playButtonPressed"), game, onPlayClick);

    Runnable onDemoClick = new Runnable() {
      @Override
      public void run() {
        game.setScreen(new GameScreen(game, true));
      }
    };

    // repurpose this button
    diffButton = new Button(
        new Vector2(0f, Gdx.graphics.getHeight() / 4 - 150f),
        1f, game.atlas.createSprite("easyButton"), game.atlas.createSprite("easyButtonPressed"), game, onDemoClick);
  }

  public void render(World world, SpriteBatch spriteBatch) {

    spriteBatch.begin();

    title.setScale(0.5f);
    title.setPosition(-200f, 600f);
    title.draw(spriteBatch);

    playButton.render(spriteBatch);
    diffButton.render(spriteBatch);

    spriteBatch.end();
  }
}
