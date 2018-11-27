/*
Handles everything to do with the fleet
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;

public class Fleet extends Sprite {

    public World world;
    private Body body;
    private Texture fleet;

    public Fleet(World world){
        this.world = world;
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
        bdef.position.set(2000 / Main.PPM, 1200 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius(80 / Main.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }

    // Gives the fleet a linear impulse to the left... still needs a lot of work for proper fleet movement
    public void fleetMovement(float dt){
        body.applyLinearImpulse(new Vector2(-0.001f, 0f), body.getWorldCenter(), true);
    }
}
