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
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputFilter;
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

import com.example.myeducationalapp.databinding.FragmentHomeBinding;
import com.example.myeducationalapp.databinding.FragmentMessagesBinding;
import com.example.myeducationalapp.userInterface.Generation.GeneratedUserInterfaceViewModel;
import com.example.myeducationalapp.userInterface.Generation.HomeCategoryCard;
import com.example.myeducationalapp.userInterface.Generation.MessageListCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.google.android.material.divider.MaterialDivider;

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

    private final String toolbarTitle = "Chats";
    private int headingMaxLength = 22;
    private int subHeadingMaxLength = 10;

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

        GeneratedUserInterfaceViewModel genUserInterfaceManager = new ViewModelProvider(this).get(GeneratedUserInterfaceViewModel.class);
        genUserInterfaceManager.addToListOfElements(new MessageListCard(null, "Firstname Lastname", "This is an example preview message"));
        genUserInterfaceManager.addToListOfElements(new MessageListCard(null, "Another Longer Lastname So Longggg", "This is a very long example of a preview message"));

        Log.d("MessagesFragment", String.valueOf(genUserInterfaceManager.listOfElements.size()));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(toolbarTitle);

        GeneratedUserInterfaceViewModel genUserInterfaceManager = new ViewModelProvider(this).get(GeneratedUserInterfaceViewModel.class);
        generateMessageListCard((MessageListCard) genUserInterfaceManager.listOfElements.get(0), getActivity());
        generateMessageListCard((MessageListCard) genUserInterfaceManager.listOfElements.get(1), getActivity());

    }

    public void generateMessageListCard(MessageListCard template, Context context) {
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
        image.setImageResource(R.drawable.user_profile_default);
        image.setId(View.generateViewId());

        heading.setSingleLine();
        heading.setEllipsize(TextUtils.TruncateAt.END);
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        heading.setText(template.headingText);
        Typeface headingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_bold);
        heading.setTypeface(headingTypeface);
        heading.setTextColor(Color.parseColor("#000000"));
        heading.setId(View.generateViewId());


        subheading.setSingleLine();
        subheading.setEllipsize(TextUtils.TruncateAt.END);
        subheading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        subheading.setText(template.subHeadingText);
        Typeface subheadingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans);
        subheading.setTypeface(subheadingTypeface);
        subheading.setId(View.generateViewId());

        notificationDot.setBackground(ContextCompat.getDrawable(context, R.drawable.icon_notification_dot));
        notificationDot.setId(View.generateViewId());

        divider.setDividerThickness((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        divider.setDividerColor(Color.parseColor("#A6000000"));
        divider.setId(View.generateViewId());

        constraintLayout.addView(image);
        constraintLayout.addView(heading);
        constraintLayout.addView(subheading);
        constraintLayout.addView(notificationDot);
        constraintLayout.addView(divider);

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
        subheadingConstraintSet.connect(subheading.getId(), ConstraintSet.TOP, heading.getId(), ConstraintSet.BOTTOM, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics())));
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
        constraintLayoutParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        constraintLayoutParams.topMargin = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
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
            NavHostFragment.findNavController(MessagesFragment.this).navigate(R.id.action_messagesFragment_to_directMessageFragment);


        });

    }
}