package com.sepr.game.Sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Main;
import com.sepr.game.Screens.CombatScreen;


import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

public class CannonBall extends Sprite {
    public World world;
    public static Body cannonBallBody;
    private Texture cannonBallTexture;
    private Sprite cannonBall;
    private BodyDef bdef;
    public FixtureDef fdef;
    private CircleShape cannonBallShape;
    public static Fixture fixture;

    public float x, y, angle;

    Vector2 force;




    public CannonBall(CombatScreen screen, float x, float y, float cannonAngle) {
        this.world = screen.getWorld();

        this.x = x;
        this.y = y;
        this.angle = cannonAngle;

        defineCannonBall();
        cannonBallTexture = new Texture("cannonBall.png");
        cannonBall = new Sprite(cannonBallTexture);
        setBounds(x, y, 10 / Main.PPM, 10 / Main.PPM);
        setRegion(cannonBall);


        //sets an identifier in the contact listener to allow for collision detection
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
        fdef.friction = 100f;

        cannonBallBody = world.createBody(bdef);
        fixture = cannonBallBody.createFixture(fdef);
    }

    public void shoot(float angl) {
        //'pos' is the location where the impulse force will be applied to the cannon ball
        Vector2 pos = cannonBallBody.getWorldCenter();

        //applies the force of the cannonball in the DIRECTION the cannon is facing
        force = new Vector2(cos(angl), sin(angl));
        cannonBallBody.applyLinearImpulse(force, pos, true);

    }

    public void update(float dt, float angl){
        //sets the debug renderer line and the sprite into the same position
        setPosition((cannonBallBody.getPosition().x - getWidth() / 2) , (cannonBallBody.getPosition().y- getHeight() / 2 )); //sets position of the sprites to the middle of the outline
        shoot(angl);
    }



    public void dispose(){
        cannonBallTexture.dispose();
    }
}
