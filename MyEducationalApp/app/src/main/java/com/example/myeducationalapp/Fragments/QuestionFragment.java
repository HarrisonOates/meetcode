package com.example.myeducationalapp.Fragments;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myeducationalapp.Localization.DynamicLocalization;
import com.example.myeducationalapp.Question.QuestionSet;
import com.example.myeducationalapp.R;
import com.example.myeducationalapp.User.UserLocalData;
import com.example.myeducationalapp.databinding.FragmentQuestionBinding;
import com.example.myeducationalapp.userInterface.Generatable.QuestionCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe (practically everything)
 * @author u7469758 Geun Yun (dynamic localization)
 */
public class QuestionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


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

        // Setting title bar to name of question
        DynamicLocalization.translatedOrDefaultToolBar(userInterfaceManager.getCurrentlyDisplayedQuestion().getValue().getHeading(), userInterfaceManager, true);


        clearBorderDecoration();
        initializeLayoutOnEnter();
    }

    private void clearBorderDecoration() {
        // For multi choice
        for (int i = 0; i < QuestionSet.MAXIMUM_NUMBER_OF_MULTI_CHOICE_OPTIONS; i++) {
            ConstraintLayout parent = (ConstraintLayout) binding.questionMultiCarpetContraintLayout.getChildAt(i);

            GradientDrawable parentTextBox = (GradientDrawable) parent.getBackground();

            parentTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics())),
                    Color.parseColor("#FFFF102D"));

        }

        // For single choice
        GradientDrawable fakeSingleTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1stFake.getBackground();
        GradientDrawable firstSingleTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1st.getBackground();
        GradientDrawable secondSingleTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();

        secondSingleTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics())),
                Color.parseColor("#FFFF102D"));
        firstSingleTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics())),
                Color.parseColor("#FFFF102D"));
        fakeSingleTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics())),
                Color.parseColor("#FFFF102D"));



    }

    private void initializeLayoutOnEnter() {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);

        QuestionCard currentQuestion = userInterfaceManager.getCurrentlyDisplayedQuestion().getValue();

        // Setting question details to UI
        DynamicLocalization.translatedOrDefaultText(currentQuestion.getHeading(), binding.questionBodyHeadingText);
        DynamicLocalization.translatedOrDefaultText(currentQuestion.getSubheading(), binding.questionBodySubheadingText);
        DynamicLocalization.translatedOrDefaultText(currentQuestion.getCategory(), binding.questionBodyCategoryText);
        DynamicLocalization.translatedOrDefaultText(currentQuestion.getDifficulty(), binding.questionBodyDifficultyText);
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
        binding.questionMultiCarpetContraintLayout.getBackground().setColorFilter(new BlendModeColorFilter(currentQuestion.getSecondaryCardColour(), BlendMode.SRC_ATOP));

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
        if (currentQuestion.isQuestionMultiChoice()) {
            binding.questionAnswerEntryConstraintLayout.setVisibility(View.GONE);

            binding.questionMultiConstraintLayout.setVisibility(View.VISIBLE);

            // This should be of size 6 or less
            List<String> listOfMultiChoiceOptions = currentQuestion.multiChoiceOptions;

            // Checking if this question has been answered correctly before
            // also the amount of times it's been answered
            boolean hasQuestionBeenAnsweredCorrectly = UserLocalData.getInstance().hasQuestionBeenAnsweredCorrectly(currentQuestion.getQuestionID());
            int numberOfQuestionAttempts = UserLocalData.getInstance().getNumberOfFailedAttempts(currentQuestion.getQuestionID());
            List<String> incorrectAnswers = UserLocalData.getInstance().getIncorrectAnswers(currentQuestion.getQuestionID());

            // Showing the multiple choice questions
            for (int i = 0; i < QuestionSet.MAXIMUM_NUMBER_OF_MULTI_CHOICE_OPTIONS; i++) {
                // This is guaranteed to always return a non null element
                ConstraintLayout parent = (ConstraintLayout) binding.questionMultiCarpetContraintLayout.getChildAt(i);
                // This index is derived from the XML
                TextView multiChoiceText = (TextView) parent.getChildAt(0);
                View statusIcon = parent.getChildAt(1);

                // Checking to see if listOfMultiChoiceOptions size is smaller than the current
                // value of the counter
                if (i >= listOfMultiChoiceOptions.size()) {
                    parent.setVisibility(View.GONE);
                } else {
                    // we're editing the multiple choice options that we want to display
                    multiChoiceText.setText(((char)('A' + i)) + ")  " + listOfMultiChoiceOptions.get(i));

                    // If we have attempted this question before
                    // if we haven't attempted this question before then we just want to set
                    // on click listeners and do nothing else
                    if (numberOfQuestionAttempts != 0) {
                        // we need to init previous attempts

                        // we need to init incorrect dialogue message
                        // if incorrect answers contains this current option
                        if (incorrectAnswers.contains(String.valueOf((char)('A' + i)))) {
                            // Set this current option as an error state

                            // Setting status icon to red and bad
                            statusIcon.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cross_circle));
                            statusIcon.setVisibility(View.VISIBLE);

                            // Setting text to strike through and bad
                            multiChoiceText.setTextColor(Color.parseColor("#FFFF102D"));
                            multiChoiceText.setPaintFlags(multiChoiceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            // Setting red border
                            GradientDrawable incorrectTextBox = (GradientDrawable) parent.getBackground();
                            incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                    Color.parseColor("#FFFF102D"));
                        }
                    }


                    // If question has not been answered correctly
                    if (!hasQuestionBeenAnsweredCorrectly && numberOfQuestionAttempts < 2) {
                        // we haven't answered the question correctly so we need to have
                        // the multiple choice options clickeable by the user
                        // Setting on click handler
                        int finalI = i;
                        parent.setOnClickListener(view -> {

                            int numberOfQuestionAttemptsAnon = UserLocalData.getInstance().getNumberOfFailedAttempts(currentQuestion.getQuestionID());
                            boolean hasQuestionBeenAnsweredCorrectlyAnon = UserLocalData.getInstance().hasQuestionBeenAnsweredCorrectly(currentQuestion.getQuestionID());

                            if (numberOfQuestionAttemptsAnon >= 2 ||
                                    (hasQuestionBeenAnsweredCorrectlyAnon)) {
                                return;
                            }

                            if (currentQuestion.isAnswerCorrect(listOfMultiChoiceOptions.get(finalI))) {
                                UserLocalData.getInstance().submitCorrectAnswer(currentQuestion.getQuestionID());
                                // Set parent in correct state and display correct

                                multiChoiceText.setTextColor(Color.parseColor("#FF00D408"));

                                statusIcon.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
                                statusIcon.setVisibility(View.VISIBLE);

                                GradientDrawable incorrectTextBox = (GradientDrawable) parent.getBackground();
                                incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                        Color.parseColor("#FF00D408"));

                                binding.questionMultiResultCorrectText.setVisibility(View.VISIBLE);

                            } else {
                                UserLocalData.getInstance().submitIncorrectAnswer(currentQuestion.getQuestionID(),
                                        String.valueOf((char)('A' + finalI)));

                                // Setting status icon to red and bad
                                statusIcon.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cross_circle));
                                statusIcon.setVisibility(View.VISIBLE);

                                // Setting text to strike through and bad
                                multiChoiceText.setTextColor(Color.parseColor("#FFFF102D"));
                                multiChoiceText.setPaintFlags(multiChoiceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                // Setting red border
                                GradientDrawable incorrectTextBox = (GradientDrawable) parent.getBackground();
                                incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                        Color.parseColor("#FFFF102D"));

                                numberOfQuestionAttemptsAnon = UserLocalData.getInstance().getNumberOfFailedAttempts(currentQuestion.getQuestionID());

                                if (numberOfQuestionAttemptsAnon == 1) {
                                    binding.questionMultiQuestionText.setText(getString(R.string.your_second_attempt));
                                }

                                if (numberOfQuestionAttemptsAnon >= 2) {
                                    binding.questionMultiResultIncorrectText.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            }

            if (numberOfQuestionAttempts >= 1) {
                binding.questionMultiQuestionText.setText(getString(R.string.your_second_attempt));
            }

            // Now we want to check if the question has been answered correctly or
            // if the total amount of question attempts has been used up so we can set the
            // rest of the UI that needs to be set
            if (hasQuestionBeenAnsweredCorrectly) {

                // As multi choice getAnswer will always return a single letter string/char
                char correctOption = currentQuestion.getAnswer().toCharArray()[0];

                ConstraintLayout parent = (ConstraintLayout) binding.questionMultiCarpetContraintLayout
                        .getChildAt(correctOption - 'A');
                // This index is derived from the XML
                TextView multiChoiceText = (TextView) parent.getChildAt(0);
                View statusIcon = parent.getChildAt(1);

                multiChoiceText.setTextColor(Color.parseColor("#FF00D408"));

                statusIcon.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
                statusIcon.setVisibility(View.VISIBLE);

                GradientDrawable incorrectTextBox = (GradientDrawable) parent.getBackground();
                incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                        Color.parseColor("#FF00D408"));

                binding.questionMultiResultCorrectText.setVisibility(View.VISIBLE);

            } else if (numberOfQuestionAttempts == 2) {

                binding.questionMultiResultIncorrectText.setVisibility(View.VISIBLE);
            }

        } else {
            // Single choice UI should be ready for first attempt by default
            binding.questionAnswerEntryConstraintLayout.setVisibility(View.VISIBLE);

            binding.questionMultiConstraintLayout.setVisibility(View.GONE);


            int numberOfQuestionAttempts = UserLocalData.getInstance().getNumberOfFailedAttempts(currentQuestion.getQuestionID());
            boolean hasQuestionBeenAnsweredCorrectly = UserLocalData.getInstance().hasQuestionBeenAnsweredCorrectly(currentQuestion.getQuestionID());

            // Checking to see if user has answered question before
            if (numberOfQuestionAttempts != 0) {
                if (numberOfQuestionAttempts == 1) {
                    // Checking if the user has answered this question correctly, this means they've
                    // done so on their second attempt
                    if (hasQuestionBeenAnsweredCorrectly) {

                        // updating first and second textboxes
                        setErrorStateFirstEntryBox();
                        binding.questionAnswerEntryText1st.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(0));

                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                        binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

                        // making second entry box green, green border with correct icon
                        binding.questionAnswerEntryIcon2nd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
                        binding.questionAnswerEntryIcon2nd.setVisibility(View.VISIBLE);
                        binding.questionAnswerEntryText2nd.setEnabled(false);
                        binding.questionAnswerEntryText2nd.setText(currentQuestion.getAnswer());
                        binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FF00D408"));
                        binding.questionAnswerEntryText2nd.setHint("");
                        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
                        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                Color.parseColor("#FF00D408"));
                    } else {

                        // updating first textbox
                        // making it red, strikethrough, and red border
                        setErrorStateFirstEntryBox();
                        binding.questionAnswerEntryText1st.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(0));


                        // Setting button up to process 2nd attempt
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setOnClickListener(view -> {

                            innerButtonOnClickAction(currentQuestion);


                        });
                    }
                } else if (numberOfQuestionAttempts == 2) {

                    // Hiding submit button
                    binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                    // updating first and second textboxes
                    setErrorStateFirstEntryBox();
                    binding.questionAnswerEntryText1st.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(0));


                    if (hasQuestionBeenAnsweredCorrectly) {
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                        binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

                        // making second entry box green, green border with correct icon
                        binding.questionAnswerEntryIcon2nd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
                        binding.questionAnswerEntryIcon2nd.setVisibility(View.VISIBLE);
                        binding.questionAnswerEntryText2nd.setEnabled(false);
                        binding.questionAnswerEntryText2nd.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(1));
                        binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FF00D408"));
                        binding.questionAnswerEntryText2nd.setHint("");
                        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
                        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                Color.parseColor("#FF00D408"));
                    } else {
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

                        binding.questionAnswerResultIncorrectText.setVisibility(View.VISIBLE);

                        // making second entry box red, strikethrough, and red border
                        binding.questionAnswerEntryIcon2nd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cross_circle));
                        binding.questionAnswerEntryIcon2nd.setVisibility(View.VISIBLE);
                        binding.questionAnswerEntryText2nd.setEnabled(false);
                        binding.questionAnswerEntryText2nd.setText(UserLocalData.getInstance().getYourAnswers(currentQuestion.getQuestionID()).get(1));
                        binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FFFF102D"));
                        binding.questionAnswerEntryText2nd.setHint("");
                        binding.questionAnswerEntryText2nd.setPaintFlags(binding.questionAnswerEntryText2nd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
                        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                Color.parseColor("#FFFF102D"));
                    }
                }
            } else {
                // if numberOfQuestionAttempts == 0 and user has answered the question correctly
                // that means they did so on their first try
                if (hasQuestionBeenAnsweredCorrectly) {
                    // making "fake" ui elements disappear
                    binding.questionAnswerEntryBoxConstraintLayout1stFake.setVisibility(View.GONE);
                    binding.questionAnswerSubmitOuterButtonConstraintLayout.setVisibility(View.GONE);

                    binding.questionAnswerEntryCarpetContraintLayout.setVisibility(View.VISIBLE);
                    binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

                    binding.questionAnswerEntryIcon1st.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
                    binding.questionAnswerEntryIcon1st.setVisibility(View.VISIBLE);
                    binding.questionAnswerEntryText1st.setEnabled(false);
                    binding.questionAnswerEntryText1st.setText(currentQuestion.getAnswer());
                    binding.questionAnswerEntryText1st.setTextColor(Color.parseColor("#FF00D408"));
                    binding.questionAnswerEntryText1st.setHint("");
                    GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1st.getBackground();
                    incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                            Color.parseColor("#FF00D408"));
                } else {
                    // if they haven't answered the question correctly on their first go then
                    // this means that they haven't answered the question at all

                    // For when user submits their first answer
                    binding.questionAnswerSubmitOuterButtonConstraintLayout.setOnClickListener(view1 -> {

                        boolean isUserAnswerCorrect = currentQuestion.isAnswerCorrect(binding.
                                questionAnswerEntryText1stFake.getText().toString().trim());

                        if (isUserAnswerCorrect) {

                            UserLocalData.getInstance().submitCorrectAnswer(currentQuestion.getQuestionID());

                            // Hide dummy button and and transfer stuff to 1st button, and make it
                            // not iteractable
                            binding.questionAnswerEntryBoxConstraintLayout1stFake.setVisibility(View.GONE);
                            binding.questionAnswerSubmitOuterButtonConstraintLayout.setVisibility(View.GONE);

                            binding.questionAnswerEntryCarpetContraintLayout.setVisibility(View.VISIBLE);
                            binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

                            binding.questionAnswerEntryIcon1st.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
                            binding.questionAnswerEntryIcon1st.setVisibility(View.VISIBLE);
                            binding.questionAnswerEntryText1st.setEnabled(false);
                            binding.questionAnswerEntryText1st.setText(currentQuestion.getAnswer());
                            binding.questionAnswerEntryText1st.setTextColor(Color.parseColor("#FF00D408"));
                            binding.questionAnswerEntryText1st.setHint("");
                            GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1st.getBackground();
                            incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                                    Color.parseColor("#FF00D408"));

                        } else {
                            UserLocalData.getInstance().submitIncorrectAnswer(currentQuestion.getQuestionID(),
                                    binding.questionAnswerEntryText1stFake.getText().toString().trim());

                            // Hide fake text box and make 1st one appear with wrong answer details
                            setErrorStateFirstEntryBox();
                            binding.questionAnswerEntryText1st.setText(binding.questionAnswerEntryText1stFake.getText());

                            // Making 2nd question state of this UI element appear
                            // and making uneeded stuff disappear
                            binding.questionAnswerEntryCarpetContraintLayout.setVisibility(View.VISIBLE);
                            binding.questionAnswerEntryText2nd.setVisibility(View.VISIBLE);
                            binding.questionAnswerEntryBoxConstraintLayout2nd.setVisibility(View.VISIBLE);
                            binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.VISIBLE);

                        }


                        // Setting button up to process 2nd attempt
                        binding.questionAnswerSubmitInnerButtonConstraintLayout.setOnClickListener(view -> {
                            innerButtonOnClickAction(currentQuestion);
                        });


                    });
                }


            }


        }


    }

    private void innerButtonOnClickAction(QuestionCard currentQuestion) {
        boolean isUserAnswerCorrect = currentQuestion.isAnswerCorrect(binding.
                questionAnswerEntryText2nd.getText().toString().trim());

        if (isUserAnswerCorrect) {

            UserLocalData.getInstance().submitCorrectAnswer(currentQuestion.getQuestionID());

            // making "fake" ui elements disappear
            binding.questionAnswerEntryBoxConstraintLayout1stFake.setVisibility(View.GONE);
            binding.questionAnswerSubmitOuterButtonConstraintLayout.setVisibility(View.GONE);
            binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

            binding.questionAnswerEntryCarpetContraintLayout.setVisibility(View.VISIBLE);
            binding.questionAnswerResultCorrectText.setVisibility(View.VISIBLE);

            binding.questionAnswerEntryIcon2nd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_check_mark_circle));
            binding.questionAnswerEntryIcon2nd.setVisibility(View.VISIBLE);
            binding.questionAnswerEntryText2nd.setEnabled(false);
            binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FF00D408"));
            binding.questionAnswerEntryText2nd.setHint("");
            GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
            incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                    Color.parseColor("#FF00D408"));

        } else {

            UserLocalData.getInstance().submitIncorrectAnswer(currentQuestion.getQuestionID(),
                    binding.questionAnswerEntryText2nd.getText().toString().trim());

            binding.questionAnswerSubmitInnerButtonConstraintLayout.setVisibility(View.GONE);

            binding.questionAnswerResultIncorrectText.setVisibility(View.VISIBLE);

            // making second entry box red, strikethrough, and red border
            binding.questionAnswerEntryIcon2nd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cross_circle));
            binding.questionAnswerEntryIcon2nd.setVisibility(View.VISIBLE);
            binding.questionAnswerEntryText2nd.setEnabled(false);
            binding.questionAnswerEntryText2nd.setTextColor(Color.parseColor("#FFFF102D"));
            binding.questionAnswerEntryText2nd.setHint("");
            binding.questionAnswerEntryText2nd.setPaintFlags(binding.questionAnswerEntryText2nd.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout2nd.getBackground();
            incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                    Color.parseColor("#FFFF102D"));
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
        binding.questionAnswerEntryIcon1st.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cross_circle));
        binding.questionAnswerEntryIcon1st.setVisibility(View.VISIBLE);

        // making first entry box red, strikethrough, and red border
        binding.questionAnswerEntryText1st.setEnabled(false);
        binding.questionAnswerEntryText1st.setTextColor(Color.parseColor("#FFFF102D"));
        binding.questionAnswerEntryText1st.setHint("");
        binding.questionAnswerEntryText1st.setPaintFlags(binding.questionAnswerEntryText1st.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        GradientDrawable incorrectTextBox = (GradientDrawable) binding.questionAnswerEntryBoxConstraintLayout1st.getBackground();
        incorrectTextBox.setStroke(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                Color.parseColor("#FFFF102D"));
    }
}