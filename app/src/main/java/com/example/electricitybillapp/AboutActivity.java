package com.example.electricitybillapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // 🔹 import untuk toolbar

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        Toolbar toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ← Show back arrow
        getSupportActionBar().setTitle("About This App");


        toolbar.setNavigationOnClickListener(v -> finish());


        Button buttonGitHub = findViewById(R.id.buttonGitHub);
        buttonGitHub.setOnClickListener(v -> {
            String url = "https://github.com/Farishakamal/ElectricityBillApp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
