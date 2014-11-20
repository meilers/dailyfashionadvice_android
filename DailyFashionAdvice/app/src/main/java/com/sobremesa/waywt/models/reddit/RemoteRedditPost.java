package com.sobremesa.waywt.models.reddit;

import com.google.gson.annotations.Expose;
import com.sobremesa.waywt.models.RemoteObject;

/**
 * Created by omegatai on 14-11-12.
 */
public class RemoteRedditPost extends RemoteObject {

    @Expose
    public String kind;

    @Expose
    public RedditPostType postType;

    @Expose
    public RemoteRedditPostData data;

    @Override
    public String getIdentifier() {
        return data.permalink;
    }


    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof RemoteRedditPost)
        {
            sameSame = this.data.permalink == ((RemoteRedditPost) object).data.permalink;
        }

        return sameSame;
    }
}
