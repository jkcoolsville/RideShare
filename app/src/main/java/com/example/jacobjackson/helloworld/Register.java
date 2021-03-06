package com.example.jacobjackson.helloworld;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if(checkPassword()){
                    goToSecondActivity();
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(Register.this).create();
                    alertDialog.setTitle("Passwords don't match");
                    alertDialog.setMessage("Please check your password to make sure they are the same.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    recreate();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        final RadioButton carButton = (RadioButton) findViewById(R.id.radioButton5);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton5){
                    findViewById(R.id.editText15).setVisibility(View.VISIBLE);
                    findViewById(R.id.editText14).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.editText15).setVisibility(View.INVISIBLE);
                    EditText radius = (EditText) findViewById(R.id.editText15);
                    radius.setText("0");
                    findViewById(R.id.editText14).setVisibility(View.INVISIBLE);
                    EditText seats = (EditText) findViewById(R.id.editText14);
                    seats.setText("0");
                }
            }
        });

        final EditText startTime = (EditText) findViewById(R.id.editText16);
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Register.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        final EditText endTime = (EditText) findViewById(R.id.editText17);
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Register.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }

    private void goToSecondActivity() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        EditText text = (EditText)findViewById(R.id.editText1);
        String value = text.getText().toString().replace(" ", ".");

        String data = "name:" + value;

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        RadioButton carButton = (RadioButton) findViewById(R.id.radioButton5);
        if(radioButton.getText().equals(carButton.getText())){
            data += "((car:1";
        }
        else {
            data += "((car:0";
        }

        text = (EditText)findViewById(R.id.editText14);
        value = text.getText().toString();
        data += "((seats:" + value;

        text = (EditText)findViewById(R.id.editText15);
        value = text.getText().toString();
        data += "((radius:" + value;

        text = (EditText)findViewById(R.id.editText16);
        value = text.getText().toString().replace(":","");
        if(value.length() < 4){
            value = '0' + value;
        }
        data += "((timegoing:" + value;

        text = (EditText)findViewById(R.id.editText17);
        value = text.getText().toString().replace(":","");
        if(value.length() < 4){
            value = '0' + value;
        }
        data += "((timeleaving:" + value;

        text = (EditText)findViewById(R.id.editText21);
        value = text.getText().toString();
        double longitude = 0;
        double latitude = 0;
        Geocoder coder = new Geocoder(this);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(value, 50);
            for(Address add : adresses){
                longitude = add.getLongitude();
                latitude = add.getLatitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        data += "((lat:" + Double.toString(latitude);
        data += "((lon:" + Double.toString(longitude);

        text = (EditText)findViewById(R.id.editText18);
        value = text.getText().toString();
        data += "((email:" + value;

        text = (EditText)findViewById(R.id.editText19);
        value = text.getText().toString();
        data += "((password:" + value;


        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://104.236.19.68/ride.cgi?register=" + data);
        try {
            HttpResponse response = httpclient.execute(httpget);
            if (response != null) {
                String line = "";
                InputStream inputstream = response.getEntity().getContent();
                line = convertStreamToString(inputstream);
                Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
            }
        } catch (ClientProtocolException e) {
            Toast.makeText(this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Caught IOException", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Caught Exception" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, SecondActivity.class);

        startActivity(intent);
    }

    private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
        }
        return total.toString();
    }

    private boolean checkPassword(){
        EditText text = (EditText)findViewById(R.id.editText19);
        String password = text.getText().toString();
        Log.w("Password", password);

        EditText text2 = (EditText)findViewById(R.id.editText20);
        String rePassword = text2.getText().toString();
        Log.w("Password2", rePassword);

        if(password.equals(rePassword)){
            return true;
        }
        return false;
    }
}
