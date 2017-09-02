package com.example.thame.tenms;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class LoanActivity extends AppCompatActivity {

    String dataList[] = new String[]{"LO001 - LKR 150,000","LO002 - LKR 250,000","LO003 - LKR 50,000","LO004 - LKR 10,000"
            ,"LO005 - LKR 1,000","LO006 - LKR 500,000","LO007 - LKR 1,100,000","LO008 - LKR 60,000","LO009 - LKR 8,500"
            ,"LO010 - LKR 750,000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_loan32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Loan");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner = (Spinner) findViewById(R.id.loanItem);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LoanActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}


