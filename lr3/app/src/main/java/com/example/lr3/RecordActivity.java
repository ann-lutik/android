package com.example.lr3;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RecordActivity extends ListActivity {
    private DatabaseHelper dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        this.getListView().setDividerHeight(2);
        dbHelper = new DatabaseHelper(this);
        fillData();
        registerForContextMenu(getListView());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    // Реакция на выбор меню
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createNewTask();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createNewTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                dbHelper.deleteRec(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void createNewTask() {
        Intent intent = new Intent(this, EditRecordActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }
    private void fillData() {
        cursor = dbHelper.getAllRec();
        startManagingCursor(cursor);
        String[] from = new String[] { DatabaseHelper.DESCRIPTION_RECORD };
        int[] to = new int[] { R.id.label };
        // адаптер массива для отображения данных
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.list_row, cursor, from, to);
        setListAdapter(notes);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, EditRecordActivity.class);
        intent.putExtra(DatabaseHelper.COLUMN_ID1, id);
        // активити вернет результат если будет вызвано с помощью этого метода
        startActivityForResult(intent, ACTIVITY_EDIT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            fillData();
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
