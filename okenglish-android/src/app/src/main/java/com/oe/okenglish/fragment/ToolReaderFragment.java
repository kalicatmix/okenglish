package com.oe.okenglish.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.oe.okenglish.R;
import com.oe.okenglish.activity.OkEnglishNews;
import com.oe.okenglish.api.NewsFetcher;
import com.oe.okenglish.api.VideoURLFetcher;
import com.oe.okenglish.application.OKApplication;
import com.oe.okenglish.callback.OkCallback;
import com.oe.okenglish.entity.NewsData;
import com.oe.okenglish.entity.NewsJson;
import com.oe.okenglish.entity.Reader;
import com.oe.okenglish.entity.Video;
import com.oe.okenglish.entity.VideoJson;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.GeneratorTool;
import com.oe.okenglish.kit.RetroFitClientKit;
import com.oe.okenglish.presentor.ToolReaderPresentor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToolReaderFragment extends Fragment {
    private ToolReaderPresentor toolReaderPresentor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tool_reader, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.tool_reader);
        SwipeRefreshLayout swiper = view.findViewById(R.id.tool_reader_swiper);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        toolReaderPresentor = ToolReaderPresentor.newPresentor();
        toolReaderPresentor.setReaders(new Reader[]{}).setView(recyclerView).addOnItemClickListener(new ToolReaderPresentor.ToolReaderClickListener() {
            @Override
            public void onItemClick(Reader reader) {
                if(!((OKApplication)(getContext().getApplicationContext())).isNetwork()){
                    Snackbar.make(view,"没有网络哟",1000).show();
                    return;
                }
                Intent intent = new Intent(getContext(), OkEnglishNews.class);
                intent.putExtra("id", reader.getId());
                intent.putExtra("title", reader.getTitle());
                intent.putExtra("type", reader.getType());
                intent.putExtra("sound", reader.getSound());
                startActivity(intent);
            }
        }).setReaderViewAdaptor();
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(((OKApplication)(getContext().getApplicationContext())).isNetwork())
                toolReaderPresentor.setReaders(ReaderHelper.getInstance((OKApplication)(getContext().getApplicationContext())).getSomeReaders(10)).update();
                swiper.setRefreshing(false);
            }
        });
        ReaderHelper.getInstance((OKApplication)(getContext().getApplicationContext())).addCallback(new OkCallback() {
            @Override
            public void call(boolean isSuccess) {
                if (isSuccess) {
                    toolReaderPresentor.setReaders(ReaderHelper.getInstance((OKApplication)(getContext().getApplicationContext())).getSomeReaders(10)).update();
                }
            }
        }).fetchReaders();
        return view;
    }

    public static class ReaderHelper {
        private ArrayList<Reader> readers;
        private static ReaderHelper helper;
        private OkCallback callback;
        private OKApplication application;
        private ReaderHelper(OKApplication application) {
            readers = new ArrayList<Reader>();
            this.application=application;
        }

        public static ReaderHelper getInstance(OKApplication okApplication) {
            synchronized (ReaderHelper.class) {
                if (helper == null) helper = new ReaderHelper(okApplication);
            }
            return helper;
        }

        public ReaderHelper addCallback(OkCallback callback) {
            this.callback = callback;
            return this;
        }

        public ReaderHelper fetchReaders() {
            if(!application.isNetwork()){
                return this;
            }
            RetroFitClientKit.getInstance().build(ApiKit.URL_NEWS_BASE, NewsFetcher.class).listNews("100").enqueue(new Callback<NewsJson>() {
                @Override
                public void onResponse(Call<NewsJson> call, Response<NewsJson> response) {
                    if(response.body().getData()==null){
                        callback.call(true);
                        return;
                    }
                    for (NewsData news : response.body().getData()) {
                        Reader reader = new Reader(news.getTitle(), news.getPic(), news.getSound(), news.getNewsId(), Reader.TYPE_NEWS);
                        readers.add(reader);
                    }
                    RetroFitClientKit.getInstance().build(ApiKit.URL_NEWS_VIDEO_LIST_BASE, VideoURLFetcher.class).listVideo(GeneratorTool.generateVideoSign()).enqueue(new Callback<VideoJson>() {
                        @Override
                        public void onResponse(Call<VideoJson> call, Response<VideoJson> response) {
                            if(response.body().getData()==null){
                                callback.call(true);
                                return;
                            }
                            for (Video video : response.body().getData()) {
                                Reader reader = new Reader(video.getTitle(), video.getPic(), video.getSound(), video.getId(), Reader.TYPE_MOVIE);
                                readers.add(reader);
                            }
                            callback.call(true);
                        }

                        @Override
                        public void onFailure(Call<VideoJson> call, Throwable t) {
                            callback.call(false);
                        }
                    });
                }

                @Override
                public void onFailure(Call<NewsJson> call, Throwable t) {
                }
            });

            return this;
        }

        public Reader[] getSomeReaders(int size) {
            if(readers.size()<=0||readers.size()<size)return new Reader[]{};
            Reader[] someReaders = new Reader[size];
            ArrayList<Integer> integers = new ArrayList<>();
            for (int i = 0, j; i < size; i++) {
                while (integers.contains(j = GeneratorTool.generateRandomNumber(0, readers.size())))
                    ;
                someReaders[i] = readers.get(j);
                integers.add(j);
            }
            return someReaders;
        }
    }
}
