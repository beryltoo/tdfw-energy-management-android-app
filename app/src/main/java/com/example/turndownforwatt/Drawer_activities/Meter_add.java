package com.example.turndownforwatt.Drawer_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Meter_add extends AppCompatActivity {

    DrawerLayout drawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Status Bar Color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        setContentView(R.layout.activity_meter_add);


        drawerlayout = findViewById(R.id.drawerLayout);


        // Initialize Toolbar
        Toolbar tb = findViewById(R.id.toolbar_meter_add);
        setSupportActionBar(tb);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getString(R.string.Meter_add));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Intent intent;
                        if (menuItem.getItemId() == R.id.Overview_item) {
                            intent = new Intent(Meter_add.this, MainActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_item) {
                            intent = new Intent(Meter_add.this, Meter.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Meter_add_item) {
                            intent = new Intent(Meter_add.this, Meter_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_item) {
                            intent = new Intent(Meter_add.this, Readings.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Readings_add_item) {
                            intent = new Intent(Meter_add.this, Readings_add.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.Statistics_item) {
                            intent = new Intent(Meter_add.this, Statistics.class);
                            startActivity(intent);
                        }
                        drawerlayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );




        // Add Meter Button
        final Button btnadd = findViewById(R.id.add_meter_button);
        btnadd.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText ename = findViewById(R.id.add_meter_name);
                EditText ecost = findViewById(R.id.add_meter_costs);
                EditText start = findViewById(R.id.z0);
                EditText maxim = findViewById(R.id.tmax);

                    Date date = Calendar.getInstance().getTime();
                    DateFormat df;
                    df = DateFormat.getDateInstance(DateFormat.MEDIUM);
                    String strDate = df.format(date);
                    String end1 = "";
                    end1 += strDate.substring(0,2);//0,2
                    if((end1.charAt(0)-'0')==0){
                        end1=Character.toString(end1.charAt(1));

                }
                    String end2 ="";
                    end2 += strDate.substring(3,5);//3,5
                    if((end2.charAt(0)-'0')==0){
                        end2=Character.toString(end2.charAt(1));
                    }
                    String end3 ="";
                    end3 += strDate.substring(6,10);

                    btnadd.setText(strDate);

                String a= ename.getText().toString();
                String b= ecost.getText().toString();
                Double b2 = Double.valueOf(b);
                String sta=start.getText().toString();
                String mv=maxim.getText().toString();
                Double sta2=Double.valueOf(sta);
                if(a.isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Name), Toast.LENGTH_LONG).show();
                    return;
                }
                if(b.isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Number), Toast.LENGTH_LONG).show();
                    return;
                }
                if(sta.isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Starting_Value), Toast.LENGTH_LONG).show();
                    return;
                }
                if(mv.isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Maximum_Consumption), Toast.LENGTH_LONG).show();
                    return;
                }
                Double number = Double.valueOf(b);
                if(number <= 0.0){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_Input), Toast.LENGTH_LONG).show();
                    return;
                }
                String c=a.substring(0,a.length());
                SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
                String stremail = sharedPreferences.getString("LIST", "");
                String d=stremail+"\n"+c;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LIST", d+"\t");
                editor.putString("AKTIV",c);
                editor.putString("mv"+c,mv);

                editor.putString("d"+c,"\t"+end1+"\n"+end2+"\n"+end3);
                editor.putString(c,b+"€\t"+sta);
                Toast.makeText(getApplicationContext(),end1+end2+end3,Toast.LENGTH_LONG).show();
                editor.commit();
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
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("P",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
