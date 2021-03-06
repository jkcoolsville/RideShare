package com.example.jacobjackson.helloworld;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerButton = (Button) findViewById(R.id.button5);

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                goToRegister();
            }
        });

        Button loginButton = (Button) findViewById(R.id.button4);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                EditText text = (EditText)findViewById(R.id.editText39);
                String value = text.getText().toString();

                String data = "email:" + value;

                text = (EditText)findViewById(R.id.editText40);
                value = text.getText().toString();

                data += "((password:" + value;

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://104.236.19.68/ride.cgi?login=" + data);
                try {
                    HttpResponse response = httpclient.execute(httpget);
                    if (response != null) {
                        String line = "";
                        InputStream inputstream = response.getEntity().getContent();
                        line = convertStreamToString(inputstream);
                        Toast.makeText(MainActivity.this, line, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable to complete your request", Toast.LENGTH_LONG).show();
                    }
                } catch (ClientProtocolException e) {
                    Toast.makeText(MainActivity.this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Caught IOException", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Caught Exception" + e.toString(), Toast.LENGTH_SHORT).show();
                }

                goToSecondActivity();
            }
        });

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

    private void goToRegister(){
        Intent intent = new Intent(this, Register.class);

        startActivity(intent);
    }

    private void goToSecondActivity(){
        Intent intent = new Intent(this, SecondActivity.class);

        startActivity(intent);
    }
}