package com.sobremesa.waywt.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.sobremesa.waywt.database.DFADatabaseHelper;
import com.sobremesa.waywt.database.ThreadTable;

/**
 * Created by omegatai on 2014-06-17.
 */
public class DFAContentProvider extends ContentProvider {

    private static final String TAG = DFAContentProvider.class.getSimpleName();

    public static final String SCHEME = "content";
    public static final String AUTHORITY = "com.sobremesa.waywt.providers.DFAContentProvider";

    public static final class Uris {

        public static final Uri THREADS_URI = Uri.parse(SCHEME + "://" + AUTHORITY + "/" + Paths.THREADS);
    }

    public static final class Paths {
        public static final String THREADS = "threads";
    }


    private static final int THREADS_DIR = 0;
    private static final int THREAD_ID = 1;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    // mDatabase
    private DFADatabaseHelper mDatabase;


    static {
        sURIMatcher.addURI(AUTHORITY, Paths.THREADS, THREADS_DIR);
        sURIMatcher.addURI(AUTHORITY, Paths.THREADS + "/#", THREAD_ID);
    }





    @Override
    public boolean onCreate() {
        mDatabase = DFADatabaseHelper.getInstance(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (sURIMatcher.match(uri)) {
            case THREAD_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Paths.THREADS;
            case THREADS_DIR:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Paths.THREADS;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String groupBy = null;


        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case THREAD_ID:
                queryBuilder.appendWhere(ThreadTable.ID + "="
                        + uri.getLastPathSegment());
            case THREADS_DIR:
                queryBuilder.setTables(ThreadTable.TABLE_NAME);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, groupBy, null, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // Not Used (using raw insertion)
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (uriType) {
                case THREADS_DIR:
                case THREAD_ID:
                    final long threadId = dbConnection.insertOrThrow(
                            ThreadTable.TABLE_NAME, null, values);
                    final Uri newThread = ContentUris.withAppendedId(
                            Uris.THREADS_URI, threadId);
//                    getContext().getContentResolver().notifyChange(newSighting,
//                            null);
                    dbConnection.setTransactionSuccessful();
                    return newThread;

                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        } catch (Exception e) {
            Log.e(TAG, "Insert Exception", e);
        } finally {
            dbConnection.endTransaction();
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int deleteCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (uriType) {
                case THREADS_DIR :
                    deleteCount = dbConnection.delete(ThreadTable.TABLE_NAME,
                            selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case THREAD_ID :
                    deleteCount = dbConnection.delete(ThreadTable.TABLE_NAME,
                            ThreadTable.ID + "=?", new String[]{uri
                                    .getPathSegments().get(1)});
                    dbConnection.setTransactionSuccessful();
                    break;




                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (deleteCount > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int updateCount = 0;

        try {
            dbConnection.beginTransaction();

            switch (uriType) {

                case THREADS_DIR :
                    updateCount = dbConnection.update(ThreadTable.TABLE_NAME,
                            values, selection, selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;
                case THREAD_ID :
                    final Long threadId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(
                            ThreadTable.TABLE_NAME,
                            values,
                            ThreadTable.ID
                                    + "="
                                    + threadId
                                    + (TextUtils.isEmpty(selection)
                                    ? ""
                                    : " AND (" + selection + ")"),
                            selectionArgs);
                    dbConnection.setTransactionSuccessful();
                    break;


                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (updateCount > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;
    }


}