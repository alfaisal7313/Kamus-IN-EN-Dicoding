package com.example.arsa.kamus_in_en_dicoding.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.arsa.kamus_in_en_dicoding.R;
import com.example.arsa.kamus_in_en_dicoding.data.Pref;
import com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract;
import com.example.arsa.kamus_in_en_dicoding.data.db.DictionaryHelper;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreloadActivity extends AppCompatActivity {

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        ButterKnife.bind(this);

        new LoadData().execute();
    }

    public class LoadData extends AsyncTask<Void, Integer, Void> {

        Context context = PreloadActivity.this;
        DictionaryHelper helper;
        Pref preference;
        double progress;
        double maxProgress = 100;

        @Override
        protected void onPreExecute() {
            helper = new DictionaryHelper(context);
            preference = new Pref(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Boolean firstRun = preference.getFirstApp();

            if (firstRun) {

                ArrayList<Word> words_En = preLoadRaw(R.raw.english_indonesia);
                ArrayList<Word> words_In = preLoadRaw(R.raw.indonesia_english);

                helper.open();
                progress = 5;
                publishProgress((int) progress);
                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (words_In.size() + words_En.size());

                helper.beginTransaction();
                try {
                    for (Word wordModelEng : words_En) {
                        helper.insertTransaction(DatabaseContract.TABLE_WORD_EN, wordModelEng);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }

                    for (Word wordModelInd : words_In) {
                        helper.insertTransaction(DatabaseContract.TABLE_WORD_ID, wordModelInd);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }
                    helper.setTransactionSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                helper.endTransaction();
                helper.close();

                preference.startFirstApp(false);
                publishProgress((int) maxProgress);
            } else {
                try {
                    synchronized (this) {
                        this.wait(2000);
                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int) maxProgress);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public ArrayList<Word> preLoadRaw(int langType) {
        ArrayList<Word> words = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(langType);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                Word wordModel = new Word();
                wordModel.setWord(splitstr[0]);
                wordModel.setMeans(splitstr[1]);
                words.add(wordModel);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }
}
