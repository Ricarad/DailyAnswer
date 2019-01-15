package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.CollectionAdapter;
import com.ricarad.app.dailyanswer.fragment.SettingFragment;
import com.ricarad.app.dailyanswer.model.Mycollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SettingMyCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView setting_my_collection_lv;
    private ImageView setting_my_collection_return_iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_my_collection);
        setting_my_collection_return_iv = findViewById(R.id.setting_my_collection_return);
        setting_my_collection_lv = findViewById(R.id.setting_my_collection_lv);
        List<Mycollection> data = new ArrayList<Mycollection>();
        CollectionAdapter adapter = new CollectionAdapter(this,data);
        setting_my_collection_lv.setAdapter(adapter);
        setting_my_collection_return_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.setting_my_collection_return: {
                finish();
            }break;
        }
    }
}
