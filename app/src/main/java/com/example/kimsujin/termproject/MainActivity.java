package com.example.kimsujin.termproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToPick1Activity(View v) {
        Intent intent = new Intent(getApplicationContext(), Pick1Activity.class);
        startActivity(intent);
    }

    public void goToPick2Activity(View v) {
        Intent intent = new Intent(getApplicationContext(), Pick2Activity.class);
        startActivity(intent);
    }

    public void goToRankingActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
        startActivity(intent);
    }
}
