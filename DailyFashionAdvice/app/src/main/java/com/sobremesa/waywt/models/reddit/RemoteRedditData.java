package com.sobremesa.waywt.models.reddit;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by omegatai on 14-11-12.
 */
public class RemoteRedditData {

    @Expose
    public String modhash;

    @Expose
    public String after;

    @Expose
    public List<RemoteRedditPost> children;


}
