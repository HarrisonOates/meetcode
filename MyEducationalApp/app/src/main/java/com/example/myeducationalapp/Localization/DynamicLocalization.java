package com.example.myeducationalapp.Localization;


import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class DynamicLocalization extends LanguageSetting {
    public static void translateText(String textToTranslate, final TextView textView) {
        // Create translator options with the specified source and target language
        TranslatorOptions.Builder optionsBuilder = new TranslatorOptions.Builder()
                .setSourceLanguage(previousLanguage)
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


}
