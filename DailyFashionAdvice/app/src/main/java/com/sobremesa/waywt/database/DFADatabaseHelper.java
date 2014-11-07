package com.sobremesa.waywt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Michael on 2014-03-10.
 */
public class DFADatabaseHelper extends SQLiteOpenHelper {

    private static DFADatabaseHelper mInstance = null;

    public static final String DATABASE_NAME = "dailyfashionadvice.db";
    private static final int DATABASE_VERSION = 1;

    public static DFADatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DFADatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    private DFADatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        ThreadTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // BaseActivity.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        ThreadTable.onUpgrade(database, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}

