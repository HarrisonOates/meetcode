package com.example.myeducationalapp;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
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
import androidx.navigation.fragment.NavHostFragment;

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

import com.example.myeducationalapp.databinding.FragmentCategoriesListBinding;
import com.example.myeducationalapp.databinding.FragmentQuestionBinding;
import com.example.myeducationalapp.userInterface.Generatable.CategoryListCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe
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

        generateAllCategoryListCards(getActivity());

    }

    private void generateAllCategoryListCards(Context context) {
        for (QuestionSet.Category category : QuestionSet.Category.values()) {
            if (category != QuestionSet.Category.TestQuestion) {
                generateCategoryListCard(new CategoryListCard(category), context);
            }
        }
    }

    private void generateCategoryListCard(CategoryListCard categoryListCard, Context context) {

        // inflating XML into an object we can use
        ConstraintLayout homeCategoryCard = (ConstraintLayout) LayoutInflater.from(context).
                inflate(R.layout.category_list_card, null);

        // Setting background of parent view
        homeCategoryCard.getBackground().setColorFilter(new BlendModeColorFilter(categoryListCard.getCardColor(), BlendMode.SRC_ATOP));

        // Setting layout parameters relative to the parent as this is not set from the inflated file
        LinearLayout.LayoutParams homeCategoryCardLayoutParams = new LinearLayout.LayoutParams(
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics())),
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()))
        );
        homeCategoryCardLayoutParams.topMargin = (((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        homeCategoryCardLayoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        homeCategoryCard.setLayoutParams(homeCategoryCardLayoutParams);

        // The indices for these are determined from the above inflated XML hierarchy
        ImageView categoryImage = (ImageView) homeCategoryCard.getChildAt(0);
        TextView headingText = (TextView) homeCategoryCard.getChildAt(1);
        TextView subheadingText = (TextView) homeCategoryCard.getChildAt(2);
        ConstraintLayout starWrapper = (ConstraintLayout) homeCategoryCard.getChildAt(3);
        TextView starHeadingText = (TextView) (starWrapper).getChildAt(0);

        starWrapper.getBackground().setColorFilter(new BlendModeColorFilter(categoryListCard.getSecondaryCardColor(), BlendMode.SRC_ATOP));

        // Setting UI elements with relevant data
        categoryImage.setImageResource(categoryListCard.getCardImage());
        headingText.setText(categoryListCard.getHeading());
        subheadingText.setText(categoryListCard.getSubheading());
        starHeadingText.setText(categoryListCard.getStarProgress());

        // Adding the final parent to the LinearLayout nested within the ScrollView
        binding.categoriesListCardLinearLayout.addView(homeCategoryCard);

        // Setting on click listener to the entire view
        homeCategoryCard.setOnClickListener(view -> {
            // Updating the view model so Categories fragment will update when it is created
            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.setCurrentlyDisplayedCategory(categoryListCard.getCategory());
            // Navigating to the Categories fragment
            NavHostFragment.findNavController(CategoriesListFragment.this).navigate(R.id.action_categoriesListFragment_to_categoryFragment);

        });

    }


}