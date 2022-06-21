package com.mcup.predi.ui.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mcup.predi.MainActivity;
import com.mcup.predi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ChartFragment extends Fragment implements View.OnClickListener{

    private ChartViewModel chartViewModel;
    private TextView high, low, prePrice, amplitude, volume, value, currPrice, change, dayOpen, limitUp, limitDown, chartStart, chartEnd;
    private RequestQueue queue;
    private Timer timer;
    private TimerTask timerTask;
    private LineChart lineChart;
    private float highestClose, lowestClose, chartFirst, chartLast, chartColourManager, chartColourManagerDay;
    private int dataDays, chartType, N=0;
    private Button hr,dy,wk,mo,yr,tst;
    private String periodState;
    private MainActivity mainActivity;
    private String stringxxx;
    //private Integer i = 1;
    private Integer timerManager;
    private Integer xAxisLabelManager;
    private float preCloseChart;
    private String[] timeStamp;
    private final int Time = 5000;
    private String getChartStart, getChartEnd;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*chartViewModel =
                new ViewModelProvider(this).get(ChartViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //final TextView textView = root.findViewById(R.id.text_news);
        /*chartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        high = root.findViewById(R.id.chart_high);
        low = root.findViewById(R.id.chart_low);
        prePrice = root.findViewById(R.id.chart_pre_price);
        amplitude = root.findViewById(R.id.chart_amplitude);
        volume = root.findViewById(R.id.chart_volume);
        //value = root.findViewById(R.id.chart_value);
        dayOpen = root.findViewById(R.id.chart_open);
        currPrice = root.findViewById(R.id.chart_current_price);
        change = root.findViewById(R.id.chart_change);
        lineChart = root.findViewById(R.id.lineChart);
        limitUp = root.findViewById(R.id.chart_limitUp);
        limitDown = root.findViewById(R.id.chart_limitDown);
        chartStart = root.findViewById(R.id.chart_start);
        chartEnd = root.findViewById(R.id.chart_end);
        //hr = (Button)root.findViewById(R.id.chart_hour); hr.setOnClickListener(this);
        dy = (Button)root.findViewById(R.id.chart_day); dy.setOnClickListener(this);
        wk = (Button)root.findViewById(R.id.chart_week); wk.setOnClickListener(this);
        mo = (Button)root.findViewById(R.id.chart_month); mo.setOnClickListener(this);
        yr = (Button)root.findViewById(R.id.chart_year); yr.setOnClickListener(this);
        //tst = (Button)root .findViewById(R.id.cbt); tst.setOnClickListener(this);
        //value.setText(stringxxx);
        //mainActivity.getInfo();


        highestClose = 0;
        lowestClose = 9999;
        getchartInfo("day");
        getInfo();
        handler.postDelayed(runnable,Time);
        //volleyPost();


        /*
        if(timerManager == 1) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getInfo();
                }
            }, 0, 10000);
        }


        //chart data
        //chartData();
                /*
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 596f));
        values.add(new Entry(1, 591f));
        values.add(new Entry(2, 590f));
        values.add(new Entry(3, 586f));
        values.add(new Entry(4, 581f));
        values.add(new Entry(5, 584f));
*/
        //繪製圖表



        return root;
    }

    /*public void setMsg(String change){
        stringxxx = change;
        Log.v("msg","msg is fuck fuck "+ stringxxx);
    }*/

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,Time);
            N++;
            Log.v("msg",""+N);
            getInfo();
            if(chartType == 0){
                getchartInfo("day");
            }
        }
    };




    public void onClick(View v){
        switch (v.getId()){
            case R.id.chart_day:
                highestClose = 0;
                lowestClose = 9999;
                xAxisLabelManager = 0;
                chartType = 0;
                Log.v("msg","DAY");
                getchartInfo("day");
                break;

            case R.id.chart_week:
                highestClose = 0;
                lowestClose = 9999;
                xAxisLabelManager = 1;
                chartType = 1;
                Log.v("msg","WEEK");
                getchartInfo("week");
                break;

            case R.id.chart_month:
                highestClose = 0;
                lowestClose = 9999;
                xAxisLabelManager = 2;
                chartType = 1;
                Log.v("msg","MONTH");
                getchartInfo("month");
                break;

            case R.id.chart_year:
                highestClose = 0;
                lowestClose = 9999;
                xAxisLabelManager = 3;
                chartType = 1;
                Log.v("msg","YEAR");
                getchartInfo("year");
                break;


        }
    }

    private void getchartInfo(String periodState){
        // Request a string response from the provided URL.
        //Log.v("msg","getInfo");

        String url = "http://192.168.0.12/history/" + periodState;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.v("msg", "onResponse");
                        parseJSONforChart(response);
                        //Log.v("msg", response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("msg","GetChartInfo ERROR");
                Log.v("msg",error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseJSONforChart(String json){
        //Log.v("msg","JSON");
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> baseline = new ArrayList<>();
        try {
            JSONArray root = new JSONArray(json);

            dataDays = 99999;
            int n = 0;

            for(int i = 0;i<dataDays;i++){
                JSONObject object = root.getJSONObject(i);
                Integer len = object.getInt("l");
                dataDays = len;
                String closeString = object.getString("Close");
                long DateNtime = object.getLong("t");

                if(closeString == "null" ){

                }else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(DateNtime);
                    if(chartType==0){
                        String datetime = DateFormat.format("HH:mm",calendar).toString();
                        if(n==0){getChartStart = datetime;n=1;}
                        getChartEnd = datetime;
                    }else {
                        String datetime = DateFormat.format("MM/dd",calendar).toString();
                        if(n==0){getChartStart = datetime;n=1;}
                        getChartEnd = datetime;
                    }
                    double Close = object.getDouble("Close");
                    values.add(new Entry(i, (float) Close));
                    if(i==0){ chartFirst = (float) Close; }
                    if(i==dataDays-1){
                        chartLast = (float) Close;
                        chartColourManagerDay = chartLast - preCloseChart;
                        chartColourManager = chartLast - chartFirst;
                    }

                    if (Close > highestClose) {
                        highestClose = (int) Close;
                    }
                    if (Close < lowestClose) {
                        lowestClose = (int) Close;
                    }
                    baseline.add(new Entry(i,(float) chartFirst));
                }
            }
            initDataSet(values,baseline);
            lineChart.invalidate();
            chartStart.setText(getChartStart);
            chartEnd.setText(getChartEnd);
        }catch (Exception e){
            Log.v("msg","JSON "+e.toString());
        }
    }

    private void getInfo(){
        // Request a string response from the provided URL.
        //Log.v("msg","getInfo");

        String url = "http://192.168.0.12/info";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.v("msg", "onResponse");
                        parseJSON(response);
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

    private void parseJSON(String json){
        try {
            JSONObject root = new JSONObject(json);

            String currentPrice = root.getString("currentPrice");
            String dayHigh = root.getString("dayHigh");
            String dayLow = root.getString("dayLow");
            String previousClose = root.getString("previousClose");
            double preCloseForChart = root.getDouble("previousClose");
            String open = root.getString("open");
            float changePF = (float) root.getDouble("changePercent");
            String changePS = String.format("%.2f",changePF);
            float amplitudeF = (float) root.getDouble("amplitude");
            String amplitudeS = String.format("%.2f",amplitudeF);
            String getLimitUp = root.getString("limitUp");
            String getLimitDown = root.getString("limitDown");
            int changeI = root.getInt("change");
            int dayVolume = root.getInt("volume");
            if(dayVolume > 10000){
                int temp = dayVolume;
                dayVolume /= 10000;
                if(temp % 10000 > 5000){
                    dayVolume += 1;
                }
                volume.setText(dayVolume + "萬");
            }

            if(changeI > 0){
                change.setTextColor(getResources().getColor(R.color.red));
                currPrice.setTextColor(getResources().getColor(R.color.red));
                change.setText("+" + changeI + "(+" + changePS + "%)");
                currPrice.setText(currentPrice);
            }else if(changeI == 0){
                change.setTextColor(getResources().getColor(R.color.yellow));
                currPrice.setTextColor(getResources().getColor(R.color.yellow));
                change.setText("+" + changeI + "(+" + changePS + "%)");
                currPrice.setText(currentPrice);
            }else{
                change.setTextColor(getResources().getColor(R.color.green));
                currPrice.setTextColor(getResources().getColor(R.color.green));
                change.setText(changeI + "(" + changePS + "%)");
                currPrice.setText(currentPrice);
            }


            high.setText(dayHigh);
            low.setText(dayLow);
            prePrice.setText(previousClose);
            preCloseChart = (float)preCloseForChart;
            amplitude.setText(amplitudeS + "%");
            dayOpen.setText(open);
            limitUp.setText(getLimitUp);
            limitDown.setText(getLimitDown);

            //volume.setText(dayVolume);
            //Log.v("msg", "Current Price: " + currentPrice +" High: " + dayHigh + " Low: " +
            //        dayLow + " volume: " + dayVolume + " change: " + changeI +" "+ changePS + "% " + amplitudeS + "%");
        }catch (Exception e){
            Log.v("msg","JSON "+e.toString());
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy/MM/dd HH:mm:ss zzzz", cal).toString();
        return date;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(timerTask != null){
            timerTask.cancel();
            //cancel timer task and assign null
        }
    }


    private void chartData(int x, double Close){



    }



    private void initDataSet(final ArrayList<Entry> values, final ArrayList<Entry> baseline) {

        final LineDataSet set;
        // greenLine
        set = new LineDataSet(values, "TSMC Price");
        set.setMode(LineDataSet.Mode.LINEAR);//類型為折線
        if(chartType == 0){
            if(chartColourManagerDay > 0){
                set.setColor(getResources().getColor(R.color.red));//線的顏色
            }else if(chartColourManagerDay < 0){
                set.setColor(getResources().getColor(R.color.green));//線的顏色
            } else { set.setColor(getResources().getColor(R.color.yellow)); }//線的顏色
        }else {
            if (chartColourManager > 0) {
                set.setColor(getResources().getColor(R.color.red));//線的顏色
            } else if (chartColourManager < 0) {
                set.setColor(getResources().getColor(R.color.green));//線的顏色
            } else {
                set.setColor(getResources().getColor(R.color.yellow));
            }//線的顏色
        }

        set.setLineWidth(1f);//線寬
        set.setDrawCircles(false); //不顯示相應座標點的小圓圈(預設顯示)
        set.setDrawValues(false);//顯示座標點對應Y軸的數字(預設顯示)
        set.setDrawFilled(false);//填充底下
        set.setValueTextSize(15);
        //set.setLabel("123");// 啟用/禁用與圖表的所有可能的觸控互動。
        //set.DragEnabled(false);
        initX();
        initY();


        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        Description description = lineChart.getDescription();
        description.setEnabled(false);
//理解爲多條線的集合
        LineData data = new LineData(set);
        lineChart.setData(data);//一定要放在最後
        lineChart.invalidate();//繪製圖表
    }

    private void initX() {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Color.GRAY);//X軸標籤顏色
        xAxis.setTextSize(12);//X軸標籤大小
        //xAxis.setGridLineWidth(1);// 设置该轴网格线的宽度。
        xAxis.setDrawGridLines(true);//不顯示每個座標點對應X軸的線 (預設顯示)
        xAxis.setDrawLabels(false);
        xAxis.setAxisMinimum(0); //X axis Min
        xAxis.setAxisMaximum(dataDays); //X axis Max (depend on how many days in data) here have problem
        if(xAxisLabelManager == 0){
            //xAxis.setLabelCount(5);
        }
        xAxis.setLabelCount(6);
        //X軸標籤個數
        //xAxis.setSpaceMin(0.1f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.1f);//折線終點距離右側Y軸距離
        //xAxis.setLabelsToSkip(4);
        //xAxis.resetLabelsToSkip();



        //設定所需特定標籤資料
        //final ArrayList<String> xAxisLabel = new ArrayList<>();
        //xAxisLabel.add("09");
        //xAxisLabel.add("10");
        //xAxisLabel.add("11");
        //xAxisLabel.add("12");
        //xAxisLabel.add("13");

        //String[] xValue = new String[]{"8/9", "8/10", "8/11", "8/12", "8/13", "8/16"};
        //List<String> xList = new ArrayList<>();
        //for (int i = 0; i < xValue.length; i++) {
        //   xList.add(xValue[i]);
        //}
        List<String> label = new ArrayList<String>();
        label.add("09");
        label.add("10");
        label.add("11");
        label.add("12");
        label.add("13");
        /**
         * 格式化軸標籤二種方式：
         * 1、用圖表庫已寫好的類_如下X 軸使用
         * 2、自己實現接口_下一步驟中Y 軸使用
         * */
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));
    }

    public void initY() {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        //leftAxis.setLabelCount(4);//Y軸標籤個數
        leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小

        if(highestClose < preCloseChart){ highestClose = preCloseChart;}
        if(lowestClose > preCloseChart){lowestClose = preCloseChart;}
        leftAxis.setAxisMinimum((float) (lowestClose / 1.005));//Y軸標籤最小值
        leftAxis.setAxisMaximum((float) (highestClose * 1.005));//Y軸標籤最大值

        LimitLine yLimitLine = new LimitLine(preCloseChart,"昨收 "+preCloseChart);
        if(xAxisLabelManager == 0){
            yLimitLine.setLineColor(Color.RED);
            yLimitLine.setTextSize(20f);
            yLimitLine.setTextColor(Color.RED);
            yLimitLine.enableDashedLine(10f,20f,0f);
            leftAxis.addLimitLine(yLimitLine);
        }
        else{
            leftAxis.removeAllLimitLines();
            Log.v("msg","Remove LimitLine");
        }
        Log.v("msg",""+xAxisLabelManager);



        leftAxis.setValueFormatter(new MyYAxisValueFormatter());

    }

    class MyYAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###.##");//Y軸數值格式及小數點位數
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }
    }

    class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyXAxisValueFormatter() {
            mFormat = new DecimalFormat("##");//X軸數值格式及小數點位數
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }
    }
/*
    public void volleyPost(){
        String purl =  "http://192.168.0.12/historytest";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject post = new JSONObject();
        try{
            post.put("period","1h");
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, purl, post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);
                Log.v("msg", String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }*/

    @Override
    public void onStart() {
        super.onStart();
        Log.v("msg","ConStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        Log.v("msg","chart timer stop");
        Log.v("msg","ConPause");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.v("msg","ConAttach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("msg","ConResume");
        Log.v("msg","chart timer start");
    }
}
