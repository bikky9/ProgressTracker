package com.example.nikhil.progresstracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText stu = (EditText) findViewById(R.id.user) ;
        final EditText pass = (EditText) findViewById(R.id.password) ;
     }

     public void checkUser(View view ) {
         final EditText stu = (EditText) findViewById(R.id.user) ;
         final EditText pass = (EditText) findViewById(R.id.password) ;
         String student_id = stu.getText().toString() ;
         String password = pass.getText().toString() ;
         new detail().execute(student_id,password);
     }
    public class detail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String inputLine = null;
            URL url = null;
            try {
                url = new URL("http://192.168.43.157:8000/polls/check_user/?student_id=" + strings[0] + "&student_password=" + strings[1]);
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
            if (Boolean.parseBoolean(string)) {
                Intent intent = new Intent(getApplicationContext(),CheckPointActivity.class) ;
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
