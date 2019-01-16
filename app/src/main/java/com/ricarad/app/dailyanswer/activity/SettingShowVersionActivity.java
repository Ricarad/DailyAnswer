package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ricarad.app.dailyanswer.R;

public class SettingShowVersionActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private RelativeLayout setting_show_version;
    private ImageView setting_show_version_back;
    private ImageView logoImg;

    private static final int SCROLL_MIN_DISTANCE = 80;// 移动最小距离
    private GestureDetector mygesture;//手势探测器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_show_version);
        setting_show_version = findViewById(R.id.setting_version_background);
        setting_show_version_back = findViewById(R.id.setting_show_version_back_iv);
        logoImg = findViewById(R.id.setting_show_version_logo);
        setting_show_version_back.setOnClickListener(this);
        mygesture = new GestureDetector(this,myGestureListener);
    }


    @Override
    public void onClick(View view) {
        finish();
    }

    private GestureDetector.OnGestureListener myGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SCROLL_MIN_DISTANCE) {
                finish();
            }
            return false;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mygesture.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mygesture.onTouchEvent(ev)){
            return mygesture.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
