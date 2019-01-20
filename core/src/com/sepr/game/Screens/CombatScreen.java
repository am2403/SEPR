package com.sepr.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Main;
import com.sepr.game.Sprites.Cannon;
import com.sepr.game.Sprites.Fleet;
import com.sepr.game.Sprites.Ship;


public class CombatScreen extends ScreenAdapter {

    Viewport viewport;
    public static OrthographicCamera gamecam;
    private Stage stage;
    private Main game;
    private SpriteBatch batch;

    // ship and fleet
    private Ship ship_combat;
    private Fleet fleet_combat;
    private Cannon cannon_combat;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public static final int V_WIDTH = 1000;
    public static final int V_HEIGHT = 1000;

    public CombatScreen(Main game){

        this.game = game;

        gamecam = new OrthographicCamera();
        viewport = new StretchViewport(V_WIDTH / Main.PPM ,V_HEIGHT / Main.PPM, gamecam); //Maintains aspect ratio as window is resized
        stage = new Stage(viewport, game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Combat Map/combat.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/Main.PPM);




        world  = new World(new Vector2(0, 0), true); // Can apply gravity / wind speed force
        b2dr = new Box2DDebugRenderer();
        ship_combat = new Ship(this);
        fleet_combat = new Fleet(this);

        gamecam.position.set(ship_combat.shipBody.getWorldCenter().x, ship_combat.shipBody.getWorldCenter().y, 0);

        //ship_combat.shipBody.setTransform(viewport.getWorldWidth()/2f, viewport.getWorldHeight()/2f, 0);

//        // centres the camera in middle of map
//        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);
//        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth() / 2, layer0.getHeight() * layer0.getTileHeight() / 2, 0);
//        gamecam.position.set(center);




        //ship_combat.shipBody.setTransform(V_WIDTH/2f, V_HEIGHT/2f, 0);

        //ship_combat.setScale(200, 100);



    }


    @Override
    public void show() {

    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);

        gamecam.update();

        // sprite updates below
        ship_combat.update(dt);
        fleet_combat.update(dt);

        checkShipBoundary();
        checkFleetBoundary();

        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gamecam.update();
        //renderer.setView(gamecam);
        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        ship_combat.draw(game.batch);
        fleet_combat.draw(game.batch);

        game.batch.end();


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

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

    public void handleInput(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                System.out.println("my name jeff");
                ship_combat.moveUp();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                ship_combat.rotateClockwise();
                ship_combat.cannon.rotateClockwise();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                ship_combat.rotateCounterClockwise();
                ship_combat.cannon.rotateCounterClockwise();
            }
            else ship_combat.stopShip();
            }
        }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public Viewport getViewport(){
        return viewport;
    }

    public void checkShipBoundary() {
        System.out.println("yeeto"+gamecam.position);
        // this checks the right boundary for the ship
        if (ship_combat.shipBody.getPosition().x > (gamecam.position.x + (gamecam.viewportWidth / 2) - (ship_combat.getWidth()/2))) {
            ship_combat.shipBody.setTransform((gamecam.position.x + (gamecam.viewportWidth / 2)) - (ship_combat.getWidth()/2), ship_combat.shipBody.getPosition().y, ship_combat.shipBody.getAngle());
        }
        // checks the left boundary for the ship
        if (ship_combat.shipBody.getPosition().x < (gamecam.position.x - (gamecam.viewportWidth / 2) + (ship_combat.getWidth()/2))) {
            ship_combat.shipBody.setTransform((gamecam.position.x - (gamecam.viewportWidth / 2)) + (ship_combat.getWidth()/2), ship_combat.shipBody.getPosition().y, ship_combat.shipBody.getAngle());
        }

        if (ship_combat.shipBody.getPosition().y > (gamecam.position.y + (gamecam.viewportHeight / 2) - (ship_combat.getHeight()/2))) {
            ship_combat.shipBody.setTransform( ship_combat.shipBody.getPosition().x,(gamecam.position.y + (gamecam.viewportHeight / 2)) - (ship_combat.getHeight()/2), ship_combat.shipBody.getAngle());
        }

        if (ship_combat.shipBody.getPosition().y < (gamecam.position.y - (gamecam.viewportHeight / 2) + (ship_combat.getHeight()/2))) {
            ship_combat.shipBody.setTransform( ship_combat.shipBody.getPosition().x,(gamecam.position.y - (gamecam.viewportHeight / 2)) + (ship_combat.getHeight()/2), ship_combat.shipBody.getAngle());
        }
    }

    public void checkFleetBoundary() {
        System.out.println("yeeto"+gamecam.position);
        // this checks the right boundary for the ship
        if (fleet_combat.body.getPosition().x > (gamecam.position.x + (gamecam.viewportWidth / 2) - (fleet_combat.getWidth()/2))) {
            fleet_combat.body.setTransform((gamecam.position.x + (gamecam.viewportWidth / 2)) - (fleet_combat.getWidth()/2), fleet_combat.body.getPosition().y, fleet_combat.body.getAngle());
        }
        // checks the left boundary for the ship
        if (fleet_combat.body.getPosition().x < (gamecam.position.x - (gamecam.viewportWidth / 2) + (fleet_combat.getWidth()/2))) {
            fleet_combat.body.setTransform((gamecam.position.x - (gamecam.viewportWidth / 2)) + (fleet_combat.getWidth()/2), fleet_combat.body.getPosition().y, fleet_combat.body.getAngle());
        }

        if (fleet_combat.body.getPosition().y > (gamecam.position.y + (gamecam.viewportHeight / 2) - (fleet_combat.getHeight()/2))) {
            fleet_combat.body.setTransform(fleet_combat.body.getPosition().x,(gamecam.position.y + (gamecam.viewportHeight / 2)) - (fleet_combat.getHeight()/2), fleet_combat.body.getAngle());
        }

        if (fleet_combat.body.getPosition().y < (gamecam.position.y - (gamecam.viewportHeight / 2) + (ship_combat.getHeight()/2))) {
            fleet_combat.body.setTransform( fleet_combat.body.getPosition().x,(gamecam.position.y - (gamecam.viewportHeight / 2)) + (fleet_combat.getHeight()/2), fleet_combat.body.getAngle());
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
