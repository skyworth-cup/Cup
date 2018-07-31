package com.yang.cupwar.models;


import android.content.Context;
import android.graphics.Color;

public class Level {
    private int levelId;
    private Water mWater;
    private WarningLine mLine;
    private Timer mTimer;
    private int ScreenH;
    private int ScreenW;

    public Water getWater() {
        return mWater;
    }

    public WarningLine getLine() {
        return mLine;
    }

    public Timer getTimer() {
        return mTimer;
    }

    public Level(Context context, int id, int screenW, int screenH){
        levelId = id;
        mWater = new Water(screenW, screenH);
        mTimer = new Timer(context, screenW, screenH);
        mLine = new WarningLine(context, screenW, screenH);
        ScreenW = screenW;
        ScreenH = screenH;
        setLevelInfor();
    }

    private void setLevelInfor(){
        // 关卡参数
        switch (levelId){
            case 1:
                mLine.setBaseLine(ScreenH/3.2f + 60);
                mLine.setWidth(100);
                break;
            case 2:
                mLine.setBaseLine(ScreenH/3.2f + 100);
                mLine.setWidth(50);
             //   mWater.setColor(Color.argb(255,251,234,107), Color.argb(200,255,255,0));
                break;
            case 3:
                mLine.setBaseLine(ScreenH/3.2f+200);
                mLine.setWidth(25);
                break;
        }
    }

}
