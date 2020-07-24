package com.oe.okenglish.view;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.oe.okenglish.R;
import com.oe.okenglish.callback.CommonCallback;
import com.oe.okenglish.entity.MessageCode;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.OkHttpClientKit;
import com.oe.okenglish.text.PwdInputFilter;
import com.oe.okenglish.text.UserInputFilter;

import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.Response;

public class LoginRegistDialog extends Dialog {
    public static LoginRegistDialog loginRegistDialog;
    private static Object mLock = new Object();
    private Handler handler;

    public static LoginRegistDialog with(Context context) {
        loginRegistDialog = new LoginRegistDialog(context);
        return loginRegistDialog;
    }

    private LoginRegistDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login_regist, null);
        EditText regUser = view.findViewById(R.id.reg_user), regPwd = view.findViewById(R.id.reg_pwd), regEmail = view.findViewById(R.id.reg_email);
        Button reg = view.findViewById(R.id.reg_logBtn);
        regUser.setFilters(new InputFilter[]{new UserInputFilter()});
        regPwd.setFilters(new InputFilter[]{new PwdInputFilter()});
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStr = regUser.getText().toString();
                String pwdStr = regPwd.getText().toString();
                String emailStr = regEmail.getText().toString();
                if (userStr.equals("") || userStr.length() < 5) {
                    regUser.setText("");
                    regUser.setHint("请输入用户,最少5位数字和字母组合");
                    regUser.requestFocus();
                } else if (pwdStr.equals("") || pwdStr.length() < 5) {
                    regPwd.setText("");
                    regPwd.setHint("请输入密码,最少5位数字，字母，符号组合");
                    regPwd.requestFocus();
                } else if (!Pattern.matches("^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", emailStr)) {
                    regEmail.setText("");
                    regEmail.setHint("邮箱格式不符合");
                    regEmail.requestFocus();
                } else {
                    String pwdSigned = GeneratorTool.md5Sign(pwdStr);
                    OkHttpClientKit.getInstance().post(ApiKit.URL_REMOTE_HOST + ApiKit.URL_USER_ADD, "name=" + userStr + "&pwd=" + pwdSigned + "&email=" + emailStr, Headers.of(new HashMap<>())).enqueue(new CommonCallback() {
                        @Override
                        public void call(Object data) {
                            Response response = (Response) data;
                            try {
                                Gson gson = new Gson();
                                com.oe.okenglish.entity.Message message = gson.fromJson(response.body().string(), com.oe.okenglish.entity.Message.class);
                                if (message.getCode().equals(MessageCode.success))
                                    handler.sendEmptyMessage(2);
                                else handler.sendEmptyMessage(1);
                            } catch (Exception e) {
                                AppKit.log("reg error", e.getMessage());
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });
                }

            }
        };
        reg.setOnClickListener(btnOnClickListener);
        WindowManager manager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getSize(point);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(point.x / 4 * 3, point.y / 2);
        setContentView(view, layoutParams);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        Snackbar.make(view, "注册失败,用户名已被注册", 1000).show();
                        break;
                    case 2:
                        regUser.setText("");
                        regPwd.setText("");
                        regEmail.setText("");
                        Toast.makeText(getContext().getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}
