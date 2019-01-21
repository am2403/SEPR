/*
This class listens for BOX2D object collisions
 */

package com.sepr.game.Tools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sepr.game.Main;
import com.sepr.game.Screens.CombatScreen;
import com.sepr.game.Screens.PlayScreen;
import com.sepr.game.Sprites.*;

import java.util.ArrayList;

public class WorldContactListener implements ContactListener {
    public PlayScreen playScreen;
    //public Main game = new Main();

    private Array<Body> bodiesToRemove;




    public WorldContactListener(PlayScreen playScreen){
        this.playScreen = playScreen;
        bodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {

        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        Body b1 = f1.getBody();
        Body b2 = f2.getBody();

        Object o1 = b1.getUserData();
        Object o2 = b2.getUserData();

        //when the cannonBall hits the wall/dock the cannonBall will no longer have a force applied to it
        if(o2.getClass() == CannonBall.class && o1.getClass() == Dock.class){
            b1.applyForce(new Vector2(0,0), b1.getWorldCenter(), true);
        }

        if (o2.getClass() == CannonBall.class && o1.getClass() == Fleet.class){
            System.out.println("Cannonball hit fleet");
            //game.setScreen(new CombatScreen(game));

            bodiesToRemove.add(f2.getBody());

            ArrayList<CannonBall> cannonBallsToRemove = new ArrayList<CannonBall>();
            for (CannonBall cannonBall: playScreen.ship.cannonBalls){
                if(cannonBall == o2){
                    cannonBallsToRemove.add(cannonBall);
                }
            }
            playScreen.ship.cannonBalls.removeAll(cannonBallsToRemove);
        }

        if (o2.getClass() == Ship.class && o1.getClass() == Fleet.class) {
            System.out.print("beep");
            playScreen.game.setScreen(new CombatScreen(playScreen.game));
        }
        if (o1.getClass() == Ship.class && o2.getClass() == Fleet.class) {
            System.out.print("beep");
            playScreen.game.setScreen(new CombatScreen(playScreen.game));
        }

    }

    @Override
    public void endContact(Contact contact) {



    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public Array<Body> getBodiesToRemove(){
        return bodiesToRemove;
    }

}
