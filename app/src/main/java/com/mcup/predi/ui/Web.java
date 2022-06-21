package com.mcup.predi.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.mcup.predi.R;

import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

public class Web extends Fragment {
    private WebView webView;
    private URL url;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.news_web,container,false);

        //webView = (WebView) root.findViewById(R.id.navigation_web);

        textView = (TextView) root.findViewById(R.id.tttt);

        return root;
    }

    public void setMsg(String s){
        textView.setText(s);
    }
}
