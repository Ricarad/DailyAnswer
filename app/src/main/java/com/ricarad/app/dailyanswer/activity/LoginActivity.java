package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ricarad.app.dailyanswer.R;

import cn.bmob.v3.Bmob;


public class LoginActivity extends Activity implements View.OnClickListener{
    private Button loginButton;
    private TextView registerButton;
    private EditText accountEv;
    private EditText passwordEv;
    private TextView forgetPassTv;
  //  private TextView stateText;

    private CheckBox rememberPass;
    private SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private final static int REGISTER_CODE = 1;
    private final static int ADMINISTRATOR_CODE = 1;
    private final static int NORMALUSER_CODE = 1;
    private final static String BMOBAPPKEY = "e197fce2a9812ccbb9419f2193211af0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        Bmob.initialize(this, BMOBAPPKEY);

        rememberPass = (CheckBox)findViewById(R.id.remPass);
        loginButton = (Button)findViewById(R.id.login_button);
        registerButton = (TextView) findViewById(R.id.login_register_tv);
        accountEv = (EditText)findViewById(R.id.usrcount_text);
        passwordEv = (EditText)findViewById(R.id.password_text);
        forgetPassTv = (TextView)findViewById(R.id.login_forgetPass_tv);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgetPassTv.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        String account = pref.getString("account","");
        accountEv.setText(account);
        if(isRemember){
            String password = pref.getString("password","");
            passwordEv.setText(password);
            rememberPass.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case REGISTER_CODE:{
               if(resultCode == RESULT_OK){
                   accountEv.setText(data.getStringExtra("usercount"));
                   passwordEv.setText(data.getStringExtra("password"));
               }
           }break;

       }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_button:{
                String usercount = accountEv.getText().toString();
                String password = passwordEv.getText().toString();
                if(usercount.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    loginButton.setText("登录中。。。");
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                    login(usercount,password);

                }
            }break;
            case R.id.login_register_tv:{
                Toast.makeText(this,"注册按钮",Toast.LENGTH_SHORT).show();
              /*  Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,REGISTER_CODE);*/
            }break;
            case R.id.login_forgetPass_tv:{
                Toast.makeText(this,"忘记密码",Toast.LENGTH_SHORT).show();
            }break;

        }
    }

    @Override
    protected void onResume() {
      //  stateText.setText("请登录");
        loginButton.setText("登录");
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        super.onResume();
    }

    public void login(final String usercount, final String password){



    }
}
