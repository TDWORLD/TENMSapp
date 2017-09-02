package com.example.thame.tenms;

import android.graphics.Color;
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
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class SalaryActivity extends AppCompatActivity {

    String chartHeader[] = {"Basic","Allowance","No Pay","OT","Performance","Loan","ETF / EPF"};
    int charValue[] = {65,5,3,10,5,10,2};
    String TotalSalary = "25000";
    int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(242, 103, 38),Color.rgb(80, 85, 87),Color.rgb(0, 161, 168),Color.rgb(100, 36, 201)};

    String YearList[] = new String[]{"2014","2015","2016","2017"};
    String MonthList[] = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};

    String basicsal =  "Basic Salary Amount        : "+"30000";
    String allowance = "Allowance Amount            : "+"3000";
    String nopay =     "No Pay Amount                  : "+"2000";
    String ot =     "OT Amount                          : "+"5000";
    String perform =     "Performance Amount       : "+"3000";
    String loan =      "Loan Reduction Amount  : "+"5000";
    String etf =       "ETF / EPF Amount             : "+"1000";
    String totalsal =  "Total Salary Amount   : "+TotalSalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_sal32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Salary");

        setUpYear();
        setUpMonth();
        setupChart();
        setText();

    }

    private void setupChart() {
        List<PieEntry> pieEntrys = new ArrayList<>();
        for(int i=0; i<chartHeader.length;i++){
            pieEntrys.add(new PieEntry(charValue[i], chartHeader[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntrys,"");
        dataset.setValueFormatter(new PercentFormatter());
        dataset.setColors(chartColor);
        PieData data = new PieData(dataset);
        data.setValueTextSize(16);

        PieChart chart = (PieChart) findViewById(R.id.chartSalary);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText("Total Salary \n"+TotalSalary);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setText(){
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

    private void setUpYear(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,YearList);
        final Spinner spinner = (Spinner) findViewById(R.id.salaryYear);
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
        final Spinner spinner = (Spinner) findViewById(R.id.salaryMonth);
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
