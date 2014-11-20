package com.sobremesa.waywt.rest;

import com.sobremesa.waywt.models.reddit.RemoteRedditResponse;

import retrofit.http.EncodedPath;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by omegatai on 14-11-12.
 */
public interface DownloadRedditPostsClient {
    @GET("/r/{path}.json")
    RemoteRedditResponse getPosts(@EncodedPath("path") String path, @Query("t")String time, @Query("after")String after);
}