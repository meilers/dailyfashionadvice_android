package com.sobremesa.waywt.models.reddit;

import com.google.gson.annotations.Expose;

/**
 * Created by omegatai on 14-11-12.
 */
public class RemoteRedditResponse {

    @Expose
    public String kind;

    @Expose
    public RemoteRedditData data;

}
