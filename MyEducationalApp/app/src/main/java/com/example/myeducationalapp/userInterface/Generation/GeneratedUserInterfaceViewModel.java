package com.example.myeducationalapp.userInterface.Generation;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GeneratedUserInterfaceViewModel extends ViewModel {

    public ArrayList<GeneratedUserInterfaceElement> listOfElements = new ArrayList<>();

    public void addToListOfElements(GeneratedUserInterfaceElement element) {

        if (element != null){
            listOfElements.add(element);
        }

    }


}
