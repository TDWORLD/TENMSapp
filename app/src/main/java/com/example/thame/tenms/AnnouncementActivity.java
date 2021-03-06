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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.thame.tenms.R.string.password;

public class AnnouncementActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    EditText cDate;
    Button btnComplain;
    EditText complainState;
    EditText txtComplainID;
    Spinner spComplainType;
    EditText txtComplainTitle;
    EditText txtComplain;
    EditText txtComplainDate;
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener DateSetListner;

    //String dataList[] = new String[]{"","","","","","","","","",""};
    ArrayList<String> dataList = new ArrayList<String>();
    String CategoryList[] = new String[]{"-SELECT-","Accountant","Driver","Sales","Marketing","Security","Other"};

    //String AnnounceList[] = new String[]{"","","","","","","","","",""};
    ArrayList<String> AnnounceList = new ArrayList<String>();
            /*{
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
    };*/

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
        con =  db.getConnection();

        setUpDate();
        setUpComplain();
        setUpCatogory();
        setUpAnnounce();
        clickComplain();
        clickClear();
        getID();

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
                String date = year + "-" + month + "-" + dayOfMonth;
                cDate.setText(date);
            }
        };
    }

    private void setUpComplain(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner2 = (Spinner) findViewById(R.id.loanItem);

        try {
            // Change below query according to your own database.

            String query = "SELECT ComplainID,ComplainTitle FROM Complain";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        String cid = rs.getString("ComplainID");
                        String ctitle = rs.getString("ComplainTitle");
                        //dataList[arrayValue] = cid+" - "+ctitle;
                        dataList.add(arrayValue,cid+" - "+ctitle);
                        arrayValue += 1;
                    }
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage("Error while loading complain data");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();

                }

            } else {
                AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                alertDialog2.setTitle("Error");
                alertDialog2.setMessage("Error while loading complain data");
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

        spinner2.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complainState = (EditText)findViewById(R.id.txtComplainState);
                try {
                    String complain = spinner2.getSelectedItem().toString();
                    String complain2[] = complain.split(" - ");
                    // Change below query according to your own database.
                    String query = "SELECT * FROM Complain WHERE ComplainID = '"+complain2[0]+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        try{
                            complainState.setText(rs.getString("ComplainState"));
                        }catch (Exception ex){
                            AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                            alertDialog2.setTitle("Error");
                            alertDialog2.setMessage("Can't load the complain state. Please try again later.");
                            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog2.show();

                        }

                    } else {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                        alertDialog2.setTitle("Error");
                        alertDialog2.setMessage("No values found in the database");
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
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage("Please check your internet connection");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                alertDialog2.setTitle("Error");
                alertDialog2.setMessage("Nothing selected");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog2.show();
            }
        });
    }

    private void setUpCatogory(){
            String query = "SELECT DesignationName FROM Designation";
            Statement stmt = null;
            try {
                ArrayAdapter adapter2 = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,CategoryList);
                spComplainType = (Spinner) findViewById(R.id.complainType);
                spComplainType.setAdapter(adapter2);

                /*stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs!=null){
                    rs.last();
                    int count = rs.getRow();
                    rs.beforeFirst();
                    CategoryList = new String[count];
                    count = 0;
                    while(rs.next()) {
                        String desiName = rs.getString("DesignationName");
                        CategoryList[count] = desiName;
                        count++;
                    }

                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    private void setUpAnnounce() {
        // Connect to database

        if (con == null) {
            AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
            alertDialog2.setTitle("Error");
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
                int arrayValue = 0;

                if (rs!=null) {
                    try{
                        while(rs.next()){
                            String id = rs.getString("AnnouncementID");
                            String title = rs.getString("AnnouncementTitle");
                            String body= rs.getString("AnnouncementBody");
                            //AnnounceList[arrayValue] = id+" - "+title+"\n"+body;
                            AnnounceList.add(arrayValue,id+" - "+title+"\n"+body);
                            arrayValue += 1;
                        }
                    }catch (Exception ex){
                        AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                        alertDialog2.setTitle("Error");
                        alertDialog2.setMessage("Error occured while loading data. Please try again later");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();

                    }

                } else {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage("No values found in the database");
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
                alertDialog2.setTitle("Error");
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
                    if(textView.getText() != null && !textView.getText().equals("")) {
                        Toast.makeText(AnnouncementActivity.this, textView.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void clickComplain() {
        final String[] categoryName = new String[1];
        //btnComplain = (Button)findViewById(R.id.btnComplainSave);
        final Spinner spinner = (Spinner) findViewById(R.id.complainType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnClick = (Button) findViewById(R.id.btnComplainSave);
        txtComplainTitle = (EditText) findViewById(R.id.txtComplainTitle);
        txtComplain = (EditText) findViewById(R.id.txtComplain);
        txtComplainDate = (EditText) findViewById(R.id.txtComplainDate);
        spComplainType = (Spinner)findViewById(R.id.complainType);
        if (myCalendar.get(Calendar.DATE)<10){
            String dateeToday = myCalendar.get(Calendar.YEAR) + "-" + (myCalendar.get(Calendar.MONTH) + 1) + "-0" + myCalendar.get(Calendar.DATE);
            txtComplainDate.setText(dateeToday);
        }
        else if ((myCalendar.get(Calendar.MONTH) + 1)<10){
            String dateeToday = myCalendar.get(Calendar.YEAR) + "-0" + (myCalendar.get(Calendar.MONTH) + 1) + "-" + myCalendar.get(Calendar.DATE);
            txtComplainDate.setText(dateeToday);
        }
        else{
            String dateeToday = myCalendar.get(Calendar.YEAR) + "-" + (myCalendar.get(Calendar.MONTH) + 1) + "-" + myCalendar.get(Calendar.DATE);
            txtComplainDate.setText(dateeToday);
        }
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtComplainTitle.getText().toString())) {
                    txtComplainTitle.setError("Please enter the complain title");
                }
                else if (TextUtils.isEmpty(txtComplain.getText().toString())){
                    txtComplain.setError("Please enter the complain description");
                }
                else if (spComplainType.getSelectedItem().toString().equals("-SELECT-")){
                    ((TextView)spComplainType.getSelectedView()).setError("Please select a category to continue");
                }
                else {
                    try {
                        String Query = "INSERT INTO Complain(ComplainID,ComplainTitle,ComplainBody,ComplainCategory,ComplainDate,ComplainState) VALUES('" + txtComplainID.getText() + "','" + txtComplainTitle.getText() + "','" + txtComplain.getText() + "','" + spComplainType.getSelectedItem() + "','" + cDate.getText() + "','Open')";
                        Statement stmt = null;
                        stmt = con.createStatement();
                        stmt.execute(Query);

                        AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                        alertDialog2.setTitle("Success");
                        alertDialog2.setMessage("Your complain added successfully to the system.");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();
                        getID();
                        Clean();
                    } catch (Exception ex) {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
                        alertDialog2.setTitle("Error");
                        alertDialog2.setMessage("Error occured while inserting data to the database");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();
                    }
                }
            }
        });
    }

    private void clickClear(){
        Button btnClear = (Button) findViewById(R.id.btnComplainClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getID();
                Clean();
            }
        });
    }

    public void getID(){
        String query = "SELECT MAX(ComplainID) AS ComplainID FROM Complain";
        Statement stmt = null;
        txtComplainID = (EditText) findViewById(R.id.txtComplainID);
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs!=null){
                while (rs.next()){
                    int x = rs.getInt("ComplainID");
                    x++;
                    String s = new DecimalFormat("0000").format(x);
                    txtComplainID.setText(s);
                    txtComplainID.setEnabled(false);
                }
            }else{
                String s = "0001";
                txtComplainID.setText(s);
                txtComplainID.setEnabled(false);
            }
        }catch (Exception ex){
            AlertDialog alertDialog2 = new AlertDialog.Builder(AnnouncementActivity.this).create();
            alertDialog2.setTitle("Error");
            alertDialog2.setMessage("Error occured while loading data");
            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog2.show();
        }
    }

    public void Clean(){
        txtComplain.setText("");
        txtComplainTitle.setText("");
        spComplainType.setSelection(0);
        cDate.setText("");
    }
}
