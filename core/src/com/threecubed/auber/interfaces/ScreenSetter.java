package com.threecubed.auber.interfaces;

import com.badlogic.gdx.Screen;

public interface ScreenSetter {
    /** Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     * @param screen may be {@code null} */
    void setScreen(Screen screen);
}
