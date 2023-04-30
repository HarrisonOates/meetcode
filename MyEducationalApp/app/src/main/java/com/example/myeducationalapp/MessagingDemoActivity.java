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

import java.util.ArrayList;
import java.util.List;

public class MessagingDemoActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_demo);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        Firebase.getInstance().dump();
        UserLogin.getInstance().loadUsers();

        ListView list = (ListView) findViewById(R.id.myListView);
        list.setAdapter(adapter);

        ((Button) findViewById(R.id.demoSendBtn)).setEnabled(false);
        ((EditText) findViewById(R.id.demoMsgText)).setEnabled(false);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());
                MessageThread dms = new DirectMessageThread(to);
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
    void updateList(MessageThread dms, List<String> soFar) {
        if (soFar.size() == dms.getMessages().size()) {
            adapter.clear();
            adapter.addAll(soFar);
        } else {
            Message msg = dms.getMessages().get(soFar.size());
            msg.getPoster().runWhenReady((ignored) -> {
                soFar.add(String.format("%s: %d likes: %s", msg.getPoster().getUsername(), msg.getLikeCount(), msg.getContent()));
                updateList(dms, soFar);
                return null;
            });
        }
    }

    public void update(String text, String to) {
        MessageThread dms = new DirectMessageThread(to);
        dms.runWhenReady((obj) -> {
            if (text != null) {
                dms.postMessage(text);
            }
            updateList(dms, new ArrayList<>());
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

        boolean authWorked = login.authoriseUser(username, "12345678");
        if (!authWorked) {
            throw new AssertionError("yikes");
        }

        ((Button) findViewById(R.id.demoSendBtn)).setEnabled(true);
        ((Button) findViewById(R.id.demoSendBtn2)).setEnabled(false);
        ((EditText) findViewById(R.id.demoMsgText)).setEnabled(true);
        ((EditText) findViewById(R.id.demoMsgText2)).setEnabled(false);
        ((EditText) findViewById(R.id.demoMsgText3)).setEnabled(false);

        String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());

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
        String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());
        String from = String.valueOf(((EditText) findViewById(R.id.demoMsgText3)).getText());

        Firebase.getInstance().debugDeleteAllDirectMessages(to, from);
    }
}