package com.example.catsandmazes;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.graphics.Bitmap;
import android.content.Context;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class GameView extends View {
    // hardcoded mazes
    int[][] maze0 = {
            {1,1,1,0,0,0,1,0,1,1},
            {0,1,0,1,1,1,1,1,1,0},
            {1,1,2,1,0,1,0,0,1,0},
            {0,1,0,1,0,1,1,1,1,1},
            {0,1,1,1,0,0,1,0,0,1},
            {0,0,1,0,1,0,0,0,0,1},
            {1,1,1,1,1,0,3,0,1,1},
            {0,1,0,0,1,0,1,0,0,1},
            {1,1,1,0,1,0,1,1,1,1},
            {0,0,1,1,1,1,0,0,0,0}
    };

    int[][] maze1 = {
            {2, 1, 1},
            {1, 0, 1},
            {0, 0, 3}
    };

    // Bitmaps
    Bitmap background, cat, pathBitmap, lightningBitmap, arrowLeft, arrowRight, arrowDown, arrowUp;

    // Coordinates
    int catX, catY;

    // reference rectangles
    Rect rectBackground, rectCat, rectPath, rectLightning, rectLeft, rectRight, rectDown, rectUp;
    Context context;
    Handler handler;
    final long UPDATE_MILLS = 30;
    // font vars
    float TEXT_SIZE = 120;
    int energy = 19;
    // device dimentions
    static int dWidth, dHeight;
    // array to store path instances
    ArrayList<Path> paths;
    Runnable runnable;
    // paints
    Paint textPaint = new Paint();
    Paint backgroundPaint = new Paint();


    public GameView(Context context) {
        super(context);
        this.context = context;

        // initialize Bitmaps
        cat = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sprite);
        pathBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grass_sprite_3d);
        lightningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lightning_charge_fill);
        arrowDown = BitmapFactory.decodeResource(getResources(), R.drawable.play_fill_down);
        arrowUp = BitmapFactory.decodeResource(getResources(), R.drawable.play_fill_up);
        arrowLeft = BitmapFactory.decodeResource(getResources(), R.drawable.play_fill_left);
        arrowRight = BitmapFactory.decodeResource(getResources(), R.drawable.play_fill_right);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // Device width and height
        dWidth = size.x;
        dHeight = size.y;
        Log.d("debug", dWidth + " " + dHeight);
        // initialize reference rectangle
        rectBackground = new Rect(0,0, dWidth, dHeight);
        rectCat = new Rect(0,0, dWidth/4, dWidth*2363/(4*1796));
        rectPath = new Rect(0,200, dWidth/4, 200+dWidth*235/1568);
        rectLightning = new Rect(0, 20, dWidth/9,20+dWidth/9);
        rectDown = new Rect(dWidth-dWidth*2/9, dHeight-dWidth/9, dWidth-dWidth/9,dHeight);
        rectUp = new Rect(dWidth-dWidth*2/9, dHeight-dWidth*3/9, dWidth-dWidth/9,dHeight-dWidth*2/9);
        rectLeft = new Rect(dWidth-dWidth*3/9, dHeight-dWidth*2/9, dWidth-dWidth*2/9,dHeight-dWidth/9);
        rectRight = new Rect(dWidth-dWidth/9, dHeight-dWidth*2/9, dWidth,dHeight-dWidth/9);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                invalidate();
            }
        };
        // initialize paints
        textPaint.setColor(Color.rgb(255,255,255));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.caveatbrushregular));
        backgroundPaint.setColor(Color.rgb(111, 196, 252));

        // initialize coordinates
        catX = dWidth / 2 - cat.getWidth() /2;
        catY = dHeight/2 - cat.getHeight() / 2;
        paths = new ArrayList<>();
        // initialise number of paths
        int previousX = 0;
        int pathX = 0;
        int pathY = dHeight/5;
        for (int i = 0, l = maze0.length; i < l; i++) {
            for (int j = 0, w = maze0[i].length; j < w; j++ ) {
                if (j == 0){
                    previousX = pathX;
                }
                if (maze0[i][j] > 0) {
                    paths.add(new Path(context, pathBitmap, pathX, pathY, dWidth));
                    // if it is the start of the maze
                    // set cat's coordinates
                    if (maze0[i][j] == 2) {
                        catX = pathX - 20;
                        catY = pathY - dWidth*2363/(5*1796);
                        rectCat = new Rect(catX,catY, catX+dWidth/4, catY+dWidth*2363/(4*1796));
                    }
                }
                pathX += dWidth/4;
            }
            pathX = previousX - dWidth/12;
            pathY += dWidth*27/224;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw background
        canvas.drawRect(0,0,dWidth, dHeight, backgroundPaint);
        // print the paths according to the maze
        for (Path p : paths) {
            canvas.drawBitmap(pathBitmap, null, p.rectPath, null);
        }
        // draw cat
        canvas.drawBitmap(cat, null, rectCat, null);
        //draw energy
        canvas.drawBitmap(lightningBitmap, null, rectLightning, null);
        canvas.drawText("    " + energy, 20, TEXT_SIZE, textPaint);
        // draw controls
        canvas.drawBitmap(arrowDown, null, rectDown, null);
        canvas.drawBitmap(arrowUp, null, rectUp, null);
        canvas.drawBitmap(arrowLeft, null, rectLeft, null);
        canvas.drawBitmap(arrowRight, null, rectRight, null);
        handler.postDelayed(runnable, UPDATE_MILLS);
    }
}
