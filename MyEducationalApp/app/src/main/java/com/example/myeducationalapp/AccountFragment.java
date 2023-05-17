package com.example.myeducationalapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myeducationalapp.Localization.LanguageSetting;
import com.example.myeducationalapp.databinding.FragmentAccountBinding;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentAccountBinding binding;

    private final String toolbarTitle = "Menu";

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        userInterfaceManager.getUiState().getValue().enterNewFragment(toolbarTitle);

        binding.accountMenuCategoryConstraintLayout.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_accountFragment_to_categoriesListFragment);
        });

        binding.accountMenuDailyChallengeConstraintLayout.setOnClickListener(view1 -> {
            userInterfaceManager.setCurrentlyDisplayedQuestion(QuestionSet.getInstance().getQuestionOfTheDay());

            NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_accountFragment_to_questionFragment);
        });

        binding.accountMenuLogOutConstraintLayout.setOnClickListener(view1 -> {
            UserLogin.getInstance().logout();
            NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_accountFragment_to_loginActivity);
        });

        binding.accountMenuLanguageMenuConstraintLayout.setOnClickListener(view1 -> {
            Intent languageIntent = new Intent(getContext(), LanguageSetting.class);
            startActivity(languageIntent);
        });

        //category_question_car_heading_text
        String formattedStars = getString(R.string.you_ve_achieved_x_stars, UserLocalData.getInstance().getPoints());

        binding.textView.setText(UserLogin.getInstance().getCurrentUsername());
        binding.categoryQuestionCarHeadingText.setText(formattedStars);
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}