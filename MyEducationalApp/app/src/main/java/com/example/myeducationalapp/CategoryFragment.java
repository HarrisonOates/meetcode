package com.example.myeducationalapp;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myeducationalapp.Localization.DynamicLocalization;
import com.example.myeducationalapp.databinding.FragmentCategoryBinding;
import com.example.myeducationalapp.userInterface.Generatable.CategoryListCard;
import com.example.myeducationalapp.userInterface.Generatable.HomeCategoryCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCategoryBinding binding;

    private final String toolbarTitle = "CategoryName";

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(
                userInterfaceManager.getCurrentlyDisplayedCategory().getValue().getHeading());


        generateAllCards(getActivity());



    }

    private void generateAllCards(Context context) {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        CategoryListCard categoryListCard = userInterfaceManager.getCurrentlyDisplayedCategory().getValue();

        // Initialize fields in the static category card at the top of this fragment
        generateCategoryCard(categoryListCard, context);

        int questionCounter = 1;

        // Loop through all the questions in this question and generate the UI
        for (String questionId : QuestionSet.getInstance().getQuestionIDsInCategory(categoryListCard.getCategory())) {
            generateCategoryQuestionCard(questionCounter, questionId, categoryListCard, getActivity());
            questionCounter++;
        }
    }
    
    private void generateCategoryCard(CategoryListCard categoryListCard, Context context) {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);

        // Setting category card's heading and subheading as well as the star container area
        DynamicLocalization.translatedOrDefaultText(categoryListCard.getHeading(), binding.categoryCardHeadingText);
        DynamicLocalization.translatedOrDefaultText(categoryListCard.getSubheading(), binding.categoryCardSubheadingText);
        DynamicLocalization.translatedOrDefaultText(categoryListCard.getStarProgress(), binding.categoryCardSecondaryStarHeadingText);

        binding.categoryCardImage.setImageResource(categoryListCard.getCardImage());
        binding.categoryCard1.getBackground().setColorFilter(new BlendModeColorFilter(categoryListCard.getCardColor(), BlendMode.SRC_ATOP));
        binding.categoryStarWrapper.getBackground().setColorFilter(new BlendModeColorFilter(categoryListCard.getSecondaryCardColor(), BlendMode.SRC_ATOP));
    }
    
    private void generateCategoryQuestionCard(int questionCount, String questionID, CategoryListCard categoryListCard, Context context) {
        // inflating XML into an object we can use
        ConstraintLayout categoryQuestionCard = (ConstraintLayout) LayoutInflater.from(context).
                inflate(R.layout.category_question_card, null);

        // Setting background of parent view
        categoryQuestionCard.getBackground().setColorFilter(new BlendModeColorFilter(categoryListCard.getSecondaryCardColor(), BlendMode.SRC_ATOP));

        // Setting layout parameters relative to the parent as this is not set from the inflated file
        LinearLayout.LayoutParams categoryQuestionCardLayoutParams = new LinearLayout.LayoutParams(
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics())),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        categoryQuestionCardLayoutParams.topMargin = (((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        categoryQuestionCardLayoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        categoryQuestionCard.setLayoutParams(categoryQuestionCardLayoutParams);

        // Getting data to set UI element
        boolean isQuestionAnsweredCorrectly = UserLocalData.getInstance().hasQuestionBeenAnsweredCorrectly(questionID);
        String questionTitle = QuestionSet.getInstance().getQuestionFromID(questionID).getName();

        // Setting data to UI elements
        // The indices for these are determined from the above inflated XML hierarchy
        TextView headingText = (TextView) categoryQuestionCard.getChildAt(0);
        View starIcon = (View) categoryQuestionCard.getChildAt(1);

        DynamicLocalization.translatedOrDefaultText(questionCount + ". " + questionTitle, headingText);
        if (isQuestionAnsweredCorrectly) {
            starIcon.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_star));
        }

        // Adding the final parent to the LinearLayout nested within the ScrollView
        binding.categoryLinearLayout.addView(categoryQuestionCard);

        // Setting on click listener on the entire view which should transition
        // to the relevant question when clicked
        categoryQuestionCard.setOnClickListener(view -> {
            // Updating the view model so Question fragment will update when it is created
            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.setCurrentlyDisplayedQuestion(QuestionSet.getInstance().getQuestionFromID(questionID));

            // Navigating to the Question fragment
            NavHostFragment.findNavController(CategoryFragment.this).navigate(R.id.action_categoryFragment_to_questionFragment);
        });

    }
}