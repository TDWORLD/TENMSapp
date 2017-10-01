package com.example.thame.tenms;

import android.content.DialogInterface;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    String chartHeader[] = {"Present","Absent","HalfDay","ShortLeave"};
    int charValue[] = {0,0,0,0};
    String TotalDays = "0";
    int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(242, 103, 38)};
    String AttPrecentage = "Attendance Percentage : "+"86%";
    String AbsPrecentage = "Absent Percentage : "+"13%";
    String EmpID;

    //New Edited
    Spinner spYear;
    Spinner spMonth;

    ArrayList<String> YearList = new ArrayList<String>();//New Edited
    String MonthList[] = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_att32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Attendance");

        db = new DataAccess();
        con =  db.getConnection();

        EmpID = ((Global)this.getApplication()).getEmpID();

        spYear = (Spinner) findViewById(R.id.attYear);//New Edited
        spMonth = (Spinner) findViewById(R.id.attMonth);//New Edited

        setUpYear();
        setUpMonth();
        //setupChart();
    }

    private void setupChart(String CenterText) {
        List<PieEntry> pieEntrys = new ArrayList<>();
        for(int i=0; i<chartHeader.length;i++){
            pieEntrys.add(new PieEntry(charValue[i], chartHeader[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntrys,"");
        dataset.setColors(chartColor);
        PieData data = new PieData(dataset);
        data.setValueTextSize(16);

        PieChart chart = (PieChart) findViewById(R.id.chartAttendance);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText(CenterText);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setUpYear(){

        if (con == null) {
            AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
            alertDialog2.setTitle("Connection error");
            alertDialog2.setMessage("Check your internet access");
            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog2.show();
        }else{
            try {
                // Change below query according to your own database.
                String query = "SELECT DISTINCT TOP 10 Year AS onlyYear FROM MonthlyAttendanceTotal ORDER BY onlyYear DESC";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                int arrayValue = 0;

                if (rs!=null) {
                    try{
                        while(rs.next()){
                            String year = rs.getString("onlyYear");
                            YearList.add(arrayValue,year);
                            arrayValue += 1;
                        }
                    }catch (Exception ex){
                        AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
                        alertDialog2.setTitle("Error");
                        alertDialog2.setMessage("No Leave Data: "+ex.toString());
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();

                    }

                } else {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
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
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,YearList);
        spYear.setAdapter(adapter);

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }//New Edited

    private void setUpMonth(){

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,MonthList);
        spMonth.setAdapter(adapter);

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadChart(){

        try{
            int fromSPyear = Integer.parseInt(spYear.getSelectedItem().toString());
            int fromSPmonth = spMonth.getSelectedItemPosition()+1;
            if (con == null) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
                alertDialog2.setTitle("Connection error");
                alertDialog2.setMessage("Check your internet access");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog2.show();
            }else{
                try {
                    // Change below query according to your own database.
                    String query = "SELECT * FROM MonthlyAttendanceTotal WHERE EmpID = '"+EmpID+"' AND Year='"+fromSPyear+"' AND Month='"+fromSPmonth+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs!=null) {
                        try{
                            if(rs.next()){
                                charValue[1] = rs.getInt("MonthlyFullTotal") + rs.getInt("MonthlyNopayTotal");
                                charValue[2] = rs.getInt("MonthlyHalfTotal");
                                charValue[3] = rs.getInt("MonthlyShortTotal");
                                charValue[0] = 30-(charValue[1]+charValue[2]+charValue[3]);
                                setupChart("");
                            }else{
                                charValue[0] = 0;
                                charValue[1] = 0;
                                charValue[2] = 0;
                                charValue[3] = 0;
                                setupChart("No Data Available");
                            }


                        }catch (Exception ex){
                            AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
                            alertDialog2.setTitle("Error");
                            alertDialog2.setMessage("No Performance Data: "+ex.toString());
                            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog2.show();

                        }

                    } else {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
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
                    AlertDialog alertDialog2 = new AlertDialog.Builder(AttendanceActivity.this).create();
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
            }
        }catch (Exception ex){

        }

    } //New Edited
}
