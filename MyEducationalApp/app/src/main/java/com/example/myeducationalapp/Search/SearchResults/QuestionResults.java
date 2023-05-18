package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Question.Question;
import com.example.myeducationalapp.Question.QuestionSet;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

/**
 * Obtains the results of how likely each question is to be the result being searched for
 * @author u7146309
 */
public class QuestionResults extends Results{
    /**
     * A list of all possible search results, which is updated to include likelihood
     */
    private ArrayList<SearchResult> searchResults;

    /**
     * A map of all the question topics and their weighting of likelihood
     */
    HashMap<QuestionSet.Category, Double> topics = new HashMap<>(Map.of(QuestionSet.Category.Algorithm,1.0, QuestionSet.Category.ControlFlow,1.0, QuestionSet.Category.DataStructure,1.0, QuestionSet.Category.Miscellaneous,1.0, QuestionSet.Category.Recursion,1.0));

    /**
     * Returns a list containing the results from a question search
     * @param search The user inputted search
     * @return A list containing results from each question
     */
    @Override
    public List<SearchResult> results(List<String> search) {
        if (searchResults == null || searchResults.size() == 0) updateQuestions();

        searchResults.forEach(searchResult -> {
            double count = 0;
            for (var searchWord : search) {
                for (var questionWord : searchResult.getWords()) {
                    if (Objects.equals(searchWord.toLowerCase(), questionWord.toLowerCase())) {
                        count++;
                        break;
                    }
                    else if (questionWord.toLowerCase().contains(searchWord.toLowerCase())) {
                        count += (double)searchWord.length()/(double)questionWord.length();
                        break;
                    }
                }
            }


            double percentage = count / (double) search.size();
            double confidence = -12 * Math.pow((percentage - 1),2.0);

            var multiplier = topics.get(QuestionSet.charToCategory(searchResult.getId().charAt(0)));

            searchResult.setConfidence(multiplier * confidence);
        });

        return searchResults;
    }

    /**
     * Updates the questions separately, instead of updating every time something is searched
     */
    public void updateQuestions() {
        SortedMap<String, Question> questions = QuestionSet.getInstance().getUsedQuestionSets();

        if (questions == null) return;

        searchResults = new ArrayList<>();

        var entrySet = questions.keySet();
        for (String entry : entrySet) {
            if (!topics.containsKey(questions.get(entry).getCategory())) continue;

            var searchResult = new SearchResult(entry, SearchToken.Query.Question, List.of(questions.get(entry).getName().split(" ")));

            searchResult.setConfidence(0);

            searchResults.add(searchResult);

        }
    }


    /**
     * Sets the likelihood of each question for being the one searched for
     * @param topics A map of each question and it's likelihood
     */
    public void setTopics(HashMap<QuestionSet.Category, Double> topics) {
        this.topics = topics;
    }
}
