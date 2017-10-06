package com.example.thame.tenms;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataAccess {

    String IP="66.220.4.119:1433";
    String Server="www.tenmsserver.tk:1433";
    String User = "SQLRemote";
    String Password = "12345";
    String Database = "/HRIS";

    Connection connection;


    public Connection getConnection() {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String ConnectionURL = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://" + IP + Database + ";user=" + User + ";password=" + Password + ";";
                connection = DriverManager.getConnection(ConnectionURL);
            } catch (SQLException se) {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://" + Server + Database + ";user=" + User + ";password=" + Password + ";";
                connection = DriverManager.getConnection(ConnectionURL);
            }/* catch (ClassNotFoundException e) {
                Log.e("Error : ", e.getMessage() + " \n Please contact the developer to fix this issue");
            } catch (Exception e) {
                Log.e("Error : ", e.getMessage() + "\n Please contact the developer to fix this issue");
            }*/
        } catch (Exception ex) {
            Log.e("Error : ", ex.getMessage() + "\n Please contact the developer to fix this issue");
            /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
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
            }*/
        }
            return connection;
        }
}
