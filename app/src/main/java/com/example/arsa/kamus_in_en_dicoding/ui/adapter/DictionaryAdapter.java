package com.example.arsa.kamus_in_en_dicoding.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsa.kamus_in_en_dicoding.R;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;
import com.example.arsa.kamus_in_en_dicoding.databinding.RowItemBinding;
import com.example.arsa.kamus_in_en_dicoding.ui.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>{
    private final Context context;
    private List<Word> wordList = new ArrayList<>();
    private final LayoutInflater inflater;

    public DictionaryAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addWordList(List<Word> wordList) {
        this.wordList = wordList;
        notifyDataSetChanged();
    }

    public void clear(){
        this.wordList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_item, viewGroup, false);
        return new DictionaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DictionaryViewHolder holder, final int position) {
        holder.rowItemBinding.txtWordItem.setText(wordList.get(position).getWord());
        holder.rowItemBinding.txtMeansItem.setText(wordList.get(position).getMeans());
        holder.rowItemBinding.dotsItem.setText(Html.fromHtml("&#8226;"));
        holder.rowItemBinding.dotsItem.setTextColor(getRandomMaterialColor());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_DATA, wordList.get(position));
            context.startActivity(intent);
        });
    }

    private int getRandomMaterialColor() {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + "400", "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return wordList == null ? 0 : wordList.size();
    }

    static class DictionaryViewHolder extends RecyclerView.ViewHolder {
        RowItemBinding rowItemBinding;

        public DictionaryViewHolder(@NonNull View itemView) {
            super(itemView);
            rowItemBinding = RowItemBinding.bind(itemView.getRootView());

        }
    }
}
