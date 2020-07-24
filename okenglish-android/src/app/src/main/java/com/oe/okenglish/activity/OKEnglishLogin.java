package com.oe.okenglish.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oe.okenglish.R;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.callback.CommonCallback;
import com.oe.okenglish.entity.User;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.OkCheckTool;
import com.oe.okenglish.kit.OkHttpClientKit;
import com.oe.okenglish.res.Res;
import com.oe.okenglish.text.PwdInputFilter;
import com.oe.okenglish.text.UserInputFilter;
import com.oe.okenglish.view.LoginRegistDialog;

import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.Response;

public class OKEnglishLogin extends AppCompatActivity {
    private VideoView videoView;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_login);
        videoView = findViewById(R.id.login_background);
        EditText user = findViewById(R.id.login_user), pwd = findViewById(R.id.login_pwd);
        Button loginBtn = findViewById(R.id.login_logBtn), regBtn = findViewById(R.id.login_regBtn);
        TextView skipBtn = findViewById(R.id.login_skip);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.bg);
        videoView.setFitsSystemWindows(true);
        videoView.setSoundEffectsEnabled(false);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0, 0);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        user.setHintTextColor(getColor(R.color.colorAccent));
        pwd.setHintTextColor(getColor(R.color.colorAccent));
        user.setFilters(new InputFilter[]{new UserInputFilter()});
        pwd.setFilters(new InputFilter[]{new PwdInputFilter()});
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userStr = user.getText().toString();
                String pwdStr = pwd.getText().toString();
                switch (v.getId()) {
                    case R.id.login_logBtn:
                        if (userStr.equals("") || userStr.length() < 5) {
                            user.setText("");
                            user.setHint("请输入用户,最少5位数字和字母组合");
                            user.requestFocus();
                        } else if (pwdStr.equals("") || pwdStr.length() < 5) {
                            pwd.setText("");
                            pwd.setHint("请输入密码,最少5位数字，字母，符号组合");
                            pwd.requestFocus();
                        }
                        if (OkCheckTool.isNetworkConnected((OKApplication) (getApplicationContext()))) {
                            String formData = "name=" + userStr + "&pwd=" + GeneratorTool.md5Sign(pwdStr);
                            OkHttpClientKit.getInstance().post(ApiKit.URL_REMOTE_HOST + ApiKit.URL_USER_LOGIN, formData, Headers.of(new HashMap<>())).enqueue(new CommonCallback() {
                                @Override
                                public void call(Object data) {
                                    Response response = (Response) data;
                                    try {
                                        String responseStr = response.body().string();
                                        if (responseStr != null) {
                                            String token = response.header(Res.TOKEN_HEADER);
                                            String sid = ((Response) data).header("Set-Cookie").split("=")[1];
                                            AppKit.log("data", responseStr);
                                            JsonObject resJson = JsonParser.parseString(responseStr).getAsJsonObject();
                                            JsonObject dataJson = resJson.get("data").getAsJsonObject();
                                            if (token != null) {
                                                String nameInt = "";
                                                for (int i = 0; i < userStr.length(); i++) {
                                                    String temp = "" + (int) userStr.charAt(i);
                                                    if (temp.length() > 2) temp = temp.substring(0);
                                                    nameInt += temp;
                                                }
                                                int id = Integer.parseInt(nameInt.substring(0, 5));
                                                OKApplication application = (OKApplication) getApplicationContext();
                                                application.getSharedPreferences(Res.CURRENT_LOGIN_ACCOUNT, MODE_PRIVATE).edit().putString("token", token).putString("sid", sid).putInt("id", id).commit();
                                                User user = new User();
                                                user.setId(id);
                                                user.setName(userStr);
                                                user.setPlan(dataJson.get("plan").getAsInt());
                                                user.setDayWord(dataJson.get("dayWord").getAsInt());
                                                application.setLogined(true);
                                                application.setUser(user);
                                                ContentValues values = new ContentValues();
                                                values.put("id", user.getId());
                                                values.put("name", user.getName());
                                                values.put("plan", user.getPlan());
                                                values.put("dayWord", user.getDayWord());
                                                application.getContentResolver().insert(Uri.parse(Res.USER_CONTENT_URL), values);
                                                startActivity(new Intent(getApplicationContext(), OkEnglish.class));
                                            }
                                        }
                                    } catch (Exception e) {
                                        AppKit.log("login error :", e.getMessage());
                                        handler.sendEmptyMessage(0);
                                    }
                                }
                            });
                        }
                        break;
                    case R.id.login_regBtn:
                        LoginRegistDialog.with(OKEnglishLogin.this).show();
                        break;
                    case R.id.login_skip:
                        startActivity(new Intent(getApplicationContext(), OkEnglish.class));
                }
            }
        };
        loginBtn.setOnClickListener(btnOnClickListener);
        regBtn.setOnClickListener(btnOnClickListener);
        skipBtn.setOnClickListener(btnOnClickListener);
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                user.setText("");
                pwd.setText("");
                Toast.makeText(getApplicationContext(), "登录失败,请检查密码和账户", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        videoView.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (videoView.canPause())
            videoView.pause();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        videoView.pause();
        super.onDestroy();
    }
}
