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
import android.text.TextUtils;
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
import android.widget.TextView;
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

    ArrayList<String> YearList = new ArrayList<String>();//New Edited
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Date date = new Date();
    String Today = dateFormat.format(date).toString();

    //New Edited
    Spinner spYear;
    Spinner spMonth;

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

        spYear = (Spinner) findViewById(R.id.leaveYear);//New Edited
        spMonth = (Spinner) findViewById(R.id.leaveMonth);//New Edited

        setUpYear();
        setUpMonth();
        setUpLeaveType();
        setupChart();
        setUpStartDate();
        setUpEndDate();
        calNoofDays();
        clickLeave();
        clickClear();
        loadChart();
        getID();
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
                String date = year + "-" + month + "-" + dayOfMonth;
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
                String date = year + "-" + month + "-" + dayOfMonth;
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

            if(days<0) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                alertDialog2.setTitle("Error");
                alertDialog2.setMessage("Leave end date should be a new date than leave start date");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog2.show();

                beginDate.setText("");
                endDate.setText("");
            }


        } catch (Exception e) {
            numdays.setText("0");
        }
    }

    private void setUpYear(){

        if (con == null) {
            AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
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
                String query = "SELECT DISTINCT TOP 10 datepart(yyyy,LeaveSdate) AS onlyYear FROM Leave ORDER BY onlyYear DESC";
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
                        AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
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

                } else {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage("Error in database. Please try again later.");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();
                }
            } catch (Exception ex) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
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
    } //New Edited

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
    }//New Edited

    private void setUpLeaveType(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,LeaveTypeList);
        spLeaveType = (Spinner) findViewById(R.id.spLeaveType);
        spLeaveType.setAdapter(adapter);

        spLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spLeaveType.getSelectedItemPosition()==1||spLeaveType.getSelectedItemPosition()==4){
                    beginDate.setEnabled(true);
                    endDate.setEnabled(true);
                }
                else if (spLeaveType.getSelectedItemPosition()==0){
                    beginDate.setEnabled(false);
                    endDate.setEnabled(false);
                }
                else{
                    beginDate.setEnabled(true);
                    endDate.setEnabled(true);
                }
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

        Button btnClick = (Button) findViewById(R.id.btnLeaveSave);
        Reason = (EditText) findViewById(R.id.txtLeaveReason);
        NoOfDays = (EditText) findViewById(R.id.txtLeaveNoOfDays);
        spLeaveType = (Spinner)findViewById(R.id.spLeaveType);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spLeaveType.getSelectedItemPosition()==0){
                    ((TextView)spLeaveType.getSelectedView()).setError("Please select a leave category to continue");
                }
                else if (TextUtils.isEmpty(Reason.getText().toString())){
                    Reason.setError("Please enter the leave reason");
                }
                else if (TextUtils.isEmpty(beginDate.getText().toString())){
                    beginDate.setError("Please select your leave start date");
                }
                else if (TextUtils.isEmpty(endDate.getText().toString())){
                    endDate.setError("Please select your leave end date");
                }
                else {
                    try {
                        //SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        //Date startDate = format.parse(beginDate.getText().toString());

                        String Query = "INSERT INTO Leave(LeaveID,LeaveType,LeaveRDate,LeaveSDate,LeaveEDate,LeaveReason,LeaveState,SystemHelpLeave,EmpID) " +
                                "VALUES('" + LeaveID.getText() + "','" + spLeaveType.getSelectedItemPosition() + "','" + Today + "','" + beginDate.getText() + "','" + endDate.getText() + "','" + Reason.getText() + "','" + 1 + "','" + 0 + "','" + EmpID + "')";
                        //String Query = "INSERT INTO Leave VALUES('"+LeaveID.getText()+"','2','2017-09-23','2017-09-24','2017-09-25','rehhfhfh','1','0','10')";
                        Statement stmt = null;
                        //con = db.getConnection();
                        stmt = con.createStatement();
                        Boolean res = stmt.execute(Query);

                        AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                        alertDialog2.setTitle("Success");
                        alertDialog2.setMessage("Your Leave Added Successfully");
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
                        AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                        alertDialog2.setTitle("Error");
                        alertDialog2.setMessage("Error occured while adding leaves to the system.");
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

    public void loadChart(){

        try{
            int fromSPyear = Integer.parseInt(spYear.getSelectedItem().toString());
            int fromSPmonth = spMonth.getSelectedItemPosition()+1;
            if (con == null) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
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
                    String query = "SELECT * FROM Leave WHERE EmpID = '"+EmpID+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    int Approved = 0;
                    int Rejected = 0;
                    int Pending = 0;
                    int LeaveLeft = 0;


                    if (rs!=null) {
                        try{
                            while(rs.next()){
                                Date date = rs.getDate("LeaveSdate");
                                Calendar cal=Calendar.getInstance();
                                cal.setTime(date);
                                int year = cal.get(Calendar.YEAR);
                                int month = cal.get(Calendar.MONTH)+1;
                                if(year==fromSPyear && month==fromSPmonth){
                                    String state = rs.getString("LeaveState");
                                    if(state.equals("1")){
                                        Pending++;
                                    }else if (state.equals("2")){
                                        Approved++;
                                    }else if (state.equals("3")){
                                        Rejected++;
                                    }
                                }
                            }
                            TotalDays = Integer.toString(Pending + Approved + Rejected);
                            charValue[0] = Approved;
                            charValue[1] = Rejected;
                            charValue[2] = Pending;
                            charValue[3] = LeaveLeft;
                            setupChart();

                        }catch (Exception ex){
                            AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                            alertDialog2.setTitle("Error");
                            alertDialog2.setMessage("Error while loading data. Please try again later.");
                            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog2.show();

                        }

                    } else {
                        AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
                        alertDialog2.setTitle("Error");
                        alertDialog2.setMessage("Error while loading data. Please try again later.");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();
                    }
                } catch (Exception ex) {
                    AlertDialog alertDialog2 = new AlertDialog.Builder(LeaveActivity.this).create();
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