package com.oe.okenglish.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oe.okenglish.R;
import com.oe.okenglish.adaptor.ViewAdaptor;
import com.oe.okenglish.callback.OkCallback;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.res.Res;

public class OkEnglish extends AppCompatActivity {
    private BottomNavigationView tabBar;
    private ViewPager2 container;
    private Activity okEnglish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_main);
        okEnglish = this;
        initView();
    }

    private void initView() {
        container = findViewById(R.id.container);
        tabBar = findViewById(R.id.tabBar);
        container.setAdapter(new ViewAdaptor(getSupportFragmentManager(), getLifecycle()));
        container.setUserInputEnabled(false);
        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@Nullable MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        container.setCurrentItem(ViewAdaptor.HOME_ITEM, true);
                        break;
                    case R.id.tool:
                        container.setCurrentItem(ViewAdaptor.TOOL_ITEM, true);
                        break;
                    case R.id.setting:
                        container.setCurrentItem(ViewAdaptor.SETTING_ITEM, true);
                        break;
                }
                return true;
            }
        };
        tabBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        AppKit.moveAssetFileToExternalStorage(getApplicationContext(), Res.OKENGLISH_DB, new OkCallback() {
            @Override
            public void call(boolean isSuccess) {
                if (!isSuccess) {
                    okEnglish.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
