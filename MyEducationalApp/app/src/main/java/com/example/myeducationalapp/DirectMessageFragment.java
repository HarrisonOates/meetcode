package com.example.myeducationalapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                if (messages.get(i + 1).getPoster() == currentPoster) {
                    // We have multiple messages from the same poster
                    ArrayList<Message> currentPosterMessages = new ArrayList<>();

                    currentPosterMessages.add(firstMessage);
                    currentPosterMessages.add(messages.get(i + 1));

                    // loop until we reach a message from the next poster
                    for (int j = i + 1; j < messagesSize; j++) {
                        Message nextMessage = messages.get(j);

                        if (nextMessage.getPoster() == currentPoster) {
                            currentPosterMessages.add(nextMessage);
                        } else {
                            // If poster is not the currentPoster, we've reached the end of
                            // the posts from the currentPoster, so update i-1 (to account for increment)
                            // index and then break
                            i = j - 1;
                            break;
                        }
                    }

                    // Draw currentPosterMessages to UI
                    generateDirectMessageBubble(currentPosterMessages.get(0), isRecipient, MessageBubbleOrientation.TOP);
                    for (int k = 1; k < currentPosterMessages.size() - 1; k++) {
                        generateDirectMessageBubble(currentPosterMessages.get(k), isRecipient, MessageBubbleOrientation.MIDDLE);
                    }
                    generateDirectMessageBubble(currentPosterMessages.get(currentPosterMessages.size() - 1), isRecipient, MessageBubbleOrientation.BOTTOM);

                } else {
                    // The next message is from the other poster
                    // draw firstMessage to UI and continue with loop
                    generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE);
                }
            } else {
                // This is the last single message
                generateDirectMessageBubble(firstMessage, isRecipient, MessageBubbleOrientation.SINGLE);
            }
        }

    }

    private void generateDirectMessageBubble(Message message, boolean isRecipient, MessageBubbleOrientation messageBubbleOrientation) {



    }

    private enum MessageBubbleOrientation {
        BOTTOM(R.drawable.direct_message_bubble_combo_bottom),
        MIDDLE(R.drawable.direct_message_bubble_combo_middle),
        TOP(R.drawable.direct_message_bubble_combo_top),
        SINGLE(R.drawable.direct_message_bubble_single);

        public final int drawableID;

        MessageBubbleOrientation(int drawableID) {
            this.drawableID = drawableID;
        }
    }

}