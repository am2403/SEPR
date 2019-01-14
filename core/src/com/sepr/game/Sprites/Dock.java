/*
This class is used to classify the Dock; this is currently being used for collision.
Will be used more when we change the screen when the ship docks.
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;


public class Dock extends InteractiveTileObject {
    public Dock(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);

        //Collision detection
        setCategoryFilter(Main.DOCK_BIT);
    }

    @Override
    public void shipContact() {
        Gdx.app.log("dock", "collision");
    }
}
