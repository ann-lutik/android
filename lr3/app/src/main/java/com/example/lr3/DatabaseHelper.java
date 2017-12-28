package com.example.lr3;

/**
 * Created by Анна on 15.12.2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE1 = "category";
    // поля таблицы
    public static final String COLUMN_ID = "_id";
    public static final String ICON_CATEGORY = "icon";
    public static final String TITLE_CATEGORY = "title";
    public static final String DESCRIPTION_CATEGORY = "description";
    // запрос на создание таблицы данных
    private static final String TABLE_CREATE = "create table "
            + DATABASE_TABLE1 + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + ICON_CATEGORY
            + " text not null, " + TITLE_CATEGORY + " text not null,"
            + DESCRIPTION_CATEGORY + " text not null" + ");";

    //ТАБЛИЦА 2 ЗАПИСИ
    private static final String DATABASE_TABLE2 = "record";
    // поля таблицы
    public static final String COLUMN_ID1 = "_id";
    public static final String TIME_START = "time_s";
    public static final String TIME_FINISH = "time_f";
    public static final String DESCRIPTION_RECORD = "description_rec";
    public static final String ID_CAT = "id_cat";
    public static final String TIME_CUT = "time_c";
    // запрос на создание таблицы данных о записях
    private static final String TABLE2_CREATE = "create table "
            + DATABASE_TABLE2 + "(" + COLUMN_ID1 + " integer primary key autoincrement, "
            + TIME_START + " integer not null, "
            + TIME_FINISH + " integer not null, "
            + DESCRIPTION_RECORD + " text not null,"
            + ID_CAT + " integer not null, "
            + TIME_CUT + " integer not null " + ");";
    //ТАБЛИЦА 3 ФОТОГРАФИИ
    private static final String DATABASE_TABLE3 = "photo";
    // поля таблицы
    public static final String COLUMN_ID2 = "_id";
    public static final String PHOTO_F = "photo_f";
    public static final String PHOTO_DESC = "descr";
    public static final String ID_REC = "id_rec";

    private static final String TABLE3_CREATE = "create table "
            + DATABASE_TABLE3 + "(" + COLUMN_ID2 + " integer primary key autoincrement, "
            + PHOTO_F + " text , "
            + PHOTO_DESC + " text, "
            + ID_REC + " integer not null" + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE2_CREATE);
        db.execSQL(TABLE3_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(DatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS record");
        db.execSQL("DROP TABLE IF EXISTS photo");
        onCreate(db);
    }

    /**
     * Создаёт новый элемент списка дел. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewTodo(String category, String summary,
                              String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(category, summary,
                description);

        long row = db.insert(DATABASE_TABLE1, null, initialValues);
        db.close();

        return row;
    }

    /**
     * Обновляет список
     */
    public boolean updateTodo(long rowId, String category, String summary,
                              String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(category, summary,
                description);

        return db.update(DATABASE_TABLE1, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }

    /**
     * Удаляет элемент списка
     */
    public void deleteTodo(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE1, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     *
     * @return курсор с результатами всех записей
     */
    public Cursor getAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE1, new String[] { COLUMN_ID,
                        ICON_CATEGORY, TITLE_CATEGORY, DESCRIPTION_CATEGORY }, null,
                null, null, null, null);
    }

    /**
     * Возвращает курсор с указанной записи
     */
    public Cursor getTodo(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE1,
                new String[] { COLUMN_ID, ICON_CATEGORY, TITLE_CATEGORY,
                        DESCRIPTION_CATEGORY }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    /*
     * Создаёт пару ключ-значение и записывает в базу
     */
    private ContentValues createContentValues(String category, String summary,
                                              String description) {
        ContentValues values = new ContentValues();
        values.put(ICON_CATEGORY, category);
        values.put(TITLE_CATEGORY, summary);
        values.put(DESCRIPTION_CATEGORY, description);
        return values;
    }



    /**
     * Создаёт новый элемент списка записей. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewRec(String time_s, String time_f, String description_rec,
                               String id_cat, String time_c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues1(time_s, time_f,description_rec, id_cat, time_c);

        long row = db.insert(DATABASE_TABLE2, null, initialValues);
        db.close();
        return row;
    }
    /**
     * Обновляет список
     */
    public boolean updateRec(long rowId, String time_s, String time_f, String description_rec,
                               String id_cat, String time_c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues1(time_s, time_f,description_rec, id_cat, time_c);
        return db.update(DATABASE_TABLE2, updateValues, COLUMN_ID1 + "=" + rowId,
                null) > 0;
    }
    /**
     * Удаляет элемент списка
     */
    public void deleteRec(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE2, COLUMN_ID1 + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     *
     * return курсор с результатами всех записей
     */
    public Cursor getAllRec() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE2, new String[] { COLUMN_ID1,
                        TIME_START, TIME_FINISH, DESCRIPTION_RECORD,ID_CAT,TIME_CUT}, null,
                null, null, null, null);
    }

    /**
     * Возвращает курсор с указанной записи
     */
    public Cursor getRec(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE2,
                new String[] { COLUMN_ID1, TIME_START, TIME_FINISH, DESCRIPTION_RECORD,ID_CAT,TIME_CUT },
                COLUMN_ID1 + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /*
     * Создаёт пару ключ-значение и записывает в базу
     */
    private ContentValues createContentValues1(String time_s, String time_f, String description_rec,
                                               String id_cat, String time_c) {
        ContentValues values = new ContentValues();
        values.put(TIME_START, time_s);
        values.put(TIME_FINISH, time_f);
        values.put(DESCRIPTION_RECORD, description_rec);
        values.put(ID_CAT, id_cat);
        values.put(TIME_CUT, time_c);
        return values;
    }
    public List<String> getAllCate(){
        List<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));//adding 3nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<String> getMostFrequentCategory(){
        List<String> list = new ArrayList<String>();
        //String selectQuery = "SELECT  top 1 count(id_cat) as valueCount, id_cat FROM " + DATABASE_TABLE2+ "group by id_cat order by valueCount desc";
        //String selectQuery = "SELECT id_cat FROM record group by id_cat order by id_cat desc";
        String selectQuery = "SELECT   id_cat FROM record group by id_cat order by count (1) desc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 3nd column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<String> getMostLargestSumCategory(){
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT   id_cat,SUM(time_c)  FROM record group by id_cat order by SUM(time_c) desc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add("Категория: "+cursor.getString(0) +" Суммарное время отрезков: "+cursor.getString(1) );//adding 1 column data
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<String> getMostLargestSumMyCategory(String cat){
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT   SUM(time_c)  FROM record WHERE id_cat=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { cat });
        if (cursor.moveToFirst()) {
            do {
                list.add(" Суммарное время отрезков в категории: "+cursor.getString(0) );;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<String> getMostLargestSumByChart(){
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT SUM(time_c)  FROM record group by id_cat order by SUM(time_c) desc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0) );
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<String> getAllMyRec(){
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    /**
     * Создаёт новый элемент списка фотографий. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewPhoto(String photo, String photo_desc,
                               String id_rec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues2(photo, photo_desc, id_rec);
        long row = db.insert(DATABASE_TABLE3, null, initialValues);
        db.close();
        return row;
    }
    /**
     * Обновляет список
     */
    public boolean updatePhoto(long rowId, String photo, String photo_desc,
                               String id_rec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues2(photo, photo_desc,   id_rec);

        return db.update(DATABASE_TABLE3, updateValues, COLUMN_ID2 + "=" + rowId,
                null) > 0;
    }
    /**
     * Удаляет элемент списка
     */
    public void deletePhoto(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE3, COLUMN_ID2 + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     *
     * @return курсор с результатами всех записей
     */
    public Cursor getAllPhoto() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE3, new String[] { COLUMN_ID2,
                        PHOTO_F, PHOTO_DESC, ID_REC }, null,
                null, null, null, null);
    }
    /**
     * Возвращает курсор с указанной записи
     */
    public Cursor getPhoto(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE3,
                new String[] { COLUMN_ID2, PHOTO_F, PHOTO_DESC,
                        ID_REC }, COLUMN_ID2 + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private ContentValues createContentValues2(String photo, String photo_desc,
                                               String id_rec) {
        ContentValues values = new ContentValues();
        values.put(PHOTO_F, photo);
        values.put(PHOTO_DESC, photo_desc);
        values.put(ID_REC, id_rec);
        return values;
    }

}

