package com.example.arsa.kamus_in_en_dicoding.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arsa.kamus_in_en_dicoding.R;
import com.example.arsa.kamus_in_en_dicoding.data.db.DictionaryHelper;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;
import com.example.arsa.kamus_in_en_dicoding.databinding.ActivityMainBinding;
import com.example.arsa.kamus_in_en_dicoding.ui.adapter.DictionaryAdapter;
import java.util.ArrayList;
import java.util.Objects;
import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.TABLE_WORD_EN;
import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.TABLE_WORD_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;

    String tableTypeDefault = TABLE_WORD_EN;
    DictionaryHelper helper;
    DictionaryAdapter adapter;
    ArrayList<Word> wordArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helper = new DictionaryHelper(this);
        adapter = new DictionaryAdapter(this);

        binding.contentView.rvContent.setLayoutManager(new LinearLayoutManager(this));
        binding.contentView.rvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.contentView.rvContent.setAdapter(adapter);

        helper.open();
        wordArrayList = helper.getAllWord(tableTypeDefault);
        adapter.addWordList(wordArrayList);

        binding.contentView.btnLangType.setOnClickListener(this);
        binding.contentView.edtWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                searchWord(query);

                if (!TextUtils.isEmpty(query)) {
                    binding.contentView.edtWord.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
                    binding.contentView.edtWord.setOnTouchListener((view, motionEvent) -> {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if (motionEvent.getRawX() >= binding.contentView.edtWord.getRight() - binding.contentView.edtWord.getTotalPaddingRight()) {
                                binding.contentView.edtWord.getText().clear();
                                return true;
                            }
                        }
                        return false;
                    });
                } else {
                    binding.contentView.edtWord.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
                }
            }
        });
    }

    private void searchWord(String word) {
        if (tableTypeDefault != null) {
            wordArrayList = helper.getByWord(tableTypeDefault, word);
            adapter.addWordList(wordArrayList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_lang_type) {
            final String[] menuLang = {getResources().getString(R.string.eng_ind),
                    getResources().getString(R.string.ind_eng)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setItems(menuLang, (dialogInterface, position) -> {
                switch (position) {
                    case 0:
                        tableTypeDefault = TABLE_WORD_EN;
                        binding.subTitle.setText(getResources().getString(R.string.eng_ind));
                        break;
                    case 1:
                        tableTypeDefault = TABLE_WORD_ID;
                        binding.subTitle.setText(getResources().getString(R.string.ind_eng));
                        break;
                }
                searchWord(binding.contentView.edtWord.getText().toString());
                Objects.requireNonNull(binding.contentView.rvContent.getLayoutManager())
                        .smoothScrollToPosition(
                                binding.contentView.rvContent,
                                new RecyclerView.State(), 0
                        );
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
