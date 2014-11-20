package com.sobremesa.waywt.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.sobremesa.waywt.DFAApplication;
import com.sobremesa.waywt.database.ThreadTable;
import com.sobremesa.waywt.managers.RedditApiClientManager;
import com.sobremesa.waywt.models.RemoteThread;
import com.sobremesa.waywt.models.reddit.RedditPostType;
import com.sobremesa.waywt.models.reddit.RemoteRedditData;
import com.sobremesa.waywt.models.reddit.RemoteRedditPost;
import com.sobremesa.waywt.models.reddit.RemoteRedditResponse;
import com.sobremesa.waywt.providers.DFAContentProvider;
import com.sobremesa.waywt.rest.DownloadRedditPostsClient;
import com.sobremesa.waywt.synchronizers.ThreadSynchronizer;
import com.sobremesa.waywt.util.SyncUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by omegatai on 2014-08-21.
 */
public class SyncThreadsService extends IntentService {

    private static final String TAG = SyncThreadsService.class.getSimpleName();

    public final static class EXTRAS
    {
        public static final String IS_MALE = "is_male";
        public static final String IS_TEEN = "is_teen";
    }

    public final static class ACTIONS
    {
        public static final String SYNC_THREADS = "com.sobremesa.waywt.services.SYNC_THREADS";
    }

    public SyncThreadsService()
    {
        super(TAG);

        setIntentRedelivery(false);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        boolean isMale = intent.getExtras().getBoolean(EXTRAS.IS_MALE);
        boolean isTeen = intent.getExtras().getBoolean(EXTRAS.IS_TEEN);


        if(ACTIONS.SYNC_THREADS.equals(action)) {


            Context context = getApplicationContext();

            String prefixPath = isMale ? ( !isTeen ? "malefashionadvice/" : "TeenMFA/" ) : ( !isTeen ? "femalefashionadvice/" : "TeenFFA/" );

            List<RemoteThread> totalThreads = new ArrayList<RemoteThread>();
            DownloadRedditPostsClient client = RedditApiClientManager.INSTANCE.getClient(context, DownloadRedditPostsClient.class);

            RemoteRedditResponse response;
            RemoteRedditData remoteData;
            List<RemoteRedditPost> posts;
            Iterator<RemoteRedditPost> iter;


            // new
            String after = "";
            int i = 0;

            while( after != null && i < 1)
            {
                try{
                    response = client.getPosts(prefixPath + "hot", "", after);
                }
                catch(Exception e)
                {
                    return;
                }

                if( response == null )
                    return;

                remoteData = response.data;
                posts = remoteData.children;
                iter = posts.iterator();

                while (iter.hasNext()) {
                    RemoteRedditPost post = iter.next();

                    RedditPostType postType = retrieveRedditPostType(isMale, isTeen, post);
                    post.postType = postType;

                    if ( postType != RedditPostType.INVALID && !totalThreads.contains(post) )
                    {
                        RemoteThread thread = new RemoteThread();
                        thread.setPermalink(post.data.permalink);
                        thread.setTitle(post.data.title);
                        thread.setCreated(post.data.created);
                        totalThreads.add(thread);
                    }
                }

                after = response.data.after;

                ++i;
            }


            // today
            after = "";
            i = 0;

            while( after != null && i < 2)
            {
                try
                {
                    response = client.getPosts(prefixPath + "top", "today", after);
                }
                catch(Exception e)
                {
                    return;
                }

                if( response == null )
                    return;

                remoteData = response.data;

                posts = remoteData.children;

                iter = posts.iterator();
                while (iter.hasNext()) {
                    RemoteRedditPost post = iter.next();

                    RedditPostType postType = retrieveRedditPostType(isMale, isTeen, post);
                    post.postType = postType;

                    if ( postType != RedditPostType.INVALID && !totalThreads.contains(post) )
                    {
                        RemoteThread thread = new RemoteThread();
                        thread.setPermalink(post.data.permalink);
                        thread.setTitle(post.data.title);
                        thread.setCreated(post.data.created);
                        totalThreads.add(thread);
                    }
                }

                after = response.data.after;

                ++i;
            }

            // week
            after = "";
            i = 0;

            while( after != null && i < 3)
            {
                try
                {
                    response = client.getPosts(prefixPath + "top", "week", after);
                }
                catch(Exception e)
                {
                    return;
                }

                if( response == null )
                    return;

                remoteData = response.data;

                posts = remoteData.children;

                iter = posts.iterator();
                while (iter.hasNext()) {
                    RemoteRedditPost post = iter.next();

                    RedditPostType postType = retrieveRedditPostType(isMale, isTeen, post);
                    post.postType = postType;

                    if ( postType != RedditPostType.INVALID && !totalThreads.contains(post) )
                    {
                        RemoteThread thread = new RemoteThread();
                        thread.setPermalink(post.data.permalink);
                        thread.setTitle(post.data.title);
                        thread.setCreated(post.data.created);
                        totalThreads.add(thread);
                    }
                }

                after = response.data.after;

                ++i;
            }


            // month
            after = "";
            i = 0;

            while( after != null && i < 5)
            {

                try
                {
                    response = client.getPosts(prefixPath + "top", "month", after);
                }
                catch(Exception e)
                {
                    return;
                }

                if( response == null )
                    return;

                remoteData = response.data;

                posts = remoteData.children;

                iter = posts.iterator();
                while (iter.hasNext()) {
                    RemoteRedditPost post = iter.next();

                    RedditPostType postType = retrieveRedditPostType(isMale, isTeen, post);
                    post.postType = postType;

                    if ( postType != RedditPostType.INVALID && !totalThreads.contains(post) )
                    {
                        RemoteThread thread = new RemoteThread();
                        thread.setPermalink(post.data.permalink);
                        thread.setTitle(post.data.title);
                        thread.setCreated(post.data.created);
                        totalThreads.add(thread);
                    }
                }

                after = response.data.after;

                ++i;
            }

            if (totalThreads != null && totalThreads.size() > 0) {
                // synchronize!
                Cursor localThreadCursor = context.getContentResolver().query(DFAContentProvider.Uris.THREADS_URI, ThreadTable.ALL_COLUMNS, null, null, null);
                localThreadCursor.moveToFirst();

                ThreadSynchronizer sync = new ThreadSynchronizer(context);
                SyncUtil.synchronizeRemoteThreads(totalThreads, localThreadCursor, localThreadCursor.getColumnIndex(ThreadTable.PERMALINK), sync, null);
            }
        }

    }

    private RedditPostType retrieveRedditPostType( boolean isMale, boolean isTeen, RemoteRedditPost post )
    {
        if( isMale )
        {
            if( !isTeen )
            {
                if( !post.data.domain.equals("self.malefashionadvice") || post.data.title.toLowerCase().contains("announcement") || post.data.title.toLowerCase().contains("phone") || post.data.title.toLowerCase().contains("interest") || post.data.title.toLowerCase().contains("top") || (!post.data.title.toLowerCase().contains("waywt") && !post.data.title.toLowerCase().contains("outfit feedback") && !post.data.title.toLowerCase().contains("recent purchases")) )
                    return RedditPostType.INVALID;

                if( post.data.title.toLowerCase().contains("waywt") )
                    return RedditPostType.WAYWT;
                else if( post.data.title.toLowerCase().contains("outfit feedback") )
                    return RedditPostType.OUTFIT_FEEDBACK;
                else if( post.data.title.toLowerCase().contains("recent purchases") )
                    return RedditPostType.RECENT_PURCHASES;
                else
                    return RedditPostType.INVALID;
            }
            else
            {
                if( !post.data.domain.equals("self.TeenMFA") || post.data.title.toLowerCase().contains("announcement") || post.data.title.toLowerCase().contains("interest")|| post.data.title.toLowerCase().contains("top")|| (!post.data.title.toLowerCase().contains("waywt") && !post.data.title.toLowerCase().contains("recent purchases")) )
                    return RedditPostType.INVALID;

                if( post.data.title.toLowerCase().contains("waywt") )
                    return RedditPostType.WAYWT;
                else if( post.data.title.toLowerCase().contains("outfit feedback") )
                    return RedditPostType.OUTFIT_FEEDBACK;
                else if( post.data.title.toLowerCase().contains("recent purchases") )
                    return RedditPostType.RECENT_PURCHASES;
                else
                    return RedditPostType.INVALID;
            }
        }
        else
        {
            if( !isTeen )
            {
                if( !post.data.domain.equals("self.femalefashionadvice") || post.data.title.toLowerCase().contains("announcement") || post.data.title.toLowerCase().contains("interest")|| post.data.title.toLowerCase().contains("top") ||  (!post.data.title.toLowerCase().contains("waywt") && !post.data.title.toLowerCase().contains("outfit feedback") && !post.data.title.toLowerCase().contains("theme") && !post.data.title.toLowerCase().contains("recent purchases")) )
                    return RedditPostType.INVALID;

                if( post.data.title.toLowerCase().contains("waywt") )
                    return RedditPostType.WAYWT;
                else if( post.data.title.toLowerCase().contains("outfit feedback") )
                    return RedditPostType.OUTFIT_FEEDBACK;
                else if( post.data.title.toLowerCase().contains("recent purchases") )
                    return RedditPostType.RECENT_PURCHASES;
                else
                    return RedditPostType.INVALID;
            }
            else
            {
                if( !post.data.domain.equals("self.TeenFFA") || post.data.title.toLowerCase().contains("announcement") || post.data.title.toLowerCase().contains("interest") || post.data.title.toLowerCase().contains("top") ||  (!post.data.title.toLowerCase().contains("waywt") && !post.data.title.toLowerCase().contains("outfit feedback") && !post.data.title.toLowerCase().contains("recent purchases")) )
                    return RedditPostType.INVALID;

                if( post.data.title.toLowerCase().contains("waywt") )
                    return RedditPostType.WAYWT;
                else if( post.data.title.toLowerCase().contains("outfit feedback") )
                    return RedditPostType.OUTFIT_FEEDBACK;
                else if( post.data.title.toLowerCase().contains("recent purchases") )
                    return RedditPostType.RECENT_PURCHASES;
                else
                    return RedditPostType.INVALID;
            }
        }
    }

}
