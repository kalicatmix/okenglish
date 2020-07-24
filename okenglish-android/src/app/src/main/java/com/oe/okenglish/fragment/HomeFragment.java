package com.oe.okenglish.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.oe.okenglish.R;
import com.oe.okenglish.api.WordImageFetcher;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.callback.OkCallback;
import com.oe.okenglish.entity.Image;
import com.oe.okenglish.entity.User;
import com.oe.okenglish.entity.Word;
import com.oe.okenglish.entity.WordImageJson;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;
import com.oe.okenglish.kit.DrawableTool;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.MediaKit;
import com.oe.okenglish.kit.RetroFitClientKit;
import com.oe.okenglish.provider.db.WordService;
import com.oe.okenglish.res.Res;
import com.oe.okenglish.view.PlanDetailDialog;
import com.oe.okenglish.view.PopNotificationView;
import com.oe.okenglish.view.PopSearchView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View popWindowPostionTag;
    private HomeFragmentActionExecutor executor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        TextView previousBtn = view.findViewById(R.id.home_previous_btn);
        TextView nextBtn = view.findViewById(R.id.home_next_btn);
        TextView planShow = view.findViewById(R.id.home_plan_show);
        ImageButton playBtn = view.findViewById(R.id.home_item_play);
        ImageButton favoriteBtn = view.findViewById(R.id.home_item_favorite);
        ImageView imageView = view.findViewById(R.id.home_image);
        FloatingActionButton searchBtn = view.findViewById(R.id.home_float_action);
        popWindowPostionTag = view.findViewById(R.id.pop_window_postion_tag);
        playBtn.setImageDrawable(DrawableTool.zoomDrawable(getContext(), R.drawable.ic_play, 100, 100));
        TextView showBtn = view.findViewById(R.id.home_item_show_btn);
        TextView itemWord = view.findViewById(R.id.home_item_word);
        TextView itemTranslation = view.findViewById(R.id.home_item_translation);
        executor = new HomeFragmentActionExecutor(getActivity(), itemWord, itemTranslation, favoriteBtn, planShow, imageView);
        showBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        new PopNotificationView(getActivity()).show(popWindowPostionTag);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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
            case R.id.home_previous_btn:
                executor.actPreviousWord();
                break;
            case R.id.home_next_btn:
                executor.actNextWord();
                break;
            case R.id.home_item_play:
                executor.actPlay();
                break;
            case R.id.home_item_favorite:
                executor.actFavorite();
                break;
            case R.id.home_item_show_btn:
                executor.actShow();
                break;
            case R.id.home_float_action:
                PopSearchView.with(getActivity()).animation().showAsDropDown(popWindowPostionTag, 0, 0);
                break;
        }
    }

    private static class HomeFragmentActionExecutor {
        private TextView itemWord, itemTranslation, planShow;
        private ImageButton favoriteBtn;
        private ImageView wordImg;
        private Drawable favoriteDrawable, unfavoriteDrawable, defaultWordImg;
        private Activity activity;
        private User user;
        private ArrayList<Word> words;
        private int count = 0, maxCount = 1;
        private String planTitle;
        private boolean isFavorite = false;

        public int getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        public HomeFragmentActionExecutor(Activity activity, TextView itemWord, TextView itemTranslation, ImageButton favoriteBtn, TextView planShow, ImageView img) {
            this.itemWord = itemWord;
            this.itemTranslation = itemTranslation;
            this.favoriteBtn = favoriteBtn;
            this.planShow = planShow;
            this.activity = activity;
            unfavoriteDrawable = DrawableTool.zoomDrawable(activity, R.drawable.ic_favorite, 100, 100);
            favoriteDrawable = DrawableTool.zoomDrawable(activity, R.drawable.ic_favorite_selected, 100, 100);
            defaultWordImg = activity.getDrawable(R.drawable.ic_accelerate);
            wordImg = img;
            OKApplication application = ((OKApplication) (activity.getApplicationContext()));
            user = application.getUser();
            application.setPlanDelegation(new PlanDetailDialog.PlanDelegation() {
                @Override
                public void update() {
                    user = application.getUser();
                    words.clear();
                    count = 0;
                    maxCount = 1;
                    init();
                }
            });
            words = new ArrayList<Word>();
            init();
        }

        private void init() {
            WordService service = WordService.getInstance(activity);
            ArrayList<Integer> wordId = service.queryAllWordByPlanId(user.getPlan());
            for (int i = 0; i < user.getDayWord(); i++) {
                words.add(service.queryWordById(wordId.get(GeneratorTool.generateRandomNumber(0, wordId.size()))));
            }
            switch (user.getPlan()) {
                case ToolPlanFragment.TYPE_BUSINESS:
                    planTitle = ToolPlanFragment.Plans.BUSINESS;
                    break;
                case ToolPlanFragment.TYPE_COMPUTER:
                    planTitle = ToolPlanFragment.Plans.COMPUTER;
                    break;
                case ToolPlanFragment.TYPE_DOCTOR:
                    planTitle = ToolPlanFragment.Plans.DOCOTOR;
                    break;
                case ToolPlanFragment.TYPE_FINANIAL:
                    planTitle = ToolPlanFragment.Plans.FINANIAL;
                    break;
                case ToolPlanFragment.TYPE_GMAT:
                    planTitle = ToolPlanFragment.Plans.GMAT;
                    break;
                case ToolPlanFragment.TYPE_GRE:
                    planTitle = ToolPlanFragment.Plans.GRE;
                    break;
                case ToolPlanFragment.TYPE_IELTS:
                    planTitle = ToolPlanFragment.Plans.IELTS;
                    break;
                case ToolPlanFragment.TYPE_MBA:
                    planTitle = ToolPlanFragment.Plans.MBA;
                    break;
                case ToolPlanFragment.TYPE_PETS:
                    planTitle = ToolPlanFragment.Plans.PETS;
                    break;
                case ToolPlanFragment.TYPE_POSTGRADUATE:
                    planTitle = ToolPlanFragment.Plans.POSTGRADUATE;
                    break;
                case ToolPlanFragment.TYPE_PROFESSOR:
                    planTitle = ToolPlanFragment.Plans.PROFESSOR;
                    break;
                case ToolPlanFragment.TYPE_TOEFL:
                    planTitle = ToolPlanFragment.Plans.TOEFL;
                    break;
            }
            updateView();
        }

        private void updateView() {
            Word word = words.get(count);
            itemWord.setText(word.getWord());
            itemTranslation.setText(word.getTranslate());
            itemTranslation.setVisibility(View.GONE);
            if (count >= maxCount) maxCount = count + 1;
            planShow.setText("当前计划:" + planTitle + "\n记了" + maxCount + "个单词了");
            Cursor cursor = activity.getContentResolver().query(Uri.parse(Res.FAVO_CONTENT_URL), new String[]{"word"}, "id==? and word==?", new String[]{AppKit.toString(user.getId()), word.getWord()}, null);
            if (cursor.moveToNext()) {
                favoriteBtn.setImageDrawable(favoriteDrawable);
                isFavorite = true;
            } else {
                favoriteBtn.setImageDrawable(unfavoriteDrawable);
                isFavorite = false;
            }
            if (((OKApplication) activity.getApplicationContext()).isNetwork())
                RetroFitClientKit.getInstance().build(ApiKit.URL_IMAGE_QUERY_BASE, WordImageFetcher.class).listImage(word.getWord()).enqueue(new Callback<WordImageJson>() {
                    @Override
                    public void onResponse(Call<WordImageJson> call, Response<WordImageJson> response) {
                        if (response.body().getData().size() > 0) {
                            Image img = response.body().getData().get(0);
                            String imgUrl = ApiKit.URL_IMAGE_BASE + img.getUrl().substring(0, 2) + "/" + img.getUrl();
                            Glide.with(activity.getApplicationContext()).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(wordImg);
                        } else {
                            wordImg.setImageDrawable(defaultWordImg);
                        }
                    }

                    @Override
                    public void onFailure(Call<WordImageJson> call, Throwable t) {
                    }
                });
            else wordImg.setImageDrawable(defaultWordImg);
        }

        public void actNextWord() {
            if (count < words.size() - 1) count++;
            else Snackbar.make(itemWord, "任务以达成", 1000).show();
            updateView();
        }

        public void actPreviousWord() {
            if (0 < count) count--;
            updateView();
        }

        public void actPlay() {
            Word word = words.get(count);
            if (word.getVoice().length <= 0)
                MediaKit.getInstance(activity).playAudioByUrl(ApiKit.URL_WORD_AUDIO_BASE + word.getWord(), new OkCallback() {
                    @Override
                    public void call(boolean isSuccess) {
                    }
                });
            else {
                AppKit.writeFileToExternalStorage(activity.getApplicationContext(), word.getVoice(), word.getWord(), Res.VOICE_DIR, new OkCallback() {
                    @Override
                    public void call(boolean isSuccess) {
                    }
                });
                MediaKit.getInstance(activity).playAudioFile(activity.getExternalFilesDir(null) + Res.VOICE_DIR + "/" + word.getWord(), new OkCallback() {
                    @Override
                    public void call(boolean isSuccess) {
                    }
                });
            }
        }

        public void actShow() {
            itemTranslation.setVisibility(View.VISIBLE);
        }

        private void actFavorite() {
            Word word = words.get(count);

            if (isFavorite) {
                favoriteBtn.setImageDrawable(unfavoriteDrawable);
                isFavorite = false;
                activity.getContentResolver().delete(Uri.parse(Res.FAVO_CONTENT_URL), "id==? and word==?", new String[]{AppKit.toString(user.getId()), word.getWord()});
            } else {
                favoriteBtn.setImageDrawable(favoriteDrawable);
                isFavorite = true;
                ContentValues value = new ContentValues();
                value.put("id", user.getId());
                value.put("word", word.getWord());
                value.put("trans", word.getTranslate());
                activity.getContentResolver().insert(Uri.parse(Res.FAVO_CONTENT_URL), value);
            }
        }
    }
}
