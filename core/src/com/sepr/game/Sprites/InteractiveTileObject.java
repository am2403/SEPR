/*
This class is used for detecting different types of tiles.
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


    //A collidable tile inherits from the constructor below
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

        body.setUserData(this);
    }


    // Sets a value to the object being detected
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }


}
