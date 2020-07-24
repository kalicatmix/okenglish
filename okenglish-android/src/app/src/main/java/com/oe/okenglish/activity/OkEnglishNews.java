package com.oe.okenglish.activity;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oe.okenglish.R;
import com.oe.okenglish.api.PageFetcher;
import com.oe.okenglish.callback.OkCallback;
import com.oe.okenglish.entity.Page;
import com.oe.okenglish.entity.PageJson;
import com.oe.okenglish.entity.Reader;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.MediaKit;
import com.oe.okenglish.kit.RetroFitClientKit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OkEnglishNews extends AppCompatActivity implements View.OnClickListener {
    private VideoView player;
    private boolean isTypeVideo = false, isAudioPlayed = false;
    private Button playBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        setContentView(R.layout.ok_news);
        TextView backBtn = findViewById(R.id.toolbar_back_btn);
        TextView title = findViewById(R.id.ok_news_title);
        TextView pageText = findViewById(R.id.ok_news_text);
        backBtn.setOnClickListener(this);
        title.setText(getIntent().getStringExtra("title"));
        switch (getIntent().getIntExtra("type", -1)) {
            case Reader.TYPE_MOVIE:
                isTypeVideo = true;
                initTypeVideo();
                break;
            case Reader.TYPE_NEWS:
                initTypeVoice();
                break;
        }
        RetroFitClientKit.getInstance().build(ApiKit.URL_NEWS_TEXT, PageFetcher.class).fetchPage(getIntent().getStringExtra("id")).enqueue(new Callback<PageJson>() {
            @Override
            public void onResponse(Call<PageJson> call, Response<PageJson> response) {
                if (response.body() == null) return;
                StringBuffer buffer = new StringBuffer();
                ArrayList<Page> page;
                if ((page = response.body().getData()) != null) {
                    for (Page p : page) {
                        buffer.append(p.getSentence());
                        buffer.append("\n");
                        buffer.append(p.getSentenceCh());
                        buffer.append("\n");
                    }
                    pageText.setText(buffer.toString());
                }
            }

            @Override
            public void onFailure(Call<PageJson> call, Throwable t) {

            }
        });
    }

    private void initTypeVideo() {
        player = findViewById(R.id.ok_news_video);
        player.setVisibility(View.VISIBLE);
        player.setVideoURI(Uri.parse(getIntent().getStringExtra("sound")));
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0.5f, 0.5f);
            }
        });
        player.setMediaController(new MediaController(this));
        player.start();
    }

    private void initTypeVoice() {
        playBtn = findViewById(R.id.news_playBtn);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(point.x / 9, point.y / 10);
        params.setMargins(point.x / 9 * 4, 0, point.x / 9 * 4, 0);
        playBtn.setLayoutParams(params);
        playBtn.setOnClickListener(this);
        playBtn.setVisibility(View.VISIBLE);
        playBtn.setBackgroundResource(R.drawable.ic_pause_arrow);
        MediaKit.getInstance(this).playInNewThread(ApiKit.URL_NEWS_AUDIO_BASE + getIntent().getStringExtra("sound"), new OkCallback() {
            @Override
            public void call(boolean isSuccess) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        MediaKit.getInstance(this).stopAudioPlay();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_btn:
                if (isTypeVideo) {
                    player.suspend();
                    player.setMediaController(null);
                } else MediaKit.getInstance(this).stopAudioPlay();
                startActivity(new Intent(getApplicationContext(), OkEnglish.class));
                break;
            case R.id.news_playBtn:
                if (isAudioPlayed) {
                    isAudioPlayed = false;
                    MediaKit.getInstance(this).pauseAudioPlay();
                    playBtn.setBackgroundResource(R.drawable.ic_play_arrow);
                } else {
                    isAudioPlayed = true;
                    MediaKit.getInstance(this).startAudioPlay();
                    playBtn.setBackgroundResource(R.drawable.ic_pause_arrow);
                }
                break;
        }
    }
}
