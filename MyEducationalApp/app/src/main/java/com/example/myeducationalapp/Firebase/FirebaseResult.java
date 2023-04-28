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
 *
 * The world's most disgusting code. An absolute atrocity. Hides the ugliness of Firebase
 * by wrapping it in something uglier, and then making that look pretty.
 */
public class FirebaseResult {
    Object result;

    // The use of a CountDownLatch came from here:
    // https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
    CountDownLatch gotResult = new CountDownLatch(1);

    List<Function<Object, Object>> callbacks = new ArrayList<>();

    /**
     * Waits for the current callback to complete, and another one, before calling any
     * .then() handlers. All .then() handlers will be called before the merge. The return
     * value of the data is not guaranteed to be consistent, and may be from either.
     *
     * @param other
     * @return
     */
    public FirebaseResult merge(FirebaseResult other) {
        then((obj) -> {
            Log.w("dbg", "running the merge");
            if (gotResult.getCount() == 0 && other.gotResult.getCount() == 0) {
                /*
                 * Both are already done, so don't need to do anything.
                 */

            } else if (gotResult.getCount() == 0 && other.gotResult.getCount() == 1) {
                /*
                 * This one is done, but we have to wait for the other one to finish.
                 * Hence we can just return the other one.
                 */
                gotResult = other.gotResult;
                callbacks = other.callbacks;
                result = other.result;

                Log.w("dbg", "playing the waiting game");

            } else {
                throw new AssertionError("how did this .then() handler run without the thing finishing??");
            }
            return obj;
        });

        return this;
    }

    public FirebaseResult then(Function<Object, Object> listener) {
            if (gotResult.getCount() == 0) {
                if (callbacks.size() != 0) {
                    throw new AssertionError("callbacks non-null when then called on fulfilled result");
                }
                result = listener.apply(result);
            } else {
                Log.w("dbg", "added a callback");
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
