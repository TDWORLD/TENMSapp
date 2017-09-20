package com.example.thame.tenms;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {

    // Declaring connection variables
    Connection con;
    DataAccess db;

    //Controller Define
    Spinner TrainItem;
    EditText TrainID;
    EditText TrainHeader;
    EditText Description;
    EditText TrainEmpCategory;
    EditText TrainStartDate;
    EditText TrainStartTime;
    EditText TrainSeats;

    //String dataList[] = new String[]{"LO001 - LKR 150,000","LO002 - LKR 250,000","LO003 - LKR 50,000"};
    ArrayList<String> dataList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //Controller Declare
        TrainItem = (Spinner) findViewById(R.id.trainItem);
        TrainID = (EditText) findViewById(R.id.txtTrainID);
        TrainHeader = (EditText) findViewById(R.id.txtTrainHeader);
        Description = (EditText) findViewById(R.id.txtDescription);
        TrainEmpCategory = (EditText) findViewById(R.id.txtTrainEmpCategory);
        TrainStartDate = (EditText) findViewById(R.id.txtTrainStartDate);
        TrainStartTime = (EditText) findViewById(R.id.txtTrainStartTime);
        TrainSeats = (EditText) findViewById(R.id.txtTrainSeats);

        db = new DataAccess();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setLogo(R.drawable.ico_train32);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("  Training");

        con = db.getConnection();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        final Spinner spinner = (Spinner) findViewById(R.id.trainItem);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TrainingActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setupTraining();
    }

    public void setupTraining(){

        ResultSet rs;

        try {
            // Change below query according to your own database.
            String query = "SELECT TrainingEventID,TrainingHeader FROM TrainingEvent";
            Statement stmt2 = con.createStatement();
            rs = stmt2.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        String Tid = rs.getString("TrainingEventID");
                        String TTle = rs.getString("TrainingHeader");
                        //dataList[arrayValue] = Tid+"-"+TTle;
                        dataList.add(arrayValue,Tid+" - "+TTle);
                        arrayValue += 1;
                    }
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(TrainingActivity.this).create();
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(TrainingActivity.this).create();
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
            AlertDialog alertDialog2 = new AlertDialog.Builder(TrainingActivity.this).create();
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

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,R.id.Item,dataList);
        TrainItem.setAdapter(adapter);

        TrainItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SelectItem = TrainItem.getSelectedItem().toString();
                String selectID[] = SelectItem.split(" - ");
                LoadTraining(selectID[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void LoadTraining(String Tid){
        ResultSet rs,rs3;
        try {
            // Change below query according to your own database.
            String query = "SELECT * FROM TrainingEvent WHERE TrainingEventID='"+Tid+"'";
            Statement stmt2 = con.createStatement();
            rs = stmt2.executeQuery(query);
            int arrayValue = 0;

            if (rs!=null) {
                try{
                    while(rs.next()){
                        TrainID.setText(rs.getString("TrainingEventID"));
                        TrainHeader.setText(rs.getString("TrainingHeader"));
                        Description.setText(rs.getString("TrainingDesc"));
                        TrainEmpCategory.setText("Manager");
                        TrainStartDate.setText(rs.getString("TrainingStartDate"));
                        TrainStartTime.setText(rs.getString("TrainingStartTime"));
                        TrainSeats.setText(rs.getString("NoOfSeats"));
                    }
                }catch (Exception ex){
                    AlertDialog alertDialog2 = new AlertDialog.Builder(TrainingActivity.this).create();
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(TrainingActivity.this).create();
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
            AlertDialog alertDialog2 = new AlertDialog.Builder(TrainingActivity.this).create();
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
}
