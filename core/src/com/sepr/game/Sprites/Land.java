/*
Incomplete class
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Land extends InteractiveTileObject {

    public Land(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
