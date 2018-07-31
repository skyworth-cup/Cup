package com.yang.cupwar.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.InputStream;

public class WarningLine {
    private float width;
    private float baseLine;
    private Bitmap line;  //中线
    private Paint mPaint;
    private int ScreenW;
    private int ScreenH;

    public void setWidth(float width) {
        this.width = width;
    }

    public void setBaseLine(float baseLine) {
        this.baseLine = baseLine;
    }

    private int blink = 0; //闪烁
    public WarningLine(Context context, int screenW, int screenH){
        mPaint = new Paint();
        mPaint.setColor(Color.argb(209, 246, 225, 111));
        try {
            InputStream is = context.getAssets().open("line.png");
            line = BitmapFactory.decodeStream(is);
        }catch (Exception ex){
        }
        ScreenW = screenW;
        ScreenH = screenH;
    }


    public void draw(Canvas canvas){
        if (blink%8 < 5 && blink <= 60) {
            canvas.drawRect(0, baseLine - width / 2, ScreenW, baseLine + width / 2, mPaint);
            RectF baseRect = new RectF(0, baseLine, ScreenW, line.getHeight());
            canvas.drawBitmap(line, null, baseRect, mPaint);
        }
        blink++;
    }

    public float getWidth() {
        return width;
    }

    public float getBaseLine() {
        return baseLine;
    }
}
