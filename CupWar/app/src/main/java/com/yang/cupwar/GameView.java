package com.yang.cupwar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.yang.cupwar.activity.GameActivity;
import com.yang.cupwar.dialog.PassDialog;
import com.yang.cupwar.models.Level;
import com.yang.cupwar.models.Timer;
import com.yang.cupwar.models.WarningLine;
import com.yang.cupwar.models.Water;

import java.io.InputStream;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable, View.OnClickListener{
    private SurfaceHolder sfh;
    private Thread th;
    private int ScreenW, ScreenH;  // 屏幕长、宽
    // 关卡信息
    private int levelId;
    private int modeType;
    private Bitmap background;
    private Bitmap backColor;
    private Canvas canvas;
    private Paint backPaint;
    private Rect screenRect;

    // 游戏时间元素
    private Date begin_time;
    private Date time_1 = null;
    private Date time_2 = null;

    // 游戏主要元素
    private Level level;
    private Water water;
    private Timer timer;
    private WarningLine line;
    private int count = 0;
    private GameListener mListener;

    // 游戏结束标识
    private boolean isFinished = false;
    private boolean isDestroy = false; //view销毁时停止线程
    private boolean isFull = false;
    // 游戏声音
    private SoundPool count_down;
    private int sound_id;

    public GameView(Context context, int id, int type) {
        super(context);
        th = new Thread(this);
        sfh = this.getHolder();
        sfh.addCallback(this);
        levelId = id;
        modeType = type;
        backPaint = new Paint();
        begin_time = new Date();
        count_down = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        sound_id = count_down.load(getContext(), R.raw.count_down, 1);
        loadBackground();
    }

    private void loadBackground(){
        try {
            InputStream is;
            if (levelId == 2 ){
                is = getContext().getAssets().open("game_background_2.png");
                background = BitmapFactory.decodeStream(is);
                backColor = null;
            }
            else {

                is = getContext().getAssets().open("game_background_1_1.png");
                backColor = BitmapFactory.decodeStream(is);
                is = getContext().getAssets().open("game_background_1_2.png");
                background = BitmapFactory.decodeStream(is);
            }
        }catch (Exception ex){

        }
    }

    private void draw(){
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.WHITE);
            if (backColor != null) {
                canvas.drawBitmap(backColor, null, screenRect, backPaint);
            }
            water.draw(canvas);
            line.draw(canvas);
            canvas.drawBitmap(background, null, screenRect, backPaint);
            timer.draw(canvas);
        }catch (Exception ex){

        }finally {
            if (canvas != null){
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void finish_judge(){
        if (isFinished || isDestroy)
            return;
        if (Math.abs(water.getBaseline() - line.getBaseLine()) <= line.getWidth()/2){
            // 成功：计算分数,时间

            Date stop_time = new Date();
            float time = (stop_time.getTime() - begin_time.getTime())/1000;
            float point = getScore(time);
            Message message = new Message();
            message.what = GameActivity.SUCCESS;
            Bundle bundle = new Bundle();
            bundle.putFloat(GameActivity.GAME_POIT, point);
            bundle.putFloat(GameActivity.GAME_TIME, time);
            message.setData(bundle);
            mListener.onFinish(message);
        }
        else{
            // 失败：直接弹框
            Message message = new Message();
            message.what = GameActivity.FAIL;
            mListener.onFinish(message);
        }
        isFinished = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        ScreenW = this.getWidth();
        ScreenH = this.getHeight();
        screenRect = new Rect(0, 0, ScreenW, ScreenH);
        // 关卡信息设置
        level = new Level(getContext(), levelId, ScreenW, ScreenH);
        water = level.getWater();
        line = level.getLine();
        timer = level.getTimer();

        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        count_down.release();
    }


    // 游戏线程
    @Override
    public void run() {
        while (!water.getIsStop() && !isDestroy) {
            draw(); // 绘制画面
            water.rise();
            count++;
            if (time_1 == null){
                time_1 = new Date();
            }
            if (water.getBaseline()<ScreenH/4 && !isFull){
                mListener.onFull();
                isFull = true;
            }
            time_2  = new Date();
            if (time_2.getTime() - time_1.getTime() >= 1000){

                timer.countDown();
                count_down.play(sound_id, 1,1,1,0,1);
                time_1 = time_2;
            }
            if (time_2.getTime() - begin_time.getTime() >= 10000){
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 倒计时结束进行判定
        finish_judge();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:
                water.stop();
                finish_judge();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Toast.makeText(getContext(), "下键", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Toast.makeText(getContext(), "左键", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Toast.makeText(getContext(), "右键", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                water.speedUp();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDestroy = true;
    }

    @Override
    public void onClick(View view) {
        water.stop();
        PassDialog dialog = new PassDialog(getContext(), R.style.SuccessDialog);
        dialog.show();
    }

    public void setOnGameFinishListener(GameListener listener){
        mListener = listener;
    }

    // 判定分数
    public float getScore(float time) {
        float delta = Math.abs(water.getBaseline() - line.getBaseLine());
        float point = (9-time)/7 *60 + delta/line.getWidth()*2 *40;
        return Math.round(point*100)/100;
    }

    public interface GameListener{
        void onFinish(Message message);
        void onFull();
    }
}
