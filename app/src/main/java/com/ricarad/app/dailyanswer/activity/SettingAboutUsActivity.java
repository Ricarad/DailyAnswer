package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ricarad.app.dailyanswer.R;

public class SettingAboutUsActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView setting_my_settings_return;
    private LinearLayout setting_about_us_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about_us);
        setting_my_settings_return = findViewById(R.id.setting_my_settings_return);
        setting_my_settings_return.setOnClickListener(this);
        setting_about_us_background = findViewById(R.id.setting_show_about_us_background);
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
}
