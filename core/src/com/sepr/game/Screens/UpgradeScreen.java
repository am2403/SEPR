package com.sepr.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Main;
import com.sepr.game.Tools.WorldContactListener;

public class UpgradeScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    public WorldContactListener worldContactListener;

    private Game game;
    private PlayScreen screen;

    public static final int V_WIDTH = 1600;
    public static final int V_HEIGHT = 900;

    public int priceToUpgradeCannonBall = 0;
    public Table table;

    public Label upgradeCannonBall;
    public Label confirmCannonBallTransaction;


    public UpgradeScreen(Game game,PlayScreen screen){
        this.game = game;
        this.screen = screen;
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Main) game).batch);
        worldContactListener = new WorldContactListener(screen);


        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("Click left to upgrade your ship", font);
        Label instructionOne = new Label("Click right to upgrade your cannonBall", font);

        table.add(instructionOne).expandX();
        table.row();
        table.add(gameOverLabel).expandX();



        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    public void upgradeCannonBall(){
        //if we press the up arrow it increases how much we want to spend in the shop
        //whilst subtracting from the score

        //right now we want to be able to press up and make a number increment by 100

        priceToUpgradeCannonBall+= 10;

        //stops the on-screen commands from appearing more than once
        table.removeActor(upgradeCannonBall);
        table.removeActor(confirmCannonBallTransaction);

        //onscreen commands are made into variables so we can manipulate them
        upgradeCannonBall = new Label(String.format("CannonBall: " + "%03d", priceToUpgradeCannonBall), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        confirmCannonBallTransaction  =new Label(String.format("PRESS 1 TO CONFIRM TRANSACTION"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        table.add(upgradeCannonBall);
        table.row();
        table.add(confirmCannonBallTransaction).expandX();


    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(priceToUpgradeCannonBall > screen.hud.score){
                table.add(new Label(String.format("You dont have enough points for this upgrade"), new Label.LabelStyle(new BitmapFont(), Color.RED)));
                table.row();
            }else{
                upgradeCannonBall();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1) && (priceToUpgradeCannonBall < screen.hud.score)){
            //INCREASE CANNON DMG HOW MUCH THE USER BOUGHT
            worldContactListener.setCannonBallDamage(priceToUpgradeCannonBall);

            //REDUCE THE ONSCREEN SCORE
            screen.hud.updateScore(priceToUpgradeCannonBall - screen.hud.score);
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
