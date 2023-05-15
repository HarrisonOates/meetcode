package com.example.myeducationalapp.DatastreamSimulation;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

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
import com.example.myeducationalapp.MainActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    public static void generateData(Context context) throws FileNotFoundException, InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();

                AssetManager am = context.getAssets();

                // We're simulating real conversations between people.
                InputStream beeMovie = null;
                try {
                    beeMovie = am.open("beemovie.txt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scanner sc = null;
                sc = new Scanner(beeMovie);

                // We choose a random account to send it from
                String[] randomUserNames = new String[]{"harrison", "geun", "alex", "jayden", "nikhila"};
                for (int i = 0; i < 2500; i++){
                    int n = r.nextInt(4);
                    int userNameIndex = r.nextInt(4);
                    switch (n){
                        case 0:{

                            DirectMessageThreadDatastream dms = new DirectMessageThreadDatastream(UserLogin.getInstance().getCurrentUsername(), randomUserNames[userNameIndex]);
                            Scanner finalSc = sc;
                            dms.runWhenReady((ignored) -> {
                                dms.postMessageDatastream(finalSc.nextLine(), randomUserNames[userNameIndex]);
                                return null;
                            });

                            break;
                        }
                        //case 1 -> {
                        // directMessage with a code block
                        // Will work this out later

                        // }
                        case 2:{
                            // questionMessageThread
                            // Need to get the question message thread ID
                            QuestionMessageThreadDatastream qms = new QuestionMessageThreadDatastream(randomUserNames[userNameIndex]);
                            Scanner finalSc1 = sc;
                            qms.runWhenReady((ignored) -> {
                                qms.postMessage(finalSc1.nextLine());
                                return null;
                            });
                            break;
                        }
                        //case 3 -> {
                        // questionMessage with a code block
                        // Work this out later
                        // }
                        case 4:{
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
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

                sc.close();
                try {
                    beeMovie.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        t.start();

    }
}
