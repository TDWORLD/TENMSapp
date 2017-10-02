package com.example.thame.tenms;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Thameesha Dilshan on 2017-09-03.
 */

public class DataAccess {

    String Server="124.43.35.58:1433";
    String User = "SQLRemote";
    String Password = "12345";
    String Database = "/HRIS";

    Connection connection;


    public Connection getConnection(){

        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String ConnectionURL = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://" + Server + Database + ";user=" + User + ";password=" + Password + ";";
                connection = DriverManager.getConnection(ConnectionURL);
            } catch (SQLException se) {
                Log.e("Error : ", se.getMessage() + "\n Please contact the developer to fix this issue");
            } catch (ClassNotFoundException e) {
                Log.e("Error : ", e.getMessage() + " \n Please contact the developer to fix this issue");
            } catch (Exception e) {
                Log.e("Error : ", e.getMessage() + "\n Please contact the developer to fix this issue");
            }
        }catch (Exception ex){

        }

        return connection;
    }

}
