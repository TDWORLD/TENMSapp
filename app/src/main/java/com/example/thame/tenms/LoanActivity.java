package com.example.thame.tenms;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoanActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    String dataList[] = new String[]{"","","","","","","","","",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        db = new DataAccess();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_loan32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Loan");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner = (Spinner) findViewById(R.id.loanItem);
        spinner.setAdapter(adapter);

        setLoan();

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

    public void setLoan(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner2 = (Spinner) findViewById(R.id.loanItem);

        try {
            // Change below query according to your own database.

            String query = "SELECT * FROM Loan";
            Statement stmt2 = con.createStatement();
            ResultSet rs = stmt2.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        String Lid = rs.getString("LoanID");
                        String LAmt = rs.getString("ProposedAmount");
                        dataList[arrayValue] = Lid+" - "+LAmt;
                        arrayValue += 1;
                    }
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(LoanActivity.this).create();
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage("Error: "+ex.toString());
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();

                }

            }
            else {
                AlertDialog alertDialog2 = new AlertDialog.Builder(LoanActivity.this).create();
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
            AlertDialog alertDialog2 = new AlertDialog.Builder(LoanActivity.this).create();
            alertDialog2.setTitle("Connection error");
            alertDialog2.setMessage(ex.toString());
            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog2.show();

        }

        spinner2.setAdapter(adapter);
    }
}


