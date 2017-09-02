package com.example.thame.tenms;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class TrainingActivity extends AppCompatActivity {

    String dataList[] = new String[]{"LO001 - LKR 150,000","LO002 - LKR 250,000","LO003 - LKR 50,000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_train32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Training");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner = (Spinner) findViewById(R.id.trainItem);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TrainingActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
