package com.yang.cupwar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yang.cupwar.R;

public class StartActivity extends BaseActivity {

    private Button normalLevel;
    private Button challengeLevel;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        normalLevel = findViewById(R.id.btn_normal);
        challengeLevel = findViewById(R.id.btn_challenge);
        settings = findViewById(R.id.btn_setting);

        //Button自适应大小&位置
        //KZ
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams nip;
        nip=normalLevel.getLayoutParams();
        ViewGroup.LayoutParams cip;
        cip=challengeLevel.getLayoutParams();
        normalLevel.setTranslationY(height/35);
        nip.height=height/6;
        nip.width=width*2/9;
        challengeLevel.setTranslationY(height/35);
        cip.height=height/6;
        cip.width=width*2/9;


        normalLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, LevelSelectActivity.class);
                intent.putExtra(LevelSelectActivity.MODE_TYPE, LevelSelectActivity.NORMAL_LEVEL_SELECT);
                startActivity(intent);
            }
        });

        challengeLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, LevelSelectActivity.class);
                intent.putExtra(LevelSelectActivity.MODE_TYPE, LevelSelectActivity.CHALLENGE_LEVEL_SELECT);
                startActivity(intent);
            }
        });

        //创建sharedpreference

    }


}
