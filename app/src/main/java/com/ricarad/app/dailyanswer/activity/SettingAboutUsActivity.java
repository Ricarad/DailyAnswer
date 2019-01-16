package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ricarad.app.dailyanswer.R;

public class SettingAboutUsActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView setting_my_settings_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about_us);
        setting_my_settings_return = findViewById(R.id.setting_my_settings_return);
        setting_my_settings_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.setting_my_settings_return:{
                finish();
            }
        }
    }
}
