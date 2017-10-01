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
import android.widget.TextView;

import java.security.SecureRandom;

public class ForgotPasswordActivity extends AppCompatActivity {

    String EmpID;
    String UserName;

    EditText txtEmpID;
    EditText txtUserName;

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

        //PassReset();


    }

    public void PassReset(){
        txtEmpID = (EditText) findViewById(R.id.txtFPEmpID);
        txtUserName = (EditText) findViewById(R.id.txtFPUsername);

        Button btnClick = (Button) findViewById(R.id.btnLeaveSave);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtEmpID.getText().equals(EmpID) && txtUserName.getText().equals(UserName)){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                    alertDialog2.setTitle("Password Resetting");
                    alertDialog2.setMessage("You will Receive a PIN NO shortly to your Registered Mobile No");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    setPin();
                                    Intent intent = new Intent(ForgotPasswordActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    alertDialog2.show();

                }else{
                    AlertDialog alertDialog2 = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                    alertDialog2.setTitle("Password Resetting");
                    alertDialog2.setMessage("Incorrect Details No Authentication to Change Password");
                    alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog2.show();
                }



            }
        });
    }

    public void setPin(){
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);
        ((Global)this.getApplication()).setUserName(formatted);
    }
}
