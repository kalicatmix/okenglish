package com.oe.okenglish.presentor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oe.okenglish.R;
import com.oe.okenglish.entity.Reader;
import com.oe.okenglish.kit.ApiKit;
import com.oe.okenglish.kit.AppKit;

public class ToolReaderPresentor implements Presentor<ToolReaderPresentor> {
    private static View view;
    private static ToolReaderPresentor toolReaderPresentor;
    private static Reader readers[];
    private ReaderViewAdaptor readerViewAdaptor;
    private ToolReaderClickListener listener;

    private ToolReaderPresentor() {
    }

    public Reader[] getReaders() {
        return readers;
    }

    public ToolReaderPresentor setReaders(Reader[] readers) {
        this.readers = readers;
        return this;
    }

    private void addReaderViewAdaptor() {
        readerViewAdaptor = new ReaderViewAdaptor(listener);
        ((RecyclerView) view).setAdapter(readerViewAdaptor);
    }

    public ToolReaderPresentor setReaderViewAdaptor(RecyclerView.Adapter readerViewAdaptor) {
        this.readerViewAdaptor = (ReaderViewAdaptor) readerViewAdaptor;
        addReaderViewAdaptor();
        return this;
    }

    public ToolReaderPresentor setReaderViewAdaptor() {
        if (this.readers != null) addReaderViewAdaptor();
        return this;
    }

    public ToolReaderPresentor addOnItemClickListener(ToolReaderClickListener listener) {
        this.listener = listener;
        return this;
    }

    public static ToolReaderPresentor newPresentor() {
        synchronized (ToolReaderPresentor.class) {
            if (toolReaderPresentor == null) toolReaderPresentor = new ToolReaderPresentor();
        }
        return toolReaderPresentor;
    }

    public View getView() {
        return view;
    }

    public ToolReaderPresentor setView(@NonNull View view) {
        this.view = view;
        return this;
    }

    @Override
    public ToolReaderPresentor update() {
        readerViewAdaptor.notifyDataSetChanged();
        return this;
    }

    private static class ReaderViewHolder extends RecyclerView.ViewHolder {
        public ReaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class ReaderViewAdaptor extends RecyclerView.Adapter {
        private ToolReaderClickListener listener;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool_reader, null, false);
            return new ReaderViewHolder(item);
        }

        ReaderViewAdaptor(ToolReaderClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            View item = holder.itemView;
            Reader reader = readers[position];
            ImageView imageView = item.findViewById(R.id.tool_reader_item_img);
            TextView title = item.findViewById(R.id.tool_reader_item_title);
            String imgUrl = reader.getImgUrl();
            if (reader.getType() == Reader.TYPE_NEWS) imgUrl = ApiKit.URL_NEWS_IMAGE_BASE + imgUrl;
            Glide.with(view.getContext()).load(imgUrl).dontTransform().into(imageView);
            title.setText(reader.getTitle());
            item.setTag(position);
            item.setOnClickListener(listener);
        }

        @Override
        public int getItemCount() {
            return readers.length;
        }
    }

    public static abstract class ToolReaderClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onItemClick(readers[(int) v.getTag()]);
        }

        public abstract void onItemClick(Reader reader);
    }
}
