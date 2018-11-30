/*
Handles everything to do with the ship
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

public class Ship extends Sprite {

    public World world;
    public Body shipBody, cannon;
    private Texture ship;
    private BodyDef bdef;
    private FixtureDef fdef;
    private PolygonShape shape;
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
        bdef = new BodyDef();
        bdef.position.set(2000 / Main.PPM, 1600 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        shipBody = world.createBody(bdef);

        fdef = new FixtureDef();
        fdef.restitution = 0.1f; // Don't really bounce off ships so it's a low values
        fdef.friction = 0.5f;
        shape = new PolygonShape();
        shape.setAsBox(0.8f, 0.7f);

        //Attatches the circle shape to the fixture and creates the body
        fdef.shape = shape;
        shipBody.createFixture(fdef);


        //Cannon
        shape.setAsBox(0.05f, 0.9f);
        cannon = world.createBody(bdef);
        cannon.createFixture(fdef);
        cannon.setAngularDamping(3f);

        //Joint
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = shipBody;
        jointDef.bodyB = cannon;
        jointDef.localAnchorB.y = -0.9f / 1;
        jointDef.enableLimit = true;
        jointDef.maxMotorTorque = 100;

        joint = (RevoluteJoint) world.createJoint(jointDef);


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


    public void cannonRight() {
        joint.enableLimit(false);
        joint.enableMotor(true);
        joint.setMotorSpeed(-5000f);
        joint.setMaxMotorTorque(500f);
    }

    public void cannonLeft() {
        joint.enableLimit(false);
        joint.enableMotor(true);
        joint.setMotorSpeed(50000f);
    }

    public void dispose(){
        ship.dispose();
    }

}




