package com.threecubed.auber.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.screens.MenuScreen;
import com.threecubed.auber.ui.GameOverUI;


/**
 * The game over screen is the screen that the game is set to when a win/lose condition has been
 * reached.
 *
 * @author Joseph Krystek-Walton
 * @version 1.0
 * @since 1.0
 * */
public class GameOverScreen extends ScreenAdapter {
  AuberGame game;

  BitmapFont font = new BitmapFont();
  SpriteBatch batch;

  GameOverUI gameOverUi;
  public static boolean userWon;

  /**
   * Instantiate the screen with an {@link AuberGame} object.
   *
   * @param game The game object. 
   * @param userWon Whether the user won or lost
   * */
  public GameOverScreen(AuberGame game, boolean userWon) {
    this.game = game;
    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      batch = new SpriteBatch();
      gameOverUi = new GameOverUI(game);
    }

    this.userWon = userWon;
  }

  @Override
  public void render(float deltaTime) {

    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      game.setScreen(new MenuScreen(game));
    }

    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      // Set the background color
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      // Draw UI
      gameOverUi.render(batch);
    }
  }
  
}
