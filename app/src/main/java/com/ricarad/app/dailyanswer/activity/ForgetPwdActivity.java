package com.ricarad.app.dailyanswer.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.ricarad.app.dailyanswer.common.Constant.BMOBAPPKEY;

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener {


    @ViewInject(R.id.forget_pwd_password_text)
    private EditText passwordEt;
    @ViewInject(R.id.forget_pwd_confirm_password_text)
    private EditText confirmPwdEt;
    @ViewInject(R.id.forget_pwd_phone_text)
    private EditText phoneEt;
    @ViewInject(R.id.forget_pwd_verification_code_text)
    private EditText verficationCodeEt;
    @ViewInject(R.id.forget_pwd_send_vetification_btn)
    private Button sendMsgBtn;
    @ViewInject(R.id.forget_pwd_button)
    private Button confirmFindBtn;

    private static final int MAX_LENGTH = 8;
    //设置计时器，验证码点击
    private CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            sendMsgBtn.setEnabled(false);
            sendMsgBtn.setText("已发送(" + millisUntilFinished / 1000 + ")" + "s");
        }

        @Override
        public void onFinish() {
            sendMsgBtn.setEnabled(true);
            sendMsgBtn.setText("验证码");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_pwd);
        Bmob.initialize(this, BMOBAPPKEY);
        x.view().inject(this);
        sendMsgBtn.setOnClickListener(this);
        confirmFindBtn.setOnClickListener(this);
        setEditTextInputSpace(passwordEt);
        setEditTextInputSpeChat(passwordEt);
        setEditTextInputSpace(phoneEt);
        setEditTextInputSpeChat(phoneEt);
    }

    private Boolean editVerficate() {
        String password = passwordEt.getText().toString();
        String confirmPwd = confirmPwdEt.getText().toString();
        String phone = phoneEt.getText().toString();
        if (password.equals("") || password == null) {
            Toast.makeText(ForgetPwdActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!confirmPwd.equals(password)) {
            Toast.makeText(ForgetPwdActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isRightPhone(phone)) {
            Toast.makeText(ForgetPwdActivity.this, "请输入正确格式的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_pwd_send_vetification_btn: {
                String phone = phoneEt.getText().toString();
                if (!isRightPhone(phone)) {
                    Toast.makeText(ForgetPwdActivity.this, "请输入正确格式的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobSMS.requestSMSCode(phone, "微客社区", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                            timer.start();
                            Toast.makeText(ForgetPwdActivity.this, "发送验证码成功，短信ID:" +
                                    smsId + "\n", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgetPwdActivity.this, "发送验证码失败,失败信息：" +
                                    e.getErrorCode() + ":" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.i("TGA", "发送验证码失败" + e.getErrorCode() + ":" + e.getMessage());
                        }
                    }
                });
            }
            break;
            case R.id.forget_pwd_button:{
                if (!editVerficate()) {
                    return;
                }
                final String code = verficationCodeEt.getText().toString();
                if (code.equals("") || code == null) {
                    Toast.makeText(ForgetPwdActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = passwordEt.getText().toString();
                BmobUser.resetPasswordBySMSCode(code, password, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(ForgetPwdActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetPwdActivity.this, "重置失败：" +
                                    e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }break;
        }
    }

    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    //验证手机号是否正确ֻ
    public static boolean isRightPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        String regx = "[1][34578]\\d{9}";
        return phone.matches(regx);
    }

}
