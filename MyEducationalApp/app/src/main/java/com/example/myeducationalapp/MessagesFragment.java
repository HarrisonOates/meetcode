package com.example.myeducationalapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.databinding.FragmentMessagesBinding;
import com.example.myeducationalapp.userInterface.Generation.MessageListCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.google.android.material.divider.MaterialDivider;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
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

    Firebase firebase;

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


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);

        Log.d("MessagesFragment", String.valueOf(userInterfaceManager.getCurrentDirectMessages().getValue().size()));

//        // If view model has no data then we need to load it from the server into the view model
//        if (userInterfaceManager.getCurrentDirectMessages().getValue().size() == 0) {
//
//            Firebase.getInstance().getAllUsersYouHaveMessaged(dms -> {
//
//                String directMessageRecipient = dms.getUsername();
//
//                MessageListCard template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, "THIS IS EMPTY", dms);
//
//                if (dms.getMessages().size() > 0) {
//                    Message lastDirectMessage = dms.getMessages().get(dms.getMessages().size() - 1);
//
//                    if (Objects.equals(lastDirectMessage.getPoster().getUsername(), UserLogin.getInstance().getCurrentUsername())) {
//                        template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, "You: " + lastDirectMessage.getContent(), dms);
//                    } else {
//                        template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, lastDirectMessage.getContent(), dms);
//                    }
//                }
//
//                userInterfaceManager.getCurrentDirectMessages().getValue().put(directMessageRecipient, template);
//                Log.d("MessagesFragment", String.valueOf(userInterfaceManager.getCurrentDirectMessages().getValue().size()));
//                //drawAllMessageListCards();
//
//                return null;
//            });
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(toolbarTitle, false);


        // do background stuff here
        if (userInterfaceManager.getCurrentDirectMessages().getValue().size() == 0) {
            Log.d("MessagesFragment", "Reload");
            Firebase.getInstance().getAllUsersYouHaveMessaged(dms -> {

                String directMessageRecipient = dms.getUsername();

                MessageListCard template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, "THIS IS EMPTY", dms);

                if (dms.getMessages().size() > 0) {
                    Message lastDirectMessage = dms.getMessages().get(dms.getMessages().size() - 1);

                    if (Objects.equals(lastDirectMessage.getPoster().getUsername(), UserLogin.getInstance().getCurrentUsername())) {
                        template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, "You: " + lastDirectMessage.getContent(), dms);
                    } else {
                        template = new MessageListCard(R.drawable.user_profile_default, directMessageRecipient, lastDirectMessage.getContent(), dms);
                    }
                }

                userInterfaceManager.getCurrentDirectMessages().getValue().put(directMessageRecipient, template);

                generateMessageListCard(template, getActivity(), template.isNotification);

                return null;
            });
        } else {
            drawAllMessageListCards();
        }

        Log.d("MessagesFragment", "OnViewCreated");

    }

    public void getAllUsersYouHaveMessagedCallback(DirectMessageThread directMessageThread) {

    }

    private void drawAllMessageListCards() {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getCurrentDirectMessages().getValue().values().forEach(messageListCard -> {
            generateMessageListCard(messageListCard, getActivity(), messageListCard.isNotification);
        });
    }

    private ConstraintLayout generateMessageListCard(MessageListCard template, Context context, boolean isNotification) {
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

            // TODO some way of relating this to the actual message thread
            // Maybe store a reference to a relevant class in the genUI list?
            // Need to send some intents as well

            // TODO for notifications, need to store reference to heading, subheading,
            // and notificationDot possible in the genUI class, or make a new viewModel?
            // need to find a way to reference the template object that is in the list then
            // maybe with a global counter, used to iterate through the list in genUI

            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.getUiState().getValue().setToolbarTitle(heading.getText().toString());
            userInterfaceManager.getCurrentDirectMessages().getValue().get(template.headingText).isNotification = false;

            NavHostFragment.findNavController(MessagesFragment.this).navigate(R.id.action_messagesFragment_to_directMessageFragment);

        });

        return constraintLayout;

    }
}