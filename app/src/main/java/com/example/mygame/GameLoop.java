package com.example.mygame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.core.content.ContextCompat;

public class GameLoop extends Thread {
    public static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private boolean isRunning=false;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private double averageUPS;
    private double averageFPS;



    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game=game;
        this.surfaceHolder=surfaceHolder;

    }

    public void startLoop() {
        isRunning=true;
        start();
    }

    @Override
    public void run() {
        super.run();


        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapseTime;
        long sleepTime;

        //GameLoop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();

        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;

                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //pause the game loop to not exceed target ups
                elapseTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapseTime);
                if (sleepTime > 0) {
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Skip frames to keep up with target UPS
                while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                    game.update();
                    updateCount++;
                    elapseTime = System.currentTimeMillis() - startTime;
                    sleepTime = (long) (updateCount * UPS_PERIOD - elapseTime);
                }
                //calculate average UPS and FPS
                elapseTime = System.currentTimeMillis() - startTime;
                if (elapseTime >= 1000) {
                    averageUPS = updateCount / (1E-3 * elapseTime);
                    averageFPS = frameCount / (1E-3 * elapseTime);
                    updateCount = 0;
                    frameCount = 0;
                    startTime = System.currentTimeMillis();
                }
            }
        }
    }

    public double getAvregeFPS() {
        return averageFPS;
    }

    public double getAvregeUPS() {
        return averageUPS;
    }
}
