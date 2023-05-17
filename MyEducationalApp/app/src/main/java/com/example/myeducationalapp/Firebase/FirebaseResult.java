package com.example.myeducationalapp.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;


/**
 * Stores the result of a database request. Used to get asynchronous results. The result is not
 * immediately returned, instead callbacks can be chained using the then() method to read the data.
 *
 * @author u7468248 Alex Boxall
 */

public class FirebaseResult {
    private Object result;

    // The use of a CountDownLatch came from here:
    // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
    private CountDownLatch gotResult = new CountDownLatch(1);

    private List<Function<Object, Object>> callbacks = new ArrayList<>();

    /**
     *
     * @param listener A callback that gets called when the data loads. The Object argument is the
     *                 result from Firebase. The return value of the callback becomes the new value
     *                 of the object.
     * @return A new FirebaseResult which can be used to chain .then() calls. The return value from
     * the callback will be used in the value for the next one.
     */
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

    /**
     * Waits for the data to be ready and then returns it. This needs to be called from a
     * separate thread otherwise Firebase will not return any results.
     *
     * @return The object from firebase
     */
    public Object await() {
        try {
            gotResult.await();
            while (!callbacks.isEmpty()) {
                result = callbacks.remove(0).apply(result);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private FirebaseResult(Direction dir, DatabaseReference ref, Object value) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                {
                    DatabaseReference current = snapshot.getRef();
                    DatabaseReference parent = current.getKey() == null ? null : current.getParent();
                    DatabaseReference grandparent = parent == null || parent.getKey() == null ? null : parent.getParent();

                    if (grandparent != null && grandparent.getKey() != null && grandparent.getKey().equals("dm")) {
                        List<String> dmPath = Firebase.getInstance().getDirectMessageFilepath(current.getKey(), parent.getKey(), false);
                        System.out.printf("DM PATH = %s\n", dmPath);
                        Firebase.getInstance().notifyObservers(dmPath);
                    }
                }

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
