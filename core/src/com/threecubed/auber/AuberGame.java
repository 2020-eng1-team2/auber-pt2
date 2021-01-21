package com.threecubed.auber;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.threecubed.auber.interfaces.ScreenSetter;
import com.threecubed.auber.screens.MenuScreen;

public class AuberGame extends Game implements ScreenSetter {
  @Override
  public void create() {
    Assets.create();
    Gdx.graphics.setWindowedMode(1920, 1080);
    setScreen(new MenuScreen(this));
  }

  @Override
  public void dispose() {
    Assets.dispose();
  }
}
