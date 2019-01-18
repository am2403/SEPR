package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

import java.util.Random;

public class Fleet extends Sprite {

    public World world;
    public Body body;
    private Texture fleetTexture;
    private Sprite fleet;
    private int spawnX = 65, spawnY = 71; //x and y location that the fleet spawns at

    Random rand;

    int topLimit;
    int bottomLimit;
    int rightLimit;
    int leftLimit;

    float xLimit;
    float yLimit;

    public static int getFleetHealth() {
        return fleetHealth;
    }

    public static void setFleetHealth(int fleetHealth) {
        Fleet.fleetHealth = fleetHealth;
    }

    public static int fleetHealth = 100;



    public Fleet(PlayScreen screen){
        this.world = screen.getWorld();
        defineFleet();
        fleetTexture = new Texture("fleet.png");
        fleet = new Sprite(fleetTexture);
        fleet.setPosition(body.getPosition().x, body.getPosition().y);
        setBounds(0, 0, 100 / Main.PPM, 100 / Main.PPM);
        setRegion(fleet);

        body.setUserData(this);


        //these variables are limits to how far the fleet can travel
        topLimit = spawnY + 4;
        bottomLimit = spawnY - 4;
        rightLimit = spawnX + 1;
        leftLimit = spawnX - 5;

        rand = new Random();

        xLimit = -2+ rand.nextInt(5);
        yLimit = -2+ rand.nextInt(5);
    }

    public void update(float dt){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees); //Here in case the body every rotates
        fleetMovement(dt);


    }

    //Creates a Box2D Object and attaches a shape to it

    public void defineFleet(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX * 100 / Main.PPM, spawnY * 100 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        fdef.shape = shape;
        fdef.restitution = 0.01f;

        body.createFixture(fdef);
        body.setLinearDamping(50f);
    }


    public void fleetMovement(float dt) {
        //int number = random.nextInt(max + 1 -min) + min;

        if(body.getPosition().x > rightLimit || body.getPosition().x < leftLimit) xLimit = -2+ rand.nextInt(5);
        if(body.getPosition().y  > topLimit|| body.getPosition().y < bottomLimit) yLimit = -2+ rand.nextInt(5);

        //body.applyLinearImpulse(new Vector2(-1f, -1f), body.getWorldCenter(), true);
        System.out.println(body.getPosition());

/*          if(body.getPosition().x == xLimit){
            xLimit = -100+ rand.nextInt(200);
        }

        if(body.getPosition().y == yLimit){
            yLimit = -100+ rand.nextInt(200);
        }*/


        if(xLimit > 0 && yLimit > 0){
            body.applyLinearImpulse(new Vector2(1f, 1f), body.getWorldCenter(), true);
        }else if (xLimit > 0 && yLimit < 0){
            body.applyLinearImpulse(new Vector2(1f, -1f), body.getWorldCenter(), true);
        }else if (xLimit < 0 && yLimit > 0){
            body.applyLinearImpulse(new Vector2(-1f, 1f), body.getWorldCenter(), true);
        }else if(xLimit < 0 && yLimit < 0){
            body.applyLinearImpulse(new Vector2(-1f, -1f), body.getWorldCenter(), true);
        }

    }



    public void dispose(){
        world.dispose();
        fleetTexture.dispose();
    }

}
