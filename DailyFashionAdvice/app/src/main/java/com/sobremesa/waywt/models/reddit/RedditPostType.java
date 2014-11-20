package com.sobremesa.waywt.models.reddit;

import java.util.HashMap;

/**
 * Created by omegatai on 14-11-12.
 */
public enum RedditPostType {
    INVALID(0), WAYWT(1), OUTFIT_FEEDBACK(2), RECENT_PURCHASES(3);

    private static final HashMap<Integer, RedditPostType> idToTypeMap = new HashMap<Integer, RedditPostType>();
    static {
        for (RedditPostType type : values()) {
            idToTypeMap.put(type.getId(), type);
        }
    }

    private int mId;

    private RedditPostType(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public String getIdString() {
        return Integer.toString(mId);
    }

}
