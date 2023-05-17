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

import com.example.myeducationalapp.Localization.TranslationCallback;
import com.example.myeducationalapp.MainActivity;
import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.QuestionSet;
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

public class LanguageSetting extends AppCompatActivity implements TranslationCallback {

    public Translator portugueseTranslator;
    public Translator koreanTranslator;
    public Translator englishTranslator;

    public String currentLanguage = TranslateLanguage.ENGLISH;
    public Translator currentTranslator;

    RadioGroup languages;
    RadioButton english, korean, japanese, portuguese, chinese;
    ArrayList<String> dynamicTranslations;
    ArrayList<Integer> dynamicTextIDs;
    public String qotddd;

    private CountDownLatch translationLatch; // Latch to synchronize threads

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
                switch (languageID) {
                    case R.id.buttonEn:
                        currentLanguage="en";
                        currentTranslator = englishTranslator;
                        break;
                    case R.id.buttonKr:
                        currentLanguage="ko";
                        currentTranslator = koreanTranslator;
                        break;
                    case R.id.buttonJa:
                        currentLanguage="ja";
                        break;
                    case R.id.buttonPo:
                        currentLanguage="pt";
                        currentTranslator = portugueseTranslator;
                        break;
                    case R.id.buttonCh:
                        currentLanguage="zh";
                        break;
                }
                setLanguage(currentLanguage);
            }
        });

        Button backBtn =findViewById(R.id.button2);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }

    @SuppressLint("SetTextI18n")
    private void setLanguage(String languageCode) {
        Question qotd = QuestionSet.getInstance().getQuestionOfTheDay();
        System.out.println("Before : " + qotd.getName());
        translateString(qotd.getName());
        try {
            qotddd = getTranslatedText();
            System.out.println("Translated text: " + qotddd);
            // Call other methods that require the translated text
        } catch (InterruptedException e) {
            // Handle the interruption exception
        }
        System.out.println("QOTDD 되나여?: " + qotddd);

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

        System.out.println("port: " + portugueseTranslator.toString());
        System.out.println("eng: " + englishTranslator.toString());
        System.out.println("kor: " + koreanTranslator.toString());
    }



    public void translateString(String stringToTranslate) {
        translationLatch = new CountDownLatch(1);

        currentTranslator.translate(stringToTranslate)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String translatedText) {
                        qotddd = translatedText; // Assign the translated text
                        translationLatch.countDown(); // Release the latch
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        translationLatch.countDown(); // Release the latch in case of failure
                    }
                });
    }

    public String getTranslatedText() throws InterruptedException {
        translationLatch.await(); // Wait for the translation operation to complete
        return qotddd;
    }

//    // Usage example
//    translateString("Hello");
//
//try {
//        String translatedText = getTranslatedText();
//        System.out.println("Translated text: " + translatedText);
//        // Call other methods that require the translated text
//        doSomethingWithTranslatedText(translatedText);
//    } catch (InterruptedException e) {
//        // Handle the interruption exception
//    }

//    public void doSomethingWithTranslatedText(String translatedText) {
//        // Do something with the translated text
//        // ...
//        // You can also access the global variable directly
//        System.out.println("Global variable: " + qotddd);
//    }


//    // Usage example
//    translateString("Hello", new TranslationCallback() {
//        @Override
//        public void onTranslationComplete(String translatedText) {
//            // You can access the translated text here
//            System.out.println("Translated text: " + translatedText);
//
//            // Call other methods that require the translated text
//            doSomethingWithTranslatedText(translatedText);
//        }
//    });

    public void doSomethingWithTranslatedText(String translatedText) {
        // Do something with the translated text
        // ...
    }


    public void translateApp() {
        System.out.println(QuestionSet.getInstance().getQuestionOfTheDay().getName());

    }


    @Override
    public void onTranslationComplete(String translatedText) {
        qotddd = translatedText;
    }
}
