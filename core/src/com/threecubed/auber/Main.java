package com.threecubed.auber;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Npc;
import com.threecubed.auber.entities.Player;


public class Main extends ApplicationAdapter {
  World world;

  @Override
  public void create() {
    Gdx.graphics.setWindowedMode(1920, 1080);

    world = new World();
    world.addEntity(new Player(290f, 290f));
    world.addEntity(new Npc(300f, 290f, new Texture("player.png")));

  }

  @Override
  public void render() {
    // Set the background color
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    OrthogonalTiledMapRenderer renderer = world.renderer;

    renderer.setView(world.camera);
    renderer.render(world.backgroundLayersIds);

    Batch batch = renderer.getBatch();
    // Iterate over all entities. Perform movement logic and render them.
    // TODO: Make .update() call every 5 frames? better performance
    batch.begin();
    for (GameEntity entity : world.getEntities()) {
      entity.update(world);
      entity.render(batch, world.camera);

      if (entity instanceof Player) {
        world.camera.position.set(entity.position.x, entity.position.y, 0);
        world.camera.update();
      }
    }
    batch.end();
    renderer.render(world.foregroundLayersIds);
  }

  @Override
  public void dispose() {
  }
}
