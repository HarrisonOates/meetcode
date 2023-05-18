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

    /**
     * Used to synchronise whether or not Firebase has returned a result or not.
     * Set to 1 if the result has not come, or 0 if the result has come. The use of a
     * CountDownLatch comes from here:
     * https://stackoverflow.com/questions/55785311/how-do-i-wait-until-my-data-is-retrieved-from-firebase
     */
    private CountDownLatch gotResult = new CountDownLatch(1);

    /**
     * Stores which functions need to be called at the completion of the callback. They are
     * stored in order, with the earliest elements needing to be called first.
     */
    private List<Function<Object, Object>> callbacks = new ArrayList<>();

    /**
     * Registers a callback that will be executed when the data is filled. Once the data is filled,
     * all registered callbacks execute in order. If the data has already been filled, the callback
     * is executed immediately.
     *
     * @param listener A callback that gets called when the data loads. The Object argument is the
     *                 result from Firebase. The return value of the callback becomes the new value
     *                 of the object.
     * @return A new FirebaseResult which can be used to chain .then() calls. The return value from
     * the callback will be used in the value for the next one.
     */
    public FirebaseResult then(Function<Object, Object> listener) {
        if (gotResult.getCount() == 0) {
            /*
             * The data is already here, so just call the function.
             */
            if (callbacks.size() != 0) {
                throw new AssertionError("callbacks non-null when then called on fulfilled result");
            }
            result = listener.apply(result);
        } else {
            /*
             * We don't have the data yet, so just save the callback for later.
             */
            callbacks.add(listener);
        }

        return this;
    }

    /**
     * Used to determine if a read or a write operation should take place.
     */
    enum Direction {
        READ,
        WRITE
    }

    /**
     * Constructs a write call to Firebase.
     * @param ref The object to read
     * @param value The value to write
     */
    public FirebaseResult(DatabaseReference ref, Object value) {
        this(Direction.WRITE, ref, value);
    }

    /**
     * Constructs a read call from Firebase.
     * @param ref The object to read
     */
    public FirebaseResult(DatabaseReference ref) {
        this(Direction.READ, ref, null);
    }

    /**
     * Waits for the data to be ready and then returns it.
     * WARNING: This needs to be called from a separate thread otherwise Firebase will not
     * return any results, and the app will hang.
     *
     * @return The object from firebase
     */
    public Object await() {
        try {
            /*
             * Wait until the data is there. This blocks, and will hang the app if not
             * called from a different thread.
             */
            gotResult.await();

            /*
             * Reply all stored callbacks.
             */
            while (!callbacks.isEmpty()) {
                result = callbacks.remove(0).apply(result);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /**
     * Constructs a read or write operation to/from Firebase.
     * @param dir Whether or not it is a read or write operation
     * @param ref The object to access
     * @param value The value to write, if it is a write operation. This value doesn't matter on
     *              read operations.
     */
    private FirebaseResult(Direction dir, DatabaseReference ref, Object value) {
        /*
         * Register a listener so we can be notified when the operation completes.
         * This is used to run the .then() callbacks, as well as updating any observers
         * which may be waiting for data changes.
         */
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*
                 * Determine whether or not it is a DM read/write by looking at the current key
                 * and any of its parents. If it is, there may be an observer which needs to be
                 * notified.
                 */
                DatabaseReference current = snapshot.getRef();
                DatabaseReference parent = current.getKey() == null ? null : current.getParent();
                DatabaseReference grandparent = parent == null || parent.getKey() == null ? null : parent.getParent();

                if (grandparent != null && grandparent.getKey() != null && grandparent.getKey().equals("dm")) {
                    /*
                     * Can now reconstruct the exact DM path and update any observers.
                     */
                    List<String> dmPath = Firebase.getInstance().getDirectMessageFilepath(current.getKey(), parent.getKey(), false);
                    Firebase.getInstance().notifyObservers(dmPath);
                }

                /*
                 * Mark it as having received the data.
                 */
                gotResult.countDown();
                result = snapshot.getValue();

                /*
                 * Replay any saved callbacks.
                 */
                while (!callbacks.isEmpty()) {
                    result = callbacks.remove(0).apply(result);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "that's not good");
            }
        });

        /*
         * Now we can just perform the read/write directly to/from Firebase.
         */
        if (dir == Direction.READ) {
            ref.get();
        } else {
            ref.setValue(value);
        }
    }
}
