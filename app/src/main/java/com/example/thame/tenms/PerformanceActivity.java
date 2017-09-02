package com.example.thame.tenms;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class PerformanceActivity extends AppCompatActivity {

    String YearList[] = new String[]{"2014","2015","2016","2017"};

    String chartHeader[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    int charValue[] = {13, 22, 11, 6,20,25,36,45,78,85,42,12};
    String TotalDays = "10";
    int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(242, 103, 38),
                        Color.rgb(80, 85, 87),Color.rgb(0, 161, 168),Color.rgb(100, 36, 201),Color.rgb(0, 143, 0),
                        Color.rgb(115, 66, 13),Color.rgb(251, 66, 170),Color.rgb(247, 86, 40),Color.rgb(221, 175, 255)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_perfo32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Performance");

        setUpYear();
        setupChart();
    }

    private void setUpYear(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,YearList);
        final Spinner spinner = (Spinner) findViewById(R.id.performYear);
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

    private void setupChart() {

        List<BarEntry> barEntrys = new ArrayList<>();

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return chartHeader[(int) value];
            }
        };

        for (int i = 0; i < chartHeader.length; i++) {
            barEntrys.add(new BarEntry(i,charValue[i]));
        }

        BarDataSet dataset = new BarDataSet(barEntrys,"Months");
        dataset.setColors(chartColor);
        BarData data = new BarData(dataset);
        data.setValueTextSize(16);

        BarChart chart = (BarChart) findViewById(R.id.chartPerform);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(0.5f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelCount(11);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }
}
