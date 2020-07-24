package com.oe.okenglish.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oe.okenglish.R;
import com.oe.okenglish.callback.CommonCallback;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.OkHttpClientKit;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Response;

public class PopNotificationView extends PopupWindow {
    private Context context;
    private View view;
    private int margin;
    private Handler handler;
    private SimpleAdapter simpleAdapter;

    public PopNotificationView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.pop_notification_window, null);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        WindowManager manager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getRealSize(point);
        this.setWidth(point.x - point.x / 5);
        this.setHeight(point.y / 5 * 2);
        margin = point.x / 10;
        setContentView(view);
        initView();
    }

    public void initView() {
        ListView listView = view.findViewById(R.id.notification);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                listView.setAdapter(simpleAdapter);
                return true;
            }
        });
        OkHttpClientKit.getInstance().get(ApiKit.URL_REMOTE_HOST + ApiKit.URL_NOTIFICATION).enqueue(new CommonCallback() {
            //new SimpleAdapter(context, data, R.layout.item_notification, new String[]{"name"}, new int[]{R.id.notification_item}
            @Override
            public void call(Object data) {
                try {
                    Response response = (Response) data;
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();
                    JsonObject notificationJson = JsonParser.parseString(response.body().string()).getAsJsonObject();
                    response.close();
                    JsonArray notificatioins = notificationJson.getAsJsonArray("data");
                    for (JsonElement notification : notificatioins) {
                        JsonObject no = notification.getAsJsonObject();
                        HashMap<String, String> item = new HashMap<>();
                        item.put("item", no.get("date").getAsString() + ":" + no.get("content").getAsString());
                        items.add(item);
                    }
                    simpleAdapter = new SimpleAdapter(context, items, R.layout.item_notification, new String[]{"item"}, new int[]{R.id.notification_item});
                } catch (Exception e) {
                    AppKit.log("notification error", e.getMessage());
                    simpleAdapter = new SimpleAdapter(context, new ArrayList<HashMap<String, String>>(), R.layout.item_notification, new String[]{"item"}, new int[]{R.id.notification_item});
                }
                handler.sendEmptyMessage(1);
            }
        });
    }

    public void show(View tag) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.pop_window_fade_in));
        showAsDropDown(tag, margin, margin * 4);
    }
}
