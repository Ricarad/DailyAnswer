package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ricarad.app.dailyanswer.R;


import com.ricarad.app.dailyanswer.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.ricarad.app.dailyanswer.common.Constant.*;
import static com.ricarad.app.dailyanswer.common.Constant.ResultCode.GUIDE_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.ResultCode.REGISTER_CODE;


public class LoginActivity extends Activity implements View.OnClickListener {
    private Button loginButton;
    private TextView registerButton;
    private EditText accountEv;
    private EditText passwordEv;
    private TextView forgetPassTv;

    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        Bmob.initialize(this, BMOBAPPKEY);
        rememberPass = (CheckBox) findViewById(R.id.remPass);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (TextView) findViewById(R.id.login_register_tv);
        accountEv = (EditText) findViewById(R.id.usrcount_text);
        passwordEv = (EditText) findViewById(R.id.password_text);
        forgetPassTv = (TextView) findViewById(R.id.login_forgetPass_tv);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgetPassTv.setOnClickListener(this);
        loadInfo();
    }

    public void loadInfo() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
        String account = pref.getString("account", "");
        accountEv.setText(account);
        if (isRemember) {
            String password = pref.getString("password", "");
            passwordEv.setText(password);
            rememberPass.setChecked(true);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REGISTER_CODE: {
                if (resultCode == RESULT_OK) {
                    accountEv.setText(data.getStringExtra("usercount"));
                    passwordEv.setText(data.getStringExtra("password"));
                }
            }
            break;
            case GUIDE_CODE: {
                if (resultCode == RESULT_OK) {
                    User tempUser = (User) data.getSerializableExtra(USER);
                    tempUser.logOut();
                }
            }
            break;
        }
    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_button: {
                final String account = accountEv.getText().toString();
                final String password = passwordEv.getText().toString();
                if (account == null || password == null || account.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setUsername(account);
                    user.setPassword(password);
                    //  Log.i("TGA","登陆用户的详细信息"+user.toString());
                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                user = BmobUser.getCurrentUser(User.class);
                                editor = pref.edit();
                                editor.putString("account", account);
                                if (rememberPass.isChecked()) {
                                    editor.putString("password", password);
                                    editor.putBoolean("remember_password", true);
                                } else {
                                    editor.clear();
                                }
                                editor.commit();
                                //对用户的上次登录时间 和 累计登录天数进行更新
                                try {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date lastLogin = simpleDateFormat.parse(user.getLastLoginDate().getDate());
                                    Date now = new Date();
                                    if (isTodayFirstLogin(lastLogin, now)) {
                                        user.setDays(user.getDays() + 1);
                                    }
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    user.setLastLoginDate(BmobDate.createBmobDate("yyyy-MM-dd HH:mm:ss", sdf.format(new Date())));
                                    final Intent intent = new Intent(LoginActivity.this, GuideActivity.class);
                                    intent.putExtra(USER, user);
                                    intent.putExtra(USERID, user.getObjectId());
                                    user.update(user.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                startActivityForResult(intent, GUIDE_CODE);
                                            } else {
                                                Snackbar.make(view, "更新登录信息失败：" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } catch (Exception exp) {
                                    Snackbar.make(view, "登录日期出现异常：" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
            break;
            case R.id.login_register_tv: {
                Toast.makeText(this, "注册按钮", Toast.LENGTH_SHORT).show();
              /*  Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,REGISTER_CODE);*/
            }
            break;
            case R.id.login_forgetPass_tv: {
                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
            }
            break;

        }
    }

    private boolean isTodayFirstLogin(Date lastLoginDate, Date todayDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String todayTime = df.format(todayDate);// 获取当前的日期
        String lastTime = df.format(lastLoginDate);
        if (lastTime.equals(todayTime)) { //如果两个时间段相等
            return false;
        } else {
            return true;
        }
    }




}
