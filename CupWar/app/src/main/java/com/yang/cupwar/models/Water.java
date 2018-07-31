package com.yang.cupwar.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Water {
    private float speed = 0.5f; // 上升速度
    private int ScreenW;
    private int ScreenH;


    private boolean isStop = false;

    // 波浪设置

    private int wave_width;
    private float baseline;
    private int wave_height = 30;
    private float first_offset = 0f;
    private float second_offset = 0f;
    private Paint first_paint;
    private Paint second_paint;

    public Water(int width, int height){
        ScreenW = width;
        ScreenH = height;
        wave_width = (int) (ScreenW/1.5);
        baseline = ScreenH*9/10;
        initPaint();
    }

    // 波浪绘制

    public void draw(Canvas canvas){
        Path path = getPath_1();
        canvas.drawPath(path, first_paint);
        path = getPath_2();
        canvas.drawPath(path, second_paint);
        wave_move();
    }
    private void initPaint(){
        first_paint = new Paint();
        first_paint.setColor(Color.argb(218,159, 243, 249));
        second_paint = new Paint();
        second_paint.setColor(Color.argb(218,116, 231, 230));
    }

    private Path getPath_1(){
        int itemWidth = wave_width/2;
        Path mPath = new Path();
        mPath.moveTo(-itemWidth*3, baseline);
        for(int i=-3;i<2;i++){
            int startx = i*itemWidth;
            mPath.quadTo(startx+itemWidth/2+first_offset,
                    getWaveHeigh(i),
                    startx+itemWidth+first_offset,
                    baseline);
        }
        mPath.lineTo(ScreenW,ScreenH);
        mPath.lineTo(0,ScreenH);
        mPath.close();
        return mPath;
    }

    public float getBaseline() {
        return baseline;
    }

    private Path getPath_2(){

        int itemWidth = wave_width/2;
        Path mPath = new Path();
        mPath.moveTo(ScreenW+itemWidth*3, baseline);
        for(int i=3;i>-2;i--){
            int startx = ScreenW+i*itemWidth;
            mPath.quadTo(startx-itemWidth/2-second_offset,
                    getWaveHeigh(i),
                    startx-itemWidth-second_offset,
                    baseline);
        }
        mPath.lineTo(0,ScreenH);
        mPath.lineTo(ScreenW,ScreenH);
        mPath.close();
        return mPath;
    }

    private int getWaveHeigh(int num){
        if(num % 2 == 0){
            return (int) (baseline + wave_height);
        }
        return (int) (baseline - wave_height);
    }

    private void wave_move(){
        if (first_offset >= wave_width){
            first_offset = 0;
        }
        else {
            first_offset += wave_width/100;
        }
        second_offset = first_offset;
    }
    // 逻辑功能
    // TODO: 2018/7/27 加速功能完善？？ 根据屏幕宽度？？
    public void speedUp(){
        speed *=1.8;
    }

    public void rise(){
        if(baseline < ScreenH/3.025 || isStop){
            return;
        }
        else
            baseline -= speed;

    }

    public void stop(){
        isStop = true;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setBaseline(float baseline) {
        this.baseline = baseline;
    }

    public boolean getIsStop(){
        return isStop;
    }

    public void setColor(int first, int second){
        first_paint.setColor(first);
        second_paint.setColor(second);
    }
}
