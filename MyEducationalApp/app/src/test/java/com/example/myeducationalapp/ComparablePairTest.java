package com.example.myeducationalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.myeducationalapp.User.ComparablePair;

import org.junit.Test;

public class ComparablePairTest {
    @Test
    public void testWithBothValuesBigger() {
        ComparablePair<Integer> a = new ComparablePair<>(4, 6);
        ComparablePair<Integer> b = new ComparablePair<>(3, 5);

        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));
    }

    @Test
    public void testWithFirstValueBigger() {
        ComparablePair<Integer> a = new ComparablePair<>(4, 5);
        ComparablePair<Integer> b = new ComparablePair<>(3, 6);

        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));
    }

    @Test
    public void testWithSameFirstValueDifferentSecond() {
        ComparablePair<Integer> a = new ComparablePair<>(4, 5);
        ComparablePair<Integer> b = new ComparablePair<>(4, 3);

        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));
    }

    @Test
    public void testWithSameFirstValueSameSecond() {
        ComparablePair<Integer> a = new ComparablePair<>(4, 3);
        ComparablePair<Integer> b = new ComparablePair<>(4, 3);

        assertEquals(0, a.compareTo(b));
        assertEquals(0, b.compareTo(a));
    }

    @Test
    public void testToString() {
        ComparablePair<Integer> a = new ComparablePair<>(4, 3);
        assertEquals("4@3", a.toString());
    }
}
