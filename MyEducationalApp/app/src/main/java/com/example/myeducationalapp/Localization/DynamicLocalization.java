package com.example.myeducationalapp.Localization;

import android.widget.TextView;

import androidx.navigation.NavController;

import com.example.myeducationalapp.R;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.Objects;

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

    public static void translateToolBar(String textToTranslate, UserInterfaceManagerViewModel userInterfaceManager, boolean visibility) {
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
                                                // Update the translated text in the Toolbar
                                                userInterfaceManager.getUiState().getValue().enterNewFragment(translatedText, visibility);
                                            })
                                    .addOnFailureListener(
                                            exception -> {
                                                // Handle translation failure
                                                userInterfaceManager.getUiState().getValue().enterNewFragment(exception.toString());
                                            });
                        })
                .addOnFailureListener(
                        exception -> {
                            // Handle model download failure
                            userInterfaceManager.getUiState().getValue().enterNewFragment(exception.toString());
                        });

    }

    public static void navigateTranslatedToolBar(UserInterfaceManagerViewModel userInterfaceManager, NavController navController, String englishToolBar, int fragmentID) {
        // Create translator options with the specified source and target language
        TranslatorOptions.Builder optionsBuilder = new TranslatorOptions.Builder()
                .setSourceLanguage("en")
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
                            translator.translate(englishToolBar)
                                    .addOnSuccessListener(
                                            translatedText -> {
                                                if (!Objects.equals(userInterfaceManager.getUiState().getValue().getToolbarTitle().getValue(), translatedText)) {
                                                    //userInterfaceManager.getUiState().getValue().setIsActionBarInBackState(false);
                                                    navController.navigate(fragmentID);
                                                }
                                            })
                                    .addOnFailureListener(
                                            exception -> {
                                                // Handle translation failure
                                                userInterfaceManager.getUiState().getValue().enterNewFragment(exception.toString());
                                            });
                        })
                .addOnFailureListener(
                        exception -> {
                            // Handle model download failure
                            userInterfaceManager.getUiState().getValue().enterNewFragment(exception.toString());
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

    public static void translatedOrDefaultToolBar(String textToTranslate, UserInterfaceManagerViewModel userInterfaceManager, boolean visibility) {
        try {
            translateToolBar(textToTranslate, userInterfaceManager, visibility);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userInterfaceManager.getUiState().getValue().enterNewFragment(textToTranslate, visibility);
    }


}
