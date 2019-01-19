package com.sepr.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

public class CannonBall extends Sprite {
    public World world;
    public Body cannonBallBody;
    private Texture cannonBallTexture;
    private Sprite cannonBall;
    private BodyDef bdef;
    private FixtureDef fdef;
    private CircleShape cannonBallShape;

    public float x, y, angle;
    private float magnitude = 2f;

    Vector2 force;




    public CannonBall(PlayScreen screen, float x, float y, float cannonAngle) {
        this.world = screen.getWorld();

        this.x = x;
        this.y = y;
        this.angle = cannonAngle;

        defineCannonBall();
        cannonBallTexture = new Texture("cannonBall.png");
        cannonBall = new Sprite(cannonBallTexture);
        setBounds(x, y, 10 / Main.PPM, 10 / Main.PPM);
        setRegion(cannonBall);

        //applies the force of the cannonball in the direction the cannon is facing
        force = new Vector2(cos(angle), sin(angle));

        cannonBallBody.setUserData(this);
    }

    // Creates a Box2D object for the ship and the ship's cannon, then attaches the cannon to the ship with a ResoluteJoint
    public void defineCannonBall() {
        //Generic variables, can be applied to ship and cannon/
        bdef = new BodyDef();
        fdef = new FixtureDef();


        //cannonBall creation creation
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x * 100 / Main.PPM, y * 100 / Main.PPM); //the cannonball body will be created in the position of the cannon

        cannonBallShape = new CircleShape();
        cannonBallShape.setRadius(0.05f);
        fdef.shape = cannonBallShape;

        fdef.restitution = 0.1f;

        cannonBallBody = world.createBody(bdef);
        cannonBallBody.createFixture(fdef);
    }

    public void shoot() {
        Vector2 pos = cannonBallBody.getWorldCenter();
        cannonBallBody.applyLinearImpulse(force, pos, true);

/*        shootTimer += Gdx.graphics.getDeltaTime();

        if(shootTimer >= SHOOT_WAIT_TIME){
            shootTimer = 0; //resets the shoot timer

            cannonBalls.add(new CannonBall(this, ship.cannon.cannonBody.getWorldCenter().x, cannon.cannonBody.getWorldCenter().y, cannon.cannonBody.getAngle()));

            //since a force is applied to the ship when we shoot our bullet, we apply an equal force in the
            //opposite direction, stopping the ship from continiously moving backwards (acting a bit like recoil)
            ship.shipBody.applyLinearImpulse(new Vector2(cos(cannon.cannonBody.getAngle()), sin(cannon.cannonBody.getAngle())), ship.shipBody.getWorldCenter(), true);
        }*/
    }

    public void update(float dt){
        //sets the debug renderer line and the sprite into the same position
        setPosition((cannonBallBody.getPosition().x - getWidth() / 2) , (cannonBallBody.getPosition().y- getHeight() / 2 )); //sets position of the sprites to the middle of the outline
        shoot();
    }





    public void dispose(){
        cannonBallTexture.dispose();
    }
}
