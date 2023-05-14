package com.example.myeducationalapp.DatastreamSimulation;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.Person;

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

        // We choose a random account to send it from
        String[] randomUserNames = new String[]{"harrison", "geun", "alex", "jayden", "nikhila"};

        for (int i = 0; i < 2500; i++){
            int n = r.nextInt(4);

            switch (n){
                case 0 -> {
                    // directMessageThread
                    int userNameIndex = r.nextInt(4);
                    DirectMessageThread dms = new DirectMessageThread(randomUserNames[userNameIndex]);
                    dms.runWhenReady((ignored) -> {
                        dms.postMessage("");
                        return null;
                    });


                }
                case 1 -> {
                    // directMessage with a code block

                }
                case 2 -> {
                    // questionMessageThread

                }
                case 3 -> {
                    // questionMessage with a code block
                }
                case 4 -> {
                    // Like a random message
                }
                default -> {
                    continue;
                }
            }



        }


    }
}
