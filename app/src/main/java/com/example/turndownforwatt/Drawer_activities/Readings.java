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

public class Readings extends AppCompatActivity {

    DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        setContentView(R.layout.activity_readings);


        drawerlayout = findViewById(R.id.drawerLayout);


        // Initialize Toolbar
        Toolbar tb = findViewById(R.id.toolbar_readings);
        setSupportActionBar(tb);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.Readings));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent intent;
                        if (menuItem.getItemId() == R.id.Overview_item) {
                            intent = new Intent(Readings.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_item) {
                            intent = new Intent(Readings.this, Meter.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_add_item) {
                            intent = new Intent(Readings.this, Meter_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_item) {
                            intent = new Intent(Readings.this, Readings.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_add_item) {
                            intent = new Intent(Readings.this, Readings_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Statistics_item) {
                            intent = new Intent(Readings.this, Statistics.class);
                            startActivity(intent);
                        }
                        drawerlayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );




        // Change Meter Button
        TextView t1 = findViewById(R.id.tdatum);
        t1.setText(getString(R.string.Active_Meter_a));
        final Button zw = findViewById(R.id.zweck);
        final Button btn1 = findViewById(R.id.brefresh);
        zw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
                String liste = sharedPreferences.getString("LIST", "");
                String active = sharedPreferences.getString("AKTIV","");

                String[] zahler=liste.split("\t");

                String[] daten= new String[zahler.length];
                String[] result = new String[zahler.length];
                String start= "";
                for(int l=0; l<zahler.length;l++){
                    result[l] = zahler[l].substring(1, zahler[l].length());
                }

                for(int i=0; i<zahler.length;i++){
                    if(result[i].equals(active)){
                        Toast.makeText(getApplicationContext(),getString(R.string.Active_Meter_changed),Toast.LENGTH_LONG).show();
                        if(i==zahler.length-1) {
                            SavePreferences("AKTIV",result[0]);
                        }
                        else{
                            SavePreferences("AKTIV",result[i+1]);
                        }
                    }
                }

            }});


        // Refresh Button
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
                String colors = sharedPreferences.getString("LIST","");
                String[] liste = colors.split("\t");
                final String[] as=liste;
                String active = sharedPreferences.getString("AKTIV", "");

                if(active.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.No_active_meter),Toast.LENGTH_LONG).show();
                    return;
                }
                TextView t1 = findViewById(R.id.tdatum);
                t1.setText(getString(R.string.Active_Meter_a)+":"+" " + "\n" +active);
                String wert=sharedPreferences.getString(active,"");
                Double standi=0.0;
                if(wert.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.Empty_Readings_in)+active,Toast.LENGTH_LONG).show();
                    return;
                }
                String[] wert_array = wert.split("\t");

                int laenge = wert_array.length;
                Double mittelwert = 0.0;
                Double[] wert_zahlen = new Double[laenge];

                for(int i = 2; i<laenge; i++) {
                    wert_zahlen[i] = Double.valueOf(wert_array[i]);
                    mittelwert = mittelwert + wert_zahlen[i];
                }

                if(laenge>2){
                    mittelwert =mittelwert/(laenge-2);
                    standi=mittelwert*(laenge-2)+Double.valueOf(wert_array[1]);
                    TextView t2 = findViewById(R.id.tzaehler);
                    Toast.makeText(getApplicationContext(),mittelwert.toString(),Toast.LENGTH_LONG).show();
                t2.setText(getString(R.string.Readings_c)+" "+"\n"+standi.toString() + " kWh");
                SharedPreferences.Editor editor =sharedPreferences.edit();}
                else{
                    TextView t2 = findViewById(R.id.tzaehler);
                    Toast.makeText(getApplicationContext(),getString(R.string.Readings_please_add),Toast.LENGTH_LONG).show();
                    t2.setText(getString(R.string.No_Readings_yet_c));
                }
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
    private void LoadPreferences2(){
        TextView t1 = findViewById(R.id.tzaehler);
        TextView t2 = findViewById(R.id.tdatum);

        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);

        String dzaehler =sharedPreferences.getString("AKTIV","");

        t1.setText(dzaehler);
        t2.setText(dzaehler);

    }

    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        String a = sharedPreferences.getString(key,"");

    }
}
