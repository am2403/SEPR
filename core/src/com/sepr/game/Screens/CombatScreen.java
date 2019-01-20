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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Main;
import com.sepr.game.Sprites.Cannon;
import com.sepr.game.Sprites.Fleet;
import com.sepr.game.Sprites.Ship;
import com.sepr.game.Tools.BoxPhysics;
import com.badlogic.gdx.math.Vector3;

import javax.swing.*;

public class CombatScreen extends ScreenAdapter {

    private Viewport viewport;
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



    public CombatScreen(Main game){

        gamecam = new OrthographicCamera();
        viewport = new StretchViewport(Main.V_WIDTH / Main.PPM ,Main.V_HEIGHT / Main.PPM, gamecam); //Maintains aspect ratio as window is resized
        stage = new Stage(viewport, game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Combat Map/combat_screen.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);

        world  = new World(new Vector2(0, 0), true); // Can apply gravity / wind speed force

        batch = new SpriteBatch();

        b2dr = new Box2DDebugRenderer();

        // centres the camera in middle of map
        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);
        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth() / 2, layer0.getHeight() * layer0.getTileHeight() / 2, 0);
        gamecam.position.set(center);

        ship_combat = new Ship(this);
        fleet_combat = new Fleet(this);

        ship_combat.shipBody.setTransform(400, 400, 0);

        ship_combat.setScale(200, 100);



    }


    @Override
    public void show() {

    }

    public void update(float dt) {
        handleInput(dt);

        gamecam.update();

        // sprite updates below
        ship_combat.update(dt);
        fleet_combat.update(dt);

        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gamecam.update();
        renderer.setView(gamecam);
        renderer.render();

        b2dr.render(world, gamecam.combined);

        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();

        ship_combat.draw(batch);
        fleet_combat.draw(batch);

        batch.end();


    }

    @Override
    public void resize(int width, int height) {
        gamecam.viewportWidth = width;
        gamecam.viewportHeight = height;
        gamecam.update();

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
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            System.out.println(Gdx.input.getX());
            System.out.println(Gdx.input.getY());
        }
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
