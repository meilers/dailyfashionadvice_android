package com.sobremesa.waywt.synchronizers;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.sobremesa.waywt.DFAApplication;
import com.sobremesa.waywt.database.DFADatabaseHelper;
import com.sobremesa.waywt.database.ThreadTable;
import com.sobremesa.waywt.models.RemoteThread;
import com.sobremesa.waywt.providers.DFAContentProvider;
import com.sobremesa.waywt.util.SyncUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 2014-03-17.
 */
public class ThreadSynchronizer extends BaseSynchronizer<RemoteThread>{

    private static final String TAG = ThreadSynchronizer.class.getSimpleName();

    public ThreadSynchronizer(Context context) {
        super(context);
    }

    @Override
    protected void performSynchronizationOperations(Context context, List<RemoteThread> inserts, List<RemoteThread> updates, List<Long> deletions) {

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        if( inserts.size() > 0 )
        {
            ContentValues[] val = new ContentValues[inserts.size()];
            int i = 0;
            for (RemoteThread w : inserts) {

                val[i] = this.getContentValuesForRemoteEntity(w);
                ++i;
            }

            doBulkInsertOptimised(context, val);
        }



        for (RemoteThread w : updates) {
            ContentValues values = this.getContentValuesForRemoteEntity(w);

            ContentProviderOperation op = ContentProviderOperation.newUpdate(DFAContentProvider.Uris.THREADS_URI).withSelection(ThreadTable.THREAD_ID + " = ?", new String[] { w.getIdentifier()+""})
                    .withValues(values).build();
            operations.add(op);

        }

        for (Long id : deletions) {
            ContentProviderOperation op = ContentProviderOperation.newDelete(DFAContentProvider.Uris.THREADS_URI).withSelection(ThreadTable.ID + " = ?", new String[]{String.valueOf(id)}).build();
            operations.add(op);
        }

        try {
            if( inserts.size() > 0 || operations.size() > 0 )
            {
                context.getContentResolver().applyBatch(DFAContentProvider.AUTHORITY, operations);
                context.getContentResolver().notifyChange(DFAContentProvider.Uris.THREADS_URI, null);

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private int doBulkInsertOptimised(Context context, ContentValues values[]) {

        DFADatabaseHelper helper = DFADatabaseHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(db, ThreadTable.TABLE_NAME);


        db.beginTransaction();
        int numInserted = 0;
        try {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                inserter.prepareForInsert();

                String value = (String)values[i].get(ThreadTable.THREAD_ID);

                if( value != null )
                    inserter.bind(inserter.getColumnIndex(ThreadTable.THREAD_ID), value);


                value = (String)values[i].get(ThreadTable.PERMALINK);

                if( value != null )
                    inserter.bind(inserter.getColumnIndex(ThreadTable.PERMALINK), value);



                value = (String)values[i].get(ThreadTable.TITLE);

                if( value != null )
                    inserter.bind(inserter.getColumnIndex(ThreadTable.TITLE), value);


                value = (String)values[i].get(ThreadTable.DESCRIPTION);

                if( value != null )
                    inserter.bind(inserter.getColumnIndex(ThreadTable.DESCRIPTION), value);


                value = (String)values[i].get(ThreadTable.TAG);

                if( value != null )
                    inserter.bind(inserter.getColumnIndex(ThreadTable.TAG), value);


                value = (String)values[i].get(ThreadTable.CREATED);

                if( value != null )
                    inserter.bind(inserter.getColumnIndex(ThreadTable.CREATED), value);



                inserter.execute();
            }
            numInserted = len;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            inserter.close();
        }
        return numInserted;
    }

    @Override
    protected boolean isRemoteEntityNewerThanLocal(RemoteThread remote, Cursor c) {


        return true;
    }

    @Override
    protected ContentValues getContentValuesForRemoteEntity(RemoteThread remoteProduct) {
        return SyncUtil.getContentValuesForRemoteThread(remoteProduct);
    }
}