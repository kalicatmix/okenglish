package com.oe.okenglish.view;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oe.okenglish.R;
import com.oe.okenglish.adaptor.PopSearchListAdapter;
import com.oe.okenglish.api.WordFetcher;
import com.oe.okenglish.entity.Word;
import com.oe.okenglish.entity.WordJson;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.RetroFitClientKit;
import com.oe.okenglish.kit.WindowTool;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopSearchView extends PopupWindow implements View.OnClickListener, TextView.OnEditorActionListener {
    private static PopSearchView popSearchView;
    private static Object oLOCK = new Object();
    private static View view;
    private EditText input;
    private TextView show;
    private RecyclerView list;
    private Activity activity;

    private PopSearchView(Activity activity) {
        this.activity = activity;
        view = LayoutInflater.from(activity).inflate(R.layout.pop_search_window, null);
        ImageView closeBtn = view.findViewById(R.id.pop_search_close);
        input = view.findViewById(R.id.pop_search_input);
        show = view.findViewById(R.id.pop_search_show);
        list = view.findViewById(R.id.pop_search_list);
        closeBtn.setOnClickListener(this);
        input.setOnEditorActionListener(this);
        Typeface typeface = GeneratorTool.generateTypeFaceFromAsset(activity.getApplicationContext(), "mw.ttf");
        input.setTypeface(typeface);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setTouchable(true);
        Point point = WindowTool.getWindowSize(activity);
        this.setWidth(point.x);
        this.setHeight(point.y / 5 * 3);
        this.setContentView(view);
        ArrayList<Word> items = new ArrayList<Word>();
        list.setVerticalScrollBarEnabled(true);
        list.setScrollBarStyle(RecyclerView.SCROLLBARS_INSIDE_OVERLAY);
        list.setAdapter(new PopSearchListAdapter(null, items));
    }

    public static PopSearchView with(Activity activity) {
        synchronized (oLOCK) {
            if (popSearchView == null) popSearchView = new PopSearchView(activity);
        }
        return popSearchView;
    }

    public void withAdaptor(PopSearchListAdapter adaptor) {
        list.setAdapter(adaptor);
    }

    public static PopSearchView animation() {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.pop_window_fade_in));
        return popSearchView;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        RetroFitClientKit.getInstance().build(ApiKit.URL_TRANSLATE_BASE, WordFetcher.class).getWord(AppKit.toString(v.getText())).enqueue(new Callback<WordJson>() {
            @Override
            public void onResponse(Call<WordJson> call, Response<WordJson> response) {
                WordJson.Result result = response.body().getResults().get(0).get(0);
                Word word = new Word(result.getSrc(), result.getResult());
                show.setText(result.getSrc() + "  :  " + result.getResult());
                ((PopSearchListAdapter) list.getAdapter()).addItem(word);
            }

            @Override
            public void onFailure(Call<WordJson> call, Throwable t) {
            }
        });
        input.setText("");
        return true;
    }
}
