package com.example.lr3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.Calendar;
import java.util.List;

public class EditRecordActivity extends AppCompatActivity {
    private DatabaseHelper DbHelper;
    private TimePicker mTimePicker;
    private TextView mInfoTextView;
    private TextView mInfoTextView2;
    private TextView mInfoTextView3;
    private TextView mInfoTextView4;//описание записи
    private Long mRowId;
    int hour1, hour2, hour3, minute1, minute2, minute3, t1, t2, t3;//время
    Spinner spinner;
    List<String> labels;
    int i = 0;
    String item;//назв категории

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        DbHelper = new DatabaseHelper(this);
        spinner = (Spinner) findViewById(R.id.category);
        loadSpinnerData();

        mInfoTextView = (TextView) findViewById(R.id.title3);
        mInfoTextView2 = (TextView) findViewById(R.id.title4);
        mInfoTextView3 = (TextView) findViewById(R.id.title5);
        mInfoTextView4 = (TextView) findViewById(R.id.title2);
        mTimePicker = (TimePicker) findViewById(R.id.TimePick);

        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DatabaseHelper.COLUMN_ID1);
        if (extras != null) {
            mRowId = extras.getLong(DatabaseHelper.COLUMN_ID1);
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mInfoTextView4.getText().toString())) {
                    Toast.makeText(EditRecordActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else {
                    saveState();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });


        Calendar now = Calendar.getInstance();
        mTimePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(now.get(Calendar.MINUTE));
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(getApplicationContext(), "onTimeChanged",
                        Toast.LENGTH_SHORT).show();
                mInfoTextView.setText(hourOfDay + ":" + minute);
            }
        });
        Button timeButton = (Button) findViewById(R.id.btn3);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoTextView.setText(new StringBuilder()
                        .append(mTimePicker.getCurrentHour()).append(".")
                        .append(mTimePicker.getCurrentMinute()));
                hour1 = mTimePicker.getCurrentHour();
                minute1 = mTimePicker.getCurrentMinute();
                t1 = hour1 * 60 + minute1;
            }
        });
        Button timeButton2 = (Button) findViewById(R.id.btn4);
        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoTextView2.setText(new StringBuilder()
                        .append(mTimePicker.getCurrentHour()).append(".")
                        .append(mTimePicker.getCurrentMinute()));
                hour2 = mTimePicker.getCurrentHour();
                minute2 = mTimePicker.getCurrentMinute();
                t2 = hour2 * 60 + minute2;
            }
        });

        OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                item = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void loadSpinnerData() {
        labels = DbHelper.getAllCate();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void setCut(View view) {
        hour3 = hour2 - hour1;
        minute3 = minute2 - minute1;
        t3 = hour3 * 60 + minute3;
        mInfoTextView3.setText("" + hour3 + "." + minute3);
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor recor = DbHelper.getRec(mRowId);
            startManagingCursor(recor);
            String catefor = recor.getString(recor
                    .getColumnIndexOrThrow(DatabaseHelper.ID_CAT));

            for (int i = 0; i < spinner.getCount(); i++) {

                String s = (String) spinner.getItemAtPosition(i);
                Log.e(null, s + " " + catefor);
                if (s.equalsIgnoreCase(catefor)) {
                    spinner.setSelection(i);
                }
            }

            mInfoTextView4.setText(recor.getString(recor
                    .getColumnIndexOrThrow(DatabaseHelper.DESCRIPTION_RECORD)));
            mInfoTextView.setText(recor.getString(recor
                    .getColumnIndexOrThrow(DatabaseHelper.TIME_START)));
            mInfoTextView2.setText(recor.getString(recor
                    .getColumnIndexOrThrow(DatabaseHelper.TIME_FINISH)));
            mInfoTextView3.setText(recor.getString(recor
                    .getColumnIndexOrThrow(DatabaseHelper.TIME_CUT)));
            recor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String op = mInfoTextView4.getText().toString();
        String cat_id = (String) spinner.getSelectedItem();

        String s1, s2, s3, s4;
        s1 = "" + t1;
        s2 = "" + t2;
        s3 = "" + t3;
        s4 = "" + cat_id;

        if (s1.length() == 0 && op.length() == 0 && s2.length() == 0 && s3.length() == 0 && s4.length() == 0) {
            return;
        }
        if (mRowId == null) {
            long id = DbHelper.createNewRec(s1, s2, op, s4, s3);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            DbHelper.updateRec(mRowId, s1, s2, op, s4, s3);
        }
    }
}