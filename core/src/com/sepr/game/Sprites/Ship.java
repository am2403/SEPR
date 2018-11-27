package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.sepr.game.Main;

public class Ship extends Sprite {

    public World world;
    public Body body, cannon;
    private Texture ship;
    private BodyDef bdef;
    private FixtureDef fdef;
    private PolygonShape shape;
    private RevoluteJoint joint;
    private int maxSpeed = 5;
    private float shipAcceleration = 1.5f;

    public Ship(World world){
        this.world = world;
        defineShip();
        ship = new Texture("ship.png");
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(ship);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    public void defineShip(){
        bdef = new BodyDef();
        bdef.position.set(2000 / Main.PPM, 1600 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        fdef = new FixtureDef();
        fdef.restitution = 0.1f; // Don't really bounce off ships so it's a low values
        fdef.friction = 0.5f;
        shape = new PolygonShape();
        shape.setAsBox(0.4f, 0.5f);

        //Attatches the circle shape to the fixture and creates the body
        fdef.shape = shape;
        body.createFixture(fdef);

        //Cannon
        shape.setAsBox(0.05f, 0.9f);
        cannon = world.createBody(bdef);
        cannon.createFixture(fdef);

        //Joint
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.bodyB = cannon;
        jointDef.localAnchorB.y = - 0.9f / 1;

        joint = (RevoluteJoint) world.createJoint(jointDef);


    }

    public void moveUp(){
        if(body.getLinearVelocity().y <= maxSpeed)
            body.applyForce(new Vector2(0, shipAcceleration), body.getWorldCenter(), true);
    }
    public void moveDown(){
        if(body.getLinearVelocity().y <= maxSpeed)
            body.applyForce(new Vector2(0, -shipAcceleration), body.getWorldCenter(), true);
    }
    public void moveLeft(){
        if(body.getLinearVelocity().x <= maxSpeed)
            body.applyForce(new Vector2(-shipAcceleration, 0), body.getWorldCenter(), true);
    }
    public void moveRight(){
        if(body.getLinearVelocity().x <= maxSpeed)
            body.applyForce(new Vector2(shipAcceleration,0), body.getWorldCenter(), true);
    }
    public void stopShip(){
        body.setLinearVelocity(0,0); //Need to make it gradually slow down
    }


}
