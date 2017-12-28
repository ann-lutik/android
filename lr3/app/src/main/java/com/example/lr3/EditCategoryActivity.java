package com.example.lr3;

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

public class EditCategoryActivity extends Activity {
    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DatabaseHelper mDbHelper;
    private Spinner mCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        mDbHelper = new DatabaseHelper(this);
        mCategory = (Spinner) findViewById(R.id.color);
        mTitleText = (EditText) findViewById(R.id.title1);
        mBodyText = (EditText) findViewById(R.id.text1);

        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
        mRowId = null;
        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DatabaseHelper.COLUMN_ID);
        if (extras != null) {
            mRowId = extras.getLong(DatabaseHelper.COLUMN_ID);
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    Toast.makeText(EditCategoryActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else {
                    saveState();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
    private void populateFields() {
        if (mRowId != null) {
            Cursor todo = mDbHelper.getTodo(mRowId);
            startManagingCursor(todo);
            String category = todo.getString(todo
                    .getColumnIndexOrThrow(DatabaseHelper.ICON_CATEGORY));

            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                Log.e(null, s + " " + category);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }

            mTitleText.setText(todo.getString(todo
                    .getColumnIndexOrThrow(DatabaseHelper.TITLE_CATEGORY)));
            mBodyText.setText(todo.getString(todo
                    .getColumnIndexOrThrow(DatabaseHelper.DESCRIPTION_CATEGORY)));
            todo.close();
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
        String category = (String) mCategory.getSelectedItem();
        String summary = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();

        if (description.length() == 0 && summary.length() == 0) {
            return;
        }

        if (mRowId == null) {
            long id = mDbHelper.createNewTodo(category, summary, description);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTodo(mRowId, category, summary, description);
        }
    }
}
