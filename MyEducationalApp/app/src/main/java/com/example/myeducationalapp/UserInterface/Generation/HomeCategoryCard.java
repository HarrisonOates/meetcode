package com.example.myeducationalapp.UserInterface.Generation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myeducationalapp.MainActivity;

public class HomeCategoryCard extends GeneratedUserInterfaceElement {

    Color backgroundContainerColor;
    Drawable imageReference;
    String heading;
    String subheading;

    public HomeCategoryCard(Color backgroundContainerColor, Drawable imageReference, String heading, String subheading) {
        this.backgroundContainerColor = backgroundContainerColor;
        this.imageReference = imageReference;
        this.heading = heading;
        this.subheading = subheading;
    }

    @Override
    public GeneratedUserInterfaceElement generate() {

        // making ui elements within parent
        //ImageView image = new ImageView(MainActivity);




        // assigning to super ConstraintLayout parent
        //ConstraintLayout newHomeCategoryCard = new ConstraintLayout();


        return null;
    }


}
