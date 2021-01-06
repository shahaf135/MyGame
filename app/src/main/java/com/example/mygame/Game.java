package com.example.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.example.mygame.Circle;
import com.example.mygame.Enemy;
import com.example.mygame.GameLoop;
import com.example.mygame.Joystick;
import com.example.mygame.Player;
import com.example.mygame.R;
import com.example.mygame.Spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;
    private Context context;
    private List<Enemy> enemyList=new ArrayList<Enemy>();
    private List<Spell> spellList=new ArrayList<Spell>();
    private int joystickPointerId=0;
    private int numberOfSpellsToCast=0;


    public Game(Context context) {
        super(context);


        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        this.context=context;


        joystick = new Joystick(275, 900, 100, 55);
        player= new Player(getContext(),joystick,500,500,30);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }



    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        drawUPS(canvas);
        drawFPS(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for (Enemy enemy:enemyList) {
            enemy.draw(canvas);
        }
        for (Spell spell: spellList)
        {
            spell.draw(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked())
        {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                if (joystick.getIsPressed())
                {
                    //joystick was pressed before the event->cast spell
                    numberOfSpellsToCast++;
                }

                else if (joystick.isPressed ((double) event.getX(),(double)event.getY())){
                    //joystick is pressed in this event
                    joystickPointerId=event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }
                else {
                    //joystick is not pressed and was not pressed before -> cast spell
                    numberOfSpellsToCast++;
                }
                return true;


            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()){
                    joystick.SetActuator((double) event.getX(),(double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                if (joystickPointerId==event.getPointerId(event.getActionIndex()))
                {
                    //joystick was let go of
                    joystick.setIsPressed(false);
                    joystick.restartActuator();

                }
                return true;

        }


        return super.onTouchEvent(event);
    }

    private void drawFPS(Canvas canvas) {
        String avreageFPS=Integer.toString((int) gameLoop.getAvregeFPS());
        Paint paint=new Paint();
        int color= ContextCompat.getColor(context, R.color.white);
        paint.setColor(color);
        paint.setTextSize(40);
        canvas.drawText("FPS"+ avreageFPS,100,70,paint);
    }

    private void drawUPS(Canvas canvas) {
        String avreageUPS=Integer.toString((int) gameLoop.getAvregeUPS());
        Paint paint=new Paint();
        int color= ContextCompat.getColor(context,R.color.white);
        paint.setColor(color);
        paint.setTextSize(40);
        canvas.drawText("UPS"+avreageUPS,100,220,paint);
    }

    public void update() {
        joystick.update();
        player.update();

        //spawn enemy if able to
        if (Enemy.readyToSpawn())
        {
            enemyList.add(new Enemy(getContext(),player));
        }

        while (numberOfSpellsToCast>0)
        {
            spellList.add(new Spell(getContext(),player));
            numberOfSpellsToCast--;
        }

        //update state of enemies
        for (Enemy enemy: enemyList)
        {
            enemy.update();
        }
        //update state of spells
        for (Spell spell: spellList)
        {
            spell.update();
        }

        //checks collision for each enemy in the enemy list checks collision between enemies and player and enemies and spells
        Iterator<Enemy> iteratorEnemy= enemyList.iterator();
        while (iteratorEnemy.hasNext()){
            Circle enemy=iteratorEnemy.next();
            if (Circle.isColliding(enemy,player))
            {
                //remove collided enemies
                iteratorEnemy.remove();

                player.setHealthPoint(player.getHealthPoints()-1);
                continue;
            }
            Iterator<Spell> iteratorSpell=spellList.iterator();
            while (iteratorSpell.hasNext())
            {
                Circle spell= iteratorSpell.next();
                //remove speel if colliding with enemy
                if (Circle.isColliding(spell,enemy)){
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    break;
                }
            }
        }

    }
}
