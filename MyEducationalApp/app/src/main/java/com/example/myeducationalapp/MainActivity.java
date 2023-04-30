package com.example.myeducationalapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;

import java.nio.file.AccessDeniedException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String language = preferences.getString("language", "en");

        Log.d("LanguagePreference", "Selected language: " + language);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        binding.fab.setOnClickListener(view -> {
            new Thread(() -> {
                try {
                    String res = (String) Firebase.getInstance().readDirectMessages("alex", "geun").await();
                    Log.w("dbg", res);

                } catch (AccessDeniedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            Intent intent = new Intent(this, MessagingDemoActivity.class);
            startActivity(intent);
        });

        Button langSetting =findViewById(R.id.language_setting);
        langSetting.setOnClickListener(view -> {
            Intent intent = new Intent(this, LanguageSetting.class);
            startActivity(intent);
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