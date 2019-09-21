package com.example.turndownforwatt.Drawer_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

//import androidx.recyclerview.widget.RecyclerView;
//import android.content.Context;
//import android.view.Menu;
//import android.widget.EditText;

//import java.io.File;
//import java.io.FileInputStream;
//import java.util.Scanner;

public class Meter extends AppCompatActivity {


    Button btnEmail;
    Button btnLozinka;
    Button btnPovratak;

    DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        setContentView(R.layout.activity_meter);


        // Initialize
        drawerlayout = findViewById(R.id.drawerLayout);
        btnEmail = findViewById(R.id.b1);
        btnPovratak =  findViewById(R.id.b3);
        final TextView anzeige =findViewById(R.id.anzeige);
        final TextView anzeige2=findViewById(R.id.anzeige2);



        // Initialize the Toolbar
        Toolbar tb = findViewById(R.id.toolbar_meter);
        setSupportActionBar(tb);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.Meter));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent intent;
                        if (menuItem.getItemId() == R.id.Overview_item) {
                            intent = new Intent(Meter.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_item) {
                            intent = new Intent(Meter.this, Meter.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_add_item) {
                            intent = new Intent(Meter.this, Meter_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_item) {
                            intent = new Intent(Meter.this, Readings.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_add_item) {
                            intent = new Intent(Meter.this, Readings_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Statistics_item) {
                            intent = new Intent(Meter.this, Statistics.class);
                            startActivity(intent);
                        }
                        drawerlayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );







        // Shared Preferences
        int k=0;

        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        String liste = sharedPreferences.getString("LIST", "");
         if(!liste.isEmpty()) {
            anzeige.setText(getString(R.string.Meter_Name_b)+"\n"+ liste);
            String[] zahler = liste.split("\t");

            String[] daten = new String[zahler.length];
            String[] result = new String[zahler.length];
            String start = "";
            for (int l = 0; l < zahler.length; l++) {
                result[l] = zahler[l].substring(1, zahler[l].length());
            }

            for (int i = 0; i < zahler.length; i++) {
                daten[i] = sharedPreferences.getString(result[i], "");
                String[] zs = daten[i].split("\t");
                start = start + "\n" + zs[0];
            }
            anzeige2.setText(getString(R.string.Contract_Costs_b)+"\n"+ start);
        }
        else{
            anzeige.setText(getString(R.string.Meter_Name_b)+"\n"+"...");
            anzeige2.setText(getString(R.string.Contract_Costs_b)+"\n"+"....");
            Toast.makeText(getApplicationContext(),getString(R.string.No_Meters_yet),Toast.LENGTH_LONG).show();
        }





        // Update Button
        btnEmail.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
                String liste = sharedPreferences.getString("LIST", "");

                if(liste.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.No_Meters_yet),Toast.LENGTH_LONG).show();
                    anzeige.setText(getString(R.string.Meter_Name_b)+"...");
                    anzeige2.setText(getString(R.string.Starting_Reading_Values)+"....");
                    return;
                }
                else {
                    anzeige.setText(getString(R.string.Meter_Name_b)+"\n" + liste);
                }
                String starte =swert();
                if(!starte.isEmpty()){
                    anzeige2.setText(getString(R.string.Contract_Costs_b)+"\n"+starte);
                }
                else{
                    Toast.makeText(getApplicationContext(),getString(R.string.No_Starting_Reading_Values),Toast.LENGTH_LONG).show();
                    anzeige2.setText(getString(R.string.Starting_Reading_Values)+"...");
                    return;
                }
            }
        });





        // Delete Button
        btnPovratak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                SharedPreferences preferences = getSharedPreferences("P", 0);
                preferences.edit().clear().apply();


            }
        });

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



    // Shared Preferences
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        String stremail = sharedPreferences.getString("DATUM", "");
        String strlozinka = sharedPreferences.getString("STAND", "");
        btnEmail.setText(stremail);
        btnLozinka.setText(strlozinka);

    }



    public String swert(){

        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        String liste = sharedPreferences.getString("LIST", "");
        String[] zahler = liste.split("\t");
        String[] daten= new String[zahler.length];
        String[] result = new String[zahler.length];
        String start= "";
        for(int l=0; l<zahler.length;l++){
            result[l] = zahler[l].substring(1, zahler[l].length());
        }

        for(int i=0; i<zahler.length;i++){
            daten[i]= sharedPreferences.getString(result[i],"");
            String[] zs = daten[i].split("\t");
            start= start+"\n"+zs[0];
        }
        return start;
    }

}