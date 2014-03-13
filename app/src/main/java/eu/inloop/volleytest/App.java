package eu.inloop.volleytest;

import android.app.Application;
import com.android.volley.Cache;

public class App extends Application {

    public static Cache<String> sMockCache;

    @Override
    public void onCreate() {
        super.onCreate();
        initMockCache();
    }

    private void initMockCache() {
        sMockCache = new Cache<String>() {


            public Cache.Entry<String> cacheDb;

            @Override
            public Cache.Entry<String> get(String key) {
                if (cacheDb == null) {
                    return null;
                } else {
                    return cacheDb;
                }
            }

            @Override
            public void put(String key, Entry<String> entry) {
                cacheDb = entry;
            }

            @Override
            public void initialize() {

            }

            @Override
            public void invalidate(String key, boolean fullExpire) {

            }

            @Override
            public void remove(String key) {

            }

            @Override
            public void clear() {

            }
        };
    }
}
