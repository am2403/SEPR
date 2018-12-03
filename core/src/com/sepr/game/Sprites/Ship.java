/*
Handles everything to do with the ship
 */

package com.sepr.game.Sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;


public class Ship extends Sprite {

    public World world;
    public Body shipBody;
    private Texture shipTexture;
    private Sprite ship;
    private BodyDef bdef;
    private FixtureDef fdef;
    private CircleShape shipShape;
    private float maxSpeed = 4f, shipAcceleration = 2f;

    public Ship(PlayScreen screen) {
        this.world = screen.getWorld();
        defineShip();
        shipTexture = new Texture("mainShip.png");
        ship = new Sprite(shipTexture);
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(ship);
    }


    public void update(float dt) {
        setPosition(shipBody.getPosition().x - getWidth() / 2, shipBody.getPosition().y - getHeight() / 2);
        setRotation(shipBody.getAngle() * MathUtils.radiansToDegrees);
        setOriginCenter();
        System.out.println(shipBody.getAngle() * MathUtils.radiansToDegrees);


    }

    // Creates a Box2D object for the ship and the ship's cannon, then attaches the cannon to the ship with a ResoluteJoint
    public void defineShip() {

        //Generic variables, can be applied to ship and cannon/
        bdef = new BodyDef();
        fdef = new FixtureDef();

        //Ship creation
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(6200 / Main.PPM, 7100 / Main.PPM);

        shipShape = new CircleShape();
        shipShape.setRadius(0.7f);
        fdef.shape = shipShape;

        //Ship properties
        fdef.restitution = 0.1f; // Don't really bounce off ships so it's a low values
        fdef.friction = 0.5f;

        shipBody = world.createBody(bdef);
        shipBody.createFixture(fdef);

        shipBody.setAngularDamping(30f);






    }

    public void moveUp() {
        if (shipBody.getLinearVelocity().y <= maxSpeed)
            shipBody.applyForce(new Vector2(0, shipAcceleration), shipBody.getWorldCenter(), true);

            shipBody.setAngularVelocity(0.5f);

    }

    public void moveDown() {
        if (shipBody.getLinearVelocity().y <= maxSpeed)
            shipBody.applyForce(new Vector2(0, -shipAcceleration), shipBody.getWorldCenter(), true);

        shipBody.setAngularVelocity(-0.5f);


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
        shipTexture.dispose();
    }

}




