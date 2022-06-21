package com.mcup.predi.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mcup.predi.MainActivity;
import com.mcup.predi.R;
import com.mcup.predi.getData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.SystemClock.sleep;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private TextView msg;
    private RequestQueue queue;
    private Button test;
    private Timer timer;
    private TimerTask timerTask;
    private View view;
    private TextView xxx;
    private Button predi1,predi2,predi3;
    private TextView predict, method, intro;
    private MainActivity mainActivity;
    private getData data;
    private getData getData;
    private Intent intent;
    private Integer timerManager;
    private final int Time = 5000;
    private int N=0, predictType = 0;
    final CharSequence[] ML_items = {"隨機森林", "決策樹", "SVM"}, DL_item = {"深度學習1","深度學習2"};

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //mainActivity = (MainActivity)getActivity();
        //data = (getData)getActivity();
        //data.test();
        //String string = mainActivity.dddd();
        //String s = mainActivity.test();
        //String string = getArguments().getString("msg");
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //test = (Button) view.findViewById(R.id.test);
        //test.setOnClickListener(this);
        //msg = root.findViewById(R.id.price);
        predict = root.findViewById(R.id.home_predi);
        xxx = root.findViewById(R.id.price);
        method = root.findViewById(R.id.current_method);
        intro = root.findViewById(R.id.intro);

        predi3 = (Button) root.findViewById(R.id.home_predi_3);
        predi3.setOnClickListener(this);

        predi1 = (Button) root.findViewById(R.id.home_predi_1);
        predi1.setOnClickListener(this);

        predi2 = (Button) root.findViewById(R.id.home_predi_2);
        predi2.setOnClickListener(this);
        getInfo("traditional");

        handler.postDelayed(runnable,Time);


        //mainActivity.tttt1();
        ///getData.test();
        //intent = Intent.getIntent();
        //String aaa = intent.getExtras("M");



        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //Log.v("msg",string);
        //Log.v("msg",s);

        /*
        if (timerManager == 1) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getInfo();
                }
            }, 0, 10000);
        }
*/
        return root;

    }
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,Time);
            N++;
            Log.v("msg","home timer has run "+N+" times");
            if(predictType == 0){
                getInfo("traditional");
            }else if(predictType == 1){
                getInfo("lstm");
            }else if(predictType == 2){
                getInfo("gru");
            }
        }
    };

    /*public void test(View view) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://www.twse.com.tw/exchangeReport/STOCK_DAY_ALL?response=open_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                        Log.v("msg",response);
                    }
                },
                null);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/
    /*private void test1(){
        mainActivity.getChartFragment().setMsg("afjdaghdfuhafuqahrf");
        //Log.v("msg","ccc");
    }*/



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_predi_3:
                getInfo("gru");
                predictType = 3;
                Log.v("msg","clicked GRU");
                break;

            case R.id.home_predi_1:
                Log.v("msg","clicked Traditional");
                onCreateDialog();
                getInfo("traditional");
                predictType = 0;
                intro.setText(R.string.traditional_intro);
                break;

            case R.id.home_predi_2:
                Log.v("msg","clicked LSTM");
                getInfo("lstm");
                predictType = 1;
                intro.setText(R.string.lstm_intro);
                break;


            default:
                break;
        }
    }



    private void getInfo(String predictMethod){
        // Request a string response from the provided URL.
        //Log.v("msg","getInfo");

        String url = "http://192.168.0.12:80/info";

        if(predictMethod == "gru"){
            method.setText("GRU Attention");
            intro.setText(R.string.gru_intro);

        }else if(predictMethod == "traditional"){
            method.setText("傳統");
            intro.setText(R.string.traditional_intro);
        }else {
            method.setText("LSTM Attention");
            intro.setText(R.string.lstm_intro);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.v("msg", "onResponse");
                        parseJSON(response,predictMethod);
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

    private void parseJSON(String json,String predictMethod){
        //Log.v("msg","JSON");
        try {
            JSONObject root = new JSONObject(json);
            String message = root.getString(predictMethod);
            String getprice = root.getString("currentPrice");
            //Log.v("msg",getprice);
            xxx.setText(getprice);
            if(message.equals("0")) {
                predict.setTextColor(getResources().getColor(R.color.green));
                predict.setText("▼ 下跌");
            }else if (message.equals("1")){
                predict.setTextColor(getResources().getColor(R.color.yellow));
                predict.setText("━ 持平");
            }else if (message.equals("2")){
                predict.setTextColor(getResources().getColor(R.color.red));
                predict.setText("▲ 上漲");
            }else{
                predict.setTextColor(getResources().getColor(R.color.red));
                predict.setText("X 錯誤");
            }

        }catch (Exception e){
            Log.v("msg",e.toString());
        }
    }

    public Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("請選擇要使用得傳統預測方法")
                .setItems(ML_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which == 0){
                            predict.setText("▲ 0");
                        }else if(which == 1){
                            predict.setText("▲ 1");
                        }else{
                            predict.setText("▲ 2");
                        }
                    }
                });
        return builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("msg","H Timer Start");
        timerManager = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("msg","H Timer Stop");
        handler.removeCallbacks(runnable);
        timerManager = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("msg","HonStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("msg","HonResume");
        handler.postDelayed(runnable,Time);
    }
}