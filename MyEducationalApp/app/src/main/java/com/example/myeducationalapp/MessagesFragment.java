package com.example.myeducationalapp;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.databinding.FragmentMessagesBinding;
import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;
import com.example.myeducationalapp.userInterface.UserDirectMessages;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.google.android.material.divider.MaterialDivider;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe
 */
public class MessagesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMessagesBinding binding;

    private final String toolbarTitle = "Chats";

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
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
        binding = FragmentMessagesBinding.inflate(inflater, container, false);

        // If we don't have any direct message threads currently, we need to fetch them from firebase
        if (UserDirectMessages.getInstance().isEmpty()) {
            Firebase.getInstance().getAllUsersYouHaveMessaged(dms -> {
                storeAllMessageThreads(dms);

                return null;
            });

        } else {
            // Otherwise just render what we have stored currently
            generateAllMessageListCards();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(toolbarTitle, false);

        // resetting previous error states
        GradientDrawable incorrectTextBox = (GradientDrawable) binding.messagesTextContainer.getBackground();
        incorrectTextBox.setStroke(0, Color.parseColor("#FFFF102D"));

        // sending new DM to user through the search bar
        binding.messagesSendButton.setOnClickListener(view1 -> {
            initializeNewDirectMessage();
        });

        // sending new DM to user by pressing enter on the input text field for it
        binding.messagesSendNewMessageInputText.setOnKeyListener((view1, keyCode, keyEvent) -> {

            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                initializeNewDirectMessage();
                return true;
            }

            return false;
        });
    }

    private void initializeNewDirectMessage() {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        String usernameToDirectMessage = binding.messagesSendNewMessageInputText.getText().toString();

        // Just some validation so we aren't wasting time querying Firebase with noting
        if (!usernameToDirectMessage.isBlank() && !usernameToDirectMessage.isEmpty()) {
            // Querying Firebase to see if the username is in there
            Firebase.getInstance().readAllUsernamesAsync().then((obj) -> {
                List<String> usernames = (List<String>) obj;

                // Does the username exist?
                if (usernames.contains(binding.messagesSendNewMessageInputText.getText().toString())) {
                    userInterfaceManager.getUiState().getValue().setToolbarTitle(usernameToDirectMessage);

                    // If user is already someone we have messaged then we don't have to worry about setting up new local data
                    // to support their DM, as it already exists
                    boolean isUserInLocalDirectMessages = UserDirectMessages.getInstance().doesUserExistInDirectMessages(usernameToDirectMessage);

                    if (!isUserInLocalDirectMessages) {
                        // creating local DirectMessageThread
                        DirectMessageThread dms = new DirectMessageThread(usernameToDirectMessage);

                        // adding new user to our ViewModel data
                        MessageListCard template = new MessageListCard(R.drawable.user_profile_default, usernameToDirectMessage, "", dms);
                        UserDirectMessages.getInstance().currentDirectMessages.put(usernameToDirectMessage, template);
                        UserDirectMessages.getInstance().currentDirectMessages.get(template.headingText).isNotification = false;
                    }

                    binding.messagesSendNewMessageInputText.getText().clear();
                    // Removing error state
                    GradientDrawable incorrectTextBox = (GradientDrawable) binding.messagesTextContainer.getBackground();
                    incorrectTextBox.setStroke(0, Color.parseColor("#FFFF102D"));

                    // Going to the direct message fragment
                    NavHostFragment.findNavController(MessagesFragment.this).navigate(R.id.action_messagesFragment_to_directMessageFragment);
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "This user does not exist, is their name spelt correctly?", Toast.LENGTH_LONG);
                    toast.show();

                    // Error state: thickened borders around text entry box
                    GradientDrawable incorrectTextBox = (GradientDrawable) binding.messagesTextContainer.getBackground();
                    incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                            Color.parseColor("#FFFF102D"));
                }

                return null;
            });
        } else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "This user does not exist, is their name spelt correctly?", Toast.LENGTH_LONG);
            toast.show();

            // Error state: thickened borders around text entry box
            GradientDrawable incorrectTextBox = (GradientDrawable) binding.messagesTextContainer.getBackground();
            incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                    Color.parseColor("#FFFF102D"));
        }
    }

    private void storeAllMessageThreads(DirectMessageThread dms) {
        String directMessageRecipient = dms.getUsername();

        MessageListCard template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, "", dms);

        if (dms.getMessages().size() > 0) {
            Message lastDirectMessage = dms.getMessages().get(dms.getMessages().size() - 1);

            if (Objects.equals(lastDirectMessage.getPoster().getUsername(), UserLogin.getInstance().getCurrentUsername())) {
                template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, "You: " + lastDirectMessage.getContent(), dms);
            } else {
                template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, lastDirectMessage.getContent(), dms);
            }
        }

        UserDirectMessages.getInstance().currentDirectMessages.put(directMessageRecipient, template);

        generateMessageListCard(template, getActivity(), template.isNotification, directMessageRecipient);
    }

    public void getAllUsersYouHaveMessagedCallback(DirectMessageThread directMessageThread) {

    }

    private void generateAllMessageListCards() {
        UserDirectMessages.getInstance().currentDirectMessages.values().forEach(messageListCard -> {
            // Updating the subheading of the message card before rendering
            // first checking if there are any messages
            if (messageListCard.directMessageThread.getMessages().size() > 0) {
                if (UserLocalData.getInstance().isUserBlocked(messageListCard.directMessageThread.getUsername())) {
                    messageListCard.subHeadingText = "This user is blocked";
                }
                else messageListCard.subHeadingText = messageListCard.directMessageThread.getMessages().
                        get(messageListCard.directMessageThread.getMessages().size() - 1).getContent();
            }

            // "" is default set for when a new DM is made, so we don't want to show if a new DM was made
            // and a message was never sent in that newly made DM
            if (!Objects.equals(messageListCard.subHeadingText, "")) {
                generateMessageListCard(messageListCard, getActivity(), messageListCard.isNotification, messageListCard.directMessageThread.getUsername());
            }
        });
    }

    private ConstraintLayout generateMessageListCard(MessageListCard template, Context context, boolean isNotification, String username) {
        // making ui elements within parent
        ImageView image = new ImageView(context);
        TextView heading = new TextView(context);
        TextView subheading = new TextView(context);
        View notificationDot = new View(context);
        MaterialDivider divider = new MaterialDivider(context);
        // constraint layout stuff
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        ConstraintSet imageConstraintSet = new ConstraintSet();
        ConstraintSet headingConstraintSet = new ConstraintSet();
        ConstraintSet subheadingConstraintSet = new ConstraintSet();
        ConstraintSet notificationDotConstraintSet = new ConstraintSet();
        ConstraintSet dividerConstraintSet = new ConstraintSet();

        constraintLayout.setId(View.generateViewId());

        // TODO set this using imageReference
        image.setImageResource(template.profileImage);
        image.setId(View.generateViewId());
        image.setOnClickListener(v -> {
            var popup = new PopupMenu(getContext(), image);

            popup.getMenuInflater().inflate(R.menu.blocking_menu, popup.getMenu());

            if (UserLocalData.getInstance().isUserBlocked(username)) popup.getMenu().getItem(0).setTitle("unblock");
            else popup.getMenu().getItem(0).setTitle("block");

            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("unblock")) {
                    if (UserLocalData.getInstance().isUserBlocked(username)) {
                        UserLocalData.getInstance().toggleBlockUser(username);
                        popup.getMenu().getItem(0).setTitle("block");
                    }
                    else popup.getMenu().getItem(0).setTitle("block");;
                    popup.show();
                    return true;
                }
                else if (item.getTitle().equals("block")) {
                    if (!UserLocalData.getInstance().isUserBlocked(username)) {
                        UserLocalData.getInstance().toggleBlockUser(username);
                        popup.getMenu().getItem(0).setTitle("unblock");;
                    }
                    else popup.getMenu().getItem(0).setTitle("unblock");;
                    popup.show();
                    return true;
                }
                popup.show();
                return false;
            });

            popup.show();
        });

        heading.setSingleLine();
        heading.setEllipsize(TextUtils.TruncateAt.END);
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        heading.setText(template.headingText);
        Typeface headingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_bold);
        heading.setTypeface(headingTypeface);
        heading.setTextColor(Color.parseColor("#000000"));
        heading.setId(View.generateViewId());


        subheading.setSingleLine();
        subheading.setEllipsize(TextUtils.TruncateAt.END);
        subheading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        subheading.setText(template.subHeadingText);
        Typeface subheadingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans);
        subheading.setTypeface(subheadingTypeface);
        subheading.setId(View.generateViewId());

        notificationDot.setBackground(ContextCompat.getDrawable(context, R.drawable.icon_notification_dot));
        notificationDot.setId(View.generateViewId());

        divider.setDividerThickness((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        divider.setDividerColor(Color.parseColor("#A6000000"));
        divider.setId(View.generateViewId());

        constraintLayout.addView(image, 0);
        constraintLayout.addView(heading, 1);
        constraintLayout.addView(subheading, 2);
        constraintLayout.addView(notificationDot, 3);
        constraintLayout.addView(divider, 4);

        imageConstraintSet.clone(constraintLayout);
        imageConstraintSet.connect(image.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        imageConstraintSet.connect(image.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
        imageConstraintSet.applyTo(constraintLayout);

        headingConstraintSet.clone(constraintLayout);
        headingConstraintSet.connect(heading.getId(), ConstraintSet.START, image.getId(), ConstraintSet.END, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())));
        headingConstraintSet.connect(heading.getId(), ConstraintSet.TOP, image.getId(), ConstraintSet.TOP);
        headingConstraintSet.connect(heading.getId(), ConstraintSet.END, notificationDot.getId(), ConstraintSet.START, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        headingConstraintSet.applyTo(constraintLayout);

        subheadingConstraintSet.clone(constraintLayout);
        subheadingConstraintSet.connect(subheading.getId(), ConstraintSet.START, heading.getId(), ConstraintSet.START);
        subheadingConstraintSet.connect(subheading.getId(), ConstraintSet.TOP, heading.getId(), ConstraintSet.BOTTOM, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())));
        subheadingConstraintSet.connect(subheading.getId(), ConstraintSet.END, heading.getId(), ConstraintSet.END);
        subheadingConstraintSet.applyTo(constraintLayout);

        notificationDotConstraintSet.clone(constraintLayout);
        notificationDotConstraintSet.connect(notificationDot.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        notificationDotConstraintSet.connect(notificationDot.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())));
        notificationDotConstraintSet.connect(notificationDot.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
        notificationDotConstraintSet.applyTo(constraintLayout);

        dividerConstraintSet.clone(constraintLayout);
        dividerConstraintSet.connect(divider.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        dividerConstraintSet.connect(divider.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
        dividerConstraintSet.connect(divider.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
        dividerConstraintSet.applyTo(constraintLayout);

        binding.messagesCardLinearLayout.addView(constraintLayout);

        LinearLayout.LayoutParams constraintLayoutParams = (LinearLayout.LayoutParams) constraintLayout.getLayoutParams();
        constraintLayoutParams.width = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics()));
        constraintLayoutParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105, getResources().getDisplayMetrics()));
        constraintLayoutParams.topMargin = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        constraintLayoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        constraintLayout.setLayoutParams(constraintLayoutParams);

        ViewGroup.LayoutParams imageLayoutParams = image.getLayoutParams();
        imageLayoutParams.width = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        imageLayoutParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        image.setLayoutParams(imageLayoutParams);

        ViewGroup.LayoutParams headingLayoutParams = heading.getLayoutParams();
        headingLayoutParams.width = 0;
        headingLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        heading.setLayoutParams(headingLayoutParams);

        ViewGroup.LayoutParams subheadingLayoutParams = subheading.getLayoutParams();
        subheadingLayoutParams.width = 0;
        subheadingLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        subheading.setLayoutParams(subheadingLayoutParams);

        ViewGroup.LayoutParams notificationDotLayoutParams = notificationDot.getLayoutParams();
        notificationDotLayoutParams.width = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
        notificationDotLayoutParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
        notificationDot.setLayoutParams(notificationDotLayoutParams);

        ViewGroup.LayoutParams dividerLayoutParams = divider.getLayoutParams();
        dividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dividerLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        divider.setLayoutParams(dividerLayoutParams);

        if (!isNotification) {
            // Notification dot
            constraintLayout.getChildAt(3).setVisibility(View.GONE);
        } else {
            // Subheading text
            ((TextView) constraintLayout.getChildAt(2)).setTypeface(headingTypeface);
        }

        constraintLayout.setOnClickListener(view -> {

            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.getUiState().getValue().setToolbarTitle(heading.getText().toString());
            UserDirectMessages.getInstance().currentDirectMessages.get(template.headingText).isNotification = false;

            NavHostFragment.findNavController(MessagesFragment.this).navigate(R.id.action_messagesFragment_to_directMessageFragment);

        });

        return constraintLayout;

    }
}