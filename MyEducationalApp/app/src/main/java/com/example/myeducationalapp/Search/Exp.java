package com.example.myeducationalapp.Search;

import java.util.List;

/**
 * Abstract class to represent search expressions
 * @author Jayden Skidmore
 */
public abstract class Exp {
    //Returns the expression as a string
    public abstract String show();
    //Decomposes the expression into a list of words
    public abstract List<String> decomposition();
}

