package com.example.myeducationalapp;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Localization.DynamicLocalization;
import com.example.myeducationalapp.Localization.LanguageSetting;
import com.example.myeducationalapp.Search.Search;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;
import com.example.myeducationalapp.userInterface.Generatable.GeneratedUserInterfaceViewModel;
import com.example.myeducationalapp.userInterface.Generatable.HomeCategoryCard;
import com.example.myeducationalapp.userInterface.Generatable.Iterator;
import com.example.myeducationalapp.userInterface.Generatable.MessageListCard;
import com.example.myeducationalapp.userInterface.UserDirectMessages;
import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;
import com.example.myeducationalapp.databinding.FragmentHomeBinding;

import com.example.myeducationalapp.Search.SearchResults.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author u7300256 Nikhila Gurusinghe (main code)
 * @author u7469758 Geun Yun (search bar UI)
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

    // Indicates whether the search filter is open or not to decide whether to close it or open it
    private boolean isFilterOpen = false;

    private boolean isFirstSearch = true;

    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    private RecyclerViewCustomAdapter recyclerViewCustomAdapter;

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

        // Adding all categories to the ViewModel that manages this Fragment
        GeneratedUserInterfaceViewModel<HomeCategoryCard> genUserInterfaceManager = new ViewModelProvider(this).get(GeneratedUserInterfaceViewModel.class);
        for (QuestionSet.Category category : QuestionSet.Category.values()) {
            // We want to not add the TestQuestion category :)
            if (category != QuestionSet.Category.TestQuestion) {
                genUserInterfaceManager.addToListOfElements(new HomeCategoryCard(category));
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Updating toolbar text
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        DynamicLocalization.translatedOrDefaultToolBar(toolbarTitle, userInterfaceManager, true);

        // Updating the categories carousel
        generateAllHomeCategoryCards(getActivity());

        // Adding button to "See more" text for categories
        binding.homeCategorySeeMoreText.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_categoriesListFragment);
        });

        // Updating "Question of the Day" subheading and body with with relevant stuff
        LocalDateTime date = LocalDateTime.now();
        String today = date.format(DateTimeFormatter.ofPattern("d MMMM"));
        DynamicLocalization.translatedOrDefaultText(today, binding.homeHeroSubheadingText);

        Question qotd = QuestionSet.getInstance().getQuestionOfTheDay();
        DynamicLocalization.translatedOrDefaultText(qotd.getName(), binding.homeHeroBodyText);

        boolean success = UserLocalData.getInstance().hasQuestionBeenAnsweredCorrectly(qotd.getID());
        if (success) {
            binding.homeHeroSecondaryCallToActionText.setText(getString(R.string.completed));
        } else {
            int maxStars = qotd.getDifficulty();
            int failedAttempts = UserLocalData.getInstance().getNumberOfFailedAttempts(qotd.getID());
            if (failedAttempts == 1) {
                maxStars = 1;
            } else if (failedAttempts > 0) {
                maxStars = 0;
            }

            binding.homeHeroSecondaryCallToActionText.setText(getString(maxStars == 1 ? R.string.earn_1_star : R.string.earn_x_stars, maxStars));
        }

        // Adding button to the "Question of the Day" section
        binding.homeHeroContainer.setOnClickListener(view1 -> {
            // Updating the view model so Question fragment will update when it is created
            userInterfaceManager.setCurrentlyDisplayedQuestion(qotd);

            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_questionFragment);
        });

        // Question is set as the default type of search
        binding.allSearch.setChecked(true);

        // Search filter and results should only be visible when they are explicitly used
        binding.searchResults.setVisibility(View.INVISIBLE);
        binding.searchFilter.setVisibility(View.INVISIBLE);
        binding.hideSearchResults.setVisibility(View.INVISIBLE);

        // Make the filter visible/invisible by clicking the filter icon.
        binding.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFilterOpen) {
                    binding.searchFilter.setVisibility(View.INVISIBLE);
                    isFilterOpen = false;
                } else {
                    binding.searchFilter.setVisibility(View.VISIBLE);
                    isFilterOpen = true;
                }
            }
        });

        // Make the search results invisible by clicking the hide button.
        binding.hideSearchResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchResults.setVisibility(View.INVISIBLE);
                binding.hideSearchResults.setVisibility(View.INVISIBLE);
            }
        });


        // Searches the given query by pressing enter on the input text field for it
        binding.searchInputText.setOnKeyListener((view1, keyCode, keyEvent) -> {
            // close the filter view so that the search results become visible.
            binding.searchFilter.setVisibility(View.INVISIBLE);
            isFilterOpen = false;

            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                initializeSearch();
                return true;
            }

            return false;
        });
    }

    private void initializeSearch() {
        if (isFirstSearch) {
            Toast toast = Toast.makeText(getContext(), "The first search will take a little longer", Toast.LENGTH_SHORT);
            toast.show();
            isFirstSearch = false;
        }

        new Thread(() -> {
            String adder = "";
            if (!binding.allSearch.isChecked()) {
                if (binding.questionSearch.isChecked()) {adder += "?";}//searchType = SearchToken.Query.Question;}
                else if (binding.postSearch.isChecked()) {adder += "!";}//searchType = SearchToken.Query.Discussion;}
                else if (binding.topicSearch.isChecked()) {adder += "#";}//searchType = SearchToken.Query.Topic;}
                else if (binding.userSearch.isChecked()) {adder += "@";}//searchType = SearchToken.Query.User;}
                else {throw new NullPointerException("No search filter selected, which should never happen");}
            }
            List<SearchResult> searchResults = Search.getInstance().search(adder + binding.searchInputText.getText().toString());
            this.getActivity().runOnUiThread(() -> visualizeClickableSearchResults(searchResults));

        }).start();
    }

    private void visualizeClickableSearchResults(List<SearchResult> results) {
        if (results.size() == 0) {
            Toast toast = Toast.makeText(getContext(), "No relevant result found", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            layoutManagerRecyclerView = new LinearLayoutManager(getContext());
            binding.searchResults.setLayoutManager(layoutManagerRecyclerView);

            List<String> resultStrings = new ArrayList<>();
            results.forEach(res -> resultStrings.add(res.getStringResult()));
            String[] res = resultStrings.toArray(new String[0]);
            System.out.println("the search results: " + Arrays.toString(res));

            recyclerViewCustomAdapter = new RecyclerViewCustomAdapter(res);

            binding.searchResults.setAdapter(recyclerViewCustomAdapter);

            binding.searchResults.setVisibility(View.VISIBLE);
            binding.hideSearchResults.setVisibility(View.VISIBLE);


            // Navigate to where the search result is when it is clicked
            recyclerViewCustomAdapter.setOnItemClickListener(new RecyclerViewCustomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    SearchResult clickedSearch = results.get(position);
                    String clickedID = clickedSearch.getId();
                    SearchToken.Query clickedType = clickedSearch.getType();
                    String clickedResult = clickedSearch.getStringResult();

                    UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
                    switch (clickedType) {
                        case Question -> {
                            userInterfaceManager.setCurrentlyDisplayedQuestion(QuestionSet.getInstance().getQuestionFromID(clickedID));
                            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_questionFragment);
                        }
                        case Discussion -> {
                            Toast toast = Toast.makeText(getContext(), "Can't link you to the discussion yet :(", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        case Topic -> {
                            userInterfaceManager.setCurrentlyDisplayedCategory(QuestionSet.stringToCategory(clickedResult));
                            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_categoryFragment);
                        }
                        case User -> initializeNewDirectMessageFromSearch(clickedResult);
                    }
                }
            });

        }
    }

    private void initializeNewDirectMessageFromSearch(String usernameToDirectMessage) {
        // This username will guaranteed to be a valid one so no need to check its validity
        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
        Firebase.getInstance().readAllUsernamesAsync().then((obj) -> {
            List<String> usernames = (List<String>) obj;
            userInterfaceManager.getUiState().getValue().setToolbarTitle(usernameToDirectMessage);

            // If user is already someone we have messaged then we don't have to worry about setting up new local data
            // to support their DM, as it already exists
            boolean isUserInLocalDirectMessages = UserDirectMessages.getInstance().doesUserExistInDirectMessages(usernameToDirectMessage);

            if (!isUserInLocalDirectMessages) {
                // creating local DirectMessageThread
                DirectMessageThread dms = new DirectMessageThread(usernameToDirectMessage);

                // adding new user to our ViewModel data
                MessageListCard template = new MessageListCard(R.drawable.user_profile_default, usernameToDirectMessage, "", dms);
                UserDirectMessages.getInstance().currentDirectMessages.put(usernameToDirectMessage, template);
                UserDirectMessages.getInstance().currentDirectMessages.get(template.headingText).isNotification = false;
            }
            // Going to the direct message fragment
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_directMessageFragment);
            return null;
        });
    }

    private void generateAllHomeCategoryCards(Context context) {

        GeneratedUserInterfaceViewModel<HomeCategoryCard> genUserInterfaceManager = new ViewModelProvider(this).get(GeneratedUserInterfaceViewModel.class);

        for (Iterator iterator = genUserInterfaceManager.createIterator(); iterator.hasNext();) {
            HomeCategoryCard homeCategoryCard = (HomeCategoryCard) iterator.next();

            generateHomeCategoryCard(homeCategoryCard, getActivity());
        }

    }

    private void generateHomeCategoryCard(HomeCategoryCard template, Context context){

        // inflating XML into an object we can use
        ConstraintLayout homeCategoryCard = (ConstraintLayout) LayoutInflater.from(context).
                inflate(R.layout.home_category_card, null);

        // Setting background of parent view
        homeCategoryCard.getBackground().setColorFilter(new BlendModeColorFilter(template.getCardColor(), BlendMode.SRC_ATOP));

        // Setting layout parameters relative to the parent as this is not set from the inflated file
        ConstraintLayout.LayoutParams homeCategoryCardLayoutParams = new ConstraintLayout.LayoutParams(
                ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics())),
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );
        homeCategoryCardLayoutParams.setMarginEnd(((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        homeCategoryCard.setLayoutParams(homeCategoryCardLayoutParams);

        // These indices correspond to what's in the inflated XML file
        ImageView categoryImage = (ImageView) ((ConstraintLayout) homeCategoryCard.getChildAt(0)).getChildAt(0);
        TextView headingText = (TextView) homeCategoryCard.getChildAt(1);
        TextView subheadingText = (TextView) homeCategoryCard.getChildAt(2);

        DynamicLocalization.translatedOrDefaultText(template.getHeading(), headingText);
        DynamicLocalization.translatedOrDefaultText(template.getSubheading(), subheadingText);
        categoryImage.setImageResource(template.getCardImage());

        // Adding the final parent to the LinearLayout nested within the ScrollView
        binding.homeCategoryCarouselScrollable.addView(homeCategoryCard);

        // Setting on click listener to the entire view
        homeCategoryCard.setOnClickListener(view -> {
            // Updating the view model so Categories fragment will update when it is created
            UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(getActivity()).get(UserInterfaceManagerViewModel.class);
            userInterfaceManager.setCurrentlyDisplayedCategory(template.getCategory());
            // Navigating to the Categories fragment
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_HomeFragment_to_categoryFragment);
        });


    }
}