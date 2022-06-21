package com.mcup.predi;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mcup.predi.ui.Web;
import com.mcup.predi.ui.chart.ChartFragment;
import com.mcup.predi.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public RequestQueue queue;
    private HomeFragment homeFragment;
    private ChartFragment chartFragment;
    private FragmentManager fragmentManager;
    private webFragment webFragment;
    //private TimerTask timerTask;
    //private Timer timer;
    ///Home Page
    private TextView homePrice;
    //Chart Page
    private TextView chartCurrentPrice,chartDayHigh,chartDayLow,chartPreClose,chartChange,chartAmplitude;
    private TextView chartVolume,chartValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_chart, R.id.navigation_news, R.id.navigation_basic_info,R.id.navigation_web)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        chartFragment = new ChartFragment();
        webFragment = new webFragment();
        queue = Volley.newRequestQueue(this);
        //test();
        //getInfo();


        homePrice = findViewById(R.id.price);
        chartCurrentPrice = findViewById(R.id.chart_current_price);
        chartDayHigh = findViewById(R.id.chart_high);
        chartDayLow = findViewById(R.id.chart_low);
        chartPreClose = findViewById(R.id.chart_pre_price);
        chartChange = findViewById(R.id.chart_change);
        chartAmplitude = findViewById(R.id.chart_amplitude);

        networkCheck();

        //price.setText("999");


        //msg = findViewById(R.id.price);

        /*
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getInfo();
            }
        },0,10000);*/
    }

    public void setHomeFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment,homeFragment);
        transaction.commit();
    }

    public HomeFragment getHomeFragment(){return homeFragment;}

    public void setChartFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment,chartFragment);
        transaction.commit();
    }
    public ChartFragment getChartFragment(){return chartFragment;}

    public void setWebFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment,webFragment);
        transaction.commit();
    }
    public webFragment getWebFragment(){return webFragment;}

    private void networkCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Log.v("msg","connected");
        } else {
            //Log.v("msg","No connection");
            dialog();
        }
    }
    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_network_title);
        //builder.setIcon(R.mipmap.ic_launcher_round); //this is for small icon infront the title. Maybe I can change it to a warrning icon
        builder.setMessage(R.string.dialog_network_message);

        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        builder.create().show();
    }
}