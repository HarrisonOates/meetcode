package com.example.myeducationalapp.Firebase;

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


/**
 * FirebaseResult.java
 */
public class FirebaseResult {
    Object result;

    // The use of a CountDownLatch came from here:
    // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
    CountDownLatch gotResult = new CountDownLatch(1);

    List<Function<Object, Object>> callbacks = new ArrayList<>();

    public FirebaseResult then(Function<Object, Object> listener) {
        if (gotResult.getCount() == 0) {
            if (callbacks.size() != 0) {
                throw new AssertionError("callbacks non-null when then called on fulfilled result");
            }
            result = listener.apply(result);
        } else {
            callbacks.add(listener);
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

    public Object await() {
        try {
            gotResult.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private FirebaseResult(Direction dir, DatabaseReference ref, Object value) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gotResult.countDown();
                result = snapshot.getValue();
                while (!callbacks.isEmpty()) {
                    result = callbacks.remove(0).apply(result);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("wtf!", "that's not good");
            }
        });

        if (dir == Direction.READ) {
            ref.get();
        } else {
            ref.setValue(value);
        }
    }
}
