/*
Handles everything to do with the ship
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

import java.awt.geom.RectangularShape;

public class Ship extends Sprite {

    public World world;
    public Body shipBody, cannonBody;
    private Texture ship;
    private BodyDef bdef;
    private FixtureDef fdef;
    private PolygonShape shipShape, cannonShape;
    private RevoluteJoint joint;
    private float maxSpeed = 4f, shipAcceleration = 2f;

    public Ship(PlayScreen screen) {
        this.world = screen.getWorld();
        defineShip();
        ship = new Texture("mainShip.png");
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(ship);
    }


    public void update(float dt) {
        setPosition(shipBody.getPosition().x - getWidth() / 2, shipBody.getPosition().y - getHeight() / 2);


    }

    // Creates a Box2D object for the ship and the ship's cannon, then attaches the cannon to the ship with a ResoluteJoint
    public void defineShip() {

        //Generic variables, can be applied to ship and cannon/
        bdef = new BodyDef();
        fdef = new FixtureDef();

        //Ship creation
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(2000 / Main.PPM, 1600 / Main.PPM);

        shipShape = new PolygonShape();
        shipShape.setAsBox(0.8f, 0.7f);
        fdef.shape = shipShape;

        //Ship properties
        fdef.restitution = 0.1f; // Don't really bounce off ships so it's a low values
        fdef.friction = 0.5f;

        shipBody = world.createBody(bdef);
        shipBody.createFixture(fdef);

        //Cannon
        cannonShape = new PolygonShape();
        cannonShape.setAsBox(0.06f, 0.4f);

        fdef.shape = cannonShape;

        cannonBody = world.createBody(bdef);
        cannonBody.createFixture(fdef);

        //Joint -- need to make it rotate!

        RevoluteJointDef rDef= new RevoluteJointDef();
        rDef.enableMotor = true;
        rDef.motorSpeed = 2f;
        rDef.enableLimit = false;
        rDef.maxMotorTorque = 10f;

        rDef.bodyA = shipBody;
        rDef.bodyB = cannonBody;
        rDef.collideConnected = false;
        rDef.localAnchorA.set(0, 2f);


        world.createJoint(rDef);






    }

    public void moveUp() {
        if (shipBody.getLinearVelocity().y <= maxSpeed)
            shipBody.applyForce(new Vector2(0, shipAcceleration), shipBody.getWorldCenter(), true);
    }

    public void moveDown() {
        if (shipBody.getLinearVelocity().y <= maxSpeed)
            shipBody.applyForce(new Vector2(0, -shipAcceleration), shipBody.getWorldCenter(), true);
    }

    public void moveLeft() {
        if (shipBody.getLinearVelocity().x <= maxSpeed)
            shipBody.applyForce(new Vector2(-shipAcceleration, 0), shipBody.getWorldCenter(), true);
    }

    public void moveRight() {
        if (shipBody.getLinearVelocity().x <= maxSpeed)
            shipBody.applyForce(new Vector2(shipAcceleration, 0), shipBody.getWorldCenter(), true);
    }

    public void stopShip() {
        shipBody.setLinearDamping(0.2f); //Slows down gradually
    }


    public void dispose(){
        ship.dispose();
    }

}




