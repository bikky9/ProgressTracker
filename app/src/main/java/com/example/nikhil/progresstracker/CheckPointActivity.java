package com.example.nikhil.progresstracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.android.volley.Request.Method.POST;
import static java.lang.Thread.sleep;

public class CheckPointActivity extends AppCompatActivity {
    List<String> questions=new ArrayList<String>();
    List<List<String> > checkpoints=new ArrayList<List<String> >();
    List<List<Integer>> status = new ArrayList<List<Integer>>();
    int questionNumber=0;
    int checkpointNumber=0;
    String qurl=null;
    String checkpoint=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point);
        new repo().execute();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public class repo extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... voids) {
            JSONArray object = null;
            try {
                URL url2 = new URL("http://192.168.43.157:8000/polls/checkpoints/?format=json");
                HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                BufferedReader bf2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                StringBuilder total2 = new StringBuilder();
                String line2;
                while ((line2 = bf2.readLine()) != null) {
                    total2.append(line2).append('\n');
                }
                String inputLine2 = total2.toString();
                object = (JSONArray) new JSONTokener(inputLine2).nextValue();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return object;
        }
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = null;
                try {
                    element = (JSONObject) jsonArray.get(i);
                    String qurl = element.getString("question");
                    String checkpoint = element.getString("checkpoint_text");
                    int i1 = 43;
                    String number = "";
                    while (qurl.charAt(i1) != '/') {
                        number += qurl.charAt(i1);
                        i1++;
                    }
                    Integer index = Integer.parseInt(number);
                    if (index > questions.size()) {
                        questions.add(qurl);
                        List<String> fresh = new ArrayList<String>();
                        List<Integer> add = new ArrayList<Integer>();
                        fresh.add(checkpoint);
                        add.add(0);
                        checkpoints.add(fresh);
                        status.add(add);
                    } else {
                        checkpoints.get(index - 1).add(checkpoint);
                        status.get(index - 1).add(0);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            new niru().execute(questions.get(questionNumber));
        }
    }
    public void setCheckpointNumber(boolean x) {
        if(x){
            if(checkpoints.get(questionNumber).size()>checkpointNumber+1) {
                this.checkpointNumber += 1;
            }else if(questionNumber+1!=questions.size()){
                this.checkpointNumber=0;
                questionNumber++;
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "You Have Reached the End", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            if(checkpointNumber==0 && questionNumber!=0){
                questionNumber--;
                this.checkpointNumber=(checkpoints.get(questionNumber).size()-1);
            }else if(checkpointNumber!=0){
                this.checkpointNumber-=1;
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "You Have Reached the Begining", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        new niru().execute(questions.get(questionNumber));
        return;
    }
    public class niru extends AsyncTask<String,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(String... strings) {
            qurl=strings[0];
            JSONObject object=null;
            try {
                URL url = new URL(qurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    total.append(line).append('\n');
                }
                String inputLine = total.toString();
                object = (JSONObject) new JSONTokener(inputLine).nextValue();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return object;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            String question_text = null;
            try {
                question_text = jsonObject.getString("question_text");
                TextView textView = (TextView) findViewById(R.id.question);
                TextView textView1 = (TextView) findViewById(R.id.checkpoint);
                RadioButton radioButton=(RadioButton)findViewById(R.id.yes);
                textView.setText(question_text);
                textView1.setText(checkpoints.get(questionNumber).get(checkpointNumber));
                RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
                radioGroup.clearCheck();
                boolean ischecked=(status.get(questionNumber).get(checkpointNumber)==1);
                if(ischecked){
                    radioButton.toggle();
                }
                radioButton.isClickable();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void next(View view){
        RadioButton radioButton = (RadioButton)findViewById(R.id.yes);
        if(1==status.get(questionNumber).get(checkpointNumber)){
            if(!radioButton.isChecked()){
                status.get(questionNumber).set(checkpointNumber,0);
                new decrease().execute(questionNumber+1,checkpointNumber);
            }
        }else{
            if(radioButton.isChecked()){
                status.get(questionNumber).set(checkpointNumber,1);
                new increase().execute(questionNumber+1,checkpointNumber);
            }
        }
        setCheckpointNumber(true);
    }
    public void previous(View view){
        RadioButton radioButton = (RadioButton)findViewById(R.id.yes);
        if(1==status.get(questionNumber).get(checkpointNumber)){
            if(!radioButton.isChecked()){
                status.get(questionNumber).set(checkpointNumber,0);
                new decrease().execute(questionNumber+1,checkpointNumber);
            }
        }else{
            if(radioButton.isChecked()){
                status.get(questionNumber).set(checkpointNumber,1);
                new increase().execute(questionNumber+1,checkpointNumber);
            }
        }
        setCheckpointNumber(false);
        if(radioButton.isChecked()){

        }
    }
    public void unCheckAll(View view){
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.clearCheck();
    }
    public class increase extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... strings) {
            String inputLine=null;
            try {
                URL url = new URL("http://192.168.43.157:8000/polls/vote_increase?question_id="+strings[0]+"&checkpoint_number="+strings[1]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    total.append(line);
                }
                inputLine = total.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputLine;
        }
        @Override
        protected void onPostExecute(String string) {
            if (Boolean.parseBoolean(string)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Updated your Response", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error Updating your Response", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    public class decrease extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... strings) {
            String inputLine=null;
            try {
                URL url = new URL("http://192.168.43.157:8000/polls/vote_decrease?question_id="+strings[0]+"&checkpoint_number="+strings[1]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    total.append(line);
                }
                inputLine = total.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputLine;
        }
        @Override
        protected void onPostExecute(String string) {
            if (Boolean.parseBoolean(string)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Updated your Response", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Error Updating your Response", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
