package com.example.myeducationalapp.Localization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myeducationalapp.MainActivity;
import com.example.myeducationalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * @author u7469758 Geun Yun
 */

public class LanguageSetting extends AppCompatActivity{
    public static String previousLanguage;
    public static String currentLanguage = TranslateLanguage.ENGLISH;

    public static boolean isSameLangauge;

    RadioGroup languages;
    RadioButton english, korean, japanese, portuguese, chinese;


   public LanguageSetting(){};

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
                previousLanguage = currentLanguage;
                switch (languageID) {
                    case R.id.buttonEn:
                        currentLanguage = "en";
                        break;
                    case R.id.buttonKr:
                        currentLanguage = "ko";
                        break;
                    case R.id.buttonJa:
                        currentLanguage = "ja";
                        break;
                    case R.id.buttonPo:
                        currentLanguage = "pt";
                        break;
                    case R.id.buttonCh:
                        currentLanguage = "zh";
                        break;
                }
                if (previousLanguage.equals(currentLanguage)) {
                    isSameLangauge = true;
                    Toast toast = Toast.makeText(getApplicationContext(), "Already in the selected langauge!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    isSameLangauge = false;
                    setLanguage(currentLanguage);
                }
            }
        });

        Button backBtn =findViewById(R.id.button2);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }

    /**
     * Changes all the hardcoded string values in strings.xml into a given language
     * @param languageCode
     */
    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        // Restart the activity to apply the language changes
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
