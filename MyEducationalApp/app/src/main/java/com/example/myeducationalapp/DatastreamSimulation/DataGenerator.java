package com.example.myeducationalapp.DatastreamSimulation;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.myeducationalapp.DirectMessageThread;
import com.example.myeducationalapp.QuestionSet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

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
        Thread t = new Thread(() -> {
            Random r = new Random();

            AssetManager am = context.getAssets();

            // We're simulating real conversations between people.
            InputStream beeMovie = null;
            try {
                beeMovie = am.open("beemovie.txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scanner sc = new Scanner(beeMovie);

            // The questions present in the database to comment on
            Set<String> questions = QuestionSet.getInstance().getQuestionIDs();
            String[] questionIDs = new String[questions.size()];
            questions.toArray(questionIDs);

            // Some sample answers that we'll draw from to replay
            String[] exampleCodeBlocks = new String[]{
                    "Here's how I would do Fibonacci in Java\n"
                    + "```public static int fib(n) { \n"
                    + "    if (n == 0 || n == 1){ \n"
                    + "        return 1;\n"
                    + "    }\n"
                    + "    return fib(n - 1) + fib(n - 2);\n"
                    + "}``` \n"
                    + "As you can see, this has O(2^n) complexity, so it isn't great.",

                    "It turns out XOR is commutative! How cool is that!\n"
                    + "``` a ^ b = b ^ a;```",
                    "```Class Simple { \n"
                    + "    public static void main(String args[]){ \n"
                    + "        System.out.println(\"Hello World!\");\n"
                    + "    }\n"
                    + "}```",

                    "Ready for the exam tomorrow? Remember this is bad code. \n"
                    + "```while (true) {\n"
                    + "    System.out.println(\"I'm panicking\");\n"
                    + "}```",

                    "The below code chooses where we're going this Thursday: \n"
                    + "```Random r = new Random(); \n"
                    + "int n = r.nextInt(4); \n"
                    + "switch (n) {\n"
                    + "    case 0 -> {System.out.println(\"Badger\");} \n"
                    + "    case 1 -> {System.out.println(\"One22\");} \n"
                    + "    case 2 -> {System.out.println(\"Mooseheads\");} \n"
                    + "    case 3 -> {System.out.println(\"COMP2100 revision event\");} \n"
                    + "    case 4 -> {System.out.println(\"To sleep!\");} \n"
                    + "}```"
            };



            for (int i = 0; i < 2500; i++){
                int n = r.nextInt(3);

                switch (n) {
                    case 0 -> {
                        // Sending DMs without code blocks
                        DirectMessageThread dms = new DirectMessageThread("comp2100@anu.au");
                        dms.runWhenReady((ignored) -> {
                            dms.postMessage(sc.nextLine());
                            return null;
                        });

                    }

                    case 1 -> {
                        // directMessage with a code block
                        DirectMessageThread dms = new DirectMessageThread("comp2100@anu.au");
                        int codeBlockSelector = r.nextInt(exampleCodeBlocks.length);
                        dms.runWhenReady((ignored) -> {
                            dms.postMessage(exampleCodeBlocks[codeBlockSelector]);
                            return null;
                        });


                    }
                    case 2 -> {
                        // Like / unlike a random message
                        DirectMessageThread dms = new DirectMessageThread("comp2100@anu.au");
                        if (dms.getMessages().size() > 0) {
                            int indexToLike = r.nextInt(dms.getMessages().size());

                            dms.runWhenReady((ignored) -> {
                                dms.getMessages().get(indexToLike).runWhenReady((ignored2) -> {
                                    dms.getMessages().get(indexToLike).toggleLikedByCurrentUser();
                                    return null;
                                });
                                return null;
                            });
                        }

                    }
                    default -> {
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
        });

        t.start();

    }
}
