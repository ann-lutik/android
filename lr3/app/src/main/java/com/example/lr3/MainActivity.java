package com.example.lr3;

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
    public void openCategory(View view) {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
    public void openRecord(View view) {
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
    public void openStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
    public void openChart(View view) {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }
    public void openPhoto(View view) {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }
}
