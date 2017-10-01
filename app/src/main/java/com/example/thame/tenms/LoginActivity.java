package com.example.thame.tenms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.sql.Connection;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Declaring components
    private TextView forgetPass;
    private EditText company, uname, pword;
    private Button btnLogin;
    private CheckBox checkRemember;
    private ProgressBar proLogin;

    // Declaring connection variables
    Connection con;
    DataAccess db;

    private RequestQueue requestQueue;
    //private static final String url = "http://tenms.dynu.net/androidapp/userLogin.php";
    private StringRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.login);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Login");

        forgetPass = (TextView) findViewById(R.id.txtForgetPass);
        //company = (EditText) findViewById(R.id.txtCompany);
        uname = (EditText) findViewById(R.id.txtUname);
        pword = (EditText) findViewById(R.id.txtPword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        proLogin = (ProgressBar) findViewById(R.id.proLogin);
        proLogin.setVisibility(View.INVISIBLE);

        requestQueue = Volley.newRequestQueue(this);

        // Connect to database
        db = new DataAccess();
        con =  db.getConnection();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* if (TextUtils.isEmpty(company.getText().toString())) {
                    company.setError("Please enter the company id");
                } else */

                if (TextUtils.isEmpty(uname.getText().toString())) {
                    uname.setError("Please enter the username");
                } else if (TextUtils.isEmpty(pword.getText().toString())) {
                    pword.setError("Please enter the password");
                } else {
                    proLogin.setIndeterminate(true);
                    proLogin.setVisibility(View.VISIBLE);

                    clickLogin();
                }
            }
        });

    }

    public void clickLogin() {

        String usernam = uname.getText().toString();
        String passwordd = pword.getText().toString();

        if(usernam != null && passwordd != null){
            try {

                if (con != null) {

                    // Change below query according to your own database.
                    String query = "SELECT * FROM tbl_User WHERE UsernameA= '" + usernam.toString() + "' AND PasswordA = '" + passwordd.toString() + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        //con.close();
                        ((Global)this.getApplication()).setUserName(usernam.toString());
                        ((Global)this.getApplication()).setEmpID(rs.getString("EmpID"));
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        //proLogin.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                        finish();
                    }else{
                        AlertDialog alertDialog2 = new AlertDialog.Builder(LoginActivity.this).create();
                        alertDialog2.setTitle("Invalid");
                        alertDialog2.setMessage("Invalid username or password");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog2.show();
                        uname.setText("");
                        pword.setText("");
                        //proLogin.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (Exception ex) {

                AlertDialog errorDialog = new AlertDialog.Builder(LoginActivity.this).create();
                errorDialog.setTitle("Connection error");
                errorDialog.setMessage("Check your internet access");
                //errorDialog.setMessage(ex.toString());
                errorDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                errorDialog.show();

            }
        }
    }


    public void passwordReset(View view) {
        Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(intent);
    }

}

