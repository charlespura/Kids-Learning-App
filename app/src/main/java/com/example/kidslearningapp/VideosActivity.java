package com.example.kidslearningapp;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class VideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript for YouTube

        webView.setWebViewClient(new WebViewClient());

        // Embed multiple YouTube videos without titles
        String htmlData = "<html><body style='text-align:center; font-family:sans-serif;'>" +

                "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/KFLDr8H4Em8\" frameborder=\"0\" allowfullscreen></iframe><br><br>" +

                "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/ccEpTTZW34g\" frameborder=\"0\" allowfullscreen></iframe><br><br>" +

                "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/noCiE_1YRJo\" frameborder=\"0\" allowfullscreen></iframe><br><br>" +


                "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/OMRASCzuqH8\" frameborder=\"0\" allowfullscreen></iframe><br><br>" +

                "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/6FVLPV4HmBo\" frameborder=\"0\" allowfullscreen></iframe><br><br>" +

                "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/K_Aq4H03Nm4\" frameborder=\"0\" allowfullscreen></iframe>" +

                "</body></html>";

        webView.loadData(htmlData, "text/html", "utf-8");
    }
}
