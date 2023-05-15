package com.example.myeducationalapp;

import com.example.myeducationalapp.Firebase.FirebaseResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A base class for all Firebase related things that you need to be able to wait on.
 * Provides basic methods for waiting on the object, and adding thing that need to be
 * waited on.
 *
 * @author u7468248 Alex Boxall
 */

public class Asynchronous {
    private List<FirebaseResult> requirements = new ArrayList<>();

    protected void clearRequirements() {
        requirements.clear();
    }

    protected void addWaitRequirement(FirebaseResult result) {
        requirements.add(result);
    }

    public FirebaseResult getWaitRequirement() {
        if (requirements.size() != 1) {
            throw new AssertionError("Alex wants to cry");
        }

        return requirements.get(0);
    }

    private void runWhenReadyInternal(Function<Object, Object> func, int index) {
        if (index < requirements.size()) {
            requirements.get(index).then((obj) -> {
                runWhenReadyInternal(func, index + 1);
                return obj;
            });
        } else {
            func.apply(null);
        }
    }

    public void runWhenReady(Function<Object, Object> func) {
        runWhenReadyInternal(func, 0);
    }
}
