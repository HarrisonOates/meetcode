package com.example.myeducationalapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myeducationalapp.databinding.FragmentDirectMessageBinding;
import com.example.myeducationalapp.userInterface.Generation.MessageListCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectMessageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentDirectMessageBinding binding;
    // Username of person you're messaging
    private String messageRecipient;

    public DirectMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DirectMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectMessageFragment newInstance(String param1, String param2) {
        DirectMessageFragment fragment = new DirectMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDirectMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(false);
        // Username of person you're messaging, found throught the toolbar title (set by previous fragment)
        messageRecipient = userInterfaceManager.getUiState().getValue().getToolbarTitle().getValue();
        generateAllDirectMessageBubble(getActivity());

        binding.directMessageScrollView.fullScroll(ScrollView.FOCUS_DOWN);

        binding.directMessageSendButton.setOnClickListener(view1 -> {

            DirectMessageThread dms = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread;

            dms.runWhenReady((obj) -> {

                Log.d("DirectMessageFragment", "Start");
                dms.postMessage(binding.directMessageInputText.getText().toString());
                Log.d("DirectMessageFragment", "End");

                // TODO make this better
                binding.directMessageLinearLayout.post(() -> {
                    Log.d("DirectMessageFragment", "Start ui update");
                    binding.directMessageLinearLayout.removeAllViews();
                    generateAllDirectMessageBubble(getActivity());
                    Log.d("DirectMessageFragment", "End ui update");
                });

                //binding.directMessageLinearLayout.invalidate();
                return null;
            });
        });

    }

    private void generateAllDirectMessageBubble(Context context) {

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        MessageListCard messageListCard = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient);
        String currentUsername = UserLogin.getInstance().getCurrentUsername();

        List<Message> messages = messageListCard.directMessageThread.getMessages();

        for (int i = 0, messagesSize = messages.size(); i < messagesSize; i++) {
            Message firstMessage = messages.get(i);
            Person currentPoster = firstMessage.getPoster();
            boolean isRecipient = !Objects.equals(currentUsername, currentPoster.getUsername());

            // making sure that there is a next message
            if ((i + 1) < messagesSize) {

                if (messages.get(i + 1).getPoster().equals(currentPoster)) {
                    // We have multiple messages from the same poster
                    ArrayList<Message> currentPosterMessages = new ArrayList<>();

                    currentPosterMessages.add(firstMessage);
                    currentPosterMessages.add(messages.get(i + 1));

                    // loop until we reach a message from the next poster
                    for (int j = i + 2; j < messagesSize; j++) {

                        Message nextMessage = messages.get(j);

                        if (nextMessage.getPoster().equals(currentPoster)) {
                            currentPosterMessages.add(nextMessage);
                            i = j;
                        } else {
                            // If poster is not the currentPoster, we've reached the end of
                            // the posts from the currentPoster, so update i
                            // index and then break
                            i = j;
                            break;
                        }
                    }

                    // Draw currentPosterMessages to UI
                    generateDirectMessageBubble(currentPosterMessages.get(0), isRecipient, MessageBubbleOrientation.TOP, true, context);
                    for (int k = 1; k < currentPosterMessages.size() - 1; k++) {
                        generateDirectMessageBubble(currentPosterMessages.get(k), isRecipient, MessageBubbleOrientation.MIDDLE, false, context);
                    }
                    generateDirectMessageBubble(currentPosterMessages.get(currentPosterMessages.size() - 1), isRecipient, MessageBubbleOrientation.BOTTOM, false, getActivity());

                } else {
                    // The next message is from the other poster
                    // draw firstMessage to UI and continue with loop
                    generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE, true, context);
                }
            } else {
                // This is the last single message
                generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE, true, context);
            }
        }

        /*
         * Scroll down to the most recent message.
         */
        binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void generateDirectMessageBubble(Message message, boolean isRecipient, MessageBubbleOrientation messageBubbleOrientation, boolean isSeperate, Context context) {


        // Setting up ui elements
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        ConstraintLayout messageContainerConstraintLayout = new ConstraintLayout(context);
        ConstraintLayout likeContainerConstraintLayout = new ConstraintLayout(context);
        TextView messageText = new TextView(context);
        TextView likeText = new TextView(context);
        // Constraint sets
        ConstraintSet messageContainerConstraintSet = new ConstraintSet();
        ConstraintSet messageTextConstraintSet = new ConstraintSet();
        ConstraintSet likeContainerConstraintSet = new ConstraintSet();
        ConstraintSet likeTextConstraintSet = new ConstraintSet();

        constraintLayout.setId(View.generateViewId());

        messageContainerConstraintLayout.setBackground(ContextCompat.getDrawable(context, messageBubbleOrientation.recipientMapper(messageBubbleOrientation, isRecipient).drawableID));
        messageContainerConstraintLayout.setId(View.generateViewId());

        likeContainerConstraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.direct_message_like_bubble));
        likeContainerConstraintLayout.setId(View.generateViewId());

        messageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        messageText.setText(message.getContent());
        messageContainerConstraintLayout.setPadding(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics())));
        messageText.setMaxWidth(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics())));
        Typeface messageTextTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans);
        messageText.setTypeface(messageTextTypeface);
        messageText.setTextColor(Color.parseColor("#000000"));
        messageText.setId(View.generateViewId());

        likeText.setMaxLines(1);
        likeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        likeContainerConstraintLayout.setPadding(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())));
        Typeface likeTextTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_semibold);
        likeText.setTypeface(likeTextTypeface);
        likeText.setTextColor(Color.parseColor("#000000"));
        likeText.setId(View.generateViewId());

        messageContainerConstraintLayout.addView(messageText, 0);
        likeContainerConstraintLayout.addView(likeText, 0);
        constraintLayout.addView(messageContainerConstraintLayout);
        constraintLayout.addView(likeContainerConstraintLayout);

        likeTextConstraintSet.clone(likeContainerConstraintLayout);
        likeTextConstraintSet.connect(likeText.getId(), ConstraintSet.BOTTOM, likeContainerConstraintLayout.getId(), ConstraintSet.BOTTOM);
        likeTextConstraintSet.connect(likeText.getId(), ConstraintSet.END, likeContainerConstraintLayout.getId(), ConstraintSet.END);
        likeTextConstraintSet.connect(likeText.getId(), ConstraintSet.START, likeContainerConstraintLayout.getId(), ConstraintSet.START);
        likeTextConstraintSet.connect(likeText.getId(), ConstraintSet.TOP, likeContainerConstraintLayout.getId(), ConstraintSet.TOP);
        likeTextConstraintSet.applyTo(likeContainerConstraintLayout);

        likeContainerConstraintSet.clone(constraintLayout);
        if (isRecipient) {
            likeContainerConstraintSet.connect(likeContainerConstraintLayout.getId(), ConstraintSet.END, messageContainerConstraintLayout.getId(), ConstraintSet.END, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        } else {
            likeContainerConstraintSet.connect(likeContainerConstraintLayout.getId(), ConstraintSet.START, messageContainerConstraintLayout.getId(), ConstraintSet.START, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        }
        likeContainerConstraintSet.connect(likeContainerConstraintLayout.getId(), ConstraintSet.TOP, messageContainerConstraintLayout.getId(), ConstraintSet.BOTTOM, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -10, getResources().getDisplayMetrics())));
        likeContainerConstraintSet.applyTo(constraintLayout);

        messageTextConstraintSet.clone(messageContainerConstraintLayout);
        messageTextConstraintSet.connect(messageText.getId(), ConstraintSet.BOTTOM, messageContainerConstraintLayout.getId(), ConstraintSet.BOTTOM);
        messageTextConstraintSet.connect(messageText.getId(), ConstraintSet.END, messageContainerConstraintLayout.getId(), ConstraintSet.END);
        messageTextConstraintSet.connect(messageText.getId(), ConstraintSet.START, messageContainerConstraintLayout.getId(), ConstraintSet.START);
        messageTextConstraintSet.connect(messageText.getId(), ConstraintSet.TOP, messageContainerConstraintLayout.getId(), ConstraintSet.TOP);
        messageContainerConstraintSet.applyTo(messageContainerConstraintLayout);

        messageContainerConstraintSet.clone(constraintLayout);
        messageContainerConstraintSet.connect(messageContainerConstraintLayout.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
        messageContainerConstraintSet.connect(messageContainerConstraintLayout.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
        messageContainerConstraintSet.connect(messageContainerConstraintLayout.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
        messageContainerConstraintSet.applyTo(constraintLayout);

        binding.directMessageLinearLayout.addView(constraintLayout);

        LinearLayout.LayoutParams constraintLayoutParams = (LinearLayout.LayoutParams) constraintLayout.getLayoutParams();
        constraintLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        constraintLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        if (isRecipient) {
            // Sent by recipient
            constraintLayoutParams.gravity = Gravity.START;
        } else {
            // Sent by current user
            constraintLayoutParams.gravity = Gravity.END;
        }

        if (isSeperate) {
            // Message is a reply to one from the other user
            constraintLayoutParams.topMargin = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
        } else {
            // Message is in a chain of multiple messages sent by the same user
            constraintLayoutParams.topMargin = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        }
        constraintLayout.setLayoutParams(constraintLayoutParams);

        ConstraintLayout.LayoutParams messageContainerLayoutParams = (ConstraintLayout.LayoutParams) messageContainerConstraintLayout.getLayoutParams();
        messageContainerLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        messageContainerLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        messageContainerConstraintLayout.setLayoutParams(messageContainerLayoutParams);

        ConstraintLayout.LayoutParams likeContainerLayoutParams = (ConstraintLayout.LayoutParams) likeContainerConstraintLayout.getLayoutParams();
        likeContainerLayoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        likeContainerLayoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        likeContainerConstraintLayout.setLayoutParams(likeContainerLayoutParams);

        ViewGroup.LayoutParams messageTextLayoutParams = messageText.getLayoutParams();
        messageTextLayoutParams.width = 0;
        messageTextLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        messageText.setLayoutParams(messageTextLayoutParams);

        ViewGroup.LayoutParams likeTextLayoutParams = messageText.getLayoutParams();
        likeTextLayoutParams.width = 0;
        likeTextLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        likeText.setLayoutParams(likeTextLayoutParams);

        // If post has likes
        if (message.getLikeCount() > 0) {
            if (message.getLikeCount() > 1) {
                likeText.setText("❤️  " + message.getLikeCount());
            } else {
                likeText.setText("❤️");
            }
        } else {
            // If post does not have likes
            likeContainerConstraintLayout.setVisibility(View.GONE);
        }

        // TODO set up double click listener for incrementing likes on message
    }

    private enum MessageBubbleOrientation {
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

        public MessageBubbleOrientation recipientMapper(MessageBubbleOrientation messageBubbleOrientation, boolean isRecipient) {
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

}