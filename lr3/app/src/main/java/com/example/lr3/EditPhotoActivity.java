package com.example.lr3;

import android.app.Activity;
import android.os.Bundle;
import java.io.File;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.database.Cursor;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import java.util.List;

public class EditPhotoActivity extends Activity {
    File directory;
    final int TYPE_PHOTO = 1;
    final int REQUEST_CODE_PHOTO = 1;
    final String TAG = "myLogsEditPhotoActivity";
    ImageView ivPhoto;
    String st_photo_f;//строка пути к фотографии
    private TextView mInfoTextView;//путь к фото
    private TextView mInfoTextView2;//описание фото
    Spinner spinner;//записи к которой крепить фото
    private DatabaseHelper DbHelper;
    List<String> labels;
    private Long mRowId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        createDirectory();
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        DbHelper = new DatabaseHelper(this);
        mInfoTextView = (TextView) findViewById(R.id.textView);
        mInfoTextView2 =(TextView) findViewById(R.id.title1);
        spinner = (Spinner) findViewById(R.id.my_record);
        loadSpinnerData();

        Button confirmButton = (Button) findViewById(R.id.btn2);
        mRowId = null;
        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DatabaseHelper.COLUMN_ID2);
        if (extras != null) {
            mRowId = extras.getLong(DatabaseHelper.COLUMN_ID2);
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mInfoTextView2.getText().toString())) {
                    Toast.makeText(EditPhotoActivity.this, "Данные не введены",
                            Toast.LENGTH_LONG).show();
                } else {
                    saveState();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void loadSpinnerData() {
        labels = DbHelper.getAllMyRec();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public void onClickPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri(TYPE_PHOTO));
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (intent == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    Log.d(TAG, "Photo uri: " + intent.getData());
                    Bundle bndl = intent.getExtras();
                    if (bndl != null) {
                        Object obj = intent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            Log.d(TAG, "bitmap " + bitmap.getWidth() + " x "
                                    + bitmap.getHeight());
                            ivPhoto.setImageBitmap(bitmap);
                        }
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled");
            }
        }
    }
    private Uri generateFileUri(int type) {
        File file = null;
        switch (type) {
            case TYPE_PHOTO:
                file = new File(directory.getPath() + "/" + "photo_"
                        + System.currentTimeMillis() + ".jpg");
                break;
        }
        Log.d(TAG, "fileName = " + file);
        st_photo_f=""+file;
        mInfoTextView.setText(st_photo_f);
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyFolder");
        if (!directory.exists())
            directory.mkdirs();
    }




    private void populateFields() {
        if (mRowId != null) {
            Cursor todo = DbHelper.getPhoto(mRowId);
            startManagingCursor(todo);
            String category = todo.getString(todo
                    .getColumnIndexOrThrow(DatabaseHelper.ID_REC));

            for (int i = 0; i < spinner.getCount(); i++) {

                String s = (String) spinner.getItemAtPosition(i);
                Log.e(null, s + " " + category);
                if (s.equalsIgnoreCase(category)) {
                    spinner.setSelection(i);
                }
            }

            mInfoTextView.setText(todo.getString(todo
                    .getColumnIndexOrThrow(DatabaseHelper.PHOTO_F)));
            mInfoTextView2.setText(todo.getString(todo
                    .getColumnIndexOrThrow(DatabaseHelper.PHOTO_DESC)));
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
        String id_rec = (String) spinner.getSelectedItem();
        String photo_f = mInfoTextView.getText().toString();
        String desc = mInfoTextView2.getText().toString();

        if (photo_f.length() == 0 && desc.length() == 0) {
            return;
        }

        if (mRowId == null) {
            long id = DbHelper.createNewPhoto(photo_f, desc, id_rec);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            DbHelper.updatePhoto(mRowId, photo_f, desc, id_rec);
        }
    }

}
