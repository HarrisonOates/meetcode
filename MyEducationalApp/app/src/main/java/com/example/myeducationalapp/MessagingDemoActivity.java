package com.example.myeducationalapp;

import androidx.appcompat.app.AppCompatActivity;

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

    }

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
                    adapter.add(msg.getPoster().getUsername() + ": " + msg.getContent());
                    return null;
                });
            }
            return null;
        });
    }

    public void demoBtnPress(View view) {
        String text = String.valueOf(((EditText) findViewById(R.id.demoMsgText)).getText());
        String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());

        update(text, to);
    }

    public void demoBtnPress2(View view) {
        String username = String.valueOf(((EditText) findViewById(R.id.demoMsgText3)).getText());

        UserLogin login = UserLogin.getInstance();
        login.addUser("alex", "12345678");
        login.addUser("geun", "12345678");
        login.addUser("nikhila", "12345678");
        login.addUser("harrison", "12345678");
        login.addUser("jayden", "12345678");

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
    }
}