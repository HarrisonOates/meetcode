package com.example.myeducationalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.myeducationalapp.Firebase.Firebase;
import com.example.myeducationalapp.User.UserLogin;
import com.example.myeducationalapp.databinding.ActivityLoginBinding;

/**
 * @author u7300256 Nikhila Gurusinghe
 */

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    // Instance of the UserLogin class for handling authentication
    UserLogin authenticator;

    private boolean isError = false;


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

        Firebase.getInstance().dump();
        //UserLogin.getInstance().addUser("comp2100@anu.au", "comp2100");
    }

    private void onLoginButtonPress() {

        boolean isAuthenticated = false;

        // Checking for blank inputs
        if (!binding.usernameInputText.getText().toString().isBlank()
                && !binding.usernameInputText.getText().toString().isEmpty()
                && !binding.passwordInputText.getText().toString().isBlank()
                && !binding.passwordInputText.getText().toString().isEmpty()) {
            // Passing values of the username and password inputs into the authenticator instance
            isAuthenticated = authenticator.authoriseUser(
                    String.valueOf(binding.usernameInputText.getText())
                    , String.valueOf(binding.passwordInputText.getText()));
        }

        if (isAuthenticated) {
            // User is authenticated
            Toast toast = Toast.makeText(getApplicationContext(), "Looking good, we're logging you in", Toast.LENGTH_SHORT);
            toast.show();

            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivityIntent);

            // Error state: thickened borders around text entry box
            toggleUIErrorState(View.GONE, 0,  Color.parseColor("#FFFF102D"));
        } else {
            // User is not authenticated display error message and error UI
            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect username or password, please try again", Toast.LENGTH_LONG);
            toast.show();

            // Error state: thickened borders around text entry box
            toggleUIErrorState(View.VISIBLE, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())),
                    Color.parseColor("#FFFF102D"));
        }
    }

    private void toggleUIErrorState(int visibility, int strokeThickness, int color) {
        // Error state: thickened borders around text entry box
        binding.usernameIncorrectIcon.setVisibility(visibility);
        binding.passwordIncorrectIcon.setVisibility(visibility);

        GradientDrawable incorrectUsernameTextBox = (GradientDrawable) binding.usernameTextContainer.getBackground();
        incorrectUsernameTextBox.setStroke(strokeThickness, color);

        GradientDrawable incorrectPasswordTextBox = (GradientDrawable) binding.passwordTextContainer.getBackground();
        incorrectPasswordTextBox.setStroke(strokeThickness, color);

    }


}