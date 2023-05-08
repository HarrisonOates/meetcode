package com.example.myeducationalapp.UserInterface.Generation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.myeducationalapp.R;

public class HomeCategoryCard extends GeneratedUserInterfaceElement {

    Color backgroundContainerColor;
    Drawable imageReference;
    public String headingText;
    public String subheadingText;

    public HomeCategoryCard(Color backgroundContainerColor, Drawable imageReference, String headingText, String subheadingText) {
        this.backgroundContainerColor = backgroundContainerColor;
        this.imageReference = imageReference;
        this.headingText = headingText;
        this.subheadingText = subheadingText;
    }

    @Override
    public GeneratedUserInterfaceElement generate() {




        return null;
    }


}
