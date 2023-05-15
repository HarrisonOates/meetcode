package com.example.myeducationalapp.DatastreamSimulation;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.DirectMessageThreadDatastream;
import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.Firebase.FirebaseRequest;
import com.example.myeducationalapp.Firebase.FirebaseResult;
import com.example.myeducationalapp.MessageThread;
import com.example.myeducationalapp.Person;
import com.example.myeducationalapp.QuestionMessageThread;
import com.example.myeducationalapp.QuestionMessageThreadDatastream;
import com.example.myeducationalapp.UserLogin;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Random;
import java.util.Scanner;

/**
 * Simulates a data stream to satisfy the basic features of the app.
 * As we're using a client-server model, this is a workaround for the purposes of the assignment.
 * @author u7468212 Harrison Oates
 */
public class DataGenerator {


    /**
     * Generates the data for the app to read from.
     */
    public void generateData() throws FileNotFoundException, InterruptedException {
        Random r = new Random();

        // We're simulating real conversations between people.
        File beeMovie = new File("beemovie.txt");
        Scanner sc = new Scanner(beeMovie);

        // We choose a random account to send it from
        String[] randomUserNames = new String[]{"harrison", "geun", "alex", "jayden", "nikhila"};
        for (int i = 0; i < 2500; i++){
            int n = r.nextInt(4);
            int userNameIndex = r.nextInt(4);
            switch (n){
                case 0 -> {

                    DirectMessageThreadDatastream dms = new DirectMessageThreadDatastream(UserLogin.getInstance().getCurrentUsername(), randomUserNames[userNameIndex]);
                    dms.runWhenReady((ignored) -> {
                        dms.postMessageDatastream(sc.nextLine(), randomUserNames[userNameIndex]);
                        return null;
                    });


                }
                case 1 -> {
                    // directMessage with a code block
                    // Will work this out later

                }
                case 2 -> {
                    // questionMessageThread
                    // Need to get the question message thread ID
                    QuestionMessageThreadDatastream qms = new QuestionMessageThreadDatastream(randomUserNames[userNameIndex]);
                    qms.runWhenReady((ignored) -> {
                        qms.postMessage(sc.nextLine());
                        return null;
                    });
                }
                case 3 -> {
                    // questionMessage with a code block
                    // Work this out later
                }
                case 4 -> {
                    // Like / unlike a random message
                    DirectMessageThreadDatastream dms = new DirectMessageThreadDatastream(UserLogin.getInstance().getCurrentUsername(), randomUserNames[userNameIndex]);
                    int indexToLike = r.nextInt(dms.getMessages().size() - 1);
                    dms.runWhenReady((ignored) -> {
                        dms.getMessages().get(indexToLike).runWhenReady((ignored2) -> {
                            dms.getMessages().get(indexToLike).toggleLikedByCurrentUser();
                            return null;
                        });
                        return null;
                    });
                }
                default -> {
                    continue;
                }
            }

            Thread.sleep(3000);

        }


    }
}
