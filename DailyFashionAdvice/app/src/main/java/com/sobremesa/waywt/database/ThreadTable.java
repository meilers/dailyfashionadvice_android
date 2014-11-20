package com.sobremesa.waywt.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by omegatai on 14-11-04.
 */
public class ThreadTable {

    public static final String TABLE_NAME = "thread";

    public static final String ID = "_id";
    public static final String THREAD_ID = "threadId";
    public static final String PERMALINK = "permalink";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TAG = "tag";
    public static final String CREATED = "created";


    // Full names for disambiguation
    public static final String FULL_ID = TABLE_NAME + "." + ID;
    public static final String FULL_THREAD_ID = TABLE_NAME + "." + THREAD_ID;
    public static final String FULL_PERMALINK = TABLE_NAME + "." + PERMALINK;
    public static final String FULL_TITLE = TABLE_NAME + "." + TITLE;
    public static final String FULL_DESCRIPTION = TABLE_NAME + "." + DESCRIPTION;
    public static final String FULL_TAG = TABLE_NAME + "." + TAG;
    public static final String FULL_CREATED = TABLE_NAME + "." + CREATED;

    public static String[] ALL_COLUMNS = new String[]{ID, THREAD_ID, PERMALINK, TITLE, DESCRIPTION, TAG, CREATED};
    public static String[] FULL_ALL_COLUMNS = new String[]{FULL_ID, FULL_THREAD_ID, FULL_PERMALINK, FULL_TITLE, FULL_DESCRIPTION, FULL_TAG,  FULL_CREATED};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " integer primary key autoincrement, "
            + THREAD_ID + " text,"
            + PERMALINK + " text not null,"
            + TITLE + " text,"
            + DESCRIPTION + " text,"
            + TAG + " text,"
            + CREATED + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ThreadTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


}
