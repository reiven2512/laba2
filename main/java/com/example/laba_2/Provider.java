package com.example.laba_2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

public class Provider extends ContentProvider {

    // // Константы для БД
    // БД
    static final String DB_NAME = "mydb";
    static final int DB_VERSION = 1;

    // Таблица
    static final String TABLE = "technologies";

    // Поля
    static final String ID = "id";
    static final String IMAGE = "graphic";
    static final String NAME = "name";
    static final String HELPTEXT = "helptext";

    // Скрипт создания таблицы
    static final String DB_CREATE = "create table " + TABLE + "("
            + ID + " int primary key autoincrement, "
            + IMAGE + " text, "
            + NAME + " text, "
            + HELPTEXT + " text" + ");";

    // // Uri
    // authority
    static final String AUTHORITY = "com.example.laba_2.provider";

    // path
    static final String PATH = "technologies";

    // Общий Uri
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PATH);

    // Типы данных
    // набор строк
    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + PATH;

    // одна строка
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_TECHNOLOGIES = 1;

    // Uri с указанным ID
    static final int URI_TECHNOLOGIES_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_TECHNOLOGIES);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_TECHNOLOGIES_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_TECHNOLOGIES: // общий Uri
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NAME + " ASC";
                }
                break;
            case URI_TECHNOLOGIES_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_TECHNOLOGIES)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_TECHNOLOGIES:
                break;
            case URI_TECHNOLOGIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_TECHNOLOGIES:
                break;
            case URI_TECHNOLOGIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = ID + " = " + id;
                } else {
                    selection = selection + " AND " + ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_TECHNOLOGIES:
                return CONTENT_TYPE;
            case URI_TECHNOLOGIES_ID:
                return CONTENT_ITEM_TYPE;
        }
        return null;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            List<Info> tmp = MainActivity.data;
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            for (int i = 0; i < 87; i++) {
                cv.put(IMAGE, tmp.get(i).getGf());
                cv.put(NAME, tmp.get(i).getNm());
                cv.put(HELPTEXT, tmp.get(i).getHt());
                db.insert(TABLE, null, cv);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}