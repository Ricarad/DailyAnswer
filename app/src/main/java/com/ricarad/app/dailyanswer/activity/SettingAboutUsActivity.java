package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ricarad.app.dailyanswer.R;

public class SettingAboutUsActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{
    private ImageView setting_my_settings_return;
    private LinearLayout setting_about_us_background;
    private static final int SCROLL_MIN_DISTANCE = 80;// 移动最小距离
    private GestureDetector mygesture;//手势探测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about_us);
        setting_my_settings_return = findViewById(R.id.setting_my_settings_return);
        setting_my_settings_return.setOnClickListener(this);
        setting_about_us_background = findViewById(R.id.setting_show_about_us_background);
        mygesture = new GestureDetector(this,myGestureListener);
      //  init();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.setting_my_settings_return:{
                finish();
            }
        }
    }
    private void init(){
        Animation anim = new AlphaAnimation(0.5f,1f);
        anim.setDuration(3000);
        anim.setRepeatCount(50);
        setting_about_us_background.startAnimation(anim);

    }

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
}
