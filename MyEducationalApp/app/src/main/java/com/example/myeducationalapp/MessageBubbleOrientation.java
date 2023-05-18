package com.example.myeducationalapp;

public enum MessageBubbleOrientation {
    BOTTOM(R.drawable.direct_message_bubble_combo_bottom),
    BOTTOM_RECIPIENT(R.drawable.direct_message_recipient_bubble_combo_bottom),
    MIDDLE(R.drawable.direct_message_bubble_combo_middle),
    MIDDLE_RECIPIENT(R.drawable.direct_message_recipient_bubble_combo_middle),
    TOP(R.drawable.direct_message_bubble_combo_top),
    TOP_RECIPIENT(R.drawable.direct_message_recipient_bubble_combo_top),
    SINGLE(R.drawable.direct_message_bubble_single);

    public final int drawableID;

    MessageBubbleOrientation(int drawableID) {
        this.drawableID = drawableID;
    }

    public static MessageBubbleOrientation recipientMapper(MessageBubbleOrientation messageBubbleOrientation, boolean isRecipient) {
        if (isRecipient) {
            if (messageBubbleOrientation == BOTTOM) {
                return BOTTOM_RECIPIENT;
            } else if (messageBubbleOrientation == MIDDLE) {
                return MIDDLE_RECIPIENT;
            } else if (messageBubbleOrientation == TOP) {
                return TOP_RECIPIENT;
            }
        }
        return messageBubbleOrientation;
    }
}
