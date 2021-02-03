package com.threecubed.auber.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Player;
import com.threecubed.auber.screens.GameScreen;
import com.threecubed.auber.screens.MenuScreen;
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
  Button easyDiffButton;
  Button hardDiffButton;
  Button loadButton;
  /** Displayed when save file does not exist*/
  Button loadButtonError;
  Button nextButton;
  Button lastButton;
  Button startButton;
  OrthogonalTiledMapRenderer renderer;
  Sprite instructions_first;
  Sprite instructions_second;
  Sprite title;
  SpriteBatch spriteBatch;

  FileHandle saveFile = Gdx.files.external(".auber/save.json");

  // Main menu screen counter (main, guide 1, guide 2)
  int subScreen = 0;

  /**
   * Instantiate the screen with the {@link AuberGame} object. Set the title and button up to be
   * rendered.
   *
   * @param game The game object
   * */
  public MenuUI(final AuberGame game) {
    this.game = game;

    spriteBatch = new SpriteBatch();

    instructions_first = World.atlas.createSprite("guide1");
    instructions_first.setScale(1f);
    instructions_first.setPosition(Gdx.graphics.getWidth() / 2 - instructions_first.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - instructions_first.getHeight() / 2 + 50f);
    instructions_second = World.atlas.createSprite("guide2");
    instructions_second.setScale(1f);
    instructions_second.setPosition(Gdx.graphics.getWidth() / 2 - instructions_second.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - instructions_second.getHeight() / 2 + 50f);
    title = World.atlas.createSprite("auber_logo");

    Runnable onStartClick = new Runnable() {
      @Override
      public void run() {
        game.setScreen(new GameScreen(game, MenuScreen.difficulty));
      }
    };

    // Button sprites are now drawn from the bottom left rather than center
    startButton = new Button(
        new Vector2(Gdx.graphics.getWidth() / 2 - 200f, Gdx.graphics.getHeight() / 4 - 175f),
        1f, World.atlas.createSprite("startButton"), World.atlas.createSprite("startButtonPressed"), game, onStartClick);

    Runnable onDiffClick = new Runnable() {
      @Override
      public void run() {
        MenuScreen.difficulty = MenuScreen.difficulty.nextDifficulty(MenuScreen.difficulty);
      }
    };

    easyDiffButton = new Button(
        new Vector2(0f, Gdx.graphics.getHeight() / 4 - 250f),
        1f, World.atlas.createSprite("easyButton"), World.atlas.createSprite("easyButtonPressed"), game, onDiffClick);

    hardDiffButton = new Button(
            new Vector2(0f, Gdx.graphics.getHeight() / 4 - 250f),
            1f, World.atlas.createSprite("hardButton"), World.atlas.createSprite("hardButtonPressed"), game, onDiffClick);

    Runnable onLoadClick = new Runnable() {
      @Override
      public void run() {
        if (saveFile.exists()) {
          Json json = new Json();
          JsonValue data = new JsonReader().parse(saveFile);
          GameScreen gs = new GameScreen(game, MenuScreen.difficulty, true);
          gs.world.read(json, data);
          game.setScreen(gs);
        } else {
          Gdx.app.log("load", "save file doesn't exist");
        }
      }
    };

    loadButton = new Button(
            new Vector2(0f, Gdx.graphics.getHeight() / 4 - 125f),
            1f, World.atlas.createSprite("loadButton"), World.atlas.createSprite("loadButtonPressed"), game, onLoadClick);

    loadButtonError = new Button(
            new Vector2(0f, Gdx.graphics.getHeight() / 4 - 125f),
            1f, World.atlas.createSprite("loadButtonPressed"), World.atlas.createSprite("loadButtonPressed"), game, onLoadClick);

    Runnable onNextClick = new Runnable() {
      @Override
      public void run() {
        nextScreen();
      }
    };

    nextButton = new Button(
            new Vector2(Gdx.graphics.getWidth() - 400f, Gdx.graphics.getHeight() / 4 - 175f),
            1f, World.atlas.createSprite("nextButton"), World.atlas.createSprite("nextButtonPressed"), game, onNextClick);

    playButton = new Button(
            new Vector2(0f, Gdx.graphics.getHeight() / 4),
            1f, World.atlas.createSprite("playButton"), World.atlas.createSprite("playButtonPressed"), game, onNextClick);

    Runnable onLastClick = new Runnable() {
      @Override
      public void run() {
        lastScreen();
      }
    };

    lastButton = new Button(
            new Vector2(0f, Gdx.graphics.getHeight() / 4 - 175f),
            1f, World.atlas.createSprite("backButton"), World.atlas.createSprite("backButtonPressed"), game, onLastClick);
  }

  public void render(World world, SpriteBatch spriteBatch) {

    spriteBatch.begin();

    if (subScreen == 0) {
      // draw menu
      title.setScale(0.5f);
      title.setPosition(-200f, 600f);
      title.draw(spriteBatch);

      playButton.render(spriteBatch);
      if (saveFile.exists()) {
        loadButton.render(spriteBatch);
      }
      else {
        loadButtonError.render(spriteBatch);
      }
      if (MenuScreen.difficulty == Difficulties.Easy) {
        easyDiffButton.render(spriteBatch);
      } else {
        hardDiffButton.render(spriteBatch);
      }

    }
    else if (subScreen == 1) {
      // draw guide 1
      instructions_first.draw(spriteBatch);
      nextButton.render(spriteBatch);
      lastButton.render(spriteBatch);
    }
    else {
      // draw guide 2
      instructions_second.draw(spriteBatch);
      lastButton.render(spriteBatch);
      startButton.render(spriteBatch);
    }

    spriteBatch.end();
  }

  public void nextScreen() {
    this.subScreen++;
  }

  public void lastScreen() {
    this.subScreen--;
  }
}
