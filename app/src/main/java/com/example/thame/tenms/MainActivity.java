package com.example.thame.tenms;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnAnnouncement,btnEmployee,btnAttendance,btnSalary,btnLoan,btnLeave,btnPerformance,btnComplaint,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_home);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Cethro Furniture(pvt)Ltd");//replace with company name variable

        btnAnnouncement = (ImageButton) findViewById(R.id.btnAnnounce);
        btnAttendance = (ImageButton) findViewById(R.id.btnAttendance);
        btnComplaint = (ImageButton) findViewById(R.id.btnComplaint);
        btnEmployee = (ImageButton) findViewById(R.id.btnEmployee);
        btnLeave = (ImageButton) findViewById(R.id.btnLeave);
        btnLoan = (ImageButton) findViewById(R.id.btnLoan);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnPerformance = (ImageButton) findViewById(R.id.btnPerformance);
        btnSalary = (ImageButton) findViewById(R.id.btnSalary);

        btnAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AnnouncementActivity.class);
                startActivity(intent);
            }
        });

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AttendanceActivity.class);
                startActivity(intent);
            }
        });

        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TrainingActivity.class);
                startActivity(intent);
            }
        });

        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EmployeeActivity.class);
                startActivity(intent);
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LeaveActivity.class);
                startActivity(intent);
            }
        });

        btnLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoanActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PerformanceActivity.class);
                startActivity(intent);
            }
        });

        btnSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SalaryActivity.class);
                startActivity(intent);
            }
        });


    }


}
