package com.sobremesa.waywt.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.sobremesa.waywt.DFAApplication;
import com.sobremesa.waywt.database.ThreadTable;
import com.sobremesa.waywt.models.RemoteThread;
import com.sobremesa.waywt.synchronizers.BaseSynchronizer;
import com.sobremesa.waywt.synchronizers.preprocessors.BasePreProcessor;

import java.util.List;


/**
 * Created by Michael on 2014-04-07.
 */
public class SyncUtil {

    public static void synchronizeRemoteThreads(List<RemoteThread> remoteThreads, Cursor localThreads, int remoteIdentifierColumn, BaseSynchronizer<RemoteThread> synchronizer, BasePreProcessor<RemoteThread> preProcessor) {

        if( preProcessor != null )
            preProcessor.preProcessRemoteRecords(remoteThreads);

        synchronizer.synchronize(DFAApplication.getContext(), remoteThreads, localThreads, remoteIdentifierColumn);
    }



    public static ContentValues getContentValuesForRemoteThread(RemoteThread remoteThread) {
        ContentValues values = new ContentValues();

        values.put(ThreadTable.THREAD_ID, remoteThread.getThreadId());
        values.put(ThreadTable.PERMALINK, remoteThread.getPermalink());
        values.put(ThreadTable.TITLE, remoteThread.getTitle());
        values.put(ThreadTable.DESCRIPTION, remoteThread.getDescription());
        values.put(ThreadTable.TAG, remoteThread.getTag());
        values.put(ThreadTable.CREATED, remoteThread.getCreated());

        return values;
    }


}
