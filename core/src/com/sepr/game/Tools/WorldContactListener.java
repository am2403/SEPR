/*
This class listens for BOX2D object collisions
 */

package com.sepr.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.sepr.game.Sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA(); //Ship
        Fixture fixB = contact.getFixtureB(); //Other object

        if(fixA.getUserData() == "ship" || fixB.getUserData() == "ship"){
            Fixture ship = fixA.getUserData() == "ship" ? fixA : fixB;
            Fixture object = ship == fixA ? fixB : fixA;

            //If the object is a static tile (such as the dock or land), then note that the ship has collided with it
            if(object.getUserData() instanceof InteractiveTileObject){
                ((InteractiveTileObject) object.getUserData()).shipContact();
            }
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
