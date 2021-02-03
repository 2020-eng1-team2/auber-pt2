package com.threecubed.auber;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.threecubed.auber.screens.MenuScreen;

public class AuberGame extends Game {
  @Override
  public void create() {
    World.atlas = new TextureAtlas("auber.atlas");
    Gdx.graphics.setWindowedMode(1920, 1080);
    setScreen(new MenuScreen(this));
  }

  @Override
  public void dispose() {
    World.atlas.dispose();
  }
}
