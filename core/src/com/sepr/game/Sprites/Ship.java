/*
Handles everything to do with the ship
 */

package com.sepr.game.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.sepr.game.Main;
import com.sepr.game.Screens.CombatScreen;
import com.sepr.game.Screens.PlayScreen;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Ship extends Sprite {
    public World world;
    public Body shipBody;
    private Texture shipTexture;
    private Sprite ship;
    private BodyDef bdef;
    private FixtureDef fdef;
    private PolygonShape shipShape;
    private float maxSpeed = 4f;
    private float forceX, forceY;

    private float magnitude = 2f;

    private PlayScreen playScreen;
    private CombatScreen combatScreen;

    public Cannon cannon;

    RevoluteJoint joint;

    public static final float SHOOT_WAIT_TIME = 0.15f;
    float shootTimer;

    public ArrayList<CannonBall> cannonBalls;


    public Ship(PlayScreen screen) {
        this.world = screen.getWorld();
        this.playScreen = screen;
        defineShip();
        shipTexture = new Texture("mainShip.png");
        ship = new Sprite(shipTexture);
        setBounds(0, 0, 250 / Main.PPM, 88 / Main.PPM);
        setRegion(ship);
        cannon = new Cannon(screen);

        RevoluteJointDef rjd = new RevoluteJointDef();

        rjd.initialize(shipBody, cannon.cannonBody, shipBody.getWorldCenter());
        joint = (RevoluteJoint) world.createJoint(rjd);

        shootTimer = 0;

        cannonBalls = new ArrayList<CannonBall>();
    }

    public Ship(CombatScreen screen) {
        this.world = screen.getWorld();
        this.combatScreen = screen;
        defineShip();
        shipTexture = new Texture("mainShip.png");
        ship = new Sprite(shipTexture);
        setBounds(0, 0, 250 / Main.PPM, 88 / Main.PPM);
        setRegion(ship);
        cannon = new Cannon(screen);

        RevoluteJointDef rjd = new RevoluteJointDef();

        rjd.initialize(shipBody, cannon.cannonBody, shipBody.getWorldCenter());
        joint = (RevoluteJoint) world.createJoint(rjd);

        shootTimer = 0;

        cannonBalls = new ArrayList<CannonBall>();
    }


    public Ship() {
        defineShip();
        shipTexture = new Texture("mainShip.png");
        ship = new Sprite(shipTexture);
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(ship);
    }




    public void update(float dt) {
        cannon.update(dt);

        setPosition(shipBody.getPosition().x - getWidth() / 2, shipBody.getPosition().y- getHeight() / 2); //puts the ship body onto the middle of the screen

        setRotation(shipBody.getAngle() * MathUtils.radiansToDegrees); //converts the angles to radians and rotates the ship body WILL NEED TO INPUT THE DELTA SPEED ON ROTATION
        setOriginCenter();
        forceX = cos(shipBody.getAngle()); //upon rotation this will be our new x coordinate for our ship body
        forceY = sin(shipBody.getAngle()); //upon rotation this will be our new y coordinate for our ship body


        //Update bullets
        //if the cannonBall goes past the world width it gets removed from the game
        ArrayList<CannonBall> cannonBallsToRemove = new ArrayList<CannonBall>();
        for (CannonBall cannonBall : cannonBalls) {
            cannonBall.update(dt);
            if (cannonBall.cannonBallBody.getWorldCenter().x > Gdx.graphics.getWidth() || cannonBall.cannonBallBody.getWorldCenter().x < 0) {
                cannonBallsToRemove.add(cannonBall);
            }
        }
        cannonBalls.removeAll(cannonBallsToRemove);
    }

    // Creates a Box2D object for the ship and the ship's cannon, then attaches the cannon to the ship with a ResoluteJoint
    public void defineShip() {
        //Generic variables, can be applied to ship and cannon/
        bdef = new BodyDef();
        fdef = new FixtureDef();

        //Ship creation
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(6200 / Main.PPM, 7100 / Main.PPM);

        shipShape = new PolygonShape();
        shipShape.setAsBox(1.2f, 0.44f);
        fdef.shape = shipShape;

        //Ship properties
        fdef.restitution = 0.1f; // Don't really bounce off ships so it's a low values
        fdef.friction = 0.5f;

        shipBody = world.createBody(bdef);
        shipBody.createFixture(fdef);

        shipBody.setAngularDamping(30f); //the resistance to the ships rotating force

        shipBody.setUserData(this);
    }

    public void moveUp() {
        //if the ships velocity isn't already at maximum velocity, then apply a force to the ship in the angle force x and force y by a magnitude
        if (shipBody.getLinearVelocity().y <= maxSpeed && shipBody.getLinearVelocity().x < maxSpeed)
            shipBody.applyForce(new Vector2(forceX * magnitude, forceY * magnitude), shipBody.getWorldCenter(), true);
    }


    public void rotateClockwise(){
        shipBody.setAngularVelocity(-2f); //rotation speed
    }

    public void rotateCounterClockwise(){
        shipBody.setAngularVelocity(2f); //rotation speed
    }

    public void stopShip() {
        shipBody.setLinearDamping(0.6f); //Slows down the ship gradually
    }


    public void shoot(){
        shootTimer += Gdx.graphics.getDeltaTime();

        if(shootTimer >= SHOOT_WAIT_TIME){
            shootTimer = 0; //resets the shoot timer

            cannonBalls.add(new CannonBall(playScreen, cannon.cannonBody.getWorldCenter().x, cannon.cannonBody.getWorldCenter().y, cannon.cannonBody.getAngle()));

            //since a force is applied to the ship when we shoot our bullet, we apply an equal force in the
            //opposite direction, stopping the ship from continuously moving backwards (acting a bit like recoil)
            shipBody.applyLinearImpulse(new Vector2(cos(cannon.cannonBody.getAngle()), sin(cannon.cannonBody.getAngle())), shipBody.getWorldCenter(), true);
        }
    }



    public void dispose(){
        shipTexture.dispose();
    }
}




