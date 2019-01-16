package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ricarad.app.dailyanswer.R;

public class SettingShowVersionActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout setting_show_version;
    private ImageView setting_show_version_back;
    private ImageView logoImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_show_version);
        setting_show_version = findViewById(R.id.setting_version_background);
        setting_show_version_back = findViewById(R.id.setting_show_version_back_iv);
        logoImg = findViewById(R.id.setting_show_version_logo);
        setting_show_version_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        finish();
    }
}
