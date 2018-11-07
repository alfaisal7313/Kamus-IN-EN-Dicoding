package com.example.arsa.kamus_in_en_dicoding.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.arsa.kamus_in_en_dicoding.R;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailActivity extends AppCompatActivity {


    public static final String EXTRA_DATA = "extra_data";
    @BindViews({R.id.txt_word_detail, R.id.txt_means_detail})
    List<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Word wordData = getIntent().getParcelableExtra(EXTRA_DATA);

        textViews.get(0).setText(wordData.getWord());
        textViews.get(1).setText(wordData.getMeans());
    }
}
