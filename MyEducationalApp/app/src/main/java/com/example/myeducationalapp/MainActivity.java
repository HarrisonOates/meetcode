package com.example.myeducationalapp;

import android.os.Bundle;

import com.example.myeducationalapp.DatastreamSimulation.DataGenerator;
import com.example.myeducationalapp.Localization.DynamicLocalization;
import com.example.myeducationalapp.User.UserLocalData;
import com.example.myeducationalapp.User.UserLogin;
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

import java.io.FileNotFoundException;

/**
 * @author u7300256 Nikhila Gurusinghe
 */

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
        toolbarStarContainerText.setText(String.valueOf(UserLocalData.getInstance().getPoints()));

        // Anonymous on-click handlers
        // Home icon on nav menu
        navMenuHomeIcon.setOnClickListener(view -> {
            toolbarStarContainerText.setText(String.valueOf(UserLocalData.getInstance().getPoints()));
            DynamicLocalization.navigateTranslatedToolBar(userInterfaceManager, navController, "Home", R.id.HomeFragment);

        });

        // Mail icon on nav menu
        navMenuMailIcon.setOnClickListener(view -> {

            DynamicLocalization.navigateTranslatedToolBar(userInterfaceManager, navController, "Messages", R.id.messagesFragment);

        });

        // Hamburger menu on nav menu
        navMenuHamburgerIcon.setOnClickListener(view -> {

            DynamicLocalization.navigateTranslatedToolBar(userInterfaceManager, navController, "Menu", R.id.accountFragment);

        });

        // Navigating backwards
        toolbarBackwardsArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarStarContainerText.setText(String.valueOf(UserLocalData.getInstance().getPoints()));
                onSupportNavigateUp();
            }
        });

        toolbarProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Creates the data feed for the app
        if (UserLogin.getInstance().getCurrentUsername().equals("harrison")){
            try {
                DataGenerator.generateData(getApplicationContext());
            } catch (FileNotFoundException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        toolbarStarContainerText.setText(String.valueOf(UserLocalData.getInstance().getPoints()));

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
        TextView toolbarStarContainerText = (TextView) findViewById(R.id.toolbar_star_container_number);
        toolbarStarContainerText.setText(String.valueOf(UserLocalData.getInstance().getPoints()));

    }
}