package com.threecubed.auber.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.AuberGame;


/**
 * Render a button and also handle the pressing of said button.
 *
 * @author Joseph Krystek-Walton
 * @version 1.0
 * @since 1.0
 * */
public class Button {
  Pixmap pixmap;
  Sprite sprite;
  Sprite spritePressed;
  Sprite spriteNotPressed;
  AuberGame game;
  Vector2 position;
  Runnable onClick;

  /**
   * Instantiate a button at a given position with a given scale and texture.
   * Pass the game object so that the button's action can be configured.
   *
   * @param position The position to render the center of the button at
   * @param scale The scale to render the button with
   * @param spriteNotPressed The sprite of the button normally
   * @param spritePressed The sprite of the button when the cursor is hovering over it
   * @param game The game object
   * @param onClick The code to execute when the button is pressed wrapped in a {@link Runnable}
   * */
  public Button(Vector2 position, float scale, Sprite spriteNotPressed, Sprite spritePressed, AuberGame game, Runnable onClick) {
    this.spriteNotPressed = spriteNotPressed;
    this.spritePressed = spritePressed;
    this.onClick = onClick;

    this.spritePressed.setScale(scale);
    this.spritePressed.setPosition(position.x, position.y);

    this.spriteNotPressed.setScale(scale);
    this.spriteNotPressed.setPosition(position.x, position.y);

    this.sprite = this.spriteNotPressed;

    sprite.setScale(scale);
    sprite.setPosition(position.x, position.y);

    this.position = new Vector2(sprite.getX(), sprite.getY());
    this.game = game;
  }

  /**
   * Render the button at the provided position and scale to the given {@link SpriteBatch}.
   *
   * @param spriteBatch The sprite batch to render the button to
   * */
  public void render(SpriteBatch spriteBatch) {
    sprite.draw(spriteBatch);

    if (sprite.getBoundingRectangle().contains(Gdx.input.getX(),
          Gdx.graphics.getHeight() - Gdx.input.getY())) {
      // Mouse over (change sprite)
      sprite = this.spritePressed;
      if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
        onClick.run();
      }
    } else {
      // Mouse not over, reset sprite
      sprite = this.spriteNotPressed;
    }
  }
}
