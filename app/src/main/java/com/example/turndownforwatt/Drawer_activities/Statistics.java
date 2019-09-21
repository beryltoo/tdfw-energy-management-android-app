package com.example.turndownforwatt.Drawer_activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.turndownforwatt.MainActivity;
import com.example.turndownforwatt.R;
import com.google.android.material.navigation.NavigationView;

public class Statistics extends AppCompatActivity {

    DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        setContentView(R.layout.activity_statistics);


        drawerlayout = findViewById(R.id.drawerLayout);



        // Initialilze Toolbar
        Toolbar tb = findViewById(R.id.toolbar_statistics);
        setSupportActionBar(tb);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.Statistics));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent intent;
                        if (menuItem.getItemId() == R.id.Overview_item) {
                            intent = new Intent(Statistics.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_item) {
                            intent = new Intent(Statistics.this, Meter.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_add_item) {
                            intent = new Intent(Statistics.this, Meter_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_item) {
                            intent = new Intent(Statistics.this, Readings.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_add_item) {
                            intent = new Intent(Statistics.this, Readings_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Statistics_item) {
                            intent = new Intent(Statistics.this, Statistics.class);
                            startActivity(intent);
                        }
                        drawerlayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );
    }



    // Navigation Drawer Gravity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerlayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
