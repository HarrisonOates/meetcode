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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myeducationalapp.databinding.FragmentDirectMessageBinding;
import com.example.myeducationalapp.userInterface.Generation.MessageListCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    private boolean wasLastRenderedMessageFromRecipient;
    private MessageBubbleOrientation lastRenderedMessageOrientation;

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
        // Username of person you're messaging, found through the toolbar title (set by previous fragment)
        messageRecipient = userInterfaceManager.getUiState().getValue().getToolbarTitle().getValue();
        generateAllDirectMessageBubble(getActivity(), false);

        // Adding QOL for if you press enter in the message entry field
        binding.directMessageInputText.setOnKeyListener((view1, keyCode, keyEvent) -> {

            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!binding.directMessageInputText.getText().toString().isBlank()) {
                    sendMessage();
                }
                return true;
            }

            return false;
        });

        binding.directMessageSendButton.setOnClickListener(view1 -> {
            if (!binding.directMessageInputText.getText().toString().isBlank()) {
                sendMessage();
            }
        });

        Log.d("DMFragment", String.valueOf(wasLastRenderedMessageFromRecipient));
        Log.d("DMFragment", String.valueOf(lastRenderedMessageOrientation));



    }


    private void sendMessage() {

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        DirectMessageThread dms = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread;

        dms.runWhenReady((obj) -> {

            dms.postMessage(binding.directMessageInputText.getText().toString().replace("@", "at"));

            // TODO make this better
            binding.directMessageLinearLayout.post(() -> {

                // We know this will always be sent by the current user and will always appear
                // on the right of the screen in the isRecipient=false state
                //
                // We also know that it will be the last message that we need to render
                //
                // wasLastRenderedMessageFromRecipient
                // l-> true: we need to render a SINGLE
                // l-> false: we need to check lastRenderedMessageOrientation
                //     l-> SINGLE: we need to update this SINGLE to a TOP and add new BOTTOM
                //     l-> BOTTOM: we need to make this a MIDDLE and add a new BOTTOM
                //
                // remember to update wasLastRenderedMessageFromRecipient and lastRenderedMessageOrientation

                MessageListCard messageListCard = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient);
                List<Message> messages = messageListCard.directMessageThread.getMessages();

                if (wasLastRenderedMessageFromRecipient) {
                    // Rendering a new SINGLE
                    Message messageToRender = messages.get(messages.size() - 1);
                    boolean isRecipient = false;
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, isRecipient, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = false;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;
                } else {
                    if (lastRenderedMessageOrientation == MessageBubbleOrientation.SINGLE) {
                        // last message being turned into a TOP
                        binding.directMessageLinearLayout.getChildAt(binding.directMessageLinearLayout.getChildCount() - 1).setBackground(ContextCompat.getDrawable(getActivity(), MessageBubbleOrientation.TOP.drawableID));

                        // render new BOTTOM
                        Message messageToRender = messages.get(messages.size() - 1);
                        boolean isRecipient = false;
                        int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                        generateDirectMessageBubble(messageToRender, isRecipient, MessageBubbleOrientation.BOTTOM, false, currentMessageIndex, getActivity());

                        // Remember to scroll to bottom
                        binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                        // Updating these globals
                        wasLastRenderedMessageFromRecipient = false;
                        lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;
                    } else if (lastRenderedMessageOrientation == MessageBubbleOrientation.BOTTOM) {
                        // last message being turned into a MIDDLE
                        binding.directMessageLinearLayout.getChildAt(binding.directMessageLinearLayout.getChildCount() - 1).setBackground(ContextCompat.getDrawable(getActivity(), MessageBubbleOrientation.MIDDLE.drawableID));

                        // render new BOTTOM
                        Message messageToRender = messages.get(messages.size() - 1);
                        boolean isRecipient = false;
                        int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                        generateDirectMessageBubble(messageToRender, isRecipient, MessageBubbleOrientation.BOTTOM, false, currentMessageIndex, getActivity());

                        // Remember to scroll to bottom
                        binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                        // Updating these globals
                        wasLastRenderedMessageFromRecipient = false;
                        lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;
                    }
                }
            });

            return null;
        });
    }

    private void generateAllDirectMessageBubble(Context context, boolean isImmediate) {

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        MessageListCard messageListCard = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient);
        String currentUsername = UserLogin.getInstance().getCurrentUsername();

        List<Message> messages = messageListCard.directMessageThread.getMessages();

        int currentMessageIndex = 0;

        int maxLoop = messages.size();

        if (isImmediate) {
            maxLoop -= 1;
        }

        for (int i = 0, messagesSize = maxLoop; i < messagesSize; i++) {
            Message firstMessage = messages.get(i);
            Person currentPoster = firstMessage.getPoster();

            boolean isRecipient = !Objects.equals(currentUsername, currentPoster.getUsername());

            // TODO the following may not always be true
            // making sure that there is a next message
            if ((i + 1) < messagesSize) {

                if (messages.get(i + 1).getPoster().equals(currentPoster)) {
                    // We have multiple messages from the same poster
                    ArrayList<Message> currentPosterMessages = new ArrayList<>();

                    currentPosterMessages.add(firstMessage);
                    // TODO when it gets to this line using the OR condition
                    // the following might not belong to the same author
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
                            i = j - 1;

                            break;
                        }
                    }

                    // Draw currentPosterMessages to UI
                    generateDirectMessageBubble(currentPosterMessages.get(0), isRecipient, MessageBubbleOrientation.TOP, true, currentMessageIndex, context);
                    currentMessageIndex++;

                    for (int k = 1; k < currentPosterMessages.size() - 1; k++) {
                        generateDirectMessageBubble(currentPosterMessages.get(k), isRecipient, MessageBubbleOrientation.MIDDLE, false, currentMessageIndex, context);
                        currentMessageIndex++;
                    }

                    generateDirectMessageBubble(currentPosterMessages.get(currentPosterMessages.size() - 1), isRecipient, MessageBubbleOrientation.BOTTOM, false, currentMessageIndex, getActivity());
                    currentMessageIndex++;

                    wasLastRenderedMessageFromRecipient = isRecipient;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;

                } else {
                    // The next message is from the other poster
                    // draw firstMessage to UI and continue with loop
                    generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, context);
                    currentMessageIndex++;

                    wasLastRenderedMessageFromRecipient = isRecipient;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;

                }
            } else {
                // If we're at the last message
                if (i - 1 >= 0) {

                    if (i == messagesSize - 1 && !messages.get(i - 1).getPoster().equals(currentPoster)) {
                        // This is the last single message
                        generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, context);
                        currentMessageIndex++;
                    }
                } else {
                    // This is the last single message
                    generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, context);
                    currentMessageIndex++;
                }

                wasLastRenderedMessageFromRecipient = isRecipient;
                lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;
            }
        }

        Log.d("DMFragment", String.valueOf(binding.directMessageLinearLayout.getChildCount() - 1));
        Log.d("DMFragment", String.valueOf(currentMessageIndex));

        /*
         * Scroll down to the most recent message.
         */
        binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void generateDirectMessageBubble(Message message, boolean isRecipient, MessageBubbleOrientation messageBubbleOrientation, boolean isSeperate, int currentMessageIndex, Context context) {


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

        messageContainerConstraintLayout.setBackground(ContextCompat.getDrawable(context, MessageBubbleOrientation.recipientMapper(messageBubbleOrientation, isRecipient).drawableID));
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

        // Long click to remove like when clicking on like container
        likeContainerConstraintLayout.setOnLongClickListener(view -> {

            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread.getMessages().get(currentMessageIndex).toggleLikedByCurrentUser();

            int newLikeCount = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread.getMessages().get(currentMessageIndex).getLikeCount();

            if (newLikeCount > 0) {
                likeContainerConstraintLayout.setVisibility(View.VISIBLE);

                if (newLikeCount > 1) {
                    likeText.setText("❤️  " + newLikeCount);
                } else {
                    likeText.setText("❤️");
                }

            } else {
                likeContainerConstraintLayout.setVisibility(View.GONE);
            }

            return false;
        });

        // Long click to add a like
        messageContainerConstraintLayout.setOnLongClickListener(view -> {

            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread.getMessages().get(currentMessageIndex).toggleLikedByCurrentUser();

            int newLikeCount = userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread.getMessages().get(currentMessageIndex).getLikeCount();

            if (newLikeCount > 0) {
                likeContainerConstraintLayout.setVisibility(View.VISIBLE);

                userInterfaceManager.getCurrentDirectMessages().getValue().get(messageRecipient).directMessageThread.getMessages().forEach(x -> {
                        Log.d("DirectMessageFragment", String.valueOf(x.getContent()));

                });


                if (newLikeCount > 1) {
                    likeText.setText("❤️  " + newLikeCount);
                } else {
                    likeText.setText("❤️");
                }

            } else {
                likeContainerConstraintLayout.setVisibility(View.GONE);
            }

            return false;
        });
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

}