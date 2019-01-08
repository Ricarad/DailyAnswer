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
import android.widget.Toast;


import com.ricarad.app.dailyanswer.R;

import cn.bmob.v3.Bmob;

/**
 * Created by dongdong on 2017/11/28.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    private Button loginButton;
    private Button registerButton;
    private EditText usercountText;
    private EditText passwordText;
  //  private TextView stateText;

    private CheckBox rememberPass;
    private SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private final static int REGISTER_CODE = 1;
    private final static int ADMINISTRATOR_CODE = 1;
    private final static int NORMALUSER_CODE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        Bmob.initialize(this, "8e961ca4035d0b20d23273af6988e0c6");

        rememberPass = (CheckBox)findViewById(R.id.remPass);
        loginButton = (Button)findViewById(R.id.login_button);
        registerButton = (Button)findViewById(R.id.register_button);
        usercountText = (EditText)findViewById(R.id.usrcount_text);
        passwordText = (EditText)findViewById(R.id.password_text);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password",false);
        String account = pref.getString("account","");
        usercountText.setText(account);
        if(isRemember){
            String password = pref.getString("password","");
            passwordText.setText(password);
            rememberPass.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case REGISTER_CODE:{
               if(resultCode == RESULT_OK){
                   usercountText.setText(data.getStringExtra("usercount"));
                   passwordText.setText(data.getStringExtra("password"));
               }
           }break;

       }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_button:{
                String usercount = usercountText.getText().toString();
                String password = passwordText.getText().toString();
                if(usercount.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    loginButton.setText("登录中。。。");
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                    login(usercount,password);

                }
            }break;
            case R.id.register_button:{
                /*Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,REGISTER_CODE);*/
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
        /*BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("usercount",usercount);
        query.addWhereEqualTo("password",password);
        query.setLimit(1);
      //  stateText.setText("登录中。。。");

        query.findObjects(LoginActivity.this, new FindListener<User>() {
            @Override

            public void onSuccess(List<User> list) {
                if(list != null && list.size() !=0) {
                    User user = list.get(0);
                    String role = user.getRole();
                    editor = pref.edit();
                    String account = usercountText.getText().toString();
                    editor.putString("account",account);
                    if(rememberPass.isChecked()){
                        String password1 = passwordText.getText().toString();
                        editor.putString("password",password1);
                        editor.putBoolean("remember_password",true);
                    }else {
                        editor.clear();
                    }
                    editor.commit();
                    if(role.equals("管理员")){
                        Intent intent = new Intent(LoginActivity.this,AdministratorActivity.class);
                        intent.putExtra("user",user);
                        startActivityForResult(intent,ADMINISTRATOR_CODE);

                    }else if(role.equals("用户")){
                        Intent intent = new Intent(LoginActivity.this,NormalUserActivity.class);
                        intent.putExtra("user",user);
                        startActivityForResult(intent,NORMALUSER_CODE);
                    }
                }else{
                    loginButton.setText("登录");
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                    Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(int i, String s) {
                loginButton.setText("登录");
                loginButton.setBackgroundResource(R.color.colorWhite);
                loginButton.setEnabled(true);
                registerButton.setEnabled(true);
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });*/

    }
}
