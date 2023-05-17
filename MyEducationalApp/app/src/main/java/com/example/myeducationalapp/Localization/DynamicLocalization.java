package com.example.myeducationalapp.Localization;

import android.widget.TextView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class DynamicLocalization extends LanguageSetting {
    public static void translateText(String textToTranslate, final TextView textView) {
        // Since the given textToTranslate when it first gets called will always be in English,
        // Its source language should be English if the previous and current language are the same.
        String sourceLanguage = (isSameLangauge) ? "en" : previousLanguage;

        // Create translator options with the specified source and target language
        TranslatorOptions.Builder optionsBuilder = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(currentLanguage);

        // Build the translator
        TranslatorOptions options = optionsBuilder.build();
        Translator translator = Translation.getClient(options);

        // Download the translation model if needed
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        v -> {
                            // Model downloaded successfully, perform translation
                            translator.translate(textToTranslate)
                                    .addOnSuccessListener(
                                            translatedText -> {
                                                // Update the translated text in the TextView
                                                textView.setText(translatedText);
                                            })
                                    .addOnFailureListener(
                                            exception -> {
                                                // Handle translation failure
                                                textView.setText("Translation failed: " + exception.getMessage());
                                            });
                        })
                .addOnFailureListener(
                        exception -> {
                            // Handle model download failure
                            textView.setText("Model download failed: " + exception.getMessage());
                        });
    }

    public static void translatedOrDefaultText(String text, TextView textView) {
        try{
            translateText(text, textView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initial OnViewCreate will always execute the line below
        textView.setText(text);
    }
}
