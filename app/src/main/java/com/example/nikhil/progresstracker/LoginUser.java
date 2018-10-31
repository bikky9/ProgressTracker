package com.example.nikhil.progresstracker;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginUser extends Request<String> {

    private static final String request_URL ="http://192.168.43.157:8000/polls/check_user" ;
    private Response.Listener<String> mListener;

    public LoginUser(String student_id, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST,getURL(student_id,password),errorListener);
        mListener=listener;
    }

    private static String getURL(String student_id, String password) {
        return request_URL + "student_id=" + student_id + "&student_password=" + password;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String jsonString = new String(response.data);
        return Response.success(jsonString,getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

}
