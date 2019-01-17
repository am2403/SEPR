/*
This class renders and updates everything in our game.
 */

package com.sepr.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Main;
import com.sepr.game.Scenes.HUD;
import com.sepr.game.Sprites.Cannon;
import com.sepr.game.Sprites.CannonBall;
import com.sepr.game.Sprites.Fleet;
import com.sepr.game.Sprites.Ship;
import com.sepr.game.Tools.BoxPhysics;
import com.sepr.game.Tools.WorldContactListener;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;


public class PlayScreen implements Screen {

    private Main game;
    public static OrthographicCamera gamecam;
    private Viewport gamePort;
    private HUD hud;

    //Ship and fleet
    private Ship ship;
    private Fleet fleet;
    private Cannon cannon;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    RevoluteJoint joint;

    public ArrayList<CannonBall> cannonBalls;

    public static final float SHOOT_WAIT_TIME = 0.3f;

    float shootTimer;



    public PlayScreen(Main game){

        this.game = game;

        //Create a camera and fix the viewport
        gamecam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM ,Main.V_HEIGHT / Main.PPM, gamecam); //Maintains aspect ratio as window is resized
        hud = new HUD(game.batch);

        //load the Tiled map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/Main.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world  = new World(new Vector2(0, 0), true); // Can apply gravity / wind speed forces
        b2dr = new Box2DDebugRenderer();

        new BoxPhysics(this);

        ship = new Ship(this);
        fleet = new Fleet(this);
        cannon = new Cannon(this);

        //Listens for Box2D Object collisions
        world.setContactListener(new WorldContactListener(this));


        //define joint as between two bodies
        RevoluteJointDef rjd = new RevoluteJointDef();

        rjd.initialize(ship.shipBody, cannon.cannonBody, ship.shipBody.getWorldCenter());
        joint = (RevoluteJoint) world.createJoint(rjd);

        cannonBalls = new ArrayList<CannonBall>();

        shootTimer = 0;
    }

    @Override
    public void show() {

    }



    public void handleInput(float dt){

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            if(Gdx.input.isKeyPressed(Input.Keys.W))
                ship.moveUp();
            if(Gdx.input.isKeyPressed(Input.Keys.D))
                ship.rotateClockwise();
            if(Gdx.input.isKeyPressed(Input.Keys.A))
                ship.rotateCounterClockwise();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                cannon.rotateClockwise();
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                cannon.rotateCounterClockwise();
            else ship.stopShip();
        }

        shootTimer += Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME){
            shootTimer = 0; //resets the shoot timer
            cannonBalls.add(new CannonBall(this, cannon.cannonBody.getWorldCenter().x, cannon.cannonBody.getWorldCenter().y, cannon.cannonBody.getAngle()));

            //since a force is applied to the ship when we shoot our bullet, we apply an equal force in the
            //opposite direction, stopping the ship from continiously moving backwards (acting a bit like recoil)
            ship.shipBody.applyLinearImpulse(new Vector2(cos(cannon.cannonBody.getAngle()), sin(cannon.cannonBody.getAngle())), ship.shipBody.getWorldCenter(), true);
        }



    }

    //This is called once every 60 seconds (world.step..)
    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2);

        //Updates the camera coordinates so that it remains fixed on the ship
        gamecam.position.x = ship.shipBody.getPosition().x;
        gamecam.position.y = ship.shipBody.getPosition().y;

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


        gamecam.update();
        ship.update(dt);
        cannon.update(dt);
        fleet.update(dt);
        hud.update(dt);
        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {
        update(delta);

        //Clear game screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render game map
        renderer.render();

        //render Box2D debug lines
        b2dr.render(world, gamecam.combined);

        //Render sprites
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        ship.draw(game.batch);
        fleet.draw(game.batch);
        cannon.draw(game.batch);

        for (CannonBall cannonBall: cannonBalls){
            cannonBall.draw(game.batch);
        }

        game.batch.end();

        //Renders the fixed HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
            game.setScreen(new Department1(game));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        world.dispose();
        ship.dispose();
        b2dr.dispose();
        hud.dispose();
        fleet.dispose();

    }
}
