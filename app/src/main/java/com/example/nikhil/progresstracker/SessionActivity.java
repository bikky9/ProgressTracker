package com.example.nikhil.progresstracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
    }
    public void entersession(View view) {
        EditText editText = (EditText) findViewById(R.id.sessionid);
        String sessionid = editText.getText().toString();
        new detail().execute(sessionid);
    }
    public class detail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String inputLine = null;
            URL url = null;
            try {
                url = new URL("http://192.168.43.157:8000/polls/check_session?session_id=" + strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    total.append(line);
                }
                inputLine = total.toString();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return inputLine;
        }

        @Override
        protected void onPostExecute(String string) {
            if (string.equals("Cool session is active")) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class) ;
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Session Doesn't Exist", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
