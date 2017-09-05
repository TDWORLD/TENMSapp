package com.example.thame.tenms;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.thame.tenms.R.string.password;

public class AnnouncementActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    EditText cDate;
    DatePickerDialog.OnDateSetListener DateSetListner;

    String dataList[] = new String[]{"COMO001 - dbsfhsdb","COMO002 - hfsdhdsf","COMO003 - fsdhfh"};
    String CategoryList[] = new String[]{"Accountant","Driver","Sales","Marketing","Security"};

    String AnnounceList[] = new String[]{
            "A001 - hello welcome",
            "A002 - Dear Staff: \n We're excited to add Brian to our team. He brings us 10 years of increasingly responsible experience in creating quality software.",
            "A003 - Dear Staff: \n Following his onboarding, we expect that Brian will take the lead technician role with the group. All of the technicians participated in selecting Brian for the role.",
            "A004 - Dear Staff: \n In addition to 10 years of experience, Brian has also worked in three related industries in a variety of quality roles, from development to auditing.",
            "A005 - Dear Staff: \n He's been part of a team that helped his company earn the Malcolm Baldrige Award for quality, too.",
            "A006 - Dear Staff: \n I'd like to introduce James Gonzalez who is starting at Johnson's as a product specialist on May 1. John will work in the marketing department where he will report to Sherri Howell. His job responsibilities will include coordinating all marketing services for the xx product.",
            "A007 - Dear Staff: \n He'll work with the product development team on all aspects of developing the product through launching the product in the international markets.",
            "A008 - Dear Staff: \n This gives us the opportunity to see what potential customers really want and need before we build products.",
            "A009 - Dear Staff: \n John's degree is from LaSalle University where he majored in marketing and minored in business.",
            "A010 - Dear Staff: \n Ann Thompson is joining Mediquick Products to fill our open position in customer service. Her first day is Tuesday, April 8.",
            "A011 - Dear Staff: \n Ann's new employee mentor is Mark Veja, so if you have questions or need to meet with Ann, you can talk with Mark before she starts.",
            "A012 - Dear Staff: \n She has a lot to share with her new coworkers as she loves cats, square dances weekly, and volunteers in a local homeless shelter.",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_annou32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Announcement & Complaints");

        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Announcement");
        spec1.setIndicator("Announcement");
        spec1.setContent(R.id.layoutAnnounsment);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Complaints");
        spec2.setIndicator("Complaints");
        spec2.setContent(R.id.layoutComplaints);
        tab.addTab(spec2);

        db = new DataAccess();

        setUpDate();
        setUpComplain();
        setUpCatogory();
        setUpAnnounce();
    }


    private void setUpDate() {
        //ComplainDate
        cDate = (EditText) findViewById(R.id.txtComplainDate);
        cDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AnnouncementActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListner,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        DateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                cDate.setText(date);
            }
        };
    }

    private void setUpComplain(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner = (Spinner) findViewById(R.id.loanItem);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCatogory(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,CategoryList);
        final Spinner spinner = (Spinner) findViewById(R.id.complainType);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpAnnounce() {
        // Connect to database
        con =  db.getConnection();

        if (con == null) {
            AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
            alertDialog2.setTitle("Connection error");
            alertDialog2.setMessage("Check your internet access");
            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog2.show();
        } else {
            try {
                // Change below query according to your own database.
                String query = "SELECT * FROM Announcement";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    con.close();

                } else {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                    alertDialog2.setTitle("Invalid");
                    alertDialog2.setMessage("Invalid query");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();
                }
            } catch (Exception ex) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                alertDialog2.setTitle("Connection error");
                alertDialog2.setMessage("Please check your internet connection");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog2.show();

            }

            ListView listView = (ListView) findViewById(R.id.AnnounceList);
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listitem, R.id.Item, AnnounceList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view;
                    Toast.makeText(AnnouncementActivity.this, textView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
