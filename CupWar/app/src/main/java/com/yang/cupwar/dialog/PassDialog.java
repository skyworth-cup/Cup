package com.yang.cupwar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.cupwar.R;
import com.yang.cupwar.utils.StarUtil;

public class PassDialog extends Dialog {
    private Button replay;
    private Button next;
    private ImageView leaf_1;
    private ImageView leaf_2;
    private ImageView leaf_3;
    private TextView level;
    private TextView time;
    private TextView point;
    private PassControlListener mListener;
    public PassDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_pass);
        initView();
    }

    public PassDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_pass);
        initView();
    }

    protected PassDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.dialog_pass);
        initView();
    }

    private void initView(){
        replay = findViewById(R.id.btn_replay);
        next = findViewById(R.id.btn_next_level);
        level = findViewById(R.id.dialog_level);
        time = findViewById(R.id.dialog_time);
        point = findViewById(R.id.dialog_point);
        leaf_1 = findViewById(R.id.leave1);
        leaf_2 = findViewById(R.id.leave2);
        leaf_3 = findViewById(R.id.leave3);
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onReplay();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNext();
            }
        });
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width=(int)(size.x*0.5);
        p.height= (int) (size.y*0.4);
        getWindow().setAttributes(p);
    }

    public void setData(int l, float t ,float p){
        // TODO: 2018/7/27 更新数据库数据
        level.setText(""+l);
        time.setText(""+t);
        point.setText(""+p);
        if (p > 64){
            leaf_1.setVisibility(View.VISIBLE);
            leaf_2.setVisibility(View.VISIBLE);
            leaf_3.setVisibility(View.VISIBLE);
        }
        else if (p > 32){
            leaf_1.setVisibility(View.VISIBLE);
            leaf_2.setVisibility(View.VISIBLE);
        }
        else {
            leaf_2.setVisibility(View.VISIBLE);
        }
    }

    public void setControlListener(PassControlListener listener){
        mListener = listener;
    }

    public interface PassControlListener{
        void onReplay();
        void onNext();
    }

}
