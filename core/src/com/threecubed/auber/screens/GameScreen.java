package com.threecubed.auber.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Player;
import com.threecubed.auber.ui.Difficulties;
import com.threecubed.auber.ui.GameUI;
import com.threecubed.auber.ui.PauseUI;


/**
 * The main screen of the game, responsible for rendering entities and executing their functions.
 *
 * @author Daniel O'Brien
 * @version 1.0
 * @since 1.0
 * */
public class GameScreen extends ScreenAdapter {
  public World world;
  public AuberGame game;
  Sprite stars;

  public static boolean paused = false;

  SpriteBatch screenBatch;
  GameUI ui;
  PauseUI pauseUi;

  int workingSystems = 0;

  /**
   * Initialise the game screen with the {@link AuberGame} object and add a few entities.
   *
   * @param game The game object
   * @param demoMode Whether the game should run in demo mode
   * */
  public GameScreen(AuberGame game, boolean demoMode) {
    this.game = game;

    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
       screenBatch = new SpriteBatch();
      stars = World.atlas.createSprite("stars");
      ui = new GameUI(game);
      pauseUi = new PauseUI(game, world);
    }

    world = new World(game, demoMode);


    for (int i = 0; i < World.MAX_INFILTRATORS_IN_GAME; i++) {
      world.queueEntityAdd(new Infiltrator(world));
      world.infiltratorsAddedCount++;
    }
    for (int i = 0; i < World.NPC_COUNT; i++) {
      world.queueEntityAdd(new Civilian(world));
    }
  }

  /**
   * Initialise the game screen with the {@link AuberGame} object and add a few entities.
   *
   * @param game The game object
   * @param diff Difficulty of the game
   * */
  public GameScreen(AuberGame game, Difficulties diff) {
    this.game = game;

    world = new World(game, diff);

    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      screenBatch = new SpriteBatch();
      stars = World.atlas.createSprite("stars");
      ui = new GameUI(game);
      pauseUi = new PauseUI(game, world);
    }

    for (int i = 0; i < World.MAX_INFILTRATORS_IN_GAME; i++) {
      world.queueEntityAdd(new Infiltrator(world));
      world.infiltratorsAddedCount++;
    }
    for (int i = 0; i < World.NPC_COUNT; i++) {
      world.queueEntityAdd(new Civilian(world));
    }
  }

  /**
   * Initialise the game screen with the {@link AuberGame} object. For use with save files only.
   *
   * @param game The game object
   * @param diff Difficulty of the game
   * @param fromSave Is game loading from a save file
   * */
  public GameScreen(AuberGame game, Difficulties diff, Boolean fromSave) {
    this.game = game;

    world = new World(game, diff);
    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      screenBatch = new SpriteBatch();
      stars = World.atlas.createSprite("stars");
      ui = new GameUI(game);
      pauseUi = new PauseUI(game, world);
    }
  }

  @Override
  public void render(float delta) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      // Pause game, not end game
      this.paused = !this.paused;
    }
    // Add any queued entities
    world.updateEntities();

    // Set the background color
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    screenBatch.begin();
    screenBatch.draw(stars, 0, 0);
    screenBatch.end();

    OrthogonalTiledMapRenderer renderer = world.renderer;
    renderer.setView(world.camera);
    renderer.render(world.backgroundLayersIds);
    if (!this.paused) {
      Batch batch = renderer.getBatch();
      // Iterate over all entities. Perform movement logic and render them.
      batch.begin();
      world.infiltratorCount = 0;
      for (GameEntity entity : world.getEntities()) {
        entity.update(world);
        entity.render(batch, world.camera);

        if (entity instanceof Player) {
          world.camera.position.set(entity.position.x, entity.position.y, 0);
          world.camera.update();
        } else if (entity instanceof Infiltrator) {
          Infiltrator infiltrator = (Infiltrator) entity;
          if (infiltrator.aiEnabled) {
            world.infiltratorCount += 1;
          }
        }
      }
      batch.end();
      renderer.render(world.foregroundLayersIds);

      if (world.infiltratorCount < World.MAX_INFILTRATORS_IN_GAME
              && world.infiltratorsAddedCount < World.MAX_INFILTRATORS) {
        Infiltrator newInfiltrator = new Infiltrator(world);
        while (newInfiltrator.entityOnScreen(world)) {
          newInfiltrator.moveToRandomLocation(world);
        }
        world.queueEntityAdd(newInfiltrator);
        world.infiltratorsAddedCount++;
      }

      // Draw the UI
      ui.render(world, screenBatch);
      world.checkForEndState();
    }
    else {
      // game is paused,
      // Draw pause menu UI

      renderer.render(world.foregroundLayersIds);

      pauseUi.render(world, screenBatch);
    }
  }

  @Override
  public void dispose() {
    world.renderer.dispose();
  }
}
