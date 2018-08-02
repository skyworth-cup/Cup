package com.yang.cupwar.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yang.cupwar.GameView;
import com.yang.cupwar.R;
import com.yang.cupwar.dialog.LoseDialog;
import com.yang.cupwar.dialog.PassDialog;
import com.yang.cupwar.utils.StarUtil;

import static android.os.SystemClock.sleep;

public class GameActivity extends BaseActivity {
    public static final String GAME_LEVEL = "game_level";
    public static final String GAME_TYPE = "type";

    public static final String GAME_POIT = "point";
    public static final String GAME_TIME = "time";
    //
    private PassDialog passDialog;
    private LoseDialog loseDialog;
    // 游戏信息
    private int levelId;
    private int modeType;
    // 数据存储
    private static final StarUtil db = new StarUtil();
    private SharedPreferences sp;
    // 背景音乐
    private MediaPlayer bgm;
    // 界面
    private FrameLayout viewGroup;
    private GameView view;
    private ImageView gif;
    // 消息常量
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    private static final int MESSAGE_PASS_REPLAY = 3;
    private static final int MESSAGE_LOSE_REPLAY = 4;
    private static final int MESSAGE_NEXT = 5;
    private static final int MESSAGE_FORGIVE = 6;
    private static final int MESSAGE_FULL = 7;
    private Handler gameFinishHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    // 成功弹框
                    sleep(1000);
                    passDialog= new PassDialog(GameActivity.this, R.style.SuccessDialog);
                    // 按钮设置
                    passDialog.setControlListener(new PassDialog.PassControlListener() {
                        @Override
                        public void onReplay() {
                            Message message = new Message();
                            message.what = MESSAGE_PASS_REPLAY;
                            gameFinishHandler.sendMessage(message);
                        }

                        @Override
                        public void onNext() {
                            Message message = new Message();
                            message.what = MESSAGE_NEXT;
                            gameFinishHandler.sendMessage(message);
                        }
                    });
                    Bundle bundle = msg.getData();
                    float point = bundle.getFloat(GAME_POIT);
                    if (point > 64 && db.getStars(sp, levelId) < 3) {
                        db.updateOne(sp, levelId, 3);
                    } else if (point > 32 && db.getStars(sp, levelId) < 2) {
                        db.updateOne(sp, levelId, 2);
                    } else if (db.getStars(sp, levelId) < 1) {
                        db.updateOne(sp, levelId, 1);
                    }
                    unlock();
                    passDialog.setData(levelId, bundle.getFloat(GAME_TIME), bundle.getFloat(GAME_POIT));
                    passDialog.show();
                    break;
                case FAIL:
                    sleep(1000);
                    loseDialog = new LoseDialog(GameActivity.this, R.style.SuccessDialog);
                    loseDialog.setControlListener(new LoseDialog.LoseControlListener() {
                        @Override
                        public void onReplay() {
                            Message message = new Message();
                            message.what = MESSAGE_LOSE_REPLAY;
                            gameFinishHandler.sendMessage(message);
                        }

                        @Override
                        public void onForigve() {
                            Message message = new Message();
                            message.what = MESSAGE_FORGIVE;
                            gameFinishHandler.sendMessage(message);
                        }
                    });
                    loseDialog.show();
                    break;
                case MESSAGE_PASS_REPLAY:
                    Intent pass_intent = getIntent();
                    finish();
                    startActivity(pass_intent);
                    break;
                case MESSAGE_LOSE_REPLAY:
                    Intent lose_intent = getIntent();
                    finish();
                    startActivity(lose_intent);
                    break;
                case MESSAGE_NEXT:
                    Intent next_intent = getIntent();
                    next_intent.putExtra(GAME_LEVEL, levelId+1);
                    finish();
                    startActivity(next_intent);
                    break;
                case MESSAGE_FORGIVE:
                    finish();
                    break;
                case MESSAGE_FULL:
                    FrameLayout.LayoutParams value = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    viewGroup.addView(gif,value);
                    AnimationDrawable ad = (AnimationDrawable) gif.getDrawable();
                    ad.start();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取关卡ID 和 模式
        levelId = getIntent().getIntExtra(GAME_LEVEL, 0);
        // 0：传递失败 1：普通 2：挑战模式
        modeType = getIntent().getIntExtra(GAME_TYPE, 0);
        // 界面设置
        initGameView();
        // 界面获取焦点 for 点击事件
        view.setFocusable(true);
        view.requestFocus();
        view.setOnGameFinishListener(new GameView.GameListener() {
            @Override
            public void onFinish(Message message) {
                gameFinishHandler.sendMessage(message);
            }

            @Override
            public void onFull() {
                Message message = new Message();
                message.what = MESSAGE_FULL;
                gameFinishHandler.sendMessage(message);
            }
        });
        sp = getSharedPreferences("normal", MODE_PRIVATE);
        bgm = MediaPlayer.create(this, R.raw.gameview_bgm);
        bgm.setLooping(true);
        bgm.start();
    }

    private void initGameView(){
        // 回收
        gif = new ImageView(this);
        gif.setImageResource(R.drawable.animate_flow);
        view = new GameView(this, levelId, modeType);
        viewGroup = new FrameLayout(this);
        FrameLayout.LayoutParams value = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(view, value);
        setContentView(viewGroup);

    }

    private void unlock(){
        if (db.getStars(sp,levelId+1) == -1 ) {
            db.updateOne(sp, levelId + 1, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnimationDrawable ad = (AnimationDrawable) gif.getDrawable();
        ad.stop();
        bgm.release();
    }
}
