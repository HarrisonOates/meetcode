package com.example.myeducationalapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.myeducationalapp.Firebase.Firebase;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myeducationalapp.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    int demoStage = 0;

    private void demo() {
        UserLogin login = UserLogin.getInstance();

        if (demoStage == 0) {
            login.addUser("alex", "12345678");
            login.addUser("geun", "12345678");
            login.addUser("nikhila", "12345678");
            login.addUser("harrison", "12345678");
            login.addUser("jayden", "12345678");

            login.authoriseUser("alex", "12345678");

        } else if (demoStage == 1) {
            String loggedInUser = login.getCurrentUsername();

            DirectMessageThread dms = new DirectMessageThread("geun");
            dms.runWhenReady((obj) -> {
                Log.w("dbg", "THE DM THREAD IS READY");
                dms.postMessage("ABC");
                //dms.postMessage("MSG ABC 2");
                return null;
            });

            Firebase.getInstance().dump();

        } else if (demoStage == 2) {
            String loggedInUser = login.getCurrentUsername();

            DirectMessageThread dms = new DirectMessageThread("geun");
            dms.runWhenReady((obj) -> {
                Log.w("dbg", "THE DM THREAD IS READY");
                dms.postMessage("DEF");
                //dms.postMessage("MSG ABC 2");
                return null;
            });

            Firebase.getInstance().dump();
        }

        ++demoStage;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, MessagingDemoActivity.class);
            startActivity(intent);

            /*Firebase fb = Firebase.getInstance();

            FirebaseRequest.write(fb.dbgGetRoot(), Arrays.asList("test", "with", "subdirectories"), "hello world").then((h) -> {
                Log.w("fb", "write completed");
                return null;
            });

            FirebaseResult res = FirebaseRequest.read(fb.dbgGetRoot(), Arrays.asList("test", "with", "subdirectories"));
            res.then((obj) -> {
                Log.w("fb", "the data is " + obj.toString());
                return obj.toString().toUpperCase();
            }).then((obj) -> {
                Log.w("fb", "the data is " + obj.toString());
                return obj + " haha";
            });
            */


            //fb.writeLoginDetails(UserLogin.getInstance().toString());

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}