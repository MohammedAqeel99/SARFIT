package com.mohammedaqeel.sarfit;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Fonts.applyRecursively(this, findViewById(android.R.id.content));

        String exerciseName = getIntent().getStringExtra("exerciseName");
        if (exerciseName == null) exerciseName = "";

        TextView tvTitle = findViewById(R.id.tvVideoTitle);
        tvTitle.setText(exerciseName);

        TextView tvClose = findViewById(R.id.tvVideoClose);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });

        WebView webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        String videoId = VideoLibrary.idFor(exerciseName);
        String url;
        if (videoId != null) {
            url = "https://www.youtube.com/embed/" + videoId + "?autoplay=1&playsinline=1";
        } else {
            String query;
            try {
                query = URLEncoder.encode(exerciseName + " proper form exercise", "UTF-8");
            } catch (Exception e) {
                query = exerciseName.replace(" ", "+");
            }
            url = "https://www.youtube.com/results?search_query=" + query;
        }
        webView.loadUrl(url);
    }
}
