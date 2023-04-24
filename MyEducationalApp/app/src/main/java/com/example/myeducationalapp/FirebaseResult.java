package com.example.myeducationalapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class FirebaseResult {
    Object result;

    // The use of a CountDownLatch came from here:
    // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
    final CountDownLatch gotResult = new CountDownLatch(1);

    final List<Function<Object, Object>> callbacks;

    public FirebaseResult then(Function<Object, Object> listener) {
        synchronized (gotResult) {
            if (gotResult.getCount() == 0) {
                result = listener.apply(result);
            } else {
                synchronized (callbacks) {
                    callbacks.add(listener);
                }
            }
        }
        return this;
    }

    enum Direction {
        READ,
        WRITE
    }

    public FirebaseResult(DatabaseReference ref, Object value) {
        this(Direction.WRITE, ref, value);
    }

    public FirebaseResult(DatabaseReference ref) {
        this(Direction.READ, ref, null);
    }

    private FirebaseResult(Direction dir, DatabaseReference ref, Object value) {
        callbacks = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                synchronized (gotResult) {
                    synchronized (callbacks) {
                        gotResult.countDown();
                        result = snapshot.getValue();
                        while (!callbacks.isEmpty()) {
                            result = callbacks.remove(0).apply(result);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (dir == Direction.READ) {
            ref.get();
        } else {
            ref.setValue(value);
        }
    }
}
