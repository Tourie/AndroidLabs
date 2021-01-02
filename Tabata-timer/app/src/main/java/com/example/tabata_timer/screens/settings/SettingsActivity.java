package com.example.tabata_timer.screens.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.tabata_timer.R;
import com.example.tabata_timer.screens.main.MainActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetConfigurations(this);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat listPreferenceMode = findPreference("night_theme");
            ListPreference listPreferenceFont = findPreference("font_size");
            ListPreference listPreferenceLanguage = findPreference("lang");

            listPreferenceLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setDefaultValue(newValue);
                    Locale locale = new Locale(newValue.toString());
                    Locale.setDefault(locale);
                    Configuration configuration = new Configuration();
                    configuration.locale = locale;
                    getActivity().getResources().updateConfiguration(configuration, null);
                    getActivity().recreate();
                    return true;
                }
            });

            listPreferenceMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().recreate();
                    return true;
                }
            });

            listPreferenceFont.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().recreate();
                    return true;
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public static void SetConfigurations(Context caller){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(caller);
        float size = Float.parseFloat(sp.getString("font_size", "1.0"));
        String lang = sp.getString("lang", "RU");
        Configuration configuration = new Configuration();
        configuration.fontScale = size;

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        configuration.locale = locale;

        boolean nMode = sp.getBoolean("night_theme",  false);
        if (nMode) {
            caller.setTheme(R.style.ThemeOverlay_MaterialComponents_Dark);
        } else {
            caller.setTheme(R.style.Theme_Tabatatimer);
        }
        caller.getResources().updateConfiguration(configuration, null);
    }
}