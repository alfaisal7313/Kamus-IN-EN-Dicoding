package com.example.arsa.kamus_in_en_dicoding.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsa.kamus_in_en_dicoding.R;
import com.example.arsa.kamus_in_en_dicoding.data.db.DictionaryHelper;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;
import com.example.arsa.kamus_in_en_dicoding.ui.adapter.DictionaryAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_content)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_lang_type)
    ImageButton btnChangeType;
    @BindView(R.id.sub_title)
    TextView tvLangType;
    @BindView(R.id.edt_word)
    EditText inputWord;

    String tableTypeDefault = TABLE_WORD_EN;
    DictionaryHelper helper;
    DictionaryAdapter adapter;
    ArrayList<Word> wordArrayList = new ArrayList<>();
    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        helper = new DictionaryHelper(this);
        adapter = new DictionaryAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        helper.open();
        wordArrayList = helper.getAllWord(tableTypeDefault);
        adapter.addWordList(wordArrayList);

        btnChangeType.setOnClickListener(this);
        inputWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                searchWord(query);

                if (!TextUtils.isEmpty(query)){
                    inputWord.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_close, 0);
                    inputWord.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                                if (motionEvent.getRawX() >= inputWord.getRight() - inputWord.getTotalPaddingRight()){
                                    inputWord.getText().clear();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                }else {
                    inputWord.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_search, 0);
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
        unbinder.unbind();
        helper.close();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_lang_type) {
            final String[] menuLang = {getResources().getString(R.string.eng_ind),
                    getResources().getString(R.string.ind_eng)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setItems(menuLang, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position) {
                    switch (position) {
                        case 0:
                            tableTypeDefault = TABLE_WORD_EN;
                            tvLangType.setText(getResources().getString(R.string.eng_ind));
                            break;
                        case 1:
                            tableTypeDefault = TABLE_WORD_ID;
                            tvLangType.setText(getResources().getString(R.string.ind_eng));
                            break;
                    }
                    searchWord(inputWord.getText().toString());
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, new RecyclerView.State(), 0);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
