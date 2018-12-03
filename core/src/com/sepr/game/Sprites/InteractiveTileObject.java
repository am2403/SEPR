/*
This class is needed for object collision
Will be used more later
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

public abstract class InteractiveTileObject {

    protected PlayScreen screen;
    protected Rectangle bounds;
    protected Body body;
    protected World world;
    protected TiledMap map;
    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds){
        this.screen = screen;
        this.bounds = bounds;
        this.world = screen.getWorld();
        this.map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Main.PPM, (bounds.getY() + bounds.getHeight() / 2) / Main.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth() / 2) / Main.PPM, (bounds.getHeight() / 2) / Main.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void shipContact();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }


}
