package com.example.myeducationalapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Html;
import android.widget.LinearLayout;

import com.example.myeducationalapp.SyntaxHighlighting.DetectCodeBlock;
import com.example.myeducationalapp.databinding.FragmentQuestionBinding;
import com.example.myeducationalapp.userInterface.Generation.QuestionCard;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
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

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(this).get(UserInterfaceManagerViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(toolbarTitle);

        Question testQuestion = QuestionSet.getInstance().getQuestionOfTheDay();
        userInterfaceManager.getCurrentlyDisplayedQuestion().getValue().setQuestion(testQuestion);

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
            //binding.questionAnswerEntryConstraintLayout.setVisibility(View.GONE);

            List<String> listOfMultiChoiceOptions = currentQuestion.multiChoiceOptions;

            listOfMultiChoiceOptions.forEach(questionContent -> {
                Log.d("QuestionFragment", questionContent);
            });
        } else {
            // Single choice UI should be ready for first attempt by default
            binding.questionAnswerEntryConstraintLayout.setVisibility(View.VISIBLE);

            

        }


    }
}