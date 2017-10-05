package com.example.thame.tenms;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    Spinner Gender;
    EditText TPHome;
    EditText TPMobile;
    EditText EmpNIC;
    EditText EmpDOB;
    Button Save;
    Button Cancel;
    int gnder;

    String genderList[] = new String[]{"-SELECT-","Male","Female"};

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
        Gender = (Spinner) findViewById(R.id.spGender);
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
                if (TextUtils.isEmpty(Address1.getText().toString())){
                    Address1.setError("Please enter your address line 1");
                }
                else if (TextUtils.isEmpty(Address2.getText().toString())){
                    Address2.setError("Please enter your address line 2");
                }
                else if (TextUtils.isEmpty(TPHome.getText().toString())){
                    TPHome.setError("Please enter your home telephone number");
                }
                else if (TextUtils.isEmpty(TPMobile.getText().toString())){
                    TPMobile.setError("Please enter your mobile phone number");
                }
                else if (TextUtils.isEmpty(EmpNIC.getText().toString())){
                    EmpNIC.setError("Please enter your NIC number");
                }
                else if (TextUtils.isEmpty(EmpDOB.getText().toString())){
                    EmpDOB.setError("Please enter your birthday");
                }
                else if (Gender.getSelectedItem().toString().equals("-SELECT-")){
                    ((TextView)Gender.getSelectedView()).setError("Please select a category to continue");
                }
                else if(EmpNIC.getText().toString().length()<10){
                    EmpNIC.setError("NIC number must have 10 digits");
                }
                else if(TPHome.getText().toString().length()<10){
                    TPHome.setError("Home telephone number must have 10 digits");
                }
                else if(TPMobile.getText().toString().length()<10){
                    TPMobile.setError("Mobile phone number must have 10 digits");
                }
                else{
                    updateEmployee();
                }
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
        String empid=((Global)this.getApplication()).getEmpID();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,genderList);
        Gender.setAdapter(adapter);

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
                        Gender.setSelection(rs.getInt("EmpGender"));
                        TPHome.setText("0"+rs.getString("EmpTeleH"));
                        TPMobile.setText("0"+rs.getString("EmpTeleM"));
                        EmpNIC.setText(rs.getString("EmpNic"));
                        EmpDOB.setText(rs.getString("EmpDob"));
                    }
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
                    alertDialog2.setTitle("Error");
                    alertDialog2.setMessage("Error while loading the data");
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
                alertDialog2.setTitle("Error");
                alertDialog2.setMessage("Error in connection. Please try again later");
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
            alertDialog2.setTitle("Error");
            alertDialog2.setMessage("Error in connection. Please try again later");
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
            String Query = "UPDATE Employee SET EmpAddress1='"+Address1.getText()+"', EmpAddress2='"+Address2.getText()+"', EmpGender='"+Gender.getSelectedItemPosition()+"', EmpTeleH='"+TPHome.getText()+"', EmpTeleM='"+TPMobile.getText()+"', EmpNIC='"+EmpNIC.getText()+"', EmpDob='"+EmpDOB.getText()+"' WHERE EmpID='"+EmpID.getText()+"'";
            Statement stmt = null;
            stmt = con.createStatement();
            Boolean res = stmt.execute(Query);

            if (res=true){
                AlertDialog alertDialog2 = new AlertDialog.Builder(EmployeeActivity.this).create();
                alertDialog2.setTitle("Done");
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
                alertDialog2.setTitle("Unsuccessful");
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
            alertDialog2.setMessage(ex.getMessage().toString());
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
