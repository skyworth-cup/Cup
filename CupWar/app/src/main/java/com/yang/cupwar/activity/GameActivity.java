package com.yang.cupwar.activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yang.cupwar.GameView;
import com.yang.cupwar.R;
import com.yang.cupwar.dialog.LoseDialog;
import com.yang.cupwar.dialog.PassDialog;
import com.yang.cupwar.utils.StarUtil;

public class GameActivity extends BaseActivity {
    public static final String GAME_LEVEL = "game_level";
    public static final String GAME_TYPE = "type";
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final String GAME_POIT = "point";
    public static final String GAME_TIME = "time";
    private int levelId;
    private int modeType;
    private GameView view;
    private static final StarUtil db = new StarUtil();
    private SharedPreferences sp;
    private MediaPlayer bgm;

    private Handler gameFinishHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS){
                // 成功弹框
                PassDialog dialog = new PassDialog(GameActivity.this, R.style.SuccessDialog);
                Bundle bundle = msg.getData();
                float point = bundle.getFloat(GAME_POIT);
                if (point > 64  && db.getStars(sp, levelId) < 3){
                    db.updateOne(sp, levelId, 3);
                }
                else if (point > 32 && db.getStars(sp, levelId) < 2){
                    db.updateOne(sp, levelId, 2);
                }
                else if (db.getStars(sp, levelId) < 1){
                    db.updateOne(sp, levelId, 1);
                }
                unlock();
                dialog.setData(levelId, bundle.getFloat(GAME_TIME), bundle.getFloat(GAME_POIT));
                dialog.show();
            }
            else if (msg.what == FAIL){
                LoseDialog dialog = new LoseDialog(GameActivity.this, R.style.SuccessDialog);
                dialog.show();
            }
            else {

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
        view = new GameView(this, levelId, modeType);
        setContentView(view);
        // 界面获取焦点 for 点击事件
        view.setFocusable(true);
        view.requestFocus();
        view.setOnGameFinishListener(new GameView.GameFinishListener() {
            @Override
            public void onFinish(Message message) {
                gameFinishHandler.sendMessage(message);
            }
        });
        sp = getSharedPreferences("normal", MODE_PRIVATE);
        bgm = MediaPlayer.create(this, R.raw.gameview_bgm);
        bgm.setLooping(true);
        bgm.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("game", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgm.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("game", "onResume");

    }

    private void unlock(){
        if (db.getStars(sp,levelId+1) == -1 ) {
            db.updateOne(sp, levelId + 1, 0);
        }
    }
}
