package com.example.myeducationalapp.userInterface.Generation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class HomeCategoryCard {

    Color backgroundContainerColor;
    Drawable profileImageReference;
    public String headingText;
    public String subheadingText;

    public HomeCategoryCard(Color backgroundContainerColor, Drawable profileImageReference, String headingText, String subheadingText) {
        this.backgroundContainerColor = backgroundContainerColor;
        this.profileImageReference = profileImageReference;
        this.headingText = headingText;
        this.subheadingText = subheadingText;
    }

}
