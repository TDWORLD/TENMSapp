package com.example.thame.tenms;

import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ForgotPasswordActivity extends AppCompatActivity {

    String EmpID;
    String UserName;
    String Pin;

    EditText txtEmpID;
    EditText txtUserName;

    // Declaring connection variables
    Connection con;
    DataAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.icon_passreset);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Password Reset");

        EmpID = ((Global)this.getApplication()).getEmpID();
        UserName = ((Global)this.getApplication()).getUserName();
        Pin = ((Global)this.getApplication()).getResetPin();

        getData(EmpID,UserName);
        PassReset();

        con = db.getConnection();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ForgotPasswordActivity.this, "Permission denied to Send/Read SMS", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getData(String EmpID,String UserName){
        ResultSet rs;
        try{
            //String query = "SELECT EmpID FROM tbl_User WHERE UsernameA='"+UserName+"'";
            String query2 = "SELECT EmpTeleM FROM Employee WHERE EmpID='(SELECT EmpID FROM tbl_User WHERE UsernameA='"+UserName+"')'";
            Statement stmt2 = con.createStatement();
            rs = stmt2.executeQuery(query2);
            String teleNo = "+94"+rs.getString("EmpTeleM");
            sendSMS(teleNo,Pin);
        }
        catch(Exception ex){
            AlertDialog alertDialog2 = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
            alertDialog2.setTitle("Invalid");
            alertDialog2.setMessage("The username you have entered in not valid. Please enter a valid username to reset the password.");
            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog2.show();
        }
    }

    public void sendSMS(String teleNo,String pin){
        ActivityCompat.requestPermissions(ForgotPasswordActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        try {
            String messageToSend = "Your password resetting code is : "+pin;

            SmsManager.getDefault().sendTextMessage(teleNo, null, messageToSend, null, null);
            AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
            alertDialog.setTitle("Successful");
            alertDialog.setMessage("Pin code sent successfully to your phone");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        catch (Exception e)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Problem occured"+e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void PassReset(){
        txtEmpID = (EditText) findViewById(R.id.txtFPEmpID);
        txtUserName = (EditText) findViewById(R.id.txtFPUsername);

        Button btnClick = (Button) findViewById(R.id.btnLeaveSave);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });
    }

    public void setPin(){
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);
        ((Global)this.getApplication()).setResetPin(formatted);
    }
}
