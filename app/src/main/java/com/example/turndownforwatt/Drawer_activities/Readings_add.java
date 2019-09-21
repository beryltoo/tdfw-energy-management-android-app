package com.example.turndownforwatt.Drawer_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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

public class Readings_add extends AppCompatActivity {

    DrawerLayout drawerlayout;

    private CalendarView calendar;
    public int[] date = new int[3];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_readings_add);

        // Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        drawerlayout = findViewById(R.id.drawerLayout);


        // Initialize Toolbar
        Toolbar tb = findViewById(R.id.toolbar_readings_add);
        setSupportActionBar(tb);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.Readings_add));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent intent;
                        if (menuItem.getItemId() == R.id.Overview_item) {
                            intent = new Intent(Readings_add.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_item) {
                            intent = new Intent(Readings_add.this, Meter.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_add_item) {
                            intent = new Intent(Readings_add.this, Meter_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_item) {
                            intent = new Intent(Readings_add.this, Readings.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_add_item) {
                            intent = new Intent(Readings_add.this, Readings_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Statistics_item) {
                            intent = new Intent(Readings_add.this, Statistics.class);
                            startActivity(intent);
                        }
                        drawerlayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );



        // Initialize Calendar
        calendar = findViewById(R.id.add_read_calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month = month + 1;
                date[0] = dayOfMonth;
                date[1] = month;
                date[2] = year;


            }
        });


        // Add Readings Button
        final Button addread = findViewById(R.id.add_read_button);
        final TextView acti = findViewById(R.id.actira);
        acti.setText(getString(R.string.Active_Meter_a)+":"+" "+LoadPreferences("AKTIV"));
        addread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
                String active = sharedPreferences.getString("AKTIV", "");
                if(active.isEmpty()){Toast.makeText(getApplicationContext(), getString(R.string.No_Meter_found_c), Toast.LENGTH_LONG).show();return;}




                final Button read_button = findViewById(R.id.add_read_button);
                final EditText ezahler = findViewById(R.id.add_read_readings);
                String ab = ezahler.getText().toString();
                if(ab.isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Number), Toast.LENGTH_LONG).show();
                    return;
                }
                Double number = Double.valueOf(ab);
                if(number <= 0.0){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Input), Toast.LENGTH_LONG).show();
                    return;
                }else{
                    String day = String.valueOf(date[0]);

                    String month = String.valueOf(date[1]);

                    String year = String.valueOf(date[2]);


                    read_button.setText(day+month+year);
                    if(day.length()!=2||month.length()!=2||year.length()!=4){
                        Toast.makeText(getApplicationContext(),getString(R.string.Date_Error)+" ",Toast.LENGTH_LONG).show();
                    }

                    String stand=LoadPreferences(active);
                    String[] stands=stand.split("\t");
                    String datum =LoadPreferences("d"+active);
                    String[] datums= datum.split("\t");

                    String g="";
                    if(stands.length>=2){
                        Double number2 = Double.valueOf(ab);
                        String[] wert_array = stand.split("\t");
                        Double number3 =Double.valueOf(wert_array[stands.length-1]);
                        Double number4 = Double.valueOf(wert_array[1]);
                        if(stands.length==2){
                            number2=number2-number4;
                        }
                        else {
                            number2 = number2 - number4 - number3;
                        }
                        String numberAsString = String.valueOf(number2);
                        g = stand + "\t"+numberAsString;
                    }else{
                        Toast.makeText(getApplicationContext(),getString(R.string.Error_in_Meter),Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(getApplicationContext(),"saved: "+g,Toast.LENGTH_LONG).show();
                    SavePreferences(active,g);
                    String f= "d"+active;
                    if(datum.isEmpty()){Toast.makeText(getApplicationContext(),getString(R.string.No_Starting_Value_b),Toast.LENGTH_LONG).show();}
                    String hak = datum +"\t"+day+"\n"+month+"\n"+year;
                    SavePreferences(f,hak);


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
    private String LoadPreferences(String key){


        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        String a=sharedPreferences.getString(key,"");
        return a;
    }
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}


