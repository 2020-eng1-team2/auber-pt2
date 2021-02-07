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
import com.threecubed.auber.ui.MenuUI;

import java.awt.*;


/**
 * The main screen of the game, responsible for rendering entities and executing their functions.
 *
 * @author Daniel O'Brien
 * @version 1.0
 * @since 1.0
 * */
public class MenuScreen extends ScreenAdapter {
  public World world;
  public AuberGame game;
  Sprite stars;

  public static boolean begin = false;

  SpriteBatch screenBatch;
  MenuUI ui;

  // Used to determine if screen size is 1080p (if so, fullscreen mode is unlocked)
  Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

  public static Difficulties difficulty = Difficulties.Easy;

  int workingSystems = 0;

  /**
   * Initialise the game screen with the {@link AuberGame} object and add a few entities.
   *
   * @param game The game object
   * */
  public MenuScreen(AuberGame game) {
    this.game = game;

    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      this.screenBatch = new SpriteBatch();
      stars = World.atlas.createSprite("stars");
      ui = new MenuUI(game);
    }

    world = new World(game, true);

    for (int i = 0; i < World.MAX_INFILTRATORS_IN_GAME; i++) {
      world.queueEntityAdd(new Infiltrator(world));
      world.infiltratorsAddedCount++;
    }
    for (int i = 0; i < World.NPC_COUNT; i++) {
      world.queueEntityAdd(new Civilian(world));
    }
  }

  @Override
  public void render(float delta) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT)) {
      begin = true;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.F)) {
      if (dimension.getHeight() == 1080 && dimension.getWidth() == 1920) {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.G)) {
      if (dimension.getHeight() == 1080 && dimension.getWidth() == 1920) {
        Gdx.graphics.setWindowedMode(1920, 1080);
      }
    }
    // Add any queued entities
    world.updateEntities();

    // Don't try and render anything if we're running headlessly
    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      // Set the background color
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      screenBatch.begin();
      screenBatch.draw(stars, 0, 0);
      screenBatch.end();

      OrthogonalTiledMapRenderer renderer = world.renderer;
      renderer.setView(world.camera);
      renderer.render(world.backgroundLayersIds);

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
    }

    if (world.infiltratorCount < World.MAX_INFILTRATORS_IN_GAME
            && world.infiltratorsAddedCount < World.MAX_INFILTRATORS) {
      Infiltrator newInfiltrator = new Infiltrator(world);
      while (newInfiltrator.entityOnScreen(world)) {
        newInfiltrator.moveToRandomLocation(world);
      }
      world.queueEntityAdd(newInfiltrator);
      world.infiltratorsAddedCount++;
    }
    // TODO: Make this more elegant
    // This is what causes the camera to follow an NPC in demo mode (title screen)
    world.player.position = world.getEntities().get(world.NPC_COUNT - 1).position;

    // Draw the UI
    if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
      ui.render(world, screenBatch);
    }
    world.checkForEndState();
  }

  @Override
  public void dispose() {
    world.renderer.dispose();
  }
}
