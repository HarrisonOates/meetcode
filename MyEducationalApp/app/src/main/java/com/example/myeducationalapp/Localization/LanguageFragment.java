package com.example.myeducationalapp.Localization;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myeducationalapp.R;
import com.example.myeducationalapp.databinding.FragmentLanguageBinding;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.google.mlkit.nl.translate.TranslateLanguage;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

/**
 * @author u7469758 Geun Yun
 */
public class LanguageFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FragmentLanguageBinding binding;
    public static String previousLanguage;
    public static String currentLanguage = TranslateLanguage.ENGLISH;

    public static boolean isSameLangauge;

   public LanguageFragment(){};

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    public static LanguageFragment newInstance(String param1, String param2) {
        LanguageFragment fragment = new LanguageFragment();
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
        binding = FragmentLanguageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        DynamicLocalization.translatedOrDefaultToolBar("Language Setting", userInterfaceManager, true);
        binding.buttonPo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateIfNecessary(binding.buttonPo.getId());
            }
        });

        binding.buttonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateIfNecessary(binding.buttonEn.getId());
            }
        });

        binding.buttonCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateIfNecessary(binding.buttonCh.getId());
            }
        });

        binding.buttonJa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateIfNecessary(binding.buttonJa.getId());
            }
        });

        binding.buttonKr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateIfNecessary(binding.buttonKr.getId());
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void translateIfNecessary(int languageID) {
        previousLanguage = currentLanguage;
        switch (languageID) {
            case R.id.buttonEn -> currentLanguage = "en";
            case R.id.buttonKr -> currentLanguage = "ko";
            case R.id.buttonJa -> currentLanguage = "ja";
            case R.id.buttonPo -> currentLanguage = "pt";
            case R.id.buttonCh -> currentLanguage = "zh";
        }
        if (previousLanguage.equals(currentLanguage)) {
            isSameLangauge = true;
            Toast toast = Toast.makeText(getContext(), "Already in the selected langauge!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            isSameLangauge = false;
            setLanguage(currentLanguage);
        }
    }

    /**
     * Changes all the hardcoded string values in strings.xml into a given language
     * @param languageCode
     */
    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        NavHostFragment.findNavController(LanguageFragment.this).navigate(R.id.action_languageFragment_to_HomeFragment);
    }
}
