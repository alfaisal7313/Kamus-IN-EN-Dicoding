package com.example.arsa.kamus_in_en_dicoding.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.arsa.kamus_in_en_dicoding.R;

public class Pref {
    private final SharedPreferences preferences;
    private final Context context;

    public Pref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void startFirstApp(Boolean input) {
        SharedPreferences.Editor editor = preferences.edit();
        String key = context.getResources().getString(R.string.first_open);
        editor.putBoolean(key, input);
        editor.apply();
    }

    public Boolean getFirstApp() {
        String key = context.getResources().getString(R.string.first_open);
        return preferences.getBoolean(key, true);
    }
}
