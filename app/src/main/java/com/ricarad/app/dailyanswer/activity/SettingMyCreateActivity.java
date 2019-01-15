package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.CreationAdapter;
import com.ricarad.app.dailyanswer.model.Mycollection;
import com.ricarad.app.dailyanswer.model.User;

import java.util.ArrayList;
import java.util.List;

public class SettingMyCreateActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView setting_my_create_lv;
    private ImageView setting_my_create_return;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting_my_create);
        setContentView(R.layout.activity_setting_my_create);
        setting_my_create_return = findViewById(R.id.setting_my_create_return);
        setting_my_create_return.setOnClickListener(this);
        setting_my_create_lv = findViewById(R.id.setting_my_create_lv);
        Mycollection n1 = new Mycollection(R.drawable.logo,"我不会","文档根本看不懂");
        List<Mycollection> data = new ArrayList<Mycollection>();
        data.add(n1);
        CreationAdapter adapter = new CreationAdapter(this,data);
        setting_my_create_lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_my_create_return:{
                finish();
            }  break;
        }
    }


}
