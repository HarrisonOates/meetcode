package com.example.myeducationalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myeducationalapp.Firebase.Firebase;

public class MessagingDemoActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_demo);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        ListView list = (ListView) findViewById(R.id.myListView);
        list.setAdapter(adapter);
    }

    public void demoBtnPress(View view) {
        String text = String.valueOf(((EditText) findViewById(R.id.demoMsgText)).getText());
        String to = String.valueOf(((EditText) findViewById(R.id.demoMsgText2)).getText());


        DirectMessageThread dms = new DirectMessageThread(to);
        dms.runWhenReady((obj) -> {
            dms.postMessage(text);
            adapter.clear();
            for (Message msg: dms.getMessages()) {
                adapter.add(msg.getPoster().getUsername() + ": " + msg.getContent());
            }
            return null;
        });
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
    }

    public void demoBtnPress3(View view) {
        Firebase.getInstance().eraseAllData("yes, I actually want to delete all data from firebase");
        adapter.clear();
    }
}