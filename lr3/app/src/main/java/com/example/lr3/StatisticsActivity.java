package com.example.lr3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    List<String> myList;
    List<String> myList1;
    private DatabaseHelper DbHelper;
    private TextView mInfoTextView;
    private TextView mInfoTextView1;
    private TextView mInfoTextView2;
    List<String> MyList;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        DbHelper = new DatabaseHelper(this);
        mInfoTextView = (TextView) findViewById(R.id.my_text);
        mInfoTextView1 = (TextView) findViewById(R.id.my_text1);
        mInfoTextView2 = (TextView) findViewById(R.id.textView4);

        myList = DbHelper.getMostFrequentCategory();
        String list = "";
        for (int i = 0; i < myList.size(); i++) {
            list = list + myList.get(i) + " \n";
        }
        mInfoTextView.setText("" + list);

        myList1 =DbHelper.getMostLargestSumCategory();
        //myList1 =DbHelper.getMostLargestSumByChart();
        list = "";
        for (int i = 0; i < myList1.size(); i++) {
            list = list + myList1.get(i) + " \n";
        }
        mInfoTextView1.setText("" + list);

        spinner = (Spinner) findViewById(R.id.category);
        loadSpinnerData();
    }
    private void loadSpinnerData() {
        MyList = DbHelper.getAllCate();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, MyList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    public void SetTimeCategory(View view) {
        String cat_id = (String) spinner.getSelectedItem();
        myList = DbHelper.getMostLargestSumMyCategory(cat_id);
        String list = "";
        for (int i = 0; i < myList.size(); i++) {
            list = list + myList.get(i) + " \n";
        }
        mInfoTextView2.setText(""+list);
    }
}
