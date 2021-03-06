/*
Handles everything to do with the ship
 */

package com.sepr.game.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.sepr.game.Main;
import com.sepr.game.Screens.CombatScreen;
import com.sepr.game.Screens.PlayScreen;
import com.sepr.game.Tools.WorldContactListener;

import java.util.ArrayList;
import java.util.Random;

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

    public static int health = 100;

    private float magnitude = 2f;

    private PlayScreen playScreen;
    private CombatScreen combatScreen;

    public Cannon cannon;

    RevoluteJoint joint;

    public static final float SHOOT_WAIT_TIME = 0.15f;
    float shootTimer;

    public ArrayList<CannonBall> cannonBalls;

    WorldContactListener cl;

    private Random rand;
    private int damageTimer;


    public Ship(PlayScreen screen) {
        this.world = screen.getWorld();
        this.playScreen = screen;
        defineShip();
        shipTexture = new Texture("mainShip.png");
        ship = new Sprite(shipTexture);
        setBounds(0, 0, 250 / Main.PPM, 88 / Main.PPM);
        setRegion(ship);
        cannon = new Cannon(screen);


        //a joint is created between the ship and cannon, joined in 'shipBody.getWorldCenter()'(center of shipBody)
        RevoluteJointDef rjd = new RevoluteJointDef();
        rjd.initialize(shipBody, cannon.cannonBody, shipBody.getWorldCenter());
        joint = (RevoluteJoint) world.createJoint(rjd);

        //sets the initial bullet timer to 0
        shootTimer = 0;

        cannonBalls = new ArrayList<CannonBall>();

        //Listens for Box2D Object collisions
        cl = new WorldContactListener(screen);
        world.setContactListener(cl);

        rand = new Random();
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

        //a joint is created between the ship and cannon, joined in 'shipBody.getWorldCenter()'(center of shipBody)
        rjd.initialize(shipBody, cannon.cannonBody, shipBody.getWorldCenter());
        joint = (RevoluteJoint) world.createJoint(rjd);

        shootTimer = 0;

        cannonBalls = new ArrayList<CannonBall>();

        //Listens for Box2D Object collisions
        cl = new WorldContactListener(screen);
        world.setContactListener(cl);

        rand = new Random();
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
        //if the cannonBall goes passed the world width it gets removed from the game
        ArrayList<CannonBall> cannonBallsToRemove = new ArrayList<CannonBall>();
        for (CannonBall cannonBall : cannonBalls) {
            cannonBall.update(dt, shipBody.getAngle());
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
        //shoot timer is used to stop the user from spamming space bar to shoot the cannon
        shootTimer += Gdx.graphics.getDeltaTime();
        if(shootTimer >= SHOOT_WAIT_TIME){
            shootTimer = 0; //resets the shoot timer

            cannonBalls.add(new CannonBall(combatScreen, cannon.cannonBody.getWorldCenter().x, cannon.cannonBody.getWorldCenter().y, cannon.cannonBody.getAngle()));

        }
    }


    public static int  getHealth() {
        return health;
    }

    public static void setHealth(int new_health) {
        health = new_health;
    }

    public void takeDamagePerSecond(){
        damageTimer++;
        if(damageTimer == 120){
            int randomDamage = rand.nextInt(20) + 1;
            setHealth(getHealth() - randomDamage);
            damageTimer = 0;
            System.out.println(getHealth());
        }
    }


    public void dispose(){
        shipTexture.dispose();
    }
}




