package com.example.temphumid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class AboutDevelopers extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developers);

        webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/developers.html");

    }
}