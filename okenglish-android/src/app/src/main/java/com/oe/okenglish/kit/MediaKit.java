package com.oe.okenglish.kit;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.oe.okenglish.callback.OkCallback;

public class MediaKit {
    private static MediaKit mediaKit;
    private Activity activity;
    private MediaPlayer mediaPlayer;
    private AudioAttributes audioAttributes;

    private MediaKit(Activity activity) {
        this.activity = activity;
        mediaPlayer = new MediaPlayer();
        audioAttributes = new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build();
        mediaPlayer.setAudioAttributes(audioAttributes);
    }

    public static MediaKit getInstance(Activity activity) {
        synchronized (MediaKit.class) {
            if (mediaKit == null) mediaKit = new MediaKit(activity);
        }
        return mediaKit;
    }

    public void stopAudioPlay() {
        mediaPlayer.reset();
    }

    public void pauseAudioPlay() {
        mediaPlayer.pause();
    }

    public void startAudioPlay() {
        mediaPlayer.start();
    }

    public void prepareAudioData(String url, OkCallback callback) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(activity, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    callback.call(true);
                }
            });
        } catch (Exception e) {
            callback.call(false);
        }
    }

    public void playAudioByUrl(String url, OkCallback callback) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(activity, Uri.parse(url));
            play(callback);
        } catch (Exception e) {
            callback.call(false);
        }
    }

    public void playAudioFile(String path, OkCallback callback) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            play(callback);
        } catch (Exception e) {
            AppKit.log("playAudioFile", e.getMessage());
            callback.call(false);
        }
    }

    private void play(OkCallback callback) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.stop();
                            callback.call(true);
                        }
                    });
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    callback.call(false);
                }
            }
        });
    }

    public void playInNewThread(String url, OkCallback okCallback) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(activity, Uri.parse(url));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                mp.reset();
                                AppKit.log("audioError", mp.getCurrentPosition());
                                return true;
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mediaPlayer.stop();
                                okCallback.call(true);
                            }
                        });
                        mediaPlayer.prepare();
                    } catch (Exception e) {
                        okCallback.call(false);
                    }
                }
            }).start();
        } catch (Exception e) {
            okCallback.call(false);
        }
        ;
        ;
    }
}
