/*
Incomplete class
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

public class Dock extends InteractiveTileObject {
    public Dock(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Main.DOCK_BIT);
    }

    @Override
    public void shipContact() {
        Gdx.app.log("dock", "collision");
    }
}
