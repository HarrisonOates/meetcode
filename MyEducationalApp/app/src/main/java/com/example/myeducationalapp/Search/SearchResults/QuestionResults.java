package com.example.myeducationalapp.Search.SearchResults;

import com.example.myeducationalapp.Question;
import com.example.myeducationalapp.QuestionSet;
import com.example.myeducationalapp.Search.SearchParsing.SearchToken;

import java.util.ArrayList;
import java.util.Arrays;
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
            double confidence = searchResult.getConfidence();
            double count = 0;
            for (var searchWord : search) {
                for (var questionWord : searchResult.getWords()) {
                    if (questionWord.charAt(0) == 'Y') {
                        int n = 0;
                    }


                    if (Objects.equals(searchWord.toLowerCase(), questionWord.toLowerCase())) {
                        count++;
                        break;
                    }
                    else if (questionWord.toLowerCase().contains(searchWord.toLowerCase())) {
                        count += (double)searchWord.length()/(double)questionWord.length();
                        break;
                    }

                }


//                for (var questionWord : searchResult.getWords()) {
//                    if (Objects.equals(searchWord.toLowerCase(), questionWord.toLowerCase())) confidence++;
//                    else if (questionWord.contains(searchWord)) confidence += 0.1;
//                    else confidence -= (double)search.size()/(double)searchResult.getWords().size();
//                    //TODO: Currently, caps
//                    //TODO: Currently, prioritises number of words
//                    //TODO: Currently only calculates for one word each
//                }
            }
            double percentage = count / (double) search.size();
            confidence = -12 * Math.pow((percentage - 1),2.0);

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
        //HashMap<String, String[]> questions = new HashMap<>();
        //TODO
        if (questions == null) return;

        searchResults = new ArrayList<>();

        var entrySet = questions.keySet();
        for (String entry : entrySet) {
            if (!topics.containsKey(questions.get(entry).getCategory())) continue;//Arrays.stream(topics.).noneMatch(x -> x == entry.charAt(0))) continue;

            //var searchResult = new SearchResult(entry, SearchToken.Query.Question, List.of(questions.get(entry).getContent().split(" ")));
            var searchResult2 = new SearchResult(entry, SearchToken.Query.Question, List.of(questions.get(entry).getName().split(" ")));

            //searchResult.setConfidence(-20); //TODO
            searchResult2.setConfidence(0);

            //searchResults.add(searchResult);
            searchResults.add(searchResult2);

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
