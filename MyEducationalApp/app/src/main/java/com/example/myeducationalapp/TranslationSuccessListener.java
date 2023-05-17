package com.example.myeducationalapp;

import com.google.android.gms.tasks.OnSuccessListener;


public class TranslationSuccessListener implements OnSuccessListener<String> {

    private TranslationCallback callback;

    public TranslationSuccessListener(TranslationCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess(String translatedText) {
        if (callback != null) {
            callback.onTranslationComplete(translatedText);
        }
    }
}




