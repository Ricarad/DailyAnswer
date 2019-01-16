package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.fragment.AnswerFragment;
import com.ricarad.app.dailyanswer.fragment.DiscussFragment;
import com.ricarad.app.dailyanswer.fragment.SettingFragment;
import com.ricarad.app.dailyanswer.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

import static com.ricarad.app.dailyanswer.common.Constant.USER;

public class GuideActivity extends AppCompatActivity {

    private AnswerFragment answerFragment;
    private DiscussFragment discussFragment;
    private SettingFragment settingFragment;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initFragment();
        Intent intent = new Intent();
        intent.putExtra(USER,user);
        setResult(RESULT_OK,intent);
    }

    public void initFragment() {

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER);
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        answerFragment = new AnswerFragment();
        answerFragment.setArguments(bundle);
        discussFragment = new DiscussFragment();
        discussFragment.setArguments(bundle);
        settingFragment = new SettingFragment();
        settingFragment.setArguments(bundle);
        fragments = new Fragment[]{answerFragment, discussFragment, settingFragment};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.guide_mainview, answerFragment)
                .show(answerFragment).commit();

    }

    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.guide_mainview, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_answer: {
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    return true;
                }
                case R.id.navigation_discuss: {
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                }

                case R.id.navigation_setting: {
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                    }
                    return true;
                }
            }
            return false;
        }
    };

}
