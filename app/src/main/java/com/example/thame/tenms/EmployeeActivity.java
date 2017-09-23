package com.example.thame.tenms;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EmployeeActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    //Controller Define
    EditText EmpID;
    EditText FName;
    EditText LName;
    EditText Address1;
    EditText Address2;
    EditText Gender;
    EditText TPHome;
    EditText TPMobile;
    EditText EmpNIC;
    EditText EmpDOB;
    Button Save;
    Button Cancel;
    int gnder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        db = new DataAccess();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_emp32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Profile");

        //Controller Declare
        EmpID = (EditText) findViewById(R.id.txtEmpID);
        FName = (EditText) findViewById(R.id.txtFName);
        LName = (EditText) findViewById(R.id.txtSName);
        Address1 = (EditText) findViewById(R.id.txtAddress1);
        Address2 = (EditText) findViewById(R.id.txtAddress2);
        Gender = (EditText) findViewById(R.id.txtAddress3);
        TPHome = (EditText) findViewById(R.id.txtTPHome);
        TPMobile = (EditText) findViewById(R.id.txtTPMobile);
        EmpNIC = (EditText) findViewById(R.id.txtEmail);
        EmpDOB = (EditText) findViewById(R.id.txtPassword);
        Save = (Button) findViewById(R.id.btnSave);
        Cancel = (Button) findViewById(R.id.btnCancel);

        con = db.getConnection();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });

        setupEmployee();

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setupEmployee(){
        String empid="1";

        ResultSet rs,rs3;
        try {
            // Change below query according to your own database.
            String query = "SELECT * FROM Employee WHERE EmpID='"+empid+"'";
            Statement stmt2 = con.createStatement();
            rs = stmt2.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        EmpID.setText(rs.getString("EmpID"));
                        FName.setText(rs.getString("EmpFName"));
                        LName.setText(rs.getString("EmpLName"));
                        Address1.setText(rs.getString("EmpAddress1"));
                        Address2.setText(rs.getString("EmpAddress2"));
                        Gender.setText("Male");
                        TPHome.setText(rs.getString("EmpTeleH"));
                        TPMobile.setText(rs.getString("EmpTeleM"));
                        EmpNIC.setText(rs.getString("EmpNic"));
                        EmpDOB.setText(rs.getString("EmpDob"));
                    }
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
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
            AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
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

    public void updateEmployee(){
        try{
            String Query = "UPDATE Employee SET EmpAddress1='"+Address1.getText()+"', EmpAddress2='"+Address2.getText()+"', EmpGender='"+1+"', EmpTeleH='"+TPHome.getText()+"', EmpTeleM='"+TPMobile.getText()+"', EmpNIC='"+EmpNIC.getText()+"', EmpDob='"+EmpDOB.getText()+"' WHERE EmpID='"+EmpID.getText()+"'";
            Statement stmt = null;
            stmt = con.createStatement();
            Boolean res = stmt.execute(Query);

            if (res=true){
                AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
                alertDialog2.setTitle("Success");
                alertDialog2.setMessage("Your employee data updated successfully");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog2.show();

            }
            else{
                AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
                alertDialog2.setTitle("Failed");
                alertDialog2.setMessage("Failed to update your information");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog2.show();
            }
        }
        catch(Exception ex){
            AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
            alertDialog2.setTitle("Error");
            alertDialog2.setMessage("Error:"+ex.getMessage());
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
