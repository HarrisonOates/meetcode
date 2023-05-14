package com.example.myeducationalapp.userInterface.Generation;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myeducationalapp.DirectMessageThread;

import java.util.Objects;

public class MessageListCard {

    public int profileImage;
    public String headingText;
    public String subHeadingText;
    public boolean isNotification;
    public DirectMessageThread directMessageThread;

    public MessageListCard(int profileImage, String headingText, String subHeadingText, DirectMessageThread directMessageThread) {
        this.profileImage = profileImage;
        this.headingText = headingText;
        this.subHeadingText = subHeadingText;
        this.directMessageThread = directMessageThread;
        this.isNotification = false;
    }

    @Override
    public int hashCode() {

        // Using headingText, which will be the user's unique username as a hashcode
        return Objects.hashCode(headingText);
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        MessageListCard messageListCard = (MessageListCard) obj;

        return Objects.equals(this.headingText, messageListCard.headingText);
    }

}
