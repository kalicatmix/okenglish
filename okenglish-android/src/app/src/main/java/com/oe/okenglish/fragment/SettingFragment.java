package com.oe.okenglish.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oe.okenglish.R;
import com.oe.okenglish.activity.OkEnglishAbout;
import com.oe.okenglish.activity.OkEnglishExam;
import com.oe.okenglish.activity.OkEnglishHelp;
import com.oe.okenglish.activity.OkEnglishUpdate;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.callback.CommonCallback;
import com.oe.okenglish.entity.User;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.OkHttpClientKit;
import com.oe.okenglish.res.Res;

import okhttp3.Response;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private LottieAnimationView lottieAnimationView;
    private CardView loginedView;
    private CardView unloginedView;
    private ImageView avater;
    private TextView avaterId;
    private TextView avaterContent;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        FloatingActionButton share = view.findViewById(R.id.setting_share);
        TextView update = view.findViewById(R.id.setting_update);
        TextView about = view.findViewById(R.id.setting_about);
        TextView help = view.findViewById(R.id.setting_help);
        TextView exam = view.findViewById(R.id.setting_exam);
        Button loginBtn = view.findViewById(R.id.setting_login_btn);
        avater = view.findViewById(R.id.setting_avatar);
        avaterId = view.findViewById(R.id.setting_avatar_id);
        avaterContent = view.findViewById(R.id.setting_avatar_content);
        TextView avaterEdit = view.findViewById(R.id.setting_avatar_edit);
        lottieAnimationView = view.findViewById(R.id.setting_lottie_view);
        loginedView = view.findViewById(R.id.setting_logined_panel);
        unloginedView = view.findViewById(R.id.setting_unlogined_panel);
        share.setOnClickListener(this);
        update.setOnClickListener(this);
        about.setOnClickListener(this);
        help.setOnClickListener(this);
        exam.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        avaterEdit.setOnClickListener(this);
        avater.setOnClickListener(this);
        lottieAnimationView.setAnimation(Res.settingLottieAnims[GeneratorTool.generateRandomNumber(0, Res.settingLottieAnims.length - 1)]);
        OKApplication application = (OKApplication) getActivity().getApplicationContext();
        dialog = new Dialog(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_setting_edit, container, false);
        TextView tooltip = dialogView.findViewById(R.id.setting_edit_tooltip);
        SeekBar seekBar = dialogView.findViewById(R.id.setting_edit_plan);
        seekBar.setMax(150);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tooltip.setText((seekBar.getProgress() + 50) + "单词");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialogView.findViewById(R.id.setting_edit_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dayword = seekBar.getProgress() + 50;
                OkHttpClientKit.getInstance().post(ApiKit.URL_REMOTE_HOST + GeneratorTool.replaceStr(ApiKit.URL_USER_DAYWORD_CHANGE, "?", application.getUser().getName()), "dayword=" + dayword, GeneratorTool.getTokenHeader(application)).enqueue(new CommonCallback() {
                    @Override
                    public void call(Object data) {
                        AppKit.log("dayword status", ((Response) data).code());
                    }
                });
                application.getUser().setDayWord(dayword);
                application.getPlanDelegation().update();
                ContentValues values = new ContentValues();
                values.put("dayword", dayword);
                application.getContentResolver().update(Uri.parse(Res.USER_CONTENT_URL), values, "name=?", new String[]{application.getUser().getName()});
                avaterContent.setText(dayword + "单词");
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        OKApplication application = (OKApplication) (getActivity().getApplicationContext());
        if (application.isLogined()) {
            loginedView.setVisibility(View.VISIBLE);
            unloginedView.setVisibility(View.GONE);
            User user = application.getUser();
            avaterId.setText(user.getName());
            avaterContent.setText(user.getDayWord() + "单词");
            application.getPlanDelegation().update();
        } else {
            loginedView.setVisibility(View.GONE);
            unloginedView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_update:
                startActivity(new Intent(getContext(), OkEnglishUpdate.class));
                break;
            case R.id.setting_about:
                startActivity(new Intent(getContext(), OkEnglishAbout.class));
                break;
            case R.id.setting_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, Res.SHARE_TEXT);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
            case R.id.setting_help:
                startActivity(new Intent(getContext(), OkEnglishHelp.class));
                break;
            case R.id.setting_login_btn:
                OKApplication application = (OKApplication) getActivity().getApplicationContext();
                if (application.isLogined()) {
                    application.getContentResolver().delete(Uri.parse(Res.USER_CONTENT_URL), "name=?", new String[]{application.getUser().getName()});
                    application.setLogined(false);
                    User user = new User();
                    user.setId(1);
                    user.setName("default");
                    user.setPlan(2);
                    user.setDayWord(50);
                    application.setUser(user);
                    application.getPlanDelegation().update();
                    Toast.makeText(application, "注销成功", Toast.LENGTH_SHORT).show();
                }
                Intent loginIntent = new Intent();
                loginIntent.setAction(AppKit.OKENGLISH_LOGIN);
                startActivity(loginIntent);
                break;
            case R.id.setting_exam:
                startActivity(new Intent(getContext(), OkEnglishExam.class));
                break;
            case R.id.setting_avatar_edit:
                dialog.show();
                break;
            case R.id.setting_avatar:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 0xff);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            String[] items = data.getDataString().split("/");
            String file = items[items.length - 1].split("\\%3A")[1];
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + file;
            Bitmap pic = BitmapFactory.decodeFile(path);
            avater.setImageDrawable(new BitmapDrawable(getResources(), pic.createScaledBitmap(pic, 200, 200, false)));
        } catch (Exception e) {
            avater.setImageDrawable(getResources().getDrawable(R.drawable.ic_accelerate, null));
            AppKit.log("avatar error", e.getMessage());
        }
    }
}
