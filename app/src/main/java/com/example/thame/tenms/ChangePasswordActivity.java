package com.example.thame.tenms;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChangePasswordActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    EditText ReceivePIN;
    EditText NewPassword;
    EditText NewPassword2;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ReceivePIN = (EditText) findViewById(R.id.txtReceivePIN);
        NewPassword = (EditText) findViewById(R.id.txtNewPassword);
        NewPassword2 = (EditText) findViewById(R.id.txtNewPassword2);
        btnReset=(Button) findViewById(R.id.btnChangePass);

        db = new DataAccess();

        try{
            con = db.getConnection();
        }catch (Exception ex){
            AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Problem occured"+ex.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPin();
            }
        });
    }

    public void checkPin(){
        String pin=((Global)this.getApplication()).getResetPin();
        String pinEntered = ReceivePIN.getText().toString();

        if (pin.equals(pinEntered)){
            validatePassword();
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
            alertDialog.setTitle("Invalid");
            alertDialog.setMessage("Please enter the pin you received as the PIN code");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void validatePassword(){

        String un=((Global)this.getApplication()).getUserName();

        if (NewPassword.getText().toString().equals(NewPassword2.getText().toString())){
            //if (NewPassword.getText().toString().equals("NULL")||NewPassword2.getText().toString().equals("NULL")||NewPassword.getText().toString().equals("")||NewPassword2.getText().toString().equals("")){
                setPwd(un);
            //}
            //else{
                /*AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Please enter a new password to continue");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/
            //}
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
            alertDialog.setTitle("Invalid");
            alertDialog.setMessage("Please enter the same password for the both fields.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            NewPassword.setText("");
            NewPassword2.setText("");
        }
    }

    public void setPwd(String un){
        Boolean res=null;
        try{
            String Query = "UPDATE tbl_User SET PasswordA='"+NewPassword.getText().toString()+"' WHERE UsernameA='"+un+"'";
            Statement stmt = null;
            stmt = con.createStatement();
            res = stmt.execute(Query);

            if (res=true){
                AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Password resetted successfully");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Error occured when resetting the password");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }
        catch(Exception ex){
            AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Problem occured"+ex.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

}
