/*
This class listens for BOX2D object collisions
 */

package com.sepr.game.Tools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Screens.PlayScreen;
import com.sepr.game.Sprites.*;

public class WorldContactListener implements ContactListener {
    public PlayScreen playScreen;

    public WorldContactListener(PlayScreen playScreen){
        this.playScreen = playScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        Body b1 = f1.getBody();
        Body b2 = f2.getBody();

        Object o1 = b1.getUserData();
        Object o2 = b2.getUserData();

        if(o2.getClass() == CannonBall.class && o1.getClass() == Dock.class){
            b1.applyForce(new Vector2(0,0), b1.getWorldCenter(), true);
        }

        if (o2.getClass() == CannonBall.class && o1.getClass() == Fleet.class){

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
