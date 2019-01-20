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
    private Viewport viewport;
    private HUD hud;

    //Ship and fleet
    public Ship ship;
    private Fleet fleet;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    public static final int V_WIDTH = 1000;
    public static final int V_HEIGHT = 1000;
    

    public PlayScreen(Main game){
        this.game = game;

        //Create a camera and fix the viewport
        gamecam = new OrthographicCamera();
        viewport = new StretchViewport(V_WIDTH / Main.PPM ,V_HEIGHT / Main.PPM, gamecam); //Maintains aspect ratio as window is resized
        hud = new HUD(game.batch);

        //load the Tiled map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/Main.PPM);
        gamecam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world  = new World(new Vector2(0, 0), true); // Can apply gravity / wind speed forces
        b2dr = new Box2DDebugRenderer();

        new BoxPhysics(this);

        ship = new Ship(this);
        fleet = new Fleet(this);


        //Listens for Box2D Object collisions
        world.setContactListener(new WorldContactListener(this));


    }

    @Override
    public void show() {

    }



    public void handleInput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            if(Gdx.input.isKeyPressed(Input.Keys.W))
                ship.moveUp();
            if(Gdx.input.isKeyPressed(Input.Keys.D)) {
                ship.rotateClockwise();
                ship.cannon.rotateClockwise();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)){
                ship.rotateCounterClockwise();
                ship.cannon.rotateCounterClockwise();
            }
            else ship.stopShip();

            if (Gdx.input.isKeyPressed(Input.Keys.C)){
                game.setScreen(new CombatScreen(game));
            }
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                ship.shoot();
            }

        }
    }



    //This is called once every 60 seconds (world.step..)
    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2);

        //Updates the camera coordinates so that it remains fixed on the ship
        gamecam.position.x = ship.shipBody.getPosition().x;
        gamecam.position.y = ship.shipBody.getPosition().y;

        gamecam.update();
        ship.update(dt);

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
        ship.cannon.draw(game.batch);
        //draws the cannon balls
        for (CannonBall cannonBall: ship.cannonBalls){
            cannonBall.draw(game.batch);
        }
        fleet.draw(game.batch);

        game.batch.end();

        //Renders the fixed HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
