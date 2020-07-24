package com.oe.okenglish.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.oe.okenglish.R;
import com.oe.okenglish.entity.Word;

import java.util.ArrayList;

public class PopSearchListAdapter extends ListAdapter {
    private ArrayList<Word> words;

    public PopSearchListAdapter(DiffUtil.ItemCallback<Word> itemCallback, ArrayList<Word> words) {
        super(itemCallback);
        this.words = words;
    }
    public  void addItem(Word word){
        words.add(0,word);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pop_list, parent, false);
        return new PopSearchListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;
        TextView word = view.findViewById(R.id.pop_list_item_word);
        TextView translation = view.findViewById(R.id.pop_list_item_translate);
        word.setText(words.get(position).getWord()+" :  ");
        translation.setText(words.get(position).getTranslate());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public static class PopSearchListViewHolder extends RecyclerView.ViewHolder {
        public PopSearchListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
