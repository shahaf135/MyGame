package com.example.mygame;


import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;



/**
 * player is the main character of the game,which the user can control with a touch joystick
 * the player class in an extension of a Circle, which is an extension of GameObject
 */


public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND=400.0;
    public static final int MAX_HEALTH_POINTS =10 ;
    private static final double MAX_SPEED =SPEED_PIXELS_PER_SECOND/ GameLoop.MAX_UPS ;
    private final Joystick joystick;
    private HealthBar healthBar;
    private int healthPoints;


    public Player(Context context,Joystick joystick, double positionX, double positionY, double radius){
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick=joystick;
        this.healthBar=new HealthBar(context,this);
        this.healthPoints=MAX_HEALTH_POINTS;
    }



    public void update() {
        //update velocity based on the actuator of the joystick
        velocityX=joystick.getActuatorX()*MAX_SPEED;
        velocityY=joystick.getActuatorY()*MAX_SPEED;

        //Update Position
        positionX+=velocityX;
        positionY+=velocityY;

        //update direction
        if (velocityX!=0 || velocityY!=0)
        //find vector of velocity
        {
            double distance=Utils.getDistanceBetweenPoints(0,0,velocityX,velocityY);
            directionX=velocityX/distance;
            directionY=velocityY/distance;
        }
    }


    public void draw (Canvas canvas){
        super.draw(canvas);
        healthBar.draw(canvas);
    }


    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoint) {
        if (healthPoint>=0)
            this.healthPoints=healthPoint;
    }
}










