package com.mcup.predi.ui.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mcup.predi.MainActivity;
import com.mcup.predi.R;
import com.mcup.predi.WebViewActivity;
import com.mcup.predi.ui.Web;
import com.mcup.predi.webFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class NewsFragment extends Fragment implements View.OnClickListener{

    private NewsViewModel newsViewModel;
    private ListView listView;
    private String[] from = {"title","datetime","url","imageUrl"};
    private int [] to = {R.id.news_title,R.id.news_datetime,R.id.news_image};
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private SimpleAdapter adapter;
    private RequestQueue queue;
    private Button taiwan,international,TSMC;
    private WebView webView;
    public Fragment fragment = null;
    public MainActivity mainActivity;
    private ImageView imageView;
    private TextView selected;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        //final TextView textView = root.findViewById(R.id.text_news);
        /*newsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        taiwan = (Button) root.findViewById(R.id.taiwan);
        taiwan.setOnClickListener(this);

        international =(Button) root.findViewById(R.id.international);
        international.setOnClickListener(this);

        imageView = (ImageView) root.findViewById(R.id.news_image);

        selected = root.findViewById(R.id.news_selected);

        //webView = (WebView) root.findViewById(R.id.navigation_web);
        listView = root.findViewById(R.id.list);
        initListView();
        getInfo("1");///Econamic News
        selected.setText("經濟新聞");

        return root;
    }

    private void initListView() {
        //HashMap<String,String> context = new HashMap<>();
        //context.put(from[0],"重點新聞!! 台股直擊兩萬點  創下歷史新高");
        //context.put(from[1],"台灣新聞網");
        //data.add(context);

        adapter = new SimpleAdapter(getContext(),data,R.layout.list_item,from,to);
        listView.setAdapter(adapter);
        for(int i = 0;i<19;i++){
            //Picasso.get().load(data.get(i).get(from[3])).into(imageView);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.v("msg","pos = "+position+ " "+ data.get(position).get(from[2])+" "+data.get(position).get(from[3]));
                String newsUrl = data.get(position).get(from[2]);
                Uri uri = Uri.parse(newsUrl); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
    }

    private void openWebView(String url){
        Intent intent = new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }







    /*public void onClick(View v){
        switch (v.getId()){
            case R.id.taiwan:
                getInfo();
                break;
            case R.id.international:
                getInfo();
                break;
        }
    }*/

    private void getInfo(String category){
        // Request a string response from the provided URL.
        //Log.v("msg","getInfo");

        String url = "http://192.168.0.12/time/"+category;

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
            JSONArray articles = root.getJSONArray("articles");

            for(int i = 0;i<articles.length();i++){
                JSONObject object = articles.getJSONObject(i);
                String author = object.getString("author");
                String publishedAt = object.getString("publishedAt");
                String title = object.getString("title");
                String url = object.getString("url");
                String imageUrl = object.getString("urlToImage");
                //Log.v("msg",title);

                String datetime = datetimeFormatter(publishedAt);
                HashMap<String,String> context = new HashMap<>();
                context.put(from[0],title);
                context.put(from[1],datetime);
                context.put(from[2],url);
                context.put(from[3],imageUrl);
                //Picasso.get().load(from[3]).into(imageView);
                data.add(context);
                adapter.notifyDataSetChanged();

            }

        }catch (Exception e){
            Log.v("msg","JSON "+e.toString());
        }
    }

    private String datetimeFormatter(String datetime) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        SimpleDateFormat Cdf = new SimpleDateFormat("yyyy/MM/dd EEE HH:mm", Locale.TRADITIONAL_CHINESE);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse(datetime);
        df.setTimeZone(TimeZone.getDefault());
        String fdD = Cdf.format(date);

        return fdD;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.taiwan:///經濟
                delData();
                getInfo("1");
                selected.setText("經濟新聞");
                break;
            case R.id.international://一般
                delData();
                getInfo("2");
                selected.setText("重點新聞");
                break;
        }
    }

    private void delData(){
        while(data.size()>0){
            data.remove();
        }
        adapter.notifyDataSetChanged();
    }
}


