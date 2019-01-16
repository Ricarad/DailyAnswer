package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.User;

public class SettingMyPostActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView setting_my_create_lv;
    private ImageView setting_my_create_return;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_my_post);
        setting_my_create_return = findViewById(R.id.setting_my_post_back);
        setting_my_create_return.setOnClickListener(this);
        setting_my_create_lv = findViewById(R.id.setting_my_post_lv);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_my_post_back:{
                finish();
            }  break;
        }
    }


}
