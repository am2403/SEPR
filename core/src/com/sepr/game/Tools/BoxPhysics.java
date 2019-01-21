package com.sepr.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.sepr.game.Screens.PlayScreen;
import com.sepr.game.Sprites.Dock;
import com.sepr.game.Sprites.Land;


public class BoxPhysics {

    public BoxPhysics(PlayScreen screen){

        TiledMap map = screen.getMap();

        //Set Docks as collidable

        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Dock(screen, rect);
        }

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Land(screen, rect);
        }

    }
}
