package com.example.myeducationalapp.UserInterface.Generation;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratedUserInterfaceViewModel extends ViewModel {

    // TODO Plan
    // 1. Init this view model in every OnCreate for fragments
    // 2. Have ui element generation code within the fragments themselves
    // 3. Use data persisted in this viewmodel to draw ui elements using the relevant methods in fragments
    //      L-> differentiate the elements from each other somehow
    // - might be able to simplify GeneratedUserInterfaceElement to a single thing with a hashmap of id->element
    //   need a way of differentiating the elements somehow when storing them
    // - hope this works

    public ArrayList<GeneratedUserInterfaceElement> listOfElements = new ArrayList<>();

    public void addToListOfElements(GeneratedUserInterfaceElement element) {

        if (element != null){
            listOfElements.add(element);
        }

    }


}
