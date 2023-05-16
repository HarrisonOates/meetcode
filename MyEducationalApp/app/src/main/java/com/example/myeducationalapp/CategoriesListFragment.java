package com.example.myeducationalapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.compose.foundation.gestures.Orientation;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myeducationalapp.databinding.FragmentCategoriesListBinding;
import com.example.myeducationalapp.databinding.FragmentQuestionBinding;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String toolbarTitle = "Categories";

    private FragmentCategoriesListBinding binding;

    public CategoriesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesListFragment newInstance(String param1, String param2) {
        CategoriesListFragment fragment = new CategoriesListFragment();
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
        binding = FragmentCategoriesListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(toolbarTitle);

        //generateCategoryListCard(QuestionSet.Category.TestQuestion, getActivity());

    }

    private void generateCategoryListCard(QuestionSet.Category category, Context context) {

        // Setting up UI elements
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        ImageView categoryImage = new ImageView(context);
        TextView heading = new TextView(context);
        TextView subheading = new TextView(context);
        ConstraintLayout starsContainer = new ConstraintLayout(context);
        TextView headingStarsContainer = new TextView(context);
        View iconStarsContainer = new View(context);
        // Constraint layout
        ConstraintSet categoryImageConstraintSet = new ConstraintSet();
        ConstraintSet headingConstraintSet = new ConstraintSet();
        ConstraintSet subheadingConstraintSet = new ConstraintSet();
        ConstraintSet starsContainerConstraintSet = new ConstraintSet();
        ConstraintSet headingStarsConConstraintSet = new ConstraintSet();
        ConstraintSet iconsStarsConConstraintSet = new ConstraintSet();
        // Guideline
        Guideline guideline = new Guideline(context);
        guideline.setId(Guideline.generateViewId());
        ConstraintLayout.LayoutParams guidelineLayoutParams =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        guidelineLayoutParams.orientation = 1;   // 1 is vertical
        guidelineLayoutParams.guidePercent = 0.39f;
        guideline.setLayoutParams(guidelineLayoutParams);

        constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.home_category_carousel_container));
        constraintLayout.setId(View.generateViewId());

        //categoryImage.setImageResource(category.getCategoryImageDrawableID());
        categoryImage.setId(View.generateViewId());

        heading.setSingleLine();
        heading.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        heading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        heading.setText(category.toString());
        Typeface headingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans_semibold);
        heading.setTypeface(headingTypeface);
        heading.setTextColor(Color.parseColor("#FFFFFF"));
        heading.setId(View.generateViewId());

        subheading.setSingleLine();
        subheading.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        subheading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        int numberOfQuestionsInCategory = QuestionSet.getInstance().getNumberOfQuestionsInCategory(category);
        subheading.setText(numberOfQuestionsInCategory + " Questions");
        Typeface subheadingTypeface = ResourcesCompat.getFont(context, R.font.ibm_plex_sans);
        subheading.setTypeface(subheadingTypeface);
        subheading.setId(View.generateViewId());

        starsContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.categories_list_secondary_star_container));
        starsContainer.setId(View.generateViewId());

        headingStarsContainer.setSingleLine();
        headingStarsContainer.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        headingStarsContainer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        int numberOfAnsweredQuestionsInCategory = UserLocalData.getInstance().getNumberOfAnsweredQuestionsInCategory(category);
        headingStarsContainer.setText("Achieved " + numberOfAnsweredQuestionsInCategory + "/" + numberOfQuestionsInCategory);
        headingStarsContainer.setTypeface(headingTypeface);
        headingStarsContainer.setTextColor(Color.parseColor("#FFFFFF"));
        headingStarsContainer.setId(View.generateViewId());

        iconStarsContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.icon_star));
        iconStarsContainer.setId(View.generateViewId());

        // Adding views in correct structure to parent constraintLayout
        starsContainer.addView(headingStarsContainer);
        starsContainer.addView(iconStarsContainer);
        constraintLayout.addView(guideline);
        constraintLayout.addView(heading);
        constraintLayout.addView(subheading);
        constraintLayout.addView(starsContainer);

        // Adding constraint to the various constraint layouts
        categoryImageConstraintSet.clone(constraintLayout);
        //categoryImageConstraintSet.connect(categoryImage.getId(), ConstraintSet.END, );



        // Assigning to LinearLayout on page
        binding.categoriesListCardLinearLayout.addView(constraintLayout);


    }


}