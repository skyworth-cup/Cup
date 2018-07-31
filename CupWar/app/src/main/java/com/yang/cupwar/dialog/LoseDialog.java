package com.yang.cupwar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.yang.cupwar.R;

public class LoseDialog extends Dialog{

    private ImageView text1;
    private ImageView text2;
    private ImageView text3;
    private ImageView text4;
    private Button replay;
    private Button forgive;
    private int width;
    private int height;
    public LoseDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_lose);
        initView();
    }

    public LoseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_lose);
        initView();
    }

    protected LoseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.dialog_lose);
        initView();
    }
    private void initView(){
        text1 = findViewById(R.id.text_1);
        text2 = findViewById(R.id.text_2);
        text3 = findViewById(R.id.text_3);
        text4 = findViewById(R.id.text_4);
        replay = findViewById(R.id.btn_again);
        forgive = findViewById(R.id.btn_forgive);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重玩
            }
        });
        forgive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 取消
            }
        });
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.6);
        p.height= (int) (size.y*0.7);
        getWindow().setAttributes(p);
        width = p.width;
        height = p.height;
        initAnimate();
    }

    // 弹幕效果
    // TODO: 2018/7/29 测试
    private void initAnimate(){
        Animation  animate1 = new TranslateAnimation(width+text1.getWidth()+10, -1000, text1.getY(),text1.getY());
        Animation  animate2 = new TranslateAnimation(width+text2.getWidth()+10, -1200, text2.getY(),text2.getY());
        Animation  animate3 = new TranslateAnimation(width+text3.getWidth()+10, -1500, text3.getY(),text3.getY());
        Animation  animate4 = new TranslateAnimation(width+text4.getWidth()+10, -1000, text4.getY(),text4.getY());
        animate1.setDuration(5000);
        animate2.setDuration(5000);
        animate3.setDuration(5000);
        animate4.setDuration(5000);
        animate1.setFillBefore(true);
        animate2.setFillBefore(true);
        animate3.setFillBefore(true);
        animate4.setFillBefore(true);
        animate1.setRepeatCount(Animation.INFINITE);
        animate2.setRepeatCount(Animation.INFINITE);
        animate3.setRepeatCount(Animation.INFINITE);
        animate4.setRepeatCount(Animation.INFINITE);
        animate1.setInterpolator(new LinearInterpolator());
        animate2.setInterpolator(new LinearInterpolator());
        animate3.setInterpolator(new LinearInterpolator());
        animate4.setInterpolator(new LinearInterpolator());
        text1.startAnimation(animate1);
        text2.startAnimation(animate2);
        text3.startAnimation(animate3);
        text4.startAnimation(animate4);
    }
}
