package com.example.myeducationalapp.DatastreamSimulation;

import com.example.myeducationalapp.DirectMessageThread;

import java.util.Random;

/**
 * Simulates a data stream to satisfy the basic features of the app.
 * @author u7468212 Harrison Oates
 */
public class DataGenerator {


    /**
     * Generates the data for the app to read from.
     */
    public static void generateData(){
        // TODO - remove after implementation

        // This is how to send direct messages to firebase.
        /*
        DirectMessageThread dms = new DirectMessageThread("username");
        dms.runWhenReady((ignored) -> {
            dms.postMessage("hi");
            return null;
        });
         */
        // use new QuestionMessageThread instead for comments on questions.

        // This is how to like messages
        /*
        DirectMessageThread dms = new DirectMessageThread("username");
        dms.runWhenReady((ignored) -> {
           dms.getMessages().get(message_index).runWhenReady((ignored2) -> {
               dms.getMessages().get(message_index).toggleLikedByCurrentUser();
               return null;
           });
        });
         */

        Random r = new Random();

        // TODO - set up the files to read from and iterate at least 2500 times.
        // TODO -  implement three second delay between each loop.

    }
}
