package com.example.myeducationalapp;

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
    // adapted from here:
    // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase

    Object result;
    final CountDownLatch gotResult = new CountDownLatch(1);
    final List<Function<Object, Object>> callbacks;

    public Object waitForResult() {
        try {
            new Thread(() -> {
                while (true) {
                    try {
                        gotResult.await();

                    } catch (InterruptedException ignored) {

                    }

                    synchronized (gotResult) {
                        if (gotResult.getCount() == 0) {
                            break;
                        }
                    }
                }

                synchronized (callbacks) {
                    while (!callbacks.isEmpty()) {
                        result = callbacks.remove(0).apply(result);
                    }
                }

            }).join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public FirebaseResult apply(Function<Object, Object> listener) {
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

    public FirebaseResult(DatabaseReference ref) {
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

        ref.get();
    }
}
