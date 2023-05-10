package com.example.myeducationalapp;

import android.os.Bundle;

import com.example.myeducationalapp.userInterface.UserInterfaceManagerViewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myeducationalapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInterfaceManagerViewModel userInterfaceManager = new ViewModelProvider(this).get(UserInterfaceManagerViewModel.class);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(userInterfaceManager);
        binding.setLifecycleOwner(this);


        //setContentView(binding.getRoot());

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

    public void updateMenuUI() {

    }
}