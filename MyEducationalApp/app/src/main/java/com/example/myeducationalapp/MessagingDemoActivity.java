package com.example.myeducationalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myeducationalapp.Firebase.Firebase;

public class MessagingDemoActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_demo);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        ListView list = (ListView) findViewById(R.id.myListView);
        list.setAdapter(adapter);

        ((Button) findViewById(R.id.demoSendBtn)).setEnabled(false);
        ((EditText) findViewById(R.id.demoMsgText)).setEnabled(false);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());
                DirectMessageThread dms = new DirectMessageThread(to);
                dms.runWhenReady((ignored1) -> {
                    dms.getMessages().get(i).runWhenReady((ignored2) -> {
                        dms.getMessages().get(i).toggleLikedByCurrentUser();
                        return null;
                    });
                    return null;
                });
            }
        });

        Button backBtn =findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("DefaultLocale")
    public void update(String text, String to) {
        DirectMessageThread dms = new DirectMessageThread(to);
        dms.runWhenReady((obj) -> {
            if (text != null) {
                dms.postMessage(text);
            }
            // no change
            if (adapter.getCount() == dms.getMessages().size()) {
                return null;
            }
            adapter.clear();
            for (Message msg: dms.getMessages()) {
                msg.getPoster().runWhenReady((ignored) -> {
                    adapter.add(String.format("%s: %d likes: %s", msg.getPoster().getUsername(), msg.getLikeCount(), msg.getContent()));
                    return null;
                });
            }
            return null;
        });
    }

    public void demoBtnPress(View view) {
        String text = String.valueOf(((EditText) findViewById(R.id.demoMsgText)).getText());
        String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());

        if (text.equals("***debug")) {
            Firebase.getInstance().dump();
            return;
        }

        update(text, to);
    }

    public void demoBtnPress2(View view) {
        String username = String.valueOf(((EditText) findViewById(R.id.demoMsgText3)).getText());

        UserLogin login = UserLogin.getInstance();

        login.authoriseUser(username, "12345678");

        ((Button) findViewById(R.id.demoSendBtn)).setEnabled(true);
        ((Button) findViewById(R.id.demoSendBtn2)).setEnabled(false);
        ((EditText) findViewById(R.id.demoMsgText)).setEnabled(true);
        ((EditText) findViewById(R.id.demoMsgText2)).setEnabled(false);
        ((EditText) findViewById(R.id.demoMsgText3)).setEnabled(false);

        String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());
        update(null, to);

        final int delay = 500; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());
                update(null, to);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void demoBtnPress3(View view) {
        Firebase.getInstance().eraseAllData("yes, I actually want to delete all data from firebase");
        adapter.clear();

        UserLogin login = UserLogin.getInstance();
        login.addUser("alex", "12345678");
        login.addUser("geun", "12345678");
        login.addUser("nikhila", "12345678");
        login.addUser("harrison", "12345678");
        login.addUser("jayden", "12345678");
    }
}