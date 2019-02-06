/*
This class listens for BOX2D object collisions
 */

package com.sepr.game.Tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sepr.game.Scenes.HUD;
import com.sepr.game.Screens.CombatScreen;
import com.sepr.game.Screens.PlayScreen;
import com.sepr.game.Sprites.*;

import java.util.ArrayList;

public class WorldContactListener implements ContactListener {
    public PlayScreen playScreen;
    public CombatScreen combatScreen;
    public Array<Body> bodiesToRemove;

    public int cannonBallDamage;

    public WorldContactListener(PlayScreen playScreen){
        this.playScreen = playScreen;
        bodiesToRemove = new Array<Body>();

        cannonBallDamage = 10;
    }

    public WorldContactListener(CombatScreen combatScreen){
        this.combatScreen = combatScreen;
        bodiesToRemove = new Array<Body>();
    }

    public void setCannonBallDamage(int cannonBallDamage) {
        this.cannonBallDamage = cannonBallDamage;
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


        if(o2.getClass() == CannonBall.class && (o1.getClass() == Dock.class || o1.getClass() == Land.class || o1.getClass() == Ship.class ||
                o1.getClass() == Fleet.class)){
            b1.applyForce(new Vector2(0,0), b1.getWorldCenter(), true);
        }

        if (o2.getClass() == CannonBall.class && o1.getClass() == Fleet.class){
            System.out.println("Cannonball hit fleet");

            bodiesToRemove.add(f2.getBody());
            //playScreen.

            //for every cannonball that hits the ship will reduce the fleet by 10
            ArrayList<CannonBall> cannonBallsToRemove = new ArrayList<CannonBall>();
            for (CannonBall cannonBall: combatScreen.ship_combat.cannonBalls){
                if(cannonBall == o2){
                    cannonBallsToRemove.add(cannonBall);
                }
            }
            this.combatScreen.fleet_combat.setFleetHealth(this.combatScreen.fleet_combat.getFleetHealth()- cannonBallDamage);
            System.out.println("fleet health"+this.combatScreen.fleet_combat.getFleetHealth());
        }

        if (o2.getClass() == Ship.class && o1.getClass() == Fleet.class) {
            System.out.print("beep");
            playScreen.game.setScreen(new CombatScreen(playScreen.game, playScreen));
        }
        if (o1.getClass() == Ship.class && o2.getClass() == Fleet.class) {
            System.out.print("beep");
            playScreen.game.setScreen(new CombatScreen(playScreen.game, playScreen));
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

}
