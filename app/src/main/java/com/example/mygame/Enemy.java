package com.example.mygame;


import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.mygame.Game;


/**
 * player is that always moves in the direction of the player
 * the Enemy class in the extention of a circle
 */

public class Enemy extends Circle {


    private static final double SPAWNS_PER_MINUTE =20 ;
    private static final double SPAWNS_PER_SECOND =SPAWNS_PER_MINUTE/60.0 ;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updateUntilSpawn=UPDATES_PER_SPAWN;
    private final Player player;
    private static final double SPEED_PIXELS_PER_SECOND=Player.SPEED_PIXELS_PER_SECOND*0.6;
    private static final double MAX_SPEED=SPEED_PIXELS_PER_SECOND/ GameLoop.MAX_UPS;



    public Enemy(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.enemy), Math.random()*1000, Math.random()*1000, 30);
        this.player=player;
    }

    //checks if a new enemy should spawn
    //See SPAWNS_PER_MINUTE
    public static boolean readyToSpawn() {
        if (updateUntilSpawn<=0)
        {
            updateUntilSpawn+=UPDATES_PER_SPAWN;
            return true;
        }
        else {
            updateUntilSpawn--;
            return false;

        }
    }

    @Override
    public void update() {
        //update the velocity of the enemy
        // to make the velocity in the direction of the player

        //Calculate vector from enemy to player
        double distanceToPlayerX=player.getPositionX()-positionX;
        double distanceToPlayerY=player.getPositionY()-positionY;

        //calculate absolute distance

        double distanceToPlayer= GameObject.getDistanceBetweenObjects(this,player);


        //calculate direction for enemy to player
        double directionX=distanceToPlayerX/distanceToPlayer;
        double directionY=distanceToPlayerY/distanceToPlayer;

        //set velocity in the direction of the player
        if(distanceToPlayer>0)
        {
            velocityX=directionX*MAX_SPEED;
            velocityY=directionY*MAX_SPEED;
        }
        else {
            velocityX=0;
            velocityY=0;
        }
        // update enemy position
        positionX+=velocityX;
        positionY+=velocityY;
    }
}
