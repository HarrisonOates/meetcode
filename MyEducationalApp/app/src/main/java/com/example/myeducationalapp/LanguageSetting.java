package com.example.myeducationalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author u7469758 Geun Yun
 */

public class LanguageSetting extends AppCompatActivity {

    private Translator portugueseTranslator;
    private Translator koreanTranslator;
    private Translator englishTranslator;

    public String currentLanguage = TranslateLanguage.ENGLISH;
    public Translator currentTranslator;

    RadioGroup languages;
    RadioButton english, korean, japanese, portuguese, chinese;
    ArrayList<String> dynamicTranslations;
    ArrayList<Integer> dynamicTexts;

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
                        currentLanguage="en";
                        currentTranslator = englishTranslator;
                        break;
                    case R.id.buttonKr:
                        languageCode="ko";
                        currentLanguage="ko";
                        currentTranslator = koreanTranslator;
                        break;
                    case R.id.buttonJa:
                        languageCode="ja";
                        currentLanguage="ja";
                        break;
                    case R.id.buttonPo:
                        languageCode="pt";
                        currentLanguage="pt";
                        currentTranslator = portugueseTranslator;
                        break;
                    case R.id.buttonCh:
                        languageCode="zh";
                        currentLanguage="zh";
                        break;
                }
                setLanguage(languageCode);
            }
        });

        Button backBtn =findViewById(R.id.button2);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }

    private void setLanguage(String languageCode) {
        TextView textView = findViewById(R.id.home_hero_body_text);
        Question qotd = QuestionSet.getInstance().getQuestionOfTheDay();
        textView.setText(translateString(qotd.getName()));
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


    public void buttonDownloadTranslators(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "The download will take a while...", Toast.LENGTH_LONG);
        toast.show();

        TranslatorOptions portugueseOptions =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(currentLanguage)
                        .setTargetLanguage(TranslateLanguage.PORTUGUESE)
                        .build();
        portugueseTranslator = Translation.getClient(portugueseOptions);

        TranslatorOptions englishOptions =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(currentLanguage)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
        englishTranslator = Translation.getClient(englishOptions);

        TranslatorOptions koreanOptions =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(currentLanguage)
                        .setTargetLanguage(TranslateLanguage.KOREAN)
                        .build();
        koreanTranslator = Translation.getClient(koreanOptions);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        portugueseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        System.out.println("Portuguese successfully downloaded");
                        Toast toast = Toast.makeText(getApplicationContext(), "Portuguese successfully downloaded", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });

        englishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        System.out.println("English successfully downloaded");
                        Toast toast = Toast.makeText(getApplicationContext(), "English successfully downloaded", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });

        koreanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        System.out.println("Korean successfully downloaded");
                        Toast toast = Toast.makeText(getApplicationContext(), "Korean successfully downloaded", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
    }

    public String translateString(String stringToTranslate) {
        String translated = currentTranslator.translate(stringToTranslate).toString();
        System.out.println(translated);
        return translated;
    }

    public void translateApp() {

    }
}
