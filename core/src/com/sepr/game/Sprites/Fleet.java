package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

public class Fleet extends Sprite {

    public World world;
    private Body body;
    private Texture fleet;
    private int spawnX = 20, spawnY = 12; //x and y location that the fleet spawns at

    public Fleet(PlayScreen screen){
        this.world = screen.getWorld();
        defineFleet();
        fleet = new Texture("fleet.png");
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(fleet);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        fleetMovement(dt);
    }

    //Creates a Box2D Object and attaches a shape to it

    public void defineFleet(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX * 100 / Main.PPM, spawnY * 100 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(80 / Main.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }

    // Gives the fleet a linear impulse to the left... still needs a lot of work for proper fleet movement
    public void fleetMovement(float dt) {

        //body.applyLinearImpulse(new Vector2(-0.001f, 0f), body.getWorldCenter(), true);
        body.applyAngularImpulse(100f, true);
    }




    public void dispose(){
        world.dispose();
        fleet.dispose();
    }
}
