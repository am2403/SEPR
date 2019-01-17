package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

public class Fleet extends Sprite {

    public World world;
    public Body body;
    private Texture fleetTexture;
    private Sprite fleet;
    private int spawnX = 67, spawnY = 71; //x and y location that the fleet spawns at

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

    // Gives the fleet a linear impulse to the left... still needs a lot of work for proper fleet movement
    public void fleetMovement(float dt) {

        body.applyLinearImpulse(new Vector2(-0.001f, 0f), body.getWorldCenter(), true);

    }




    public void dispose(){
        world.dispose();
        fleetTexture.dispose();
    }
}
