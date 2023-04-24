package com.example.myeducationalapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FirebaseDebugger {
    DatabaseReference root;
    DatabaseReference current;

    public FirebaseDebugger(DatabaseReference root) {
        this.root = root;
        this.current = root;
    }

    public void debugWrite() {
        // adapted from here:
        // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
        final CountDownLatch done = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            DatabaseReference ref = current.child("test");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    done.countDown();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            ref.setValue("this is some test data");

            try {
                done.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();

    }

    public void debugRead() {
        // adapted from here:
        // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
        final CountDownLatch done = new CountDownLatch(1);

        DatabaseReference ref = current.child("test");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("fb", "data changed B");
                Log.w("fb", snapshot.getValue().toString());
                done.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.get();



    }


    public void goUp() {
        if (current.getParent() != root && current.getParent() != null) {
            current = current.getParent();
        }
    }

    public void goDown(String childName) {
        current = current.child(childName);
    }

    public void set(Object value) {
        current.setValue(value);
    }

    public Object get() {
        return null;
    }

    public List<String> getChildren() {
        List<String> results = new ArrayList<>();
        for (DataSnapshot dataSnapshot : current.get().getResult().getChildren()) {
            results.add(dataSnapshot.getValue(String.class));
        }
        return results;
    }
}
