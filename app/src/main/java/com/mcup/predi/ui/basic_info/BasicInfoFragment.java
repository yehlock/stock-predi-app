package com.mcup.predi.ui.basic_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mcup.predi.R;
import com.mcup.predi.ui.home.HomeFragment;
import com.mcup.predi.ui.news.NewsViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class BasicInfoFragment extends Fragment implements View.OnClickListener{

    //private BasicInfoViewModel basicInfoViewModel;
    //private HomeFragment homeFragment;
    ///private BasicInfoViewModel2 basicInfoViewModel2;
    private String [] from = {"title","info"};
    private int [] to ={R.id.basic_info_list_title,R.id.basic_info_list_info};
    private String [] getFrom,getFromName;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private SimpleAdapter adapter;
    private RequestQueue queue;
    private Button basicInfo,dividendHistory, exDividend, calender;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //basicInfoViewModel =
          //      new ViewModelProvider(this).get(BasicInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_basic_info, container, false);
        /*final TextView textView = root.findViewById(R.id.title);
        ///final TextView textView1 = root.findViewById(R.id.test);
        basicInfoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        //homeFragment.setMsg("AAA");

        getFromName = new String[] {"全稱","英文名","英文簡稱","網站","總機電話","傳真",
                "電子信箱","地址","產業別","主要業務","公司簡介","董事長","總經理","成立日期","上市日期","發言人","代理發言人"};

        getFrom = new String[] {"companyName","companyEnglishFullName","companyEnglishShortName","website","telephone","fax",
                "email","address","category","mainBusiness","companyIntro","chairman","generalManager","dateOfIncorporation",
                "dateOfListing","spokesman","deputySpokesperson"};
        //"股本","已發行股數","市值","董監持股比例",

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //basicInfo = (Button)root.findViewById(R.id.basic_info_basic);
        //basicInfo.setOnClickListener(this);
        //dividendHistory = (Button)root.findViewById(R.id.basic_info_dividend_history);
        //dividendHistory.setOnClickListener(this);
        //exDividend = (Button)root.findViewById(R.id.basic_info_ex_dividend);
        //exDividend.setOnClickListener(this);
        //calender = (Button)root.findViewById(R.id.basic_info_calendar);
        //calender.setOnClickListener(this);

        listView = root.findViewById(R.id.basic_info_list);
        initListView();
        getInfo();

        return root;
    }

    private void initListView() {
        //HashMap<String,String> context = new HashMap<>();
        //context.put(from[0],"重點新聞!! 台股直擊兩萬點  創下歷史新高");
        //context.put(from[1],"台灣新聞網");
        //data.add(context);

        adapter = new SimpleAdapter(getContext(),data,R.layout.basic_info_list_item,from,to);
        listView.setAdapter(adapter);
    }

    private void getInfo(){
        // Request a string response from the provided URL.
        //Log.v("msg","getInfo");

        String url = "http://192.168.0.12/basic";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        //Log.v("msg", "onResponse");
                        newsParseJSON(response);
                        //Log.v("msg", response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("msg","GetInfo ERROR");
                Log.v("msg",error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void newsParseJSON(String json){
        //Log.v("msg","JSON");
        //ArrayList<Entry> values = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            for(int i=0;i<getFrom.length;i++) {
                String info = null;
                info = root.getString(getFrom[i]);
                HashMap<String, String> context = new HashMap<>();
                context.put(from[0], getFromName[i]);
                context.put(from[1], info);
                data.add(context);
                adapter.notifyDataSetChanged();
                Log.v("msg",""+info);
            }
            //context.put(from[0],"公司代碼");
            //context.put(from[1],companyCode);
            //data.add(context);
            //adapter.notifyDataSetChanged();


        }catch (Exception e){
            Log.v("msg","JSON "+e.toString());
        }
    }

    @Override
    public void onClick(View v) {

    }
}
