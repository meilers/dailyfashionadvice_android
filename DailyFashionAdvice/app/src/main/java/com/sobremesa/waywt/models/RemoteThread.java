package com.sobremesa.waywt.models;

import android.database.Cursor;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sobremesa.waywt.database.ThreadTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Michael on 2014-03-17.
 */
public class RemoteThread extends RemoteObject implements Parcelable {


    public static class DateComparator implements Comparator<RemoteThread> {
        @Override
        public int compare(RemoteThread o1, RemoteThread o2) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date1;
            Date date2;

            try {
                date1 = dateFormat.parse(o1.getCreated());
                date2 = dateFormat.parse(o2.getCreated());

                return date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }


    @Expose
    @SerializedName("threadId")
    private String mThreadId;

    @Expose
    @SerializedName("permalink")
    private String mPermalink;

    @Expose
    @SerializedName("title")
    private String mTitle;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("tag")
    private String mTag;

    @Expose
    @SerializedName("created")
    private String mCreated;



    @Override
    public String getIdentifier() {
        return mPermalink;
    }


    public RemoteThread() {}


    public RemoteThread(final Cursor cursor) {
        setThreadId(cursor.getString(cursor.getColumnIndex(ThreadTable.THREAD_ID)));
        setPermalink(cursor.getString(cursor.getColumnIndex(ThreadTable.PERMALINK)));
        setTitle(cursor.getString(cursor.getColumnIndex(ThreadTable.TITLE)));
        setDescription(cursor.getString(cursor.getColumnIndex(ThreadTable.DESCRIPTION)));
        setTag(cursor.getString(cursor.getColumnIndex(ThreadTable.TAG)));
        setCreated(cursor.getString(cursor.getColumnIndex(ThreadTable.CREATED)));
    }

    public RemoteThread(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.getThreadId() != null ? 1:0);

        if( this.getThreadId() != null )
            dest.writeString(this.getThreadId());


        dest.writeInt(this.getPermalink() != null ? 1:0);

        if( this.getPermalink() != null )
            dest.writeString(this.getPermalink());



        dest.writeInt(this.getTitle() != null ? 1:0);

        if( this.getTitle() != null )
            dest.writeString(this.getTitle());


        dest.writeInt(this.getDescription() != null ? 1:0);

        if( this.getDescription() != null )
            dest.writeString(this.getDescription());


        dest.writeInt(this.getTag() != null ? 1:0);

        if( this.getTag() != null )
            dest.writeString(this.getTag());


        dest.writeInt(this.getCreated() != null ? 1:0);

        if( this.getCreated() != null )
            dest.writeString(this.getCreated());
    }


    public void readFromParcel(Parcel in) {

        if( in.readInt() == 1 )
            setThreadId(in.readString());

        if( in.readInt() == 1 )
            setPermalink(in.readString());

        if( in.readInt() == 1 )
            setTitle(in.readString());

        if( in.readInt() == 1 )
            setDescription(in.readString());

        if( in.readInt() == 1 )
            setTag(in.readString());

        if( in.readInt() == 1 )
            setCreated(in.readString());
    }

    public static Creator<RemoteThread> CREATOR = new Creator<RemoteThread>() {

        @Override
        public RemoteThread createFromParcel(Parcel in) {
            return new RemoteThread(in);
        }

        @Override
        public RemoteThread[] newArray(int size) {
            return new RemoteThread[size];
        }
    };


    public String getThreadId() {
        return mThreadId;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTag() {
        return mTag;
    }

    public String getCreated() {
        return mCreated;
    }

    public void setThreadId(String threadId) {
        mThreadId = threadId;
    }

    public void setPermalink(String permalink) {
        mPermalink = permalink;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public void setCreated(String created) {
        mCreated = created;
    }
}