package com.example.myeducationalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.databinding.ActivityLoginBinding;
import com.example.myeducationalapp.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    // Instance of the UserLogin class for handling authentication
    UserLogin authenticator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authenticator = UserLogin.getInstance();

        // Click handler for login button
        binding.submitButton.setOnClickListener(view -> {
            onLoginButtonPress();
        });


        // Adding QOL for if you press enter in the password field
        binding.passwordInputText.setOnKeyListener((view, keyCode, keyEvent) -> {

            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                onLoginButtonPress();
                return true;
            }

            return false;
        });

        //Firebase.getInstance().dump();
        //UserLogin.getInstance().addUser("comp2100@anu.au", "comp2100");
    }

    private void onLoginButtonPress() {

        // Passing values of the username and password inputs into the authenticator instance
        boolean isAuthenticated = authenticator.authoriseUser(
                String.valueOf(binding.usernameInputText.getText())
                , String.valueOf(binding.passwordInputText.getText()));

        if (isAuthenticated) {
            // User is authenticated
            Toast toast = Toast.makeText(getApplicationContext(), "Looking good, we're logging you in", Toast.LENGTH_SHORT);
            toast.show();

            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivityIntent);
        } else {
            // User is not authenticated display error message
            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect username or password, please try again", Toast.LENGTH_LONG);
            toast.show();
        }
    }


}