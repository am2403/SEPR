package com.sepr.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Main;
import com.sepr.game.Screens.CombatScreen;
import com.sepr.game.Screens.PlayScreen;

public class Fleet extends Sprite {

    public World world;
    public Body body;
    private Texture fleetTexture;
    private Sprite fleet;
    private int spawnX = 70, spawnY = 81; //x and y location that the fleet spawns at

    float state = 1.0f; //used as a trigger to change the direction of the fleet

    public static int getFleetHealth() {
        return fleetHealth;
    }

    public static void setFleetHealth(int new_fleetHealth) { fleetHealth = new_fleetHealth; }

    public static int fleetHealth = 100;

    public Fleet(PlayScreen screen){
        this.world = screen.getWorld();
        defineFleet();

        //sets the fleet texture as a sprite
        fleetTexture = new Texture("fleet.png");
        fleet = new Sprite(fleetTexture);

        //sets the initial position of the sprite
        fleet.setPosition(body.getPosition().x, body.getPosition().y);
        setBounds(0, 0, 195 / Main.PPM, 83 / Main.PPM);
        setRegion(fleet);

        //accessed by the contact listener to identify if an object has collided with the fleet
        body.setUserData(this);
    }

    public Fleet(CombatScreen screen){
        this.world = screen.getWorld();
        defineFleet();
        fleetTexture = new Texture("fleet.png");
        fleet = new Sprite(fleetTexture);
        fleet.setPosition(body.getPosition().x, body.getPosition().y);
        setBounds(0, 0, 195 / Main.PPM, 83 / Main.PPM);
        setRegion(fleet);

        body.setUserData(this);
    }

    public void update(float dt, CombatScreen screen, Viewport viewport){
        //constantly updates the position of the sprite in correspondence with the fleet body
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees); //Here in case the body every rotates

        fleetMovement(dt, screen, viewport); //called to enable fleet movement
    }

    //a different update method for the playscreen
    public void update(float dt, PlayScreen screen, Viewport viewport){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees); //Here in case the body every rotates
    }

    //Creates a Box2D Object and attaches a shape to it
    public void defineFleet(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnX * 100 / Main.PPM, spawnY * 100 / Main.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.975f, 0.41f);
        fdef.shape = shape;
        fdef.restitution = 0.01f;

        body.applyLinearImpulse(0f, 1.0f, body.getPosition().x, body.getPosition().y, true);

        body.createFixture(fdef);
        body.setLinearDamping(50f);
    }

    public void fleetMovement(float dt, CombatScreen screen, Viewport viewport) {
        //an impulse is constantly applied to the fleet
        body.applyLinearImpulse(0f, state, body.getPosition().x, body.getPosition().y, true);

        //if the position of the fleet is greater than the top of the gamecam, we inverse the 'state' of our fleet
        //which makes it travel in the opposite direction
        if (body.getPosition().y > screen.gamecam.position.y + (screen.gamecam.viewportHeight / 3)) {
            // then fleet is below top line
           state = -1.0f;
        }
        if (body.getPosition().y < screen.gamecam.position.y - (screen.gamecam.viewportHeight / 3)) {
            // then fleet is below top line
            state = 1.0f;
        }
    }

    public void dispose(){
        //world.dispose();
        fleetTexture.dispose();
    }

}
