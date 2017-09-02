package com.example.thame.tenms;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
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

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    String chartHeader[] = {"Present","Absent","ShortLeave","HalfDay","Days Left"};
    int charValue[] = {14,3,1,2,5};
    String TotalDays = "25";
    int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(242, 103, 38),Color.rgb(80, 85, 87)};
    String AttPrecentage = "Attendance Percentage : "+"86%";
    String AbsPrecentage = "Absent Percentage : "+"13%";

    String YearList[] = new String[]{"2014","2015","2016","2017"};
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

        setUpYear();
        setUpMonth();
        setupChart();
        TextView tAtt = (TextView) findViewById(R.id.lblAttPrecent);
        tAtt.setText(AttPrecentage);
        TextView tAbs = (TextView) findViewById(R.id.lblAbsPrecent);
        tAbs.setText(AbsPrecentage);
    }

    private void setupChart() {
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
        chart.setCenterText("Total Working Days \n"+TotalDays);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setUpYear(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,YearList);
        final Spinner spinner = (Spinner) findViewById(R.id.attYear);
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
        final Spinner spinner = (Spinner) findViewById(R.id.attMonth);
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
}
