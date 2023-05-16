package com.example.myeducationalapp.userInterface.Generatable;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GeneratedUserInterfaceViewModel<T> extends ViewModel implements IterableCollection {

    private ArrayList<T> listOfGeneratables = new ArrayList<>();

    public void addToListOfElements(T element) {

        if (element != null){
            listOfGeneratables.add(element);
        }

    }

    // Iterator to iterate over and access listOfGeneratables
    @Override
    public Iterator createIterator() {
        return new GeneratedUserInterfaceIterator();
    }

    private class GeneratedUserInterfaceIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            return listOfGeneratables != null && index < listOfGeneratables.size();
        }

        @Override
        public T next() {
            if (this.hasNext()) {
                return listOfGeneratables.get(index++);
            }
            return null;
        }
    }
}
