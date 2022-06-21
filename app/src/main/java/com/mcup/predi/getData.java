package com.mcup.predi;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

public class getData extends AppCompatActivity {

    private RequestQueue queue;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
    }

    public void getInfo(){
        // Request a string response from the provided URL.
        Log.v("msg","getInfo");

        String url = "http://192.168.0.12:80/info";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("msg", "onResponse");
                        parseJSON(response);

                        //Log.v("msg", response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("msg","ERROR");
                Log.v("msg",error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseJSON(String json){
        Log.v("msg","JSON");
        String[] result = new String[9];
        try {
            JSONObject root = new JSONObject(json);
            result[0] = root.getString("predictTest");
            result[1] = root.getString("currentPrice");
            result[2] = root.getString("dayHigh");
            result[3] = root.getString("dayLow");
            result[4] = root.getString("previousClose");
            result[5] = root.getString("changePercent");
            result[6] = root.getString("amplitude");
            int changeInt = root.getInt("change");
            if(changeInt > 0){
                result[7] = "1" ;
                result[8] = "+" + getString(changeInt);
            }else if(changeInt == 0){
                result[7] = "0";
                result[8] = getString(changeInt);
            }else{
                result[7] = "-1";
                result[8] = getString(changeInt);
            }

            int dayVolume = root.getInt("volume");
            if(dayVolume>=10000){
                int temp = dayVolume;
                dayVolume/= 10000;
                if (temp % 10000 > 5000){
                    dayVolume +=1;
                }
                result[9] = getString(dayVolume) + "萬";
            }else {
                result[9] = getString(dayVolume);
            }



        }catch (Exception e){
            Log.v("msg",e.toString());
        }
    }

    public int test(){
        int a=1;
        a += a;
        Log.v("msg","GD");
        return a;
    }
}
