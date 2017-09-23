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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoanActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    //Controller Define
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Spinner LoanItem;
    EditText LoanID;
    EditText LoanState;
    EditText LoanAmount;
    EditText LoanIntrest;
    EditText LoanStartD;
    EditText LoanEndD;
    EditText LoanMRemain;
    EditText LoanMReduction;

    String EmpID;

    //String dataList[] = new String[]{"","","","","","","","","",""};
    ArrayList<String> dataList = new ArrayList<String>();

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

        //Controller Declare
        LoanItem = (Spinner) findViewById(R.id.loanItem);
        LoanID = (EditText) findViewById(R.id.txtLoanID);
        LoanState = (EditText) findViewById(R.id.txtLoanState);
        LoanAmount = (EditText) findViewById(R.id.txtLoanAmount);
        LoanIntrest = (EditText) findViewById(R.id.txtLoanIntrest);
        LoanStartD = (EditText) findViewById(R.id.txtLoanStartDate);
        LoanEndD = (EditText) findViewById(R.id.txtLoanEndDate);
        LoanMRemain = (EditText) findViewById(R.id.txtLoanMonthsRemaining);
        LoanMReduction = (EditText) findViewById(R.id.txtLoanMonthlyReduction);

        con = db.getConnection();
        EmpID = ((Global)this.getApplication()).getEmpID();
        setupLoan();


    }

    public void setupLoan(){

        ResultSet rs;

        try {
            // Change below query according to your own database.
            String query = "SELECT LoanID,ProposedAmount FROM Loan WHERE EmpID='"+EmpID+"'";
            Statement stmt2 = con.createStatement();
            rs = stmt2.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        String Lid = rs.getString("LoanID");
                        String LAmt = rs.getString("ProposedAmount");
                        //dataList[arrayValue] = Lid+"-"+LAmt;
                        dataList.add(arrayValue,Lid+" - "+LAmt);
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

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        LoanItem.setAdapter(adapter);

        LoanItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SelectItem = LoanItem.getSelectedItem().toString();
                String selectID[] = SelectItem.split(" - ");
                LoadLoan(selectID[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void LoadLoan(String LId){
        ResultSet rs;
        try {
            // Change below query according to your own database.
            String query = "SELECT * FROM Loan WHERE LoanID='"+LId+"'";
            Statement stmt2 = con.createStatement();
            rs = stmt2.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        LoanID.setText(rs.getString("LoanID"));
                        if(rs.getString("Status").equals("1")){
                            LoanState.setText("Active");
                        }else if (rs.getString("Status").equals("2")){
                            LoanState.setText("Hold");
                        }else if (rs.getString("Status").equals("3")){
                            LoanState.setText("Deactive");
                        }
                        LoanAmount.setText("LKR "+rs.getString("ProposedAmount"));
                        LoanIntrest.setText(rs.getString("InterestRate")+"%");
                        LoanStartD.setText(rs.getString("AcceptDate"));
                        LoanEndD.setText(rs.getString("RecoveryDate"));
                        LoanMRemain.setText(rs.getString("RemainingMonths"));
                        LoanMReduction.setText("LKR "+rs.getString("MonthlyAmount"));
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
    }

    public int NoOfMonths(String Start, String End){
        int Months = 0;

        try {
            Date sd = dateFormat.parse(Start);
            Date ed = dateFormat.parse(End);

            long diff = ed.getTime() - sd.getTime();
            Months = (int) (diff/(30 * 24 * 60 * 60 * 1000));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Months;
    }
}


