package com.example.jacobjackson.helloworld;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.lang.reflect.Array;


public class SecondActivity extends AppCompatActivity {
    private static final int[] BUTTON_IDS = {
            R.id.button6,
            R.id.button2,
            R.id.button8,
            R.id.button7,
            R.id.button11,
            R.id.button10,
            R.id.button9,
            R.id.button3,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://104.236.19.68/ride.cgi?getmatches=1");
        try {
            HttpResponse response = httpclient.execute(httpget);
            if (response != null) {
                String line = "";
                InputStream inputstream = response.getEntity().getContent();
                line = convertStreamToString(inputstream);
                String[] output = line.split("<br>");
                /*for(int o = 2; o < output.length; o++){
                    output[o].split(" ");
                }*/
                Button[] matches = new Button[output.length-2];
                for(int i = 0; i < matches.length; i++){
                    Button button = (Button)findViewById(BUTTON_IDS[i]);
                    button.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
                for(String s : output){
                    Log.w("Output line: ", s);
                }

            } else {
                Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
            }
        } catch (ClientProtocolException e) {
            Toast.makeText(this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
            Log.w("Output: ", e.toString());
        } catch (IOException e) {
            Toast.makeText(this, "Caught IOException", Toast.LENGTH_SHORT).show();
            Log.w("Output: ", e.toString());
        } catch (Exception e) {
            Toast.makeText(this, "Caught Exception" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.w("Output: ", e.toString());
        }
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


}
