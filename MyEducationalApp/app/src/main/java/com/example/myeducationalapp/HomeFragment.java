package com.example.myeducationalapp;

import android.content.Context;
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

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myeducationalapp.userInterface.Generation.GeneratedUserInterfaceViewModel;
import com.example.myeducationalapp.userInterface.Generation.HomeCategoryCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.example.myeducationalapp.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHomeBinding binding;
    private final String toolbarTitle = "Home";

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        genUserInterfaceManager.addToListOfElements(new HomeCategoryCard(null, null, "hiasdasdsadsasdasdsa", "hasadsi"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        GeneratedUserInterfaceViewModel genUserInterfaceManager = new ViewModelProvider(this).get(GeneratedUserInterfaceViewModel.class);
        Log.w("Fragment Home", String.valueOf(genUserInterfaceManager.listOfElements.size()));
        generateHomeCategoryCard((HomeCategoryCard) genUserInterfaceManager.listOfElements.get(0), getActivity());

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);

        userInterfaceManager.getUiState().getValue().setToolbarTitle(toolbarTitle);

        binding.homeCategorySeeMoreText.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_categoriesListFragment);
            userInterfaceManager.getUiState().getValue().transitionState(R.id.action_categoriesListFragment_to_HomeFragment);
            Log.w("ModelView", String.valueOf((Integer) userInterfaceManager.getUiState().getValue().getIsActionBarInBackState()));
        });


    }

    public void generateHomeCategoryCard(HomeCategoryCard template, Context context) {
        // making ui elements within parent
        ImageView image = new ImageView(context);
        TextView heading = new TextView(context);
        TextView subheading = new TextView(context);
        // Constraint layout stuff
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        ConstraintSet imageConstraintSet = new ConstraintSet();
        ConstraintSet headingConstraintSet = new ConstraintSet();
        ConstraintSet subheadingConstraintSet = new ConstraintSet();

        constraintLayout.setId(View.generateViewId());
        // TODO
        constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.home_category_carousel_container));

        // TODO set this using imageReference
        image.setImageResource(R.drawable.shape_placeholder_square);
        image.setId(View.generateViewId());

        heading.setText(template.headingText);
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        Typeface headingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_semibold);
        heading.setTypeface(headingTypeface);
        heading.setId(View.generateViewId());


        subheading.setText(template.subheadingText);
        subheading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        Typeface subheadingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans);
        subheading.setTypeface(subheadingTypeface);
        subheading.setId(View.generateViewId());

        constraintLayout.addView(image);
        constraintLayout.addView(heading);
        constraintLayout.addView(subheading);

        imageConstraintSet.clone(constraintLayout);
        imageConstraintSet.connect(image.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
        imageConstraintSet.connect(image.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
        imageConstraintSet.connect(image.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
        imageConstraintSet.applyTo(constraintLayout);

        headingConstraintSet.clone(constraintLayout);
        headingConstraintSet.connect(heading.getId(), ConstraintSet.TOP, image.getId(), ConstraintSet.BOTTOM, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
        headingConstraintSet.connect(heading.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        headingConstraintSet.connect(heading.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        headingConstraintSet.connect(heading.getId(), ConstraintSet.BOTTOM, subheading.getId(), ConstraintSet.TOP, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        headingConstraintSet.applyTo(constraintLayout);

        subheadingConstraintSet.clone(constraintLayout);
        subheadingConstraintSet.connect(subheading.getId(), ConstraintSet.START, heading.getId(), ConstraintSet.START);
        subheadingConstraintSet.connect(subheading.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics()));
        subheadingConstraintSet.applyTo(constraintLayout);

        binding.homeCategoryCarouselScrollable.addView(constraintLayout);

        ViewGroup.LayoutParams constraintLayoutParams = constraintLayout.getLayoutParams();
        constraintLayoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());
        constraintLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        constraintLayout.setLayoutParams(constraintLayoutParams);

        ViewGroup.LayoutParams imageLayoutParams = image.getLayoutParams();
        imageLayoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 84, getResources().getDisplayMetrics());;
        imageLayoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        image.setLayoutParams(imageLayoutParams);

        ViewGroup.LayoutParams headingLayoutParams = heading.getLayoutParams();
        headingLayoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
        headingLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        heading.setLayoutParams(headingLayoutParams);

        ViewGroup.LayoutParams subheadingLayoutParams = subheading.getLayoutParams();
        subheadingLayoutParams.width = 0;
        subheadingLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        subheading.setLayoutParams(subheadingLayoutParams);
    }
}