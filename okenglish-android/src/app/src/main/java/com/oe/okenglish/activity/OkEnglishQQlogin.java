package com.oe.okenglish.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oe.okenglish.R;
import com.oe.okenglish.callback.CommonCallback;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.OkHttpClientKit;
import com.tencent.tauth.Tencent;

import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkEnglishQQlogin extends AppCompatActivity {
    private Tencent mTencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_about);
      /*  mTencent=Tencent.createInstance("101857369",getApplicationContext());
        AppKit.log("appid",mTencent.getAppId());
        AppKit.log("id",""+ mTencent.checkSessionValid("101857369"));
        AppKit.log("qq",""+mTencent.isQQInstalled(getApplicationContext()));
        AppKit.log("session",mTencent.isSessionValid());
        if(mTencent.isSessionValid()){
             mTencent.login(this, "get_simple_userinfo", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                 AppKit.log("o",o.toString());
                }

                @Override
                public void onError(UiError uiError) {
                    AppKit.log("qq",uiError.errorMessage);
                }

                @Override
                public void onCancel() {

                }
            });
        }*/
        AppKit.log("result", GeneratorTool.replaceStr(ApiKit.URL_USER_DAYWORD,"?","jerry"));
        /*  (RequestBody.create("name=jerry&pwd=jerry", MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"))) */
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
