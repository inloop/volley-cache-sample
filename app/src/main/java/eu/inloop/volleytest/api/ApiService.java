package eu.inloop.volleytest.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.*;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

public class ApiService {

    /** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "volley";

    private static ApiService sInstance;
    private final OkHttpClient mOkHttpClient;
    private final RequestQueue mVolleyRequestQueue;
    private final Network mNetwork;
    private final Cache mHttpCache;

    public synchronized static ApiService getInstance(Context context){
        if (sInstance == null) {
            sInstance = new ApiService(context);
        }
        return sInstance;
    }

    private ApiService(Context context) {
        mOkHttpClient = new OkHttpClient();

        //init HTTP cache
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        mHttpCache = new DiskBasedCache(cacheDir);
        mNetwork = new BasicNetwork(new OkHttpStack(mOkHttpClient));
        mVolleyRequestQueue = new RequestQueue(mHttpCache, mNetwork);
        mVolleyRequestQueue.start();
    }

    public <T> void enqueueRequest(Request<T> request) {
        mVolleyRequestQueue.add(request);
    }
}
