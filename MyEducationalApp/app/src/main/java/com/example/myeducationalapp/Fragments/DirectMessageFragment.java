package com.example.myeducationalapp.Fragments;

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

import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseObserver;
import com.example.myeducationalapp.Message;
import com.example.myeducationalapp.MessageBubbleOrientation;
import com.example.myeducationalapp.User.Person;
import com.example.myeducationalapp.R;
import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;
import com.example.myeducationalapp.User.UserLocalData;
import com.example.myeducationalapp.User.UserLogin;
import com.example.myeducationalapp.databinding.FragmentDirectMessageBinding;
import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;
import com.example.myeducationalapp.userInterface.UserDirectMessages;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe
 */
public class DirectMessageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        generateAllDirectMessageBubble(getActivity());

        // Adding observer onto this direct message thread, but only one will ever be added
        if (!UserDirectMessages.getInstance().hasObserverBeenAttached(messageRecipient)) {

            Firebase.getInstance().attachDirectMessageObserver(new DirectMessagesObserver(), UserLogin.getInstance().getCurrentUsername(), messageRecipient);
            UserDirectMessages.getInstance().setObserverHasBeenAttached(messageRecipient, true);
        }

        // Adding QOL for if you press enter in the message entry field
        binding.directMessageInputText.setOnKeyListener((view1, keyCode, keyEvent) -> {

            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (!binding.directMessageInputText.getText().toString().isBlank()) {
                    sendMessage(false);
                }
                return true;
            }

            return false;
        });

        // This is normal send button associated with the text entry field
        binding.directMessageSendButton.setOnClickListener(view1 -> {
            if (!binding.directMessageInputText.getText().toString().isBlank()) {
                sendMessage(false);
            }
        });
    }


    private void sendMessage(boolean isRecipient) {
        if (UserLocalData.getInstance().isUserBlocked(messageRecipient)) {
            Toast.makeText(getContext(),"User is blocked", Toast.LENGTH_LONG).show();
            return;
        }


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        DirectMessageThread dms = UserDirectMessages.getInstance().currentDirectMessages.get(messageRecipient).directMessageThread;

        dms.runWhenReady((obj) -> {

            dms.postMessage(binding.directMessageInputText.getText().toString());

            binding.directMessageLinearLayout.post(() -> {

                // We know this will always be sent by the current user and will always appear
                // on the right of the screen in the isRecipient=false state
                //
                // We need to do the opposite if isRecipient=true :)
                //
                // We also know that it will be the last message that we need to render
                //
                // wasLastRenderedMessageFromRecipient
                // l-> true: we need to render a SINGLE
                // l-> false: we need to check lastRenderedMessageOrientation
                //     l-> SINGLE: we need to update this SINGLE to a TOP and add new BOTTOM
                //     l-> BOTTOM: we need to make this a MIDDLE and add a new BOTTOM
                //     l-> null:   we have a brand new message thread and we need to render a SINGLE
                //
                // remember to update wasLastRenderedMessageFromRecipient and lastRenderedMessageOrientation

                MessageListCard messageListCard = UserDirectMessages.getInstance().currentDirectMessages.get(messageRecipient);
                List<Message> messages = messageListCard.directMessageThread.getMessages();
                Message messageToRender = messages.get(messages.size() - 1);

                generateIndividualBubble(isRecipient, messageToRender);
            });

            // Clearing input text
            binding.directMessageInputText.getText().clear();

            return null;
        });
    }

    private void generateIndividualBubble(boolean isRecipient, Message messageToRender) {
        if (wasLastRenderedMessageFromRecipient) {
            // if this message was not sent by the recipient
            if (!isRecipient) {
                // This is from the point of view of the sender
                // Rendering a new SINGLE
                int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                generateDirectMessageBubble(messageToRender, false, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, getActivity());

                // Remember to scroll to bottom
                binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                // Updating these globals
                wasLastRenderedMessageFromRecipient = false;
                lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;
            } else {
                // This is from the point of view of the recipient
                if (lastRenderedMessageOrientation == MessageBubbleOrientation.SINGLE) {
                    // last message being turned into a TOP
                    ((ConstraintLayout) binding.directMessageLinearLayout.getChildAt(binding.directMessageLinearLayout.getChildCount() - 1)).getChildAt(0).
                            setBackground(ContextCompat.getDrawable(getActivity(), MessageBubbleOrientation.TOP_RECIPIENT.drawableID));

                    // render new BOTTOM
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, true, MessageBubbleOrientation.BOTTOM_RECIPIENT, false, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = true;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;
                } else if (lastRenderedMessageOrientation == MessageBubbleOrientation.BOTTOM) {
                    // last message being turned into a MIDDLE
                    ((ConstraintLayout) binding.directMessageLinearLayout.getChildAt(binding.directMessageLinearLayout.getChildCount() - 1)).getChildAt(0).
                            setBackground(ContextCompat.getDrawable(getActivity(), MessageBubbleOrientation.MIDDLE_RECIPIENT.drawableID));
                    // render new BOTTOM
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, true, MessageBubbleOrientation.BOTTOM_RECIPIENT, false, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = true;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;
                } else {
                    // If we are here, this means that this is the first message being sent in the DM
                    // so just render a new SINGLE

                    // Rendering a new SINGLE
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, true, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = true;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;
                }
            }
        } else {
            if (!isRecipient) {
                // This is from the point of view of the sender
                if (lastRenderedMessageOrientation == MessageBubbleOrientation.SINGLE) {
                    // last message being turned into a TOP
                    ((ConstraintLayout) binding.directMessageLinearLayout.getChildAt(binding.directMessageLinearLayout.getChildCount() - 1)).getChildAt(0).
                            setBackground(ContextCompat.getDrawable(getActivity(), MessageBubbleOrientation.TOP.drawableID));

                    // render new BOTTOM
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, false, MessageBubbleOrientation.BOTTOM, false, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = false;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;
                } else if (lastRenderedMessageOrientation == MessageBubbleOrientation.BOTTOM) {
                    // last message being turned into a MIDDLE
                    ((ConstraintLayout) binding.directMessageLinearLayout.getChildAt(binding.directMessageLinearLayout.getChildCount() - 1)).getChildAt(0).
                            setBackground(ContextCompat.getDrawable(getActivity(), MessageBubbleOrientation.MIDDLE.drawableID));
                    // render new BOTTOM
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, false, MessageBubbleOrientation.BOTTOM, false, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = false;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.BOTTOM;
                } else {
                    // If we are here, this means that this is the first message being sent in the DM
                    // so just render a new SINGLE

                    // Rendering a new SINGLE
                    int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                    generateDirectMessageBubble(messageToRender, false, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, getActivity());

                    // Remember to scroll to bottom
                    binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                    // Updating these globals
                    wasLastRenderedMessageFromRecipient = false;
                    lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;
                }
            } else {
                // This is from the point of view of the recipient
                // Rendering a new SINGLE
                int currentMessageIndex = binding.directMessageLinearLayout.getChildCount();
                generateDirectMessageBubble(messageToRender, true, MessageBubbleOrientation.SINGLE, true, currentMessageIndex, getActivity());

                // Remember to scroll to bottom
                binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

                // Updating these globals
                wasLastRenderedMessageFromRecipient = true;
                lastRenderedMessageOrientation = MessageBubbleOrientation.SINGLE;
            }
        }
    }

    private void generateAllDirectMessageBubble(Context context) {
        MessageListCard messageListCard = UserDirectMessages.getInstance().currentDirectMessages.get(messageRecipient);
        String currentUsername = UserLogin.getInstance().getCurrentUsername();

        // If messageListCard is null here then it means that we're starting a new message thread
        // hence there's nothing we should be rendering and we should stop
        if (messageListCard == null) {
            return;
        }

        DirectMessageThread messageThread = new DirectMessageThread(messageRecipient); //messageListCard.directMessageThread.getMessages();

        messageThread.runWhenReady((ignored) -> {

            List<Message> messages = messageThread.getMessages();


            int currentMessageIndex = 0;

            for (int i = 0, messagesSize = messages.size(); i < messagesSize; i++) {
                Message firstMessage = messages.get(i);
                Person currentPoster = firstMessage.getPoster();

                boolean isRecipient = !Objects.equals(currentUsername, currentPoster.getUsername());

                // making sure that there is a next message
                if (UserLocalData.getInstance().isUserBlocked(messageRecipient)) return null;
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

            binding.directMessageScrollView.post(() -> binding.directMessageScrollView.fullScroll(View.FOCUS_DOWN));

            return null;
        });

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
        // Checking to see if message includes a code block
        if (message.getContent().contains("```")) {
            messageText.setText(Html.fromHtml(DetectCodeBlock.parseCodeBlocks(message.getContent())));
        } else {
            messageText.setText(message.getContent());
        }
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
        constraintLayout.addView(messageContainerConstraintLayout, 0);
        constraintLayout.addView(likeContainerConstraintLayout, 1);

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
            Message current = UserDirectMessages.getInstance().currentDirectMessages.get(messageRecipient).directMessageThread.getMessages().get(currentMessageIndex);

            current.runWhenReady((ignored) -> {
                current.toggleLikedByCurrentUser();
                return null;
            });

            return false;
        });

        // Long click to add a like
        messageContainerConstraintLayout.setOnLongClickListener(view -> {

            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            Message current = UserDirectMessages.getInstance().currentDirectMessages.get(messageRecipient).directMessageThread.getMessages().get(currentMessageIndex);

            current.runWhenReady((ignored) -> {
                current.toggleLikedByCurrentUser();
                return null;
            });

            return false;
        });
    }

    class DirectMessagesObserver extends FirebaseObserver {

        public void update() {
            FirebaseObserver observer = this;

            DirectMessageThread dms = new DirectMessageThread(messageRecipient);

            UserDirectMessages instance = UserDirectMessages.getInstance();

            // If we don't have any data to update we should just, y'know, not update it
            // or something. Maybe return as well
            if (instance.isEmpty()) {
                Log.d("DBG", "REACHED RETURN (A)");
                observer.enable();
                return;
            }

            dms.runWhenReady((obj) -> {
                Log.d("DBG", "REACHED DMS READY");

                // 1. Check if there is any difference between firebase direct messages and
                //    local direct messages
                // 2. Update them if there is a difference, otherwise do nothing
                // 3. Then if there is a difference update the UI
                //    l-> refresh only what needs to be refreshed in terms of liked messages and
                //        then add new messages one-by-one, instead of generating the entire
                //        LinearLayout lineage

                List<Message> localMessages = instance.currentDirectMessages
                        .get(messageRecipient).directMessageThread.messages;
                List<Message> firebaseMessages = dms.getMessages();

                // firebase Messages are newer than old ones
                if (!localMessages.equals(firebaseMessages)) {
                    Log.d("DBG", "GOT NEW DATA");

                    // hence we assign the new DirectMessageThread to the view model
                    instance.currentDirectMessages
                            .get(messageRecipient).directMessageThread = dms;

                    // Updating likes on messages
                    // Going through previous messages and checking if their likes are different
                    for (int i = 0; i < firebaseMessages.size(); i++) {
                        Message firebaseMessage = firebaseMessages.get(i);

                        // if the length of firebaseMessage is greater than the length of the
                        // localMessages list then we know that firebaseMessage is newer than
                        // localMessages
                        // hence, break
                        if (i >= localMessages.size()) {
                            // adding new messages to the UI
                            List<Message> messagesToRender = firebaseMessages.subList
                                    (localMessages.size(), firebaseMessages.size());

                            messagesToRender.forEach(message -> {
                                boolean isRecipient = message.getPoster().getUsername().equals(messageRecipient);

                                // if the person's that we're messaging has the same username as us
                                // then isRecipient will always be false
                                // this is nice for when we are messaging ourself
                                if (message.getPoster().getUsername().equals(UserLogin.getInstance().getCurrentUsername())) {
                                    isRecipient = false;
                                }

                                generateIndividualBubble(isRecipient, message);
                            });

                            Log.d("DBG", "BREAKING (1)");

                            break;
                        }

                        // we know that i is a valid index of localMessage now
                        Message localMessage = localMessages.get(i);

                        if (true || firebaseMessage.getLikeCount() != localMessage.getLikeCount()) {
                            // we have to update the ui for this element in the UI
                            ConstraintLayout parent = (ConstraintLayout) binding.directMessageLinearLayout.getChildAt(i);

                            // If the parent we have gotten is a null reference we should break out of this
                            // loop and continue on our day like nothing ever happened.
                            if (parent == null) {
                                Log.d("DBG", "BREAKING (2)");
                                break;
                            }

                            ConstraintLayout likeContainer = (ConstraintLayout) parent.getChildAt(1);
                            TextView likeText = (TextView) likeContainer.getChildAt(0);
                            Log.d("DBG", "REACHED LIKE UPDATER");
                            Log.d("DBG", "-> " + firebaseMessage.getContent() + ", " + firebaseMessage.getLikeCount());

                            // Override firebase message's like count onto the current UI
                            switch (firebaseMessage.getLikeCount()) {
                                case 0:
                                    // no likes = no like container
                                    likeContainer.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    likeContainer.setVisibility(View.VISIBLE);
                                    likeText.setText("❤️");
                                    break;
                                default:
                                    likeContainer.setVisibility(View.VISIBLE);
                                    likeText.setText("❤️  " + firebaseMessage.getLikeCount());
                                    break;
                            }
                        }
                    }
                }

                Log.d("DBG", "DONE");

                observer.enable();
                return null;
            });
        }
    }


}

