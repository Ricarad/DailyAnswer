package com.ricarad.app.dailyanswer.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;
import com.ricarad.app.dailyanswer.R;

import com.ricarad.app.dailyanswer.model.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

import static com.ricarad.app.dailyanswer.common.Constant.BMOBAPPKEY;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.register_head_img)
    private CircleImageView headImg;
    @ViewInject(R.id.register_nickname_text)
    private EditText nickNameEt;
    @ViewInject(R.id.register_username_text)
    private EditText userNameEt;
    @ViewInject(R.id.register_password_text)
    private EditText passwordEt;
    @ViewInject(R.id.register_confirm_password_text)
    private EditText confirmPwdEt;
    @ViewInject(R.id.register_phone_text)
    private EditText phoneEt;
    @ViewInject(R.id.register_verification_code_text)
    private EditText verficationCodeEt;
    @ViewInject(R.id.register_send_vetification_btn)
    private Button sendMsgBtn;
    @ViewInject(R.id.register_button)
    private Button registerBtn;

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 8;

    private String imgUrl; //头像图片的url地址

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
        setContentView(R.layout.activity_register);
        x.view().inject(this);
        Bmob.initialize(this, BMOBAPPKEY);
        sendMsgBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        headImg.setOnClickListener(this);
        setEditTextInputSpace(nickNameEt);
        setEditTextInputSpeChat(nickNameEt);
        setEditTextInputSpace(userNameEt);
        setEditTextInputSpeChat(userNameEt);
        setEditTextInputSpace(passwordEt);
        setEditTextInputSpeChat(passwordEt);
        setEditTextInputSpace(phoneEt);
        setEditTextInputSpeChat(phoneEt);
        imgUrl = getResourcesUrl(R.drawable.head_img_default);
    }


    private Boolean editVerficate() {
        String nickName = nickNameEt.getText().toString();
        String userName = userNameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirmPwd = confirmPwdEt.getText().toString();
        String phone = phoneEt.getText().toString();

        if (nickName.length() > MAX_LENGTH) {
            Toast.makeText(RegisterActivity.this, "昵称不得超过7个字符", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nickName.equals("") || nickName == null) {
            Toast.makeText(RegisterActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userName.equals("") || userName == null) {
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.equals("") || password == null) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!confirmPwd.equals(password)) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isRightPhone(phone)) {
            Toast.makeText(RegisterActivity.this, "请输入正确格式的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_send_vetification_btn: {
                String phone = phoneEt.getText().toString();
                if (!isRightPhone(phone)) {
                    Toast.makeText(RegisterActivity.this, "请输入正确格式的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobSMS.requestSMSCode(phone, "微客社区", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {
                            timer.start();
                            Toast.makeText(RegisterActivity.this, "发送验证码成功，短信ID:" + smsId + "\n", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "发送验证码失败,失败信息：" + e.getErrorCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("TGA", "发送验证码失败" + e.getErrorCode() + ":" + e.getMessage());
                        }
                    }
                });
            }
            break;
            case R.id.register_button: {
                if (!editVerficate()) {
                    return;
                }
                final String code = verficationCodeEt.getText().toString();
                if (code.equals("") || code == null) {
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = phoneEt.getText().toString();
                String username = userNameEt.getText().toString();
                String nickName = nickNameEt.getText().toString();
                final String password = passwordEt.getText().toString();
                final User user = new User();
                user.setMobilePhoneNumber(phone);
                user.setNickName(nickName);
                user.setUsername(username);
                user.setPassword(password);
                user.setNumber(0);
                user.setDays(0);
                user.setRightRatio(0.0);
                user.setRightNumber(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                user.setLastLoginDate(BmobDate.createBmobDate("yyyy-MM-dd HH:mm:ss", sdf.format(new Date())));
                BmobFile bmobFile = new BmobFile(new File(imgUrl));
                Log.i("TGA", "图片地址为" + imgUrl);
                user.setUserImg(bmobFile);
                user.getUserImg().uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            user.signOrLogin(code, new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        Intent intent = new Intent();
                                        intent.putExtra("userName", userNameEt.getText().toString());
                                        intent.putExtra("password", passwordEt.getText().toString());
                                        setResult(RESULT_OK, intent);
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败，失败原因：" + e.getErrorCode() + ":" + e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "注册失败，失败原因：" + e.getErrorCode() + ":" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            break;
            case R.id.register_head_img: {
                SystemImagePicker systemImagePicker = RxImagePicker.INSTANCE.create();
                systemImagePicker.openGallery(this).subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        imgUrl = result.getUri().getPath();
                        File file = new File(imgUrl);
                        final Bitmap cackeBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        headImg.setImageBitmap(cackeBitmap);
                    }
                });
            }
            break;
        }
    }


    private String getResourcesUrl(@DrawableRes int id) {
        Resources resources = getResources();
        String urlPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return urlPath;
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