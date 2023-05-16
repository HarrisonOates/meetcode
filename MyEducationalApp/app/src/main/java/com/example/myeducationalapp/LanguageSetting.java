package com.example.myeducationalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

/**
 * @author u7469758 Geun Yun
 */

public class LanguageSetting extends AppCompatActivity {
     RadioGroup languages;
     RadioButton english, korean, japanese, portuguese, chinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);

        languages = findViewById(R.id.language_setting);
        english = findViewById(R.id.buttonEn);
        korean = findViewById(R.id.buttonKr);
        japanese = findViewById(R.id.buttonJa);
        portuguese = findViewById(R.id.buttonPo);
        chinese = findViewById(R.id.buttonCh);

        languages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int languageID) {
                String languageCode = "";
                switch (languageID) {
                    case R.id.buttonEn:
                        languageCode="en";
                        break;
                    case R.id.buttonKr:
                        languageCode="ko";
                        break;
                    case R.id.buttonJa:
                        languageCode="ja";
                        break;
                    case R.id.buttonPo:
                        languageCode="pt";
                        break;
                    case R.id.buttonCh:
                        languageCode="zh";
                        break;
                }
                setLanguage(languageCode);
            }
        });

        Button backBtn =findViewById(R.id.backBtn2);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }

    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        // Restart the activity to apply the language changes
        Intent intent = new Intent(this, LanguageSetting.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}