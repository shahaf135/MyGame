package com.example.mygame;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;



public class Joystick {

    private  Paint innerCirclePaint;
    private  Paint outerCirclePaint;


    private  int outerCircleCenterPositionX;
    private  int outerCircleCenterPositionY;
    private  int outerCircleRadius;

    private  int innerCircleCenterPositionX;
    private  int innerCircleCenterPositionY;
    private  int innerCircleRadius;
    private double joystickCenterToTouchDistance;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;


    public Joystick(int centerPositionX,int centerPositionY,int outerCircleRadius,int innerCircleRadius){

        //outer and inner circles
        outerCircleCenterPositionX=centerPositionX;
        outerCircleCenterPositionY=centerPositionY;

        innerCircleCenterPositionX=centerPositionX;
        innerCircleCenterPositionY=centerPositionY;

        //radius
        this.outerCircleRadius=outerCircleRadius;
        this.innerCircleRadius=innerCircleRadius;

        //paint  of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.RED);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCircleCenterPositionX,outerCircleCenterPositionY,outerCircleRadius,outerCirclePaint);
        canvas.drawCircle(innerCircleCenterPositionX,innerCircleCenterPositionY,innerCircleRadius,innerCirclePaint);
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX= (int) (outerCircleCenterPositionX+actuatorX*outerCircleRadius);
        innerCircleCenterPositionY= (int) (outerCircleCenterPositionY+actuatorY*outerCircleRadius);

    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joystickCenterToTouchDistance=Math.sqrt(
                Math.pow(outerCircleCenterPositionX-touchPositionX,2)+
                        Math.pow(outerCircleCenterPositionY-touchPositionY,2)
        );
        return joystickCenterToTouchDistance <outerCircleRadius;

    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed=isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void SetActuator(double touchPositionX, double touchPositionY) {
        double deltaX=touchPositionX-outerCircleCenterPositionX;
        double deltaY=touchPositionY-outerCircleCenterPositionY;
        double deltaDistance= Utils.getDistanceBetweenPoints(0,0,deltaX,deltaY);

        if(deltaDistance<outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius;
            actuatorY = deltaY / outerCircleRadius;
        }
        else {
            actuatorX=deltaX/deltaDistance;
            actuatorY=deltaY/deltaDistance;

        }

    }

    public void restartActuator() {
        actuatorX=0;
        actuatorY=0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
