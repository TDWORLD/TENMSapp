package com.example.thame.tenms;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class LeaveSummery extends Activity {

    String chartHeader[] = {"Approved","Rejected","Pending","Leave Left"};
    int charValue[] = {3,2,1,6};
    String TotalDays = "10";
    int chartColor[] = {Color.rgb(44, 197, 78),Color.rgb(219, 84, 68),Color.rgb(13, 140, 231),Color.rgb(80, 85, 87)};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_summery);

        //setupChart();
    }

    public void setupChart() {

        List<PieEntry> pieEntrys = new ArrayList<>();
        for(int i=0; i<chartHeader.length;i++){
            pieEntrys.add(new PieEntry(charValue[i], chartHeader[i]));
        }

        PieDataSet dataset = new PieDataSet(pieEntrys,"");
        dataset.setColors(chartColor);
        PieData data = new PieData(dataset);

        PieChart chart = (PieChart) findViewById(R.id.chartLeave);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setData(data);
        chart.setCenterText("Total Leave Days \n"+TotalDays);
        chart.animateY(1000);
        chart.invalidate();
    }
}
