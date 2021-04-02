package com.example.arsa.kamus_in_en_dicoding.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;
import com.example.arsa.kamus_in_en_dicoding.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "extra_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Word wordData = getIntent().getParcelableExtra(EXTRA_DATA);

        binding.txtWordDetail.setText(wordData.getWord());
        binding.txtMeansDetail.setText(wordData.getMeans());
    }
}
