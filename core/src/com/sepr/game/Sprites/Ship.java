package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;

public class Ship extends Sprite {

    public World world;
    public Body b2body;
    private Texture ship;
    private BodyDef bdef;

    public Ship(World world){
        this.world = world;
        defineShip();
        ship = new Texture("ship.png");
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(ship);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void defineShip(){
        bdef = new BodyDef();
        bdef.position.set(2000 / Main.PPM, 1600 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(10 / Main.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
