package com.example.myeducationalapp;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myeducationalapp.databinding.FragmentQuestionBinding;
import com.example.myeducationalapp.userInterface.Generatable.QuestionCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe
 */
public class QuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String toolbarTitle = "QuestionName";
    private FragmentQuestionBinding binding;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
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
        binding = FragmentQuestionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);

        // Testing
//        Question testQuestion = QuestionSet.getInstance().getQuestionOfTheDay();
//        userInterfaceManager.getCurrentlyDisplayedQuestion().getValue().setQuestion(testQuestion);

        // Setting title bar to name of question
        userInterfaceManager.getUiState().getValue().enterNewFragment(userInterfaceManager.getCurrentlyDisplayedQuestion().getValue().getHeading());

        initializeLayoutOnEnter();
    }

    private void initializeLayoutOnEnter() {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);

        QuestionCard currentQuestion = userInterfaceManager.getCurrentlyDisplayedQuestion().getValue();

        // Setting question details to UI
        binding.questionBodyHeadingText.setText(currentQuestion.getHeading());
        binding.questionBodySubheadingText.setText(currentQuestion.getSubheading());
        binding.questionBodyCategoryText.setText(currentQuestion.getCategory());
        binding.questionBodyDifficultyText.setText(currentQuestion.getDifficulty());
        if (currentQuestion.getMaxStars() == 1) {
            binding.questionBodyStarsText.setText(getString(R.string.earn_1_star));
        } else {
            binding.questionBodyStarsText.setText(getString(R.string.earn_x_stars, currentQuestion.getMaxStars()));
        }

        // Changing colour of UI to reflect current category
        // Setting primary colours
        binding.questionBodyConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getCardColour(), BlendMode.SRC_ATOP));
        binding.questionMultiConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getCardColour(), BlendMode.SRC_ATOP));
        binding.questionAnswerEntryConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getCardColour(), BlendMode.SRC_ATOP));

        // Setting secondary colours
        binding.questionAnswerSubmitOuterButtonConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getSecondaryCardColour(), BlendMode.SRC_ATOP));
        binding.questionBodyCategoryContainerConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getSecondaryCardColour(), BlendMode.SRC_ATOP));
        binding.questionBodyDifficultyContainerConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getSecondaryCardColour(), BlendMode.SRC_ATOP));
        binding.questionBodyStarsContainerConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getSecondaryCardColour(), BlendMode.SRC_ATOP));
        binding.questionAnswerEntryCarpetContraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getSecondaryCardColour(), BlendMode.SRC_ATOP));

        // Setting tertiary colours
        binding.questionAnswerSubmitInnerButtonConstraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getTertiaryCardColour(), BlendMode.SRC_ATOP));



        // Code block related UI code
        if (currentQuestion.doesQuestionHaveCodeBlock()) {
            binding.questionCodeBlockText.setText(currentQuestion.getCodeBlock());
        } else {
            binding.questionCodeBlockConstraintLayout.setVisibility(View.GONE);
            binding.questionBodyConstraintLayout.setPadding(0, 0, 0,
                    ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        }

        // Multiple choice related UI code
//        if (currentQuestion.isQuestionMultiChoice()) {
        if (false) {
            binding.questionAnswerEntryConstraintLayout.setVisibility(View.GONE);

            List<String> listOfMultiChoiceOptions = currentQuestion.multiChoiceOptions;

            listOfMultiChoiceOptions.forEach(questionContent -> {
                Log.d("QuestionFragment", questionContent);
            });
        } else {
            // Single choice UI should be ready for first attempt by default
            binding.questionAnswerEntryConstraintLayout.setVisibility(View.VISIBLE);


            int numberOfQuestionAttempts = UserLocalData.getInstance().getNumberOfFailedAttempts(currentQuestion.getQuestionID());
            boolean hasQuestionBeenAnsweredCorrectly = UserLocalData.getInstance().hasQuestionBeenAnsweredCorrectly(currentQuestion.getQuestionID());

            // Checking to see if user has answered question before
            if (numberOfQuestionAttempts != 0) {
                // If we've answered the question once, then we also need to check that we haven't
                // answered it correctly
                if (numberOfQuestionAttempts == 1) {
                    // Checking if the user has answered this question correctly
                    if (hasQuestionBeenAnsweredCorrectly) {

                        // making "fake" ui elements disappear
                        binding.questionAnswerEntryBoxConstraintLayout1stFake.setVisibility(View.GONE);
                        binding.questionAnswerSubmitOuterButtonConstraintLayout.setVisibility(View.GONE);

                        binding.questionAnswerEntryCarpetContraintLayout.setVisibility(View.VISIBLE);
                        binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

                        binding.questionAnswerEntryCorrectIcon1st.setVisibility(View.VISIBLE);
                        binding.questionAnswerEntryText1st.setEnabled(false);
                        //binding.questionAnswerEntryText1st.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(0));
                        binding.questionAnswerEntryText1st.setTextColor(Color.parseColor("#FF00D408"));
                        binding.questionAnswerEntryText1st.setHint("");
                        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1st.getBackground();
                        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                Color.parseColor("#FF00D408"));


                    } else {

                        // updating first textbox
                        // making it red, strikethrough, and red border
                        setErrorStateFirstEntryBox();

                        // Setting button up to process 2nd attempt
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setOnClickListener(view -> {

                        });
                    }
                } else if (numberOfQuestionAttempts == 2) {

                    // Hiding submit button
                    binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                    // updating first and second textboxes
                    setErrorStateFirstEntryBox();

                    if (hasQuestionBeenAnsweredCorrectly) {
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                        binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

                        // making second entry box green, green border with correct icon
                        binding.questionAnswerEntryCorrectIcon2nd.setVisibility(View.VISIBLE);
                        binding.questionAnswerEntryText2nd.setEnabled(false);
                        //binding.questionAnswerEntryText1st.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(1));
                        binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FF00D408"));
                        binding.questionAnswerEntryText2nd.setHint("");
                        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
                        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                Color.parseColor("#FF00D408"));
                    } else {
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                        binding.questionAnswerResultIncorrectText.setVisibility(View.VISIBLE);

                        // making first entry box red, strikethrough, and red border
                        binding.questionAnswerEntryIncorrectIcon2nd.setVisibility(View.VISIBLE);
                        binding.questionAnswerEntryText2nd.setEnabled(false);
                        //binding.questionAnswerEntryText2nd.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(1));
                        binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FFFF102D"));
                        binding.questionAnswerEntryText2nd.setHint("");
                        binding.questionAnswerEntryText2nd.setPaintFlags(binding.questionAnswerEntryText2nd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
                        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                Color.parseColor("#FFFF102D"));
                    }
                }
            } else {
                // if numberOfQuestionAttempts == 0

                // For when user submits their first answer
                binding.questionAnswerSubmitOuterButtonConstraintLayout.setOnClickListener(view1 -> {

                });
            }


        }


    }

    private void setErrorStateFirstEntryBox() {
        // making "fake" ui elements disappear
        binding.questionAnswerEntryBoxConstraintLayout1stFake.setVisibility(View.GONE);
        binding.questionAnswerSubmitOuterButtonConstraintLayout.setVisibility(View.GONE);

        // making relevant ui elements appear
        binding.questionAnswerEntry2ndQuestionText.setVisibility(View.VISIBLE);
        binding.questionAnswerEntryBoxConstraintLayout2nd.setVisibility(View.VISIBLE);
        binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.VISIBLE);
        binding.questionAnswerEntryCarpetContraintLayout.setVisibility(View.VISIBLE);
        binding.questionAnswerEntryIncorrectIcon1st.setVisibility(View.VISIBLE);

        // making first entry box red, strikethrough, and red border
        binding.questionAnswerEntryText1st.setEnabled(false);
        //binding.questionAnswerEntryText1st.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(0));
        binding.questionAnswerEntryText1st.setTextColor(Color.parseColor("#FFFF102D"));
        binding.questionAnswerEntryText1st.setHint("");
        binding.questionAnswerEntryText1st.setPaintFlags(binding.questionAnswerEntryText1st.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1st.getBackground();
        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                Color.parseColor("#FFFF102D"));
    }
}