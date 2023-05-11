package com.example.myeducationalapp.userInterface.Generation;

import android.graphics.drawable.Drawable;

public class MessageListCard extends GeneratedUserInterfaceElement{

    public int profileImage;
    public String headingText;
    public String subHeadingText;

    public MessageListCard(int profileImage, String headingText, String subHeadingText) {
        this.profileImage = profileImage;
        this.headingText = headingText;
        this.subHeadingText = subHeadingText;
    }

    @Override
    public GeneratedUserInterfaceElement generate() {
        return null;
    }

}
