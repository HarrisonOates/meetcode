package com.example.myeducationalapp.userInterface.Generation;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GeneratedUserInterfaceViewModel<T> extends ViewModel {

    public ArrayList<T> listOfElements = new ArrayList<>();

    public void addToListOfElements(T element) {

        if (element != null){
            listOfElements.add(element);
        }

    }


}
