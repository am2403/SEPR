package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.degreesToRadians;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Cannon extends Sprite {
    public World world;
    public Body cannonBody;
    private Texture cannonTexture;
    private Sprite cannon;
    private BodyDef bdef;
    private FixtureDef fdef;
    private CircleShape cannonShape;

    private float forceX, forceY;


    public Cannon(PlayScreen screen) {
        this.world = screen.getWorld();
        defineCannon();
        cannonTexture = new Texture("cannon.png");
        cannon = new Sprite(cannonTexture);
        setBounds(0, 0, 40 / Main.PPM, 40 / Main.PPM);
        setRegion(cannon);

        cannonBody.setUserData(this);
    }

    public void update(float dt) {
        setPosition(cannonBody.getPosition().x - getWidth() / 2, cannonBody.getPosition().y- getHeight() / 2); //puts the ship body onto the middle of the screen
        setRotation(cannonBody.getAngle() * MathUtils.radiansToDegrees); //converts the angles to radians and rotates the ship body WILL NEED TO INPUT THE DELTA SPEED ON ROTATION
        setOriginCenter();
        forceX = cos(cannonBody.getAngle()); //upon rotation this will be our new x coordinate for our ship body
        forceY = sin(cannonBody.getAngle()); //upon rotation this will be our new y coordinate for our ship body
    }

    // Creates a Box2D object for the ship and the ship's cannon, then attaches the cannon to the ship with a ResoluteJoint
    public void defineCannon() {
        //Generic variables, can be applied to ship and cannon/
        bdef = new BodyDef();
        fdef = new FixtureDef();

        //Ship creation
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(6250 / Main.PPM, 7100 / Main.PPM);


        cannonShape = new CircleShape();
        cannonShape.setRadius(0.25f);
        fdef.shape = cannonShape;

        //Ship properties
        fdef.restitution = 0.1f; // Don't really bounce off ships so it's a low values
        fdef.friction = 0.5f;

        cannonBody = world.createBody(bdef);
        cannonBody.createFixture(fdef);

        cannonBody.setAngularDamping(30f); //the resistance to the ships rotating force

    }

    public void rotateClockwise(){
        cannonBody.setAngularVelocity(-2f); //rotation speed
    }

    public void rotateCounterClockwise(){
        cannonBody.setAngularVelocity(2f); //rotation speed
    }

}
