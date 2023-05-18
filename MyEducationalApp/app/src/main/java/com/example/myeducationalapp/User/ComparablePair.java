package com.example.myeducationalapp.User;

/**
 * Used to store two integers in a way that implements the Comparable interface (so it can
 * be added to an AVL tree).
 *
 * @author u7468248 Alex Boxall
 */
public class ComparablePair<T extends Comparable> implements Comparable<ComparablePair<T>> {
    public T first;
    public T second;

    public ComparablePair(T a, T b) {
        this.first = a;
        this.second = b;
    }

    @Override
    public int compareTo(ComparablePair<T> t) {
        int res = first.compareTo(t.first);
        if (res == 0) {
            return second.compareTo(t.second);
        } else {
            return res;
        }
    }

    @Override
    public String toString() {
        return first.toString() + "@" + second.toString();
    }
}
