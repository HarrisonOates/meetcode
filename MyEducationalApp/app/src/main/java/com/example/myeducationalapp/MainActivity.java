package com.example.myeducationalapp;

import android.os.Bundle;

import com.example.myeducationalapp.UserInterface.UserInterfaceManagerViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myeducationalapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(this).get(UserInterfaceManagerViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Hiding the Action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Dynamic elements
        View toolbarBackwardsArrow = (View) findViewById(R.id.toolbar_left_arrow_icon);
        View navMenuMailNotificationDot = (View) findViewById(R.id.nav_menu_mail_notification_dot);
        // Clickable elements
        View navMenuHomeIcon = (View) findViewById(R.id.nav_menu_home_icon);
        View navMenuMailIcon = (View) findViewById(R.id.nav_menu_mail_icon);
        View navMenuHamburgerIcon = (View) findViewById(R.id.nav_menu_hamburger_icon);
        // Toolbar elements
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        ImageView toolbarProfileIcon = (ImageView) findViewById(R.id.toolbar_profile_icon);
        TextView toolbarStarContainerText = (TextView) findViewById(R.id.toolbar_star_container_number);


        // Setting initial states of dynamic elements
        if (Objects.requireNonNull(userInterfaceManager.getUiState().getValue()).isActionBarInBackState()) {
            toolbarBackwardsArrow.setVisibility(View.VISIBLE);
        } else {
            toolbarBackwardsArrow.setVisibility(View.GONE);
        }

        if (Objects.requireNonNull(userInterfaceManager.getUiState().getValue()).getNavigationMenuNotificationVisibility()) {
            navMenuMailNotificationDot.setVisibility(View.VISIBLE);
        } else {
            navMenuMailNotificationDot.setVisibility(View.GONE);
        }

        // Anonymous on-click handlers
        navMenuHomeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        navMenuMailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        navMenuHamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toolbarBackwardsArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toolbarProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
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