package com.example.thame.tenms;

import android.content.DialogInterface;
import android.graphics.Color;
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
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SalaryActivity extends AppCompatActivity {

    String chartHeader[] = {"NetSalary","LeaveAmount","EPF","ETF","OT Amount"};
    int charValue[] = {0,0,0,0,0};
    String TotalSalary = "25000";
    int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(242, 103, 38),Color.rgb(80, 85, 87)};
    //int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(242, 103, 38),Color.rgb(80, 85, 87),Color.rgb(0, 161, 168),Color.rgb(100, 36, 201)};

    //String YearList[] = new String[]{"2014","2015","2016","2017"};
    ArrayList<String> YearList = new ArrayList<String>();
    String MonthList[] = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};

    //New Edited
    Spinner spYear;
    Spinner spMonth;

    String basicsal =  "Basic Salary Amount        : "+"30000";
    String allowance = "Allowance Amount            : "+"3000";
    String nopay =     "No Pay Amount                  : "+"2000";
    String ot =     "OT Amount                          : "+"5000";
    String perform =     "Performance Amount       : "+"3000";
    String loan =      "Loan Reduction Amount  : "+"5000";
    String etf =       "ETF / EPF Amount             : "+"1000";
    String totalsal =  "Total Salary Amount   : "+TotalSalary;

    // Declaring connection variables
    Connection con;
    DataAccess db;
    String EmpID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_sal32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Salary");

        db = new DataAccess();
        con =  db.getConnection();

        EmpID = ((Global)this.getApplication()).getEmpID();

        spYear = (Spinner) findViewById(R.id.salaryYear);//New Edited
        spMonth = (Spinner) findViewById(R.id.salaryMonth);//New Edited

        setUpYear();
        setUpMonth();

        //setupChart();
        //setText();

    }


    private void setUpYear(){

        if (con == null) {
            AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
                String query = "SELECT DISTINCT TOP 10 SalaryYear FROM Salary ORDER BY SalaryYear DESC";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                int arrayValue = 0;

                if (rs!=null) {
                    try{
                        while(rs.next()){
                            String year = rs.getString("SalaryYear");
                            YearList.add(arrayValue,year);
                            arrayValue += 1;
                        }
                    }catch (Exception ex){
                        AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
                    AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
    }

    private void setUpMonth(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,MonthList);
        spMonth.setAdapter(adapter);

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupChart(String CenterText) {
        List<PieEntry> pieEntrys = new ArrayList<>();
        for(int i=0; i<chartHeader.length;i++){
            pieEntrys.add(new PieEntry(charValue[i], chartHeader[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntrys,"");
        //dataset.setValueFormatter(new PercentFormatter());
        dataset.setColors(chartColor);
        PieData data = new PieData(dataset);
        data.setValueTextSize(16);

        PieChart chart = (PieChart) findViewById(R.id.chartSalary);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText(CenterText);
        chart.animateY(1000);
        chart.invalidate();
    }
/*
    private void setText(String structurid){

        try {

            // Change below query according to your own database.
            String query = "SELECT * FROM SalaryStructure WHERE SalaryStructure='"+structurid+"'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

        }catch (Exception ex){

        }
        TextView tBasic = (TextView) findViewById(R.id.lblBasicSalary);
        tBasic.setText(basicsal);
        TextView tAllowance = (TextView) findViewById(R.id.lblAllowance);
        tAllowance.setText(allowance);
        TextView tNopay = (TextView) findViewById(R.id.lblNoPAy);
        tNopay.setText(nopay);
        TextView tOT = (TextView) findViewById(R.id.lblOT);
        tOT.setText(ot);
        TextView tPerform = (TextView) findViewById(R.id.lblPerformance);
        tPerform.setText(perform);
        TextView tLoan = (TextView) findViewById(R.id.lblLoan);
        tLoan.setText(loan);
        TextView tEtf = (TextView) findViewById(R.id.lblEtfEpf);
        tEtf.setText(etf);
        TextView tTotal = (TextView) findViewById(R.id.lblTotalSalary);
        tTotal.setText(totalsal);
    }
*/

    public void getData(){

        int fromSPyear = Integer.parseInt(spYear.getSelectedItem().toString());
        int fromSPmonth = spMonth.getSelectedItemPosition()+1;

        if (con == null) {
            AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
                String query = "SELECT * FROM Salary WHERE EmpID ='"+EmpID+"' AND SalaryYear = '"+fromSPyear+"' AND SalaryMonth = '"+fromSPmonth+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                for(int i=0; i<5;i++){
                    charValue[i] = 0;
                }
                if (rs!=null) {

                    try{
                        if(rs.next()){
                            charValue[0] = rs.getInt("NetSalary");
                            charValue[1] = rs.getInt("LeaveAmount");
                            charValue[2] = rs.getInt("Epf");
                            charValue[3] = rs.getInt("Etf");
                            charValue[4] = rs.getInt("OTAmount");
                            setupChart("");
                        }else{
                            charValue[0] = 0;
                            charValue[1] = 0;
                            charValue[2] = 0;
                            charValue[3] = 0;
                            charValue[4] = 0;
                            setupChart("No Data Available");
                        }
                    }catch (Exception ex){
                        AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
                    AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(SalaryActivity.this).create();
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
    }


}
