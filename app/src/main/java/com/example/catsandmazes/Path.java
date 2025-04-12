package com.example.catsandmazes;

import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Path {
    Bitmap path;
    int pathX, pathY;
    Rect rectPath;

    public Path(Context context, Bitmap path, int pathX, int pathY, int dWidth) {
        this.path = path;
        this.pathX = pathX;
        this.pathY = pathY;
        rectPath = new Rect(pathX, pathY, pathX+dWidth/4, pathY+dWidth*235/1568);;
        Log.d("debug","Path crated with " + pathX + "x" + pathY);
    }
}
