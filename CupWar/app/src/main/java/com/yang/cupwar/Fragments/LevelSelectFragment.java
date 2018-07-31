package com.yang.cupwar.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yang.cupwar.R;
import com.yang.cupwar.activity.GameActivity;
import com.yang.cupwar.activity.LevelSelectActivity;
import com.yang.cupwar.utils.StarUtil;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

public class LevelSelectFragment extends Fragment {
    private int pageID;  //页数
    private RecyclerView mView;
    private levelAdapter mAdapter;
    private int []level_number = new int[9];
    private Context mContext;
    private static final StarUtil db = new StarUtil();
    private SharedPreferences sp;

    public static LevelSelectFragment newInstance(int id){
        LevelSelectFragment fragment = new LevelSelectFragment();
        fragment.pageID = id;
        return fragment;
    }
    public LevelSelectFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_select, container, false);
        for (int i=0; i < 9;i++){
            level_number[i] = i+1+(pageID-1)*9;
        }
        mContext = getContext();
        mView = view.findViewById(R.id.level_recyclerview);
        mAdapter = new levelAdapter();
        mView.setAdapter(mAdapter);
        mView.setLayoutManager(new GridLayoutManager(mContext, 3));
        sp = mContext.getSharedPreferences("normal", Context.MODE_PRIVATE);
        // 第一次开始游戏初始化第一关
        if (db.getStars(sp, 1) == -1){
            db.updateOne(sp, 1, 0);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    class levelAdapter extends RecyclerView.Adapter<levelViewHolder>{
        @NonNull
        @Override
        public levelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.level_card, parent, false);
            v.getLayoutParams().height = mView.getHeight()/3;
            v.getLayoutParams().width = mView.getWidth()/3;
            return new levelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull levelViewHolder levelViewHolder, int i) {
            levelViewHolder.bindView(i);
        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }
    class levelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int level_leaf = 0;
        private int level_label;
        private boolean isLocked = true;
        private ImageView level_number;
        private ImageView level_background;
        public levelViewHolder(@NonNull View itemView) {
            super(itemView);
            level_background = itemView.findViewById(R.id.level_background);
            level_number = itemView.findViewById(R.id.level_number);
            itemView.setOnClickListener(this);
        }

        public void bindView(int number){
            level_label = number + 1 + (pageID-1)*9;
            level_leaf = db.getStars(sp, level_label);
            if (level_leaf != -1){
                isLocked = false;
            }
            try{
                InputStream is_number ;
                InputStream is_background;
                if (!isLocked) {
                    is_number = getContext().getAssets().open("level_" + level_label + ".png");
                    is_background = getContext().getAssets().open("level_background_"+level_leaf+".png");
                }
                else{
                    is_background = getContext().getAssets().open("level_locked.png");
                    is_number = null;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(is_background);
                level_background.setImageBitmap(bitmap);
                if (is_number != null) {
                    bitmap = BitmapFactory.decodeStream(is_number);
                    level_number.setImageBitmap(bitmap);
                }
            }catch (Exception ex){

            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), GameActivity.class);
            intent.putExtra(GameActivity.GAME_LEVEL, level_label);
            intent.putExtra(GameActivity.GAME_TYPE, LevelSelectActivity.NORMAL_LEVEL_SELECT);
            startActivity(intent);
        }
    }


}
