package com.example.thame.tenms;

import android.app.Application;

import java.util.Set;

/**
 * Created by Thameesha Dilshan on 9/21/2017.
 */

public class Global extends Application{

    private String UserName;
    private String EmpID;
    private String ResetPin;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }


    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String EmpID) {
        this.EmpID = EmpID;
    }

    public String getResetPin() {
        return ResetPin;
    }

    public void setResetPin(String ResetPin) {
        this.ResetPin = ResetPin;
    }
}
