package com.example.mygame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;



//Health Bar Displace The Player's health on the screen
public class HealthBar  {
    private Player player;
    private float width,height,margin;
    private Paint borderPaint,healthPaint;


    public HealthBar(Context context, Player player)
    {
        this.player=player;
        this.width=120;
        this.height=30;
        this.margin=5;

        this.borderPaint=new Paint();
        int borderColor= ContextCompat.getColor(context, R.color.healthborder);
        borderPaint.setColor(borderColor);

        this.healthPaint=new Paint();
        int healthColor= ContextCompat.getColor(context, R.color.healthbarhealth);
        healthPaint.setColor(healthColor);
    }
    public void draw(Canvas canvas)
    {
        float X= (float) player.getPositionX();
        float Y= (float) player.getPositionY();
        float distanceToPlayer=30;
        float healthPointsPercentage=(float) player.getHealthPoints()/player.MAX_HEALTH_POINTS;


        //draw boarder
        float borderLeft,borderTop,borderRight,borderBottom;
        borderLeft=X- width/2;
        borderRight=X+ width/2;
        borderBottom=Y-distanceToPlayer;
        borderTop=borderBottom-height;
        canvas.drawRect(borderLeft,borderTop,borderRight,borderBottom,borderPaint);

        //draw health
        float healthLeft,healthTop,healthRight,healthBottom,healthWidth,healthHeight;
        healthWidth=width-2*margin;
        healthHeight=height-2*margin;

        healthLeft=borderLeft+margin;
        healthRight=healthLeft+healthWidth*healthPointsPercentage;
        healthBottom=borderBottom-margin;
        healthTop=healthBottom-healthHeight;

        canvas.drawRect(healthLeft,healthTop,healthRight,healthBottom,healthPaint);
    }
}
