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
    final CountDownLatch gotResult = new CountDownLatch(1);

    final List<Function<Object, Object>> callbacks = new ArrayList<>();

    private FirebaseResult this_ = this;

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
            synchronized (this_.gotResult) {
                synchronized (other.this_.gotResult) {
                    if (this_.gotResult.getCount() == 0 && other.this_.gotResult.getCount() == 0) {
                        /*
                         * Both are already done, so don't need to do anything.
                         */

                    } else if (this_.gotResult.getCount() == 0 && other.this_.gotResult.getCount() == 1) {
                        /*
                         * This one is done, but we have to wait for the other one to finish.
                         * Hence we can just return the other one.
                         */
                        this_ = other.this_;

                    } else {
                        throw new AssertionError("how did this .then() handler run without the thing finishing??");
                    }
                }
            }
            return obj;
        });

        return this;
    }

    public FirebaseResult then(Function<Object, Object> listener) {
        synchronized (this_.gotResult) {
            if (this_.gotResult.getCount() == 0) {
                if (this_.callbacks.size() != 0) {
                    throw new AssertionError("callbacks non-null when then called on fulfilled result");
                }
                this_.result = listener.apply(this_.result);
            } else {
                synchronized (this_.callbacks) {
                    this_.callbacks.add(listener);
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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                synchronized (this_.gotResult) {
                    synchronized (this_.callbacks) {
                        this_.gotResult.countDown();
                        this_.result = snapshot.getValue();
                        while (!this_.callbacks.isEmpty()) {
                            this_.result = this_.callbacks.remove(0).apply(this_.result);
                        }
                    }
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
