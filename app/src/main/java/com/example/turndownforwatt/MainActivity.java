package com.example.turndownforwatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turndownforwatt.Drawer_activities.Meter_add;
import com.example.turndownforwatt.Drawer_activities.Readings;
import com.example.turndownforwatt.Drawer_activities.Readings_add;
import com.example.turndownforwatt.Drawer_activities.Statistics;
import com.example.turndownforwatt.Drawer_activities.Meter;
import com.example.turndownforwatt.R;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.Locale;

import static java.lang.Double.isNaN;
import static java.lang.Double.valueOf;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        // Load the chosen Language
        loadLocale();


        setContentView(R.layout.activity_main);


        drawerlayout = findViewById(R.id.drawerLayout);


        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.Overview));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent intent;
                        if (menuItem.getItemId() == R.id.Overview_item) {
                            intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_item) {
                            intent = new Intent(MainActivity.this, Meter.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_add_item) {
                            intent = new Intent(MainActivity.this, Meter_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_item) {
                            intent = new Intent(MainActivity.this, Readings.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_add_item) {
                            intent = new Intent(MainActivity.this, Readings_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Statistics_item) {
                            intent = new Intent(MainActivity.this, Statistics.class);
                            startActivity(intent);
                        }
                        drawerlayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );


        // Update Button
        Button ws = findViewById(R.id.ref);
        ws.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                master();
            }
        });



        // Shared Preferences
        int hallo = 0;
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("P", MODE_PRIVATE);
            String listen = sharedPreferences.getString("LIST", "");
            String[] zahler = listen.split("\t");
            int laenge = zahler.length;
            String[] result = new String[laenge];
            String[] daten = new String[laenge];
            for (int l = 0; l < zahler.length; l++) {
                result[l] = zahler[l].substring(1, zahler[l].length());
            }
            Integer[] molsen = new Integer[8];
            Integer[] ulsen = new Integer[3 * laenge];
            int index = 0;
            Double mittelwert = 0.0;
            Double ende = 0.0;
            Double ende2 = 0.0;
            Double ende3 = 0.0;
            Double ende4 = 0.0;
            String mansion = "";
            String mansion2 = "";
            for (int i = 0; i < laenge; i++) {
                daten[i] = sharedPreferences.getString("d" + result[i], "");
                if (daten[i].isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Empty), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    String[] otto = daten[i].split("\t");
                    if (otto[1].isEmpty()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.Empty), Toast.LENGTH_LONG).show();
                        return;
                    }
                    String[] otto2 = new String[otto.length];
                    int laenge2 = otto[2].length();

                    if (laenge2 < 2) {
                        Toast.makeText(getApplicationContext(), getString(R.string.few_data) + otto[1], Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        for (int k = 1; k < laenge2; k++) { // k=2 klappt



                            String[] olsen = otto[k].split("\n");
                            Integer[] dolsen = new Integer[olsen.length];

                            for (int ol = 0; ol < 3; ol++) {
                                dolsen[ol] = Integer.valueOf(olsen[ol]);
                                molsen[ol] = dolsen[ol];
                                ulsen[index] = molsen[ol];
                                index += 1;
                            }
                        }
                        if (index > 2) {
                            ws.setText(String.valueOf(ulsen[2]));
                        }

                        if (index > 6) {
                            try {

                                ws.setText(String.valueOf(ulsen[0]) + String.valueOf(ulsen[1]) + String.valueOf(ulsen[2]));
                                hallo += days_difference(ulsen[0], ulsen[1], ulsen[2], ulsen[3], ulsen[4], ulsen[5]);

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Index > 6", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {

        }



        // Change Language Button
        Button changeLang = findViewById(R.id.changeMyLang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showChangeLanguageDialog();

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




    // master() for the Update Button
    public void master() {

        final TextView monat = findViewById(R.id.agesamt);
        final TextView tag = findViewById(R.id.bgesamt);
        final TextView jahr = findViewById(R.id.cgesamt);
        tag.setText(getString(R.string.Consumption_per_day_few_data_a)+"\n"+getString(R.string.few_data_a));
        monat.setText(getString(R.string.Consumption_per_month_few_data_a)+"\n"+getString(R.string.few_data_a));
        jahr.setText(getString(R.string.CO2_few_data_a)+"\n"+getString(R.string.few_data_a));
        String maximal ="";
        SharedPreferences sharedPreferences = getSharedPreferences("P", MODE_PRIVATE);
        String liste = sharedPreferences.getString("LIST", "");
        if (!liste.isEmpty()) {
            String[] zahler = liste.split("\t");
            String[] daten = new String[zahler.length];
            String[] result = new String[zahler.length];
            int laenge = zahler.length;
            String start = "";

            for (int l = 0; l < zahler.length; l++) {
                result[l] = zahler[l].substring(1, zahler[l].length());
            }

            Double mittelwert = 0.0;

            Double ende = 0.0;
            Double ende2 = 0.0;
            Double ende3 = 0.0;
            Double ende4 = 0.0;
            String mansion = "";
            String mansion2 = "";
            for (int i = 0; i < laenge; i++) {
                daten[i] = sharedPreferences.getString(result[i], "");
                maximal = sharedPreferences.getString("mv"+result[i],"");
                if(maximal.isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_loading_max_cons), Toast.LENGTH_LONG).show();
                    return;
                }
                String iso = "";
                if (daten[i].isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.Empty), Toast.LENGTH_LONG).show();
                    tag.setText(getString(R.string.Consumption_per_day_few_data_a));
                    monat.setText(getString(R.string.Consumption_per_month_few_data_a));
                    jahr.setText(getString(R.string.CO2_few_data_a));
                    return;
                } else {
                    String[] otto = daten[i].split("\t");
                    String[] otto2 = new String[otto.length];
                    int laenge2 = otto.length;
                    Double[] wert_zahlen = new Double[laenge2];
                    if (laenge2 > 2) {

                        Double mittelwert2 = 0.0;
                        for (int k = 0; k < laenge2; k++) {



                            if (k == 0) {

                                otto2[k] = otto[k].substring(0, otto[k].length() - 1);

                            } else {
                                otto2[k] = otto[k];

                            }

                            wert_zahlen[k] = Double.valueOf(otto2[k]);
                            if (isNaN(wert_zahlen[k])) {
                                tag.setText(getString(R.string.Consumption_per_day_few_data_a));
                                monat.setText(getString(R.string.Consumption_per_month_few_data_a));
                                jahr.setText(getString(R.string.CO2_few_data_a));
                                return;
                            } else {
                                if (k == 1) {
                                    ende3 = wert_zahlen[1];

                                } else if (k == 0) {
                                } else if (k > 1) {

                                    mittelwert += wert_zahlen[k];
                                    mittelwert2 += wert_zahlen[k]/ultra(3 * (k - 2), 3 * (k - 1));
                                    ende4 = ende4 + wert_zahlen[0] * wert_zahlen[k];

                                    ende = (30 * wert_zahlen[k] / ultra(3 * (k - 2), 3 * (k - 1)));
                                    Toast.makeText(getApplicationContext(), getString(R.string.Last_Input_before) +" " + String.valueOf(ultra(3 * (k - 2), 3 * (k - 1))) + " "+getString(R.string.Last_Input_days), Toast.LENGTH_LONG).show();
                                    if (-1 == ultra(3 * (k - 2), 3 * (k - 1))) {
                                    }
                                    ende2 = ende / 30;
                                }
                            }

                        }
                        if (1 >= 1) {
                            mittelwert2=30*mittelwert2/(laenge2-2);
                            mansion = mansion + zahler[i] + "\n" + mittelwert2  + "\n" + getString(R.string.Max_cons_Abb)+" "+maximal+"\n";
                            mittelwert2/=30;
                            mansion2 = mansion2 + zahler[i] + "\n" + mittelwert2 +"\n\n\n";

                        }


                    }

                }
                ende3 = mittelwert;


            }

            Double co2_em = mittelwert * 0.475;
            monat.setText(getString(R.string.Total_Cons_a) +"\n"+ ende3 + " kWh\n" + getString(R.string.Total_Costs_a)+"\n" + ende4 + " €\n" + "\n" + getString(R.string.Cons_per_month_a)+"\n" + mansion);
            tag.setText("\n\n\n\n\n"+getString(R.string.Cons_per_day_a)+"\n" + mansion2);
            jahr.setText(getString(R.string.CO2_a) + "\n" + co2_em + " kg");


        } else {
            return;
        }
    }


    // Language Button Methods
    private void showChangeLanguageDialog() {
        // array of language to display in alert dialog
        final String[] listItems = {"German", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose your preferred language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("ger");
                    recreate();
                } else if (i == 1) {
                    setLocale("en");
                    recreate();
                }

                // dismiss alert dialog when language is selected
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        // show alert dialog
        mDialog.show();

    }




    public Integer ultra(int zaehlera, int zaehlerb) {
        final Button ws = findViewById(R.id.ref);
        int hallo = 1;

        Integer[] ret = new Integer[0];
        String ottonormal2 ="";

        SharedPreferences sharedPreferences = getSharedPreferences("P", MODE_PRIVATE);
        String listen = sharedPreferences.getString("LIST", "");
        String[] zahler = listen.split("\t");
        int laenge = zahler.length;
        String[] result = new String[laenge];
        String[] daten = new String[laenge];
        for (int l = 0; l < zahler.length; l++) {
            result[l] = zahler[l].substring(1, zahler[l].length());
        }
        Integer[] molsen = new Integer[8];
        Integer[] ulsen = new Integer[3 * laenge];
        int index = 0;
        Double mittelwert = 0.0;
        Double ende = 0.0;
        Double ende2 = 0.0;
        Double ende3 = 0.0;
        Double ende4 = 0.0;
        String mansion = "";
        String mansion2 = "";
        for (int i = 0; i < laenge; i++) {
            daten[i] = sharedPreferences.getString("d" + result[i], "");
            if (daten[i].isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.Empty), Toast.LENGTH_LONG).show();
                return -1;
            } else {
                String[] otto = daten[i].split("\t");
                if (otto[1].isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Otto 0 is leer", Toast.LENGTH_LONG).show();
                    return -1;
                }
                String[] otto2 = new String[otto.length];
                int laenge2 = otto.length;

                if (laenge2 < 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.few_data) + otto[1], Toast.LENGTH_LONG).show();
                    return -1;
                } else {

                    int molsenee = 0;
                    int molsene = 0;//monat 1
                    int molseneee = 0; //tag 1
                    int kolsenee = 0;
                    int kolsene = 0; //monat 2
                    int kolseneee = 0;//tag 2
                    for (int k = 1; k < laenge2; k++) { // k=2 klappt
                        ottonormal2 = otto[k];

                        String[] olsen = ottonormal2.split("\n");
                        Integer[] dolsen = new Integer[olsen.length];
                        if (k % 2 == 0) {
                            molsenee = Integer.valueOf(olsen[2]);
                            molsene = Integer.valueOf(olsen[1]);
                            molseneee = Integer.valueOf(olsen[0]);
                        } else {
                            kolsenee = Integer.valueOf(olsen[2]);
                            kolsene = Integer.valueOf(olsen[1]);
                            kolseneee = Integer.valueOf(olsen[0]);
                        }
                        if (k >= 2) {
                            hallo = days_difference(molseneee, molsene, molsenee, kolseneee, kolsene, kolsenee);

                        }
                    }

                }


            }}

        return hallo;
    }




    // Lanugage Button Shared Preferences
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }


    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }




    // Date Specifications
    private int schaltjahr(int year){
        if(year%4 == 0){
            if(!(year%100 == 0))
                return 1;
            else if(year%400 == 0)
                return 1;
        }
        return 0;
    }



    private int day_of_year(int day, int month, int year){
        int[] monthdays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        monthdays[1] = 28 + schaltjahr(year);
        int days = 0;
        for(int i = 0; i<month-1;i++){
            days = days + monthdays[i];
        }
        days = days + day;
        return days;
    }

    private int days_per_year(int year){
        return (365 + schaltjahr(year));
    }

    public int days_difference(int day_a, int month_a, int year_a, int day_b, int month_b, int year_b){
        int diff = 0;
        int days_a = day_of_year(day_a, month_a, year_a);
        int days_b = day_of_year(day_b, month_b, year_b);
        if((year_a > year_b) || ((year_a == year_b) && (days_a > days_b))){
            int day_c = day_a; int month_c = month_a; int year_c = year_a; int days_c = days_a;
            day_a = day_b; month_a = month_b; year_a = year_b; days_a = days_b;
            day_b = day_c; month_b = month_c; year_b = year_c; days_b = days_c;

        }
        if(year_a == year_b){
            diff = days_b - days_a;
        }else{
            diff = days_per_year(year_a) - days_a;
            int year = year_a + 1;
            while(year < year_b){
                diff = diff + days_per_year(year);
                year += 1;
            }
            diff = diff + days_b;
        }
        return diff;
    }




}
