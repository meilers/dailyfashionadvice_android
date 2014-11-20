package com.sobremesa.waywt.models.reddit;

import com.google.gson.annotations.Expose;
import com.sobremesa.waywt.models.RemoteObject;

/**
 * Created by omegatai on 14-11-12.
 */
public class RemoteRedditPostData extends RemoteObject {

    @Expose
    public String domain;

    @Expose
    public String subreddit;

    @Expose
    public String permalink; // unique

    @Expose
    public String author;

    @Expose
    public String created;

    @Expose
    public String title;

    @Expose
    public int ups;

    @Expose
    public int downs;

    @Expose
    public String author_flair_text;

    @Override
    public String getIdentifier() {
        return permalink;
    }
}