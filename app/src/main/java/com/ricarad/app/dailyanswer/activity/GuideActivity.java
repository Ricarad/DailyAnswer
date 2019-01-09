package com.ricarad.app.dailyanswer.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.fragment.AnswerFragment;
import com.ricarad.app.dailyanswer.fragment.DiscussFragment;
import com.ricarad.app.dailyanswer.fragment.SettingFragment;

public class GuideActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private AnswerFragment answerFragment;
    private DiscussFragment discussFragment;
    private SettingFragment settingFragment;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mTextMessage = (TextView) findViewById(R.id.message);

    }

    public void initFragment(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        answerFragment = new AnswerFragment();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_answer:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_discuss:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_setting:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

}
