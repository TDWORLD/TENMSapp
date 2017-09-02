package com.example.thame.tenms;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.icon_passreset);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Password Reset");
    }


    public void RequestReset(View view) {
        String msg = "Your Password is Resetted You will Receive a New Password Shortly Please Check Your E-Mail";
        TextView resetState = (TextView)findViewById(R.id.txt_resetpassState);
        resetState.setText(msg);
    }
}
