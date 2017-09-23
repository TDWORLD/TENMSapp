package com.example.thame.tenms;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TargetApi(Build.VERSION_CODES.N)
public class LeaveActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    String chartHeader[] = {"Approved", "Rejected", "Pending", "Leave Left"};
    int charValue[] = {3, 2, 1, 6};
    String TotalDays = "10";
    int chartColor[] = {Color.rgb(44, 197, 78), Color.rgb(219, 84, 68), Color.rgb(13, 140, 231), Color.rgb(80, 85, 87)};
    String beginD;
    String endD;
    String EmpID;
    Date begin;
    Date end;

    String YearList[] = new String[]{"2013","2014","2015","2016","2017"};
    String MonthList[] = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    String LeaveTypeList[] = new String[]{"-Select-","Full Day","Half Day","Short Leave","No Pay"};

    EditText beginDate;
    EditText endDate;
    Spinner spLeaveType;
    DatePickerDialog.OnDateSetListener startDateSetListner;
    DatePickerDialog.OnDateSetListener endDateSetListner;
    EditText LeaveID;
    EditText Reason;
    EditText NoOfDays;

    EditText numdays;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    Date date = new Date();
    String Today = dateFormat.format(date).toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_leave32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Leave");

        db = new DataAccess();
        con =  db.getConnection();

        EmpID = ((Global)this.getApplication()).getEmpID();

        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        TabSpec spec1 = tab.newTabSpec("Summary");
        spec1.setIndicator("Summary");
        spec1.setContent(R.id.layoutSummery);
        tab.addTab(spec1);

        TabSpec spec2 = tab.newTabSpec("Request");
        spec2.setIndicator("Request");
        spec2.setContent(R.id.layoutRequest);
        tab.addTab(spec2);

        setUpYear();
        setUpMonth();
        setUpLeaveType();
        setupChart();
        setUpStartDate();
        setUpEndDate();
        calNoofDays();
        clickLeave();
        clickClear();
    }

    private void setupChart() {

        List<PieEntry> pieEntrys = new ArrayList<>();
        for (int i = 0; i < chartHeader.length; i++) {
            pieEntrys.add(new PieEntry(charValue[i], chartHeader[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntrys, "");
        dataset.setColors(chartColor);
        PieData data = new PieData(dataset);
        data.setValueTextSize(16);

        PieChart chart = (PieChart) findViewById(R.id.chartLeave);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText("Total Leave Days \n" + TotalDays);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setUpStartDate() {
        //Begindate
        beginDate = (EditText) findViewById(R.id.txtLeaveBeginDate);
        beginDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        LeaveActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateSetListner,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        startDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                beginDate.setText(date);
            }
        };
    }

    private void setUpEndDate() {
        //EndDate
        endDate = (EditText) findViewById(R.id.txtLeaveEndDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        LeaveActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        endDateSetListner,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        endDateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;
                endDate.setText(date);
            }
        };
    }

    private void calNoofDays() {

        beginDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                beginD = s.toString();
                CalculateDifference();
            }
        });

        endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                endD = s.toString();
                CalculateDifference();
            }
        });
    }

    public void CalculateDifference(){
        try {
            numdays = (EditText) findViewById(R.id.txtLeaveNoOfDays);

            begin = dateFormat.parse(beginD);

            end = dateFormat.parse(endD);
            long diff = end.getTime() - begin.getTime();

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            String text = Long.toString(days+1);
            numdays.setText(text);

        } catch (Exception e) {
            numdays.setText("0");
        }
    }

    private void setUpYear(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,YearList);
        final Spinner spinner = (Spinner) findViewById(R.id.leaveYear);
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

    private void setUpMonth(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,MonthList);
        final Spinner spinner = (Spinner) findViewById(R.id.leaveMonth);
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

    private void setUpLeaveType(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,LeaveTypeList);
        spLeaveType = (Spinner) findViewById(R.id.spLeaveType);
        spLeaveType.setAdapter(adapter);

        spLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getID(){
        String query = "SELECT MAX(LeaveID) FROM Leave";
        Statement stmt = null;
        LeaveID = (EditText) findViewById(R.id.txtLeaveID);
        try{
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs!=null){
                while (rs.next()){
                    int x = Integer.parseInt(rs.getString(""));
                    x++;
                    String s = new DecimalFormat("00000").format(x);
                    LeaveID.setText(s);
                    LeaveID.setEnabled(false);
                }
            }else{
                String s = "00001";
                LeaveID.setText(s);
                LeaveID.setEnabled(false);
            }
        }catch (Exception ex){

        }
    }

    private void clickLeave() {
        getID();
        final String[] categoryName = new String[1];
        /*final Spinner spinner = (Spinner) findViewById(R.id.spLeaveType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        Button btnClick = (Button) findViewById(R.id.btnLeaveSave);
        Reason = (EditText) findViewById(R.id.txtLeaveReason);
        NoOfDays = (EditText) findViewById(R.id.txtLeaveNoOfDays);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    String Query = "INSERT INTO Leave(LeaveID,LeaveType,LeaveRDate,LeaveSDate,LeaveEDate,LeaveReason,LeaveState,SystemHelpLeave,EmpID) " +
                            "VALUES('"+LeaveID.getText()+"','"+spLeaveType.getSelectedItemPosition()+"','"+Today+"','"+beginDate.getText()+"','"+endDate.getText()+"','"+Reason.getText()+"','"+1+"','"+0+"','"+EmpID+"')";
                    //String Query = "INSERT INTO Leave VALUES('"+LeaveID.getText()+"','2','2017-09-23','2017-09-24','2017-09-25','rehhfhfh','1','0','10')";
                    Statement stmt = null;
                    //con = db.getConnection();
                    stmt = con.createStatement();
                    Boolean res = stmt.execute(Query);

                    AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                    alertDialog2.setTitle("Success");
                    alertDialog2.setMessage("Your Leave Added Successful");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();
                    getID();
                    Clean();
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage(ex.toString());
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();
                }
            }
        });
    }

    public void Clean(){
        //LeaveID.setText("");
        Reason.setText("");
        beginDate.setText("");
        endDate.setText("");
        spLeaveType.setSelection(0);
    }

    private void clickClear(){
        Button btnClear = (Button) findViewById(R.id.btnLeaveClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getID();
                Clean();
            }
        });
    }

        }